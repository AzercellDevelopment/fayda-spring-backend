package com.fayda.command.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FAYDA_MERCHANT_DEFINITIONS")
public class MerchantModel {
  @Id
  @GeneratedValue(strategy = AUTO)
  UUID id;
  String name;
  String address;
  BigDecimal tarifValue;
  BigDecimal maxTarif;
  String tarifText;
  String longitude;
  String latitude;
  String iconUrl;
  Boolean isActive;
}
