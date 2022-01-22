package com.fayda.command.services;

import com.fayda.command.dto.points.BalanceDto;
import com.fayda.command.dto.points.PointsSyncRequestDto;

import java.util.UUID;

public interface PointsService {
  void syncPoints(PointsSyncRequestDto requestDto);

  BalanceDto getBalance(UUID userId);
}
