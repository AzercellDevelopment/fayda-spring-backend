package com.fayda.command.filter;

import com.fayda.command.error.GenericError;
import com.fayda.command.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TokenFilter extends OncePerRequestFilter {

  public static final String AUTH_PREFIX = "Bearer ";

  private final JwtUtils jwtUtils;
  private final HandlerExceptionResolver exceptionResolver;
  private final List<Pattern> requiredPaths = List.of(Pattern.compile("/points/.+"), Pattern.compile("/coupons/.+"),
      Pattern.compile("/merchant/.+"));

  public TokenFilter(JwtUtils jwtUtils,
                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    this.jwtUtils = jwtUtils;
    this.exceptionResolver = exceptionResolver;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    if (!isAuthRequired(request)) {
      log.warn("No auth required");
      filterChain.doFilter(request, response);
      return;
    }
    try {
      final String authHeader = verifyAndGetAuthHeaderIfPresent(request);
      final String token = verifyAndGetTokenFromHeader(authHeader);
      verifyExpirationAndDoFilter(request, response, filterChain, token);
    } catch (GenericError err) {
      exceptionResolver.resolveException(request, response, null, err);
    }
  }

  private void verifyExpirationAndDoFilter(HttpServletRequest request,
                                           HttpServletResponse response,
                                           FilterChain filterChain,
                                           String token) throws IOException, ServletException {
    try {
      String username = jwtUtils.getUsername(token);
      request.setAttribute(JwtUtils.ATTR_USERNAME, username);
      MDC.put("uid", username);
      filterChain.doFilter(request, response);
    } catch (ExpiredJwtException exx) {
      throw new GenericError("Token Expired", 401);
    }
  }

  private boolean isAuthRequired(HttpServletRequest req) {
    return requiredPaths
        .stream()
        .anyMatch(pathPattern -> {
          String requestPath = req.getServletPath();
          return pathPattern.matcher(requestPath).matches();
        });
  }

  private String verifyAndGetTokenFromHeader(String authHeader) {
    if (authHeader.startsWith(AUTH_PREFIX)) {
      return authHeader.substring(AUTH_PREFIX.length());
    }
    throw new GenericError("Incorrect header structure", 400);
  }

  private String verifyAndGetAuthHeaderIfPresent(HttpServletRequest request) {
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (ObjectUtils.isEmpty(header)) {
      throw new GenericError("Missing required headers", 401);
    }
    return header;
  }
}
