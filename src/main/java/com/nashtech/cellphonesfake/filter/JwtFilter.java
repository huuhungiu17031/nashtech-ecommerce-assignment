package com.nashtech.cellphonesfake.filter;

import com.nashtech.cellphonesfake.exception.BadRequestException;
import com.nashtech.cellphonesfake.exception.UnAuthorizedException;
import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.repository.TokenRepository;
import com.nashtech.cellphonesfake.service.JwtService;
import com.nashtech.cellphonesfake.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserService userService;

    public JwtFilter(JwtService jwtService, TokenRepository tokenRepository, UserService userService) {
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = new StringBuilder(authHeader).delete(0, 7).toString();
            email = jwtService.extractEmail(token);
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null && Boolean.TRUE.equals(jwtService.isTokenValid(token, email))) {
            User user = userService.findByEmail(email);
            if (user.getIsBlocked() == Boolean.TRUE) throw new UnAuthorizedException("Your account is blocked");
            Claims claims = jwtService.extractAllClaims(token);
            List<String> roles = (List<String>) claims.get("roles");
            Boolean isTokenValid = tokenRepository.findByValue(token)
                    .map(t -> !t.getExpired() && !t.getRevoked())
                    .orElse(false);
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            roles.forEach(role -> {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantedAuthorities.add(authority);
            });

            if (Boolean.TRUE.equals(isTokenValid)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        grantedAuthorities
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
