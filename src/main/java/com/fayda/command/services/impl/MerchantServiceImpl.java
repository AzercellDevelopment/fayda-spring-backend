package com.fayda.command.services.impl;

import com.fayda.command.constants.MerchantTaskStatuses;
import com.fayda.command.dto.merchants.GroupedMerchantResponse;
import com.fayda.command.dto.merchants.MerchantResponseDto;
import com.fayda.command.model.MerchantModel;
import com.fayda.command.model.MerchantTaskModel;
import com.fayda.command.repository.MerchantDefinitionRepository;
import com.fayda.command.repository.MerchantTaskRepository;
import com.fayda.command.services.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

  public static final String NON_ACTIVE = "nonActive";
  public static final String ACTIVE = "active";
  private final MerchantDefinitionRepository merchantDefinitionRepository;
  private final MerchantTaskRepository merchantTaskRepository;

  @Override
  public GroupedMerchantResponse getAllMerchants(UUID userId) {
    final var activeTask = merchantTaskRepository.findFirstByUserIdAndStatus(userId,
        MerchantTaskStatuses.ACTIVE);
    final var merchantMap = merchantDefinitionRepository.findAllByIsActiveTrue()
        .stream()
        .map(mm -> buildResponseDto(mm, activeTask))
        .collect(groupingBy(MerchantResponseDto::getStatus));

    return GroupedMerchantResponse.builder()
        .active(merchantMap.getOrDefault(ACTIVE, Collections.emptyList()))
        .nonActive(merchantMap.getOrDefault(NON_ACTIVE, Collections.emptyList()))
        .build();
  }

  private MerchantResponseDto buildResponseDto(MerchantModel mm, Optional<MerchantTaskModel> activeTask) {
    final var mappedDto = buildNonActiveResponseDto(mm);
    activeTask
        .filter(task -> task.getDefinition().getId().equals(mm.getId()))
        .ifPresent(task -> updateResponseWithActiveTask(mm, mappedDto, task));
    return mappedDto;
  }

  private void updateResponseWithActiveTask(MerchantModel mm, MerchantResponseDto mappedDto, MerchantTaskModel task) {
    final var bonusCoef = task.getPoints() / 1000.;
    var calculatedTarif = mm.getTarifValue().multiply(BigDecimal.valueOf(bonusCoef));
    calculatedTarif = calculatedTarif.min(mm.getMaxTarif());
    final var calculatedTarifString = calculatedTarif.toString().concat("%");
    mappedDto.setStartDate(task.getStartDate());
    mappedDto.setStatus(ACTIVE);
    mappedDto.setCalculatedTarif(calculatedTarifString);
    mappedDto.setStepCount(BigInteger.valueOf(task.getPoints()));
  }

  private MerchantResponseDto buildNonActiveResponseDto(MerchantModel mm) {
    return MerchantResponseDto
        .builder()
        .address(mm.getAddress())
        .name(mm.getName())
        .latitude(mm.getLatitude())
        .longitude(mm.getLongitude())
        .tarif(mm.getTarifText())
        .status(NON_ACTIVE)
        .build();
  }

  @Override
  public void startTask(UUID merchantId) {

  }

  @Override
  public BigDecimal completeTask(UUID userId, UUID merchantId) {
    return null;
  }
}
