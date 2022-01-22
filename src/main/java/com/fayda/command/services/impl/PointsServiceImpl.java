package com.fayda.command.services.impl;

import com.fayda.command.constants.TransactionTypes;
import com.fayda.command.dto.points.BalanceDto;
import com.fayda.command.dto.points.PointsSyncRequestDto;
import com.fayda.command.error.GenericError;
import com.fayda.command.model.TransactionModel;
import com.fayda.command.model.UserModel;
import com.fayda.command.repository.TransactionRepository;
import com.fayda.command.repository.UserRepository;
import com.fayda.command.services.PointsService;
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
  private final UserRepository userRepository;

  @Override
  public void syncPoints(PointsSyncRequestDto requestDto) {
    final UserModel user = getUser(requestDto.getUserId());
    updateBalanceForUser(requestDto, user);
  }

  @Override
  public BalanceDto getBalance(UUID userId) {
    return BalanceDto.builder()
        .balance(getUser(userId).getBalance()).build();
  }

  private UserModel getUser(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new GenericError("Customer not found", 399));
  }

  private void updateBalanceForUser(PointsSyncRequestDto requestDto, UserModel user) {
    user.setBalance(requestDto.getPoints());
    user.setVersion(UUID.randomUUID());
    userRepository.save(user);
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
