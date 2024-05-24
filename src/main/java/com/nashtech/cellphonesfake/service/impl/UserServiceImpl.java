package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.enumeration.TokenType;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.mapper.UserMapper;
import com.nashtech.cellphonesfake.model.Token;
import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.repository.TokenRepository;
import com.nashtech.cellphonesfake.repository.UserRepository;
import com.nashtech.cellphonesfake.service.CartService;
import com.nashtech.cellphonesfake.service.JwtService;
import com.nashtech.cellphonesfake.service.UserService;
import com.nashtech.cellphonesfake.view.JwtResponse;
import com.nashtech.cellphonesfake.view.LoginVm;
import com.nashtech.cellphonesfake.view.RegisterPostVm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService, LogoutHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CartService cartService, AuthenticationManager authenticationManager, JwtService jwtService, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public JwtResponse login(LoginVm loginVm) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginVm.email(),
                loginVm.password()
        );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        List<String> listRoles = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();
        User user = findByEmail(loginVm.email());
        String accessToken = jwtService.generateAccessToken(loginVm.email(), listRoles);
        String refreshToken = jwtService.generateRefreshToken(loginVm.email());
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public String register(RegisterPostVm registerPostVm) {
        User user = UserMapper.INSTANCE.toUser(registerPostVm);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        cartService.createCart(userRepository.save(user));
        return "Registered Successfully";
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Email", email)));
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .value(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new NotFoundException("Invalid JWT token");
        jwt = authHeader.substring(7);
        Token storedToken = tokenRepository.findByValue(jwt).orElseThrow(() -> new NotFoundException(String.format(Error.Message.RESOURCE_NOT_FOUND_BY_ID, "Token", jwt)));
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
