package com.fayda.auth.services.impl;

import com.fayda.auth.dto.register.RegisterRequestDTO;
import com.fayda.auth.constants.UserStatus;
import com.fayda.auth.dto.login.EmailLoginRequestDTO;
import com.fayda.auth.dto.register.JwtResponseDTO;
import com.fayda.auth.error.GenericError;
import com.fayda.auth.model.UserModel;
import com.fayda.auth.repository.UserRepository;
import com.fayda.auth.services.AuthService;
import com.fayda.auth.services.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final AuthUtils authUtils;

  @Override
  public JwtResponseDTO register(RegisterRequestDTO request) {
    checkIfUsernameOrEmailIsTaken(request);

    final var userModel = saveNewUser(request);

    return createJwtAndBuildResponseDTO(userModel.getId().toString());
  }

  @Override
  public JwtResponseDTO login(EmailLoginRequestDTO request) {
    UserModel userModel = getUserIfExists(request.getEmail());
    checkPassword(userModel, request);
    return createJwtAndBuildResponseDTO(userModel.getId().toString());
  }

  @Override
  public UserModel getUserIfExists(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new GenericError("User not found", 404));
  }

  private void checkPassword(UserModel userModel, EmailLoginRequestDTO req) {
    if (!authUtils.checkPasswordMatching(req.getPassword(), userModel.getPassword())) {
      throw new GenericError("Passwords don't match", 403);
    }
  }

  private JwtResponseDTO createJwtAndBuildResponseDTO(String id) {
    String jwtToken = authUtils.createToken(id);
    return JwtResponseDTO
        .builder()
        .token(jwtToken)
        .build();
  }

  private UserModel saveNewUser(RegisterRequestDTO request) {
    UserModel userModel = UserModel
        .builder()
        .email(request.getEmail())
        .password(authUtils.hash(request.getPassword()))
        .status(UserStatus.ACTIVE)
        .build();
    return userRepository.save(userModel);
  }

  private void checkIfUsernameOrEmailIsTaken(RegisterRequestDTO request) {
    userRepository
        .findByEmail(request.getEmail())
        .ifPresent(u -> {
          throw new GenericError("User already exists", 400);
        });
  }
}
