package com.fayda.command.model;

import com.fayda.command.constants.TransactionTypes;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FAYDA_TRANSACTIONS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionModel {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  UUID id;
  UUID userId;
  BigInteger points;
  TransactionTypes type;
  Boolean isActive;
  String title;
  String iconUrl;
  @CreationTimestamp
  LocalDateTime createDate;
}
