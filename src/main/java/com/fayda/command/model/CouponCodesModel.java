package com.fayda.command.model;


import com.fayda.command.constants.CouponCodeStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FAYDA_COUPON_CODES")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CouponCodesModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  UUID id;
  UUID version;
  String referenceId;
  String code;
  @Enumerated(EnumType.STRING)
  CouponCodeStatus status;
  UUID ownerId;
  UUID merchantId;
}
