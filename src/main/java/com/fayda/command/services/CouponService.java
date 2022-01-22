package com.fayda.command.services;

import com.fayda.command.dto.coupon.CouponResponseDto;

import java.util.List;
import java.util.UUID;

public interface CouponService {

  List<CouponResponseDto> getAllCoupons();

  String buyCouponWithStaticId(String staticId, UUID userId);
}
