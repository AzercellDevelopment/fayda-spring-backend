package com.fayda.command.services;

import com.fayda.command.dto.merchants.GroupedMerchantResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface MerchantService {

  GroupedMerchantResponse getAllMerchants(UUID userId);

  void startTask(UUID merchantId);

  BigDecimal completeTask(UUID userId, UUID merchantId);

}
