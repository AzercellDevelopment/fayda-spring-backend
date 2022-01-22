package com.fayda.command.dto.merchants;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MerchantResponseDto {
  UUID id;
  String name;
  String address;
  String iconUrl;
  String longitude;
  String latitude;
  String tarif;
  String calculatedTarif;
  String status;
  @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
  LocalDateTime startDate;
  BigInteger stepCount;
}
