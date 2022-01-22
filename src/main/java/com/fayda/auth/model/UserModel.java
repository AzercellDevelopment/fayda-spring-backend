package com.fayda.auth.model;

import com.fayda.auth.constants.UserStatus;
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
  String email;
  String password;
  BigInteger balance;
  @Enumerated(EnumType.STRING)
  UserStatus status;
  @CreationTimestamp
  LocalDateTime createDate;
  @UpdateTimestamp
  LocalDateTime updateDate;
}
