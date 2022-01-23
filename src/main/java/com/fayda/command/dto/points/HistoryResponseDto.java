package com.fayda.command.dto.points;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fayda.command.constants.TransactionTypes;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryResponseDto {
  String title;
  String subtitle;
  String description;
  String iconUrl;
  TransactionTypes type;
  String points;
  @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
  LocalDateTime createDate;
}
