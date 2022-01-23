package com.fayda.command.services.impl;

import com.fayda.command.constants.MerchantTaskStatuses;
import com.fayda.command.constants.TransactionTypes;
import com.fayda.command.dto.merchants.GroupedMerchantResponse;
import com.fayda.command.dto.merchants.MerchantResponseDto;
import com.fayda.command.dto.points.PointsSyncRequestDto;
import com.fayda.command.error.GenericError;
import com.fayda.command.model.MerchantModel;
import com.fayda.command.model.MerchantTaskModel;
import com.fayda.command.model.TransactionModel;
import com.fayda.command.repository.MerchantDefinitionRepository;
import com.fayda.command.repository.MerchantTaskRepository;
import com.fayda.command.repository.TransactionRepository;
import com.fayda.command.services.MerchantService;
import com.fayda.command.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

  public static final String NON_ACTIVE = "nonActive";
  public static final String ACTIVE = "active";
  private final MerchantDefinitionRepository merchantDefinitionRepository;
  private final MerchantTaskRepository merchantTaskRepository;
  private final TransactionRepository pointsService;

  @Override
  public GroupedMerchantResponse getAllMerchants(UUID userId) {
    final var activeTask = merchantTaskRepository.findFirstByUserIdAndStatusOrderByStartDateDesc(userId,
        MerchantTaskStatuses.ACTIVE);
    final var completedTask = merchantTaskRepository.findFirstByUserIdAndStatusOrderByStartDateDesc(userId,
        MerchantTaskStatuses.COMPLETED);
    final var merchantMap = merchantDefinitionRepository.findAllByIsActiveTrue()
        .stream()
        .map(mm -> buildResponseDto(mm, activeTask, completedTask))
        .collect(groupingBy(MerchantResponseDto::getType));

    final var nonActiveMerchants = new ArrayList<>(merchantMap.getOrDefault(NON_ACTIVE, Collections.emptyList()));
    addCompletedToNonActive(completedTask.filter(ct -> activeTask.isEmpty()), nonActiveMerchants);
    return GroupedMerchantResponse.builder()
        .active(merchantMap.getOrDefault(ACTIVE, Collections.emptyList()))
        .nonActive(nonActiveMerchants)
        .build();
  }

  @Override
  public String startTask(UUID userId, UUID merchantId) {
    checkForActiveTasks(userId);
    final var task = MerchantTaskModel
        .builder()
        .userId(userId)
        .points(0)
        .definition(MerchantModel.builder().id(merchantId).build())
        .status(MerchantTaskStatuses.ACTIVE)
        .build();
    merchantTaskRepository.save(task);
    return "success";
  }

  @Override
  public String cancelTask(UUID userId) {
    merchantTaskRepository.findFirstByUserIdAndStatusOrderByStartDateDesc(userId, MerchantTaskStatuses.ACTIVE)
        .ifPresent(task -> {
          task.setStatus(MerchantTaskStatuses.CANCELLED);
          task.setEndDate(TimeUtils.now());
          merchantTaskRepository.save(task);
        });
    return "success";
  }

  @Override
  public void updateActiveTask(PointsSyncRequestDto requestDto) {
    merchantTaskRepository.findFirstByUserIdAndStatusOrderByStartDateDesc(requestDto.getUserId(), MerchantTaskStatuses.ACTIVE)
        .ifPresent(task -> {
          task.setPoints(task.getPoints() + requestDto.getPoints().intValue());
          merchantTaskRepository.save(task);
        });
  }

  @Override
  public BigDecimal completeTask(UUID userId, UUID merchantId) {
    return merchantTaskRepository.findFirstByUserIdAndStatusOrderByStartDateDesc(userId, MerchantTaskStatuses.ACTIVE)
        .filter(task -> task.getDefinition().getId().equals(merchantId))
        .map(task -> {
          task.setStatus(MerchantTaskStatuses.COMPLETED);
          task.setEndDate(TimeUtils.now());
          merchantTaskRepository.save(task);
          final var calcTarif = calculateTarif(task.getDefinition(), task).setScale(2, RoundingMode.HALF_DOWN);
          createAndSaveTransaction(task, calcTarif);
          return calcTarif;
        })
        .orElse(BigDecimal.ZERO);
  }

  private void createAndSaveTransaction(MerchantTaskModel task, BigDecimal calcTarif) {
    final var transactionModel = TransactionModel
        .builder()
        .title(task.getDefinition().getName())
        .iconUrl(task.getDefinition().getIconUrl())
        .isActive(true)
        .type(TransactionTypes.MERCHANT_COMPLETE)
        .subtitle(calcTarif.toString().concat("%"))
        .points(BigInteger.valueOf(task.getPoints()))
        .userId(task.getUserId())
        .build();
    pointsService.save(transactionModel);
  }

  private MerchantResponseDto buildResponseDto(MerchantModel mm, Optional<MerchantTaskModel> activeTask, Optional<MerchantTaskModel> completedTask) {
    final var mappedDto = buildNonActiveResponseDto(mm);
    activeTask
        .or(() -> completedTask)
        .filter(task -> task.getDefinition().getId().equals(mm.getId()))
        .ifPresent(task -> updateResponseWithTask(mm, mappedDto, task));
    return mappedDto;
  }

  private void updateResponseWithTask(MerchantModel mm, MerchantResponseDto mappedDto, MerchantTaskModel task) {
    BigDecimal calculatedTarif = calculateTarif(mm, task);
    final var calculatedTarifString = calculatedTarif.setScale(2, RoundingMode.HALF_DOWN)
        .toString().concat(mm.getTarifText());
    mappedDto.setStartDate(task.getStartDate());
    mappedDto.setStatus(task.getStatus().getDisplayName());
    mappedDto.setType(ACTIVE);
    mappedDto.setCalculatedTarif(calculatedTarifString);
    mappedDto.setStepCount(BigInteger.valueOf(task.getPoints()));
  }

  private BigDecimal calculateTarif(MerchantModel mm, MerchantTaskModel task) {
    final var bonusCoef = task.getPoints() / 1000.;
    final var calculatedTarif = mm.getTarifValue().multiply(BigDecimal.valueOf(bonusCoef));
    return calculatedTarif.min(mm.getMaxTarif());
  }

  private MerchantResponseDto buildNonActiveResponseDto(MerchantModel mm) {
    return MerchantResponseDto
        .builder()
        .id(mm.getId())
        .address(mm.getAddress())
        .name(mm.getName())
        .latitude(Double.valueOf(mm.getLatitude()))
        .longitude(Double.valueOf(mm.getLongitude()))
        .tarif(mm.getTarifValue().setScale(2, RoundingMode.HALF_DOWN).toString().concat(mm.getTarifText()))
        .iconUrl(mm.getIconUrl())
        .type(NON_ACTIVE)
        .maxTarif(mm.getMaxTarif().setScale(2, RoundingMode.HALF_DOWN).toString().concat(mm.getTarifText()))
        .build();
  }

  private void checkForActiveTasks(UUID userId) {
    merchantTaskRepository.findFirstByUserIdAndStatusOrderByStartDateDesc(userId, MerchantTaskStatuses.ACTIVE)
        .ifPresent(task -> {
          throw new GenericError("Already has active task", 400);
        });
  }

  private void addCompletedToNonActive(Optional<MerchantTaskModel> completedTask, List<MerchantResponseDto> nonActiveList) {
    completedTask
        .map(task -> buildNonActiveResponseDto(task.getDefinition()))
        .ifPresent(nonActiveList::add);
  }

}
