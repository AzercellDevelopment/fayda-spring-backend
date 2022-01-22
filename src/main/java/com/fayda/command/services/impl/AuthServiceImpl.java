package com.fayda.command.services.impl;

import com.fayda.command.constants.UserStatus;
import com.fayda.command.constants.UserTypes;
import com.fayda.command.dto.login.EmailLoginRequestDTO;
import com.fayda.command.dto.register.JwtResponseDTO;
import com.fayda.command.dto.register.RegisterRequestDTO;
import com.fayda.command.error.GenericError;
import com.fayda.command.model.UserModel;
import com.fayda.command.repository.UserRepository;
import com.fayda.command.services.AuthService;
import com.fayda.command.services.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    return createJwtAndBuildResponseDTO(userModel);
  }

  @Override
  public JwtResponseDTO login(EmailLoginRequestDTO request) {
    UserModel userModel = getUserIfExists(request.getEmail());
    checkPassword(userModel, request);
    return createJwtAndBuildResponseDTO(userModel);
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

  private JwtResponseDTO createJwtAndBuildResponseDTO(UserModel user) {
    String jwtToken = authUtils.createToken(user.getId().toString());
    return JwtResponseDTO
        .builder()
        .token(jwtToken)
        .type(Objects.requireNonNullElse(user.getType(), UserTypes.CUSTOMER))
        .build();
  }

  private UserModel saveNewUser(RegisterRequestDTO request) {
    UserModel userModel = UserModel
        .builder()
        .email(request.getEmail())
        .password(authUtils.hash(request.getPassword()))
        .status(UserStatus.ACTIVE)
        .type(UserTypes.CUSTOMER)
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
