package com.fayda.command.controller;

import com.fayda.command.dto.GenericResponse;
import com.fayda.command.dto.coupon.CouponResponseDto;
import com.fayda.command.services.CouponService;
import com.fayda.command.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

  private final CouponService couponService;

  @GetMapping
  public ResponseEntity<GenericResponse<List<CouponResponseDto>>> getAllActiveCoupons() {
    log.info("Getting all coupons");
    final var res = couponService.getAllCoupons();
    log.info("Getting all coupons was successful");
    return ResponseEntity.ok(GenericResponse.success(res));
  }

  @PostMapping("/buy")
  public ResponseEntity<GenericResponse<String>> getAllActiveCoupons(
      @RequestAttribute(JwtUtils.ATTR_USERNAME) String userId,
      @RequestParam("coupon_id") String couponId) {
    log.info("Buying coupon: {}", couponId);
    final var res = couponService.buyCouponWithStaticId(couponId, UUID.fromString(userId));
    log.info("Successfully bought: {}", couponId);
    return ResponseEntity.ok(GenericResponse.success(res));
  }

}
