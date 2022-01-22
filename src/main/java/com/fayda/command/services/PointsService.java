package com.fayda.command.services;

import com.fayda.command.dto.points.BalanceDto;
import com.fayda.command.dto.points.HistoryResponseDto;
import com.fayda.command.dto.points.PointsSyncRequestDto;
import com.fayda.command.model.TransactionModel;

import java.util.List;
import java.util.UUID;

public interface PointsService {
  void syncPoints(PointsSyncRequestDto requestDto);

  BalanceDto getBalance(UUID userId);

  void save(TransactionModel transactionModel);

  List<HistoryResponseDto> getHistory(UUID fromString);
}
