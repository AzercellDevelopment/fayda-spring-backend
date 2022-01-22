package com.fayda.auth.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GenericError extends RuntimeException {
  private final String message;
  private final int status;
}
