package com.fayda.command.error;

import com.fayda.command.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponseDTO> handleIllegalState(IllegalStateException err) {
    return handle(err.getMessage(), 501);
  }

  @ExceptionHandler(GenericError.class)
  public ResponseEntity<ErrorResponseDTO> handleGenericError(GenericError err) {
    return handle(err.getMessage(), err.getStatus());
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ErrorResponseDTO> handleGenericError(Throwable err) {
    return handle(err.getMessage(), 409);
  }

  private ResponseEntity<ErrorResponseDTO> handle(String message, int status) {
    log.error("Error with status {}: message {}", status, message);
    return ResponseEntity
        .status(status)
        .body(ErrorResponseDTO
            .builder()
            .date(LocalDateTime.now())
            .message(message)
            .status(status)
            .build());
  }
}
