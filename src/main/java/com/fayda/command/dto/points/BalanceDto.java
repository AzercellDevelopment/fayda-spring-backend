package com.fayda.command.dto.points;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalanceDto {
  BigInteger balance;
  @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
  LocalDateTime lastSyncDate;
  String refNum;
}
