package com.fayda.command.dto.merchants;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupedMerchantResponse {
  List<MerchantResponseDto> active;
  List<MerchantResponseDto> nonActive;
}
