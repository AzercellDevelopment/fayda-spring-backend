package com.fayda.command.services;

public interface AuthUtils {
  String createToken(String username);

  void verifyToken(String token);

  String getUsername(String token);

  boolean checkPasswordMatching(String password, String encoded);

  String hash(String password);
}
