package com.nashtech.cellphonesfake.view;

import com.nashtech.cellphonesfake.enumeration.TokenType;

import java.util.List;

public record JwtResponse(String accessToken, String refreshToken, String email, Long userId, TokenType type) {
}
