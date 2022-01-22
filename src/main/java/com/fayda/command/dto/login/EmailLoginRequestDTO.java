package com.fayda.command.dto.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailLoginRequestDTO {
  @NotNull
  @Email
  private String email;
  @NotNull
  @Size(min = 8)
  private String password;
}
