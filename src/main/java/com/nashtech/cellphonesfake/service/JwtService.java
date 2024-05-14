package com.nashtech.cellphonesfake.service;
import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwtService {
    String generateToken(String email, Map<String, Object> extraClaims);
    String extractEmail(String token);
    Claims extractAllClaims(String token);
    Boolean isTokenValid(String token, String email);
}
