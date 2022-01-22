package com.fayda.auth.controller;

import com.fayda.auth.dto.register.RegisterRequestDTO;
import com.fayda.auth.dto.GenericResponse;
import com.fayda.auth.dto.login.EmailLoginRequestDTO;
import com.fayda.auth.dto.register.JwtResponseDTO;
import com.fayda.auth.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<GenericResponse<JwtResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO req) {
    log.info("Trying to register new user {}", req.getEmail());
    final var response = authService.register(req);
    log.info("Registering new user {} was successful", req.getEmail());

    return ResponseEntity.status(CREATED.value()).body(GenericResponse.success(response));
  }

  @PostMapping("/login/email")
  public ResponseEntity<GenericResponse<JwtResponseDTO>> login(@Valid @RequestBody EmailLoginRequestDTO req) {
    log.info("Trying to login user {}", req.getEmail());
    JwtResponseDTO response = authService.login(req);
    log.info("Signing in user {} was successful", req.getEmail());

    return ResponseEntity.status(OK.value()).body(GenericResponse.success(response));
  }

}
