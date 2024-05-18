package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.view.JwtResponse;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

public interface JwtService {
    String generateToken(String email, Map<String, Object> extraClaims);

    String extractEmail(String token);

    Claims extractAllClaims(String token);

    Boolean isTokenValid(String token, String email);

    JwtResponse generateToken(String email, List<String> roles);
}
