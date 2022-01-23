package com.fayda.command.services.impl;

import com.fayda.command.constants.CouponCodeStatus;
import com.fayda.command.constants.TransactionTypes;
import com.fayda.command.dto.coupon.CouponResponseDto;
import com.fayda.command.error.GenericError;
import com.fayda.command.model.CouponCodesModel;
import com.fayda.command.model.CouponModel;
import com.fayda.command.model.TransactionModel;
import com.fayda.command.model.UserModel;
import com.fayda.command.repository.CouponCodeRepository;
import com.fayda.command.repository.CouponRepository;
import com.fayda.command.services.CouponService;
import com.fayda.command.services.PointsService;
import com.fayda.command.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

  private final CouponRepository couponRepository;
  private final CouponCodeRepository couponCodeRepository;
  private final UserService userService;
  private final PointsService pointsService;

  @Override
  public List<CouponResponseDto> getAllCoupons() {
    return couponRepository.findAllByIsActive(true)
        .stream()
        .map(couponModel -> CouponResponseDto
            .builder()
            .id(couponModel.getStaticId())
            .iconUrl(couponModel.getIconUrl())
            .title(couponModel.getTitle())
            .subtitle(couponModel.getSubtitle())
            .description(couponModel.getDescription())
            .price(couponModel.getPrice())
            .build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public String buyCouponWithStaticId(String staticId, UUID userId) {
    final var couponDefinition = checkAndGetCouponDefinition(staticId);
    final var nextCoupon = checkAndGetCouponCode(staticId);
    final var user = userService.getUserById(userId);
    couponDefinition.setSubtitle(nextCoupon.getCode());
    checkBalance(couponDefinition, user);
    reserveCoupon(nextCoupon, user);
    createAndSaveTransaction(user, couponDefinition);
    updateCustomerBalance(user, couponDefinition);

    return nextCoupon.getCode();
  }

  private void createAndSaveTransaction(UserModel user, CouponModel couponDefinition) {
    final var transactionModel = TransactionModel
        .builder()
        .userId(user.getId())
        .type(TransactionTypes.COUPON_PURCHASE)
        .points(couponDefinition.getPrice())
        .isActive(true)
        .iconUrl(couponDefinition.getIconUrl())
        .title(couponDefinition.getTitle())
        .subtitle(couponDefinition.getSubtitle())
        .description(couponDefinition.getDescription())
        .build();
    pointsService.save(transactionModel);
  }

  private CouponCodesModel checkAndGetCouponCode(String staticId) {
    return couponCodeRepository.findFirstByReferenceIdAndStatus(staticId,
        CouponCodeStatus.ACTIVE)
        .orElseThrow(() -> new GenericError("No coupon available", 400));
  }

  private CouponModel checkAndGetCouponDefinition(String staticId) {
    return couponRepository.findFirstByStaticId(staticId)
        .orElseThrow(() -> new GenericError("Coupon does not exist", 400));
  }

  private void updateCustomerBalance(UserModel user, CouponModel nextCoupon) {
    user.setBalance(user.getBalance().subtract(nextCoupon.getPrice()));
    user.setVersion(UUID.randomUUID());
    userService.save(user);
  }

  private void reserveCoupon(CouponCodesModel nextCoupon, UserModel user) {
    nextCoupon.setStatus(CouponCodeStatus.BOUGHT);
    nextCoupon.setOwnerId(user.getId());
    couponCodeRepository.save(nextCoupon);
  }

  private void checkBalance(CouponModel nextCoupon, UserModel user) {
    if (user.getBalance().compareTo(nextCoupon.getPrice()) < 0) {
      throw new GenericError("Not enough points", 400);
    }
  }
}
