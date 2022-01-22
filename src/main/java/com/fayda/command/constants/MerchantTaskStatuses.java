package com.fayda.command.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MerchantTaskStatuses {
  ACTIVE("Active"),
  CANCELLED("Cancelled"),
  COMPLETED("Completed");

  private final String displayName;
}
