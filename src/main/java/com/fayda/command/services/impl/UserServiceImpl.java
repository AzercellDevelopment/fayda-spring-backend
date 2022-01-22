package com.fayda.command.services.impl;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.fayda.command.constants.UserStatus;
import com.fayda.command.constants.UserTypes;
import com.fayda.command.dto.login.EmailLoginRequestDTO;
import com.fayda.command.dto.register.JwtResponseDTO;
import com.fayda.command.dto.register.RegisterRequestDTO;
import com.fayda.command.error.GenericError;
import com.fayda.command.model.UserModel;
import com.fayda.command.repository.UserRepository;
import com.fayda.command.services.AuthUtils;
import com.fayda.command.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private static final char[] REF_CHARS = {'A', 'B', 'C', 'D', 'E', 'F', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
  private final UserRepository userRepository;
  private final AuthUtils authUtils;
  private final Random random = new Random();

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

  @Override
  public UserModel getUserById(UUID id) {
    log.info("Getting user");
    return userRepository
        .findById(id)
        .orElseThrow(() -> new GenericError("User not found", 404));
  }

  public void save(UserModel user) {
    log.info("Updating user");
    userRepository.save(user);
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
        .balance(BigInteger.ZERO)
        .email(request.getEmail())
        .password(authUtils.hash(request.getPassword()))
        .status(UserStatus.ACTIVE)
        .type(UserTypes.CUSTOMER)
        .version(UUID.randomUUID())
        .refNum(NanoIdUtils.randomNanoId(random, REF_CHARS, 6))
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
