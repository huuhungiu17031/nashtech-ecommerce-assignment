package com.nashtech.cellphonesfake.service;

import io.jsonwebtoken.Claims;

import java.util.List;

public interface JwtService {
    String generateAccessToken(String email, List<String> roles);

    String generateRefreshToken(String email);

    String extractEmail(String token);

    Claims extractAllClaims(String token);

    Boolean isTokenValid(String token, String email);
}
