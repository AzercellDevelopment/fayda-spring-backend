package com.fayda.command.services.impl;

import com.fayda.command.constants.TransactionTypes;
import com.fayda.command.dto.points.BalanceDto;
import com.fayda.command.dto.points.PointsSyncRequestDto;
import com.fayda.command.model.TransactionModel;
import com.fayda.command.model.UserModel;
import com.fayda.command.repository.TransactionRepository;
import com.fayda.command.services.MerchantService;
import com.fayda.command.services.PointsService;
import com.fayda.command.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointsServiceImpl implements PointsService {

  private final TransactionRepository transactionRepository;
  private final UserService userService;
  private final MerchantService merchantService;

  @Override
  public void syncPoints(PointsSyncRequestDto requestDto) {
    final var user = userService.getUserById(requestDto.getUserId());
    updateBalanceForUser(requestDto, user);
    merchantService.updateActiveTask(requestDto);
  }

  @Override
  public BalanceDto getBalance(UUID userId) {
    final var user = userService.getUserById(userId);
    return BalanceDto.builder()
        .balance(user.getBalance()).lastSyncDate(user.getUpdateDate())
        .refNum(user.getRefNum())
        .build();
  }

  private void updateBalanceForUser(PointsSyncRequestDto requestDto, UserModel user) {
    user.setBalance(user.getBalance().add(requestDto.getPoints()));
    user.setVersion(UUID.randomUUID());
    userService.save(user);
    createAndSaveTransaction(user.getId(), requestDto.getPoints());
  }

  private void createAndSaveTransaction(UUID id, BigInteger points) {
    final var transaction = TransactionModel
        .builder()
        .points(points)
        .isActive(true)
        .type(TransactionTypes.AWARDING)
        .title("Sync transaction")
        .userId(id)
        .build();
    transactionRepository.save(transaction);

  }
}
