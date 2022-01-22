package com.fayda.command.dto.merchants;

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
public class MerchantResponseDto {
  String name;
  String address;
  String longitude;
  String latitude;
  String tarif;
  String calculatedTarif;
  String status;
  @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
  LocalDateTime startDate;
  BigInteger stepCount;
}
