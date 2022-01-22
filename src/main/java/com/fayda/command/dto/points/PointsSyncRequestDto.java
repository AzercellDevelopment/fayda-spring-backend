package com.fayda.command.dto.points;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PointsSyncRequestDto {
  @NotNull
  BigInteger points;

  @ApiModelProperty(hidden = true)
  UUID userId;
}
