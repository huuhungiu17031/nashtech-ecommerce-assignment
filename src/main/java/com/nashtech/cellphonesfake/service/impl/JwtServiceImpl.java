package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${token_secret_key}")
    private String secretKey;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    private String generateToken(String email, Map<String, Object> extraClaims, int expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String generateAccessToken(String email, List<String> roles) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", roles);
        int accessTokenValiditySeconds = 48 * 60 * 60 * 1000;
        return generateToken(email, extraClaims, accessTokenValiditySeconds);
    }


    @Override
    public String generateRefreshToken(String email) {
        Map<String, Object> extraClaims = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        extraClaims.put("uuid", uuid.toString());
        int refreshTokenValiditySeconds = 72 * 60 * 60 * 1000;
        return generateToken(email, extraClaims, refreshTokenValiditySeconds);
    }

    @Override
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    @Override
    public Boolean isTokenValid(String token, String email) {
        return !extractExpiration(token).before(new Date());
    }


}
