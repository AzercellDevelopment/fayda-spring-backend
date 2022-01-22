package com.fayda.command.dto.coupon;

import java.math.BigInteger;
import java.util.UUID;


public interface CouponWithCode {
  String getCode();

  UUID getVersion();

  BigInteger getPrice();
}
