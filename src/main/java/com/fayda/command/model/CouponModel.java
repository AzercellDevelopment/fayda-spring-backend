package com.fayda.command.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FAYDA_COUPON_DEFINITIONS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  UUID id;
  String staticId;
  String title;
  String subtitle;
  String description;
  String iconUrl;
  BigInteger price;
  Boolean isActive;
}
