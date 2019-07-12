package com.wojciechdm.programmers.controllers.security;

import lombok.*;

@Getter
public class JwtAuthenticationResponse {
  private final String accessToken;
  private final String tokenType = "Bearer";

  public JwtAuthenticationResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
