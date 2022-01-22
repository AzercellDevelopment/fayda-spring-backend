package com.fayda.auth.services.impl;

import com.fayda.auth.services.AuthUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthUtilsImpl implements AuthUtils {

  private static final long EXPIRATION_IN_MILLIS = 10800000;
  private final PasswordEncoder passwordEncoder;
  @Value("${secrets.jwt.key}")
  private String secretKey;

  @Override
  public String createToken(String username) {
    Key key = new SecretKeySpec(getSecretKey(), SignatureAlgorithm.HS256.getJcaName());
    return Jwts
        .builder()
        .setSubject(username)
        .setIssuedAt(getCurrentDate())
        .setExpiration(getExpirationDate())
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  @Override
  public void verifyToken(String token) {
    Jwts
        .parserBuilder()
        .setSigningKey(getSecretKey())
        .build()
        .parse(token);
  }

  @Override
  public String getUsername(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  @Override
  public boolean checkPasswordMatching(String password, String encoded) {
    return passwordEncoder.matches(password, encoded);
  }

  @Override
  public String hash(String password) {
    return passwordEncoder.encode(password);
  }

  private byte[] getSecretKey() {
    return DatatypeConverter.parseBase64Binary(secretKey);
  }

  private Date getExpirationDate() {
    return new Date(getCurrentDate().getTime() + EXPIRATION_IN_MILLIS);
  }

  private Date getCurrentDate() {
    return new Date();
  }
}
