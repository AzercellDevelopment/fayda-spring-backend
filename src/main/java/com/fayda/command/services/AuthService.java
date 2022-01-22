package com.fayda.command.services;

import com.fayda.command.dto.login.EmailLoginRequestDTO;
import com.fayda.command.dto.register.JwtResponseDTO;
import com.fayda.command.dto.register.RegisterRequestDTO;
import com.fayda.command.model.UserModel;

public interface AuthService {

  JwtResponseDTO register(RegisterRequestDTO request);

  JwtResponseDTO login(EmailLoginRequestDTO request);

  UserModel getUserIfExists(String email);
}
