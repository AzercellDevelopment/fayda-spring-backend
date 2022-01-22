package com.fayda.command.dto.coupon;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponResponseDto {
  String id;
  BigInteger price;
  String title;
  String subtitle;
  String description;
  String iconUrl;
}
