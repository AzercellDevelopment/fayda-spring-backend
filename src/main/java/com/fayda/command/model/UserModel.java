package com.fayda.command.model;

import com.fayda.command.constants.UserStatus;
import com.fayda.command.constants.UserTypes;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FAYDA_USERS")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserModel {
  @Id
  @GeneratedValue(strategy = AUTO)
  UUID id;
  @GeneratedValue(strategy = AUTO)
  String refNum;
  String email;
  String password;
  BigInteger balance = BigInteger.ZERO;
  UUID version;
  @Enumerated(EnumType.STRING)
  UserTypes type;
  @Enumerated(EnumType.STRING)
  UserStatus status;
  @CreationTimestamp
  LocalDateTime createDate;
  @UpdateTimestamp
  LocalDateTime updateDate;
}
