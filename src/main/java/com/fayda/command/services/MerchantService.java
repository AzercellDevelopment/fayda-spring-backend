package com.fayda.command.services;

import com.fayda.command.dto.merchants.GroupedMerchantResponse;
import com.fayda.command.dto.points.PointsSyncRequestDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface MerchantService {

  GroupedMerchantResponse getAllMerchants(UUID userId);

  String startTask(UUID userId, UUID merchantId);

  BigDecimal completeTask(UUID userId, UUID merchantId);

  void updateActiveTask(PointsSyncRequestDto requestDto);

}
