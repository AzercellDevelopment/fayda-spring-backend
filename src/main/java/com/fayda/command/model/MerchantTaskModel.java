package com.fayda.command.model;


import com.fayda.command.constants.MerchantTaskStatuses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FAYDA_MERCHANT_TASK")
public class MerchantTaskModel {
  @Id
  @GeneratedValue(strategy = AUTO)
  UUID id;
  @ManyToOne(targetEntity = MerchantModel.class)
  MerchantModel definition;
  UUID userId;
  @CreationTimestamp
  LocalDateTime startDate;
  LocalDateTime endDate;
  Integer points;
  @Enumerated(EnumType.STRING)
  MerchantTaskStatuses status;
}
