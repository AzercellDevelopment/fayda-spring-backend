package com.fayda.auth.services;

import com.fayda.auth.dto.login.EmailLoginRequestDTO;
import com.fayda.auth.dto.register.JwtResponseDTO;
import com.fayda.auth.dto.register.RegisterRequestDTO;
import com.fayda.auth.model.UserModel;

public interface AuthService {

  JwtResponseDTO register(RegisterRequestDTO request);

  JwtResponseDTO login(EmailLoginRequestDTO request);

  UserModel getUserIfExists(String email);
}
