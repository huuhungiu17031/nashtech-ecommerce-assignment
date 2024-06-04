package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.enumeration.TokenType;
import com.nashtech.cellphonesfake.exception.BadRequestException;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.exception.UnAuthorizedException;
import com.nashtech.cellphonesfake.mapper.UserMapper;
import com.nashtech.cellphonesfake.model.Role;
import com.nashtech.cellphonesfake.model.Token;
import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.repository.RoleRepository;
import com.nashtech.cellphonesfake.repository.TokenRepository;
import com.nashtech.cellphonesfake.repository.UserRepository;
import com.nashtech.cellphonesfake.service.CartService;
import com.nashtech.cellphonesfake.service.JwtService;
import com.nashtech.cellphonesfake.service.UserService;
import com.nashtech.cellphonesfake.view.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class UserServiceImpl implements UserService, LogoutHandler {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    @Value("${role_admin}")
    private String roleAdmin;
    @Value("${role_user}")
    private String roleUser;
    private static final String POST_FIX = "ROLE_";
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CartService cartService,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            TokenRepository tokenRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
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
        if (Boolean.TRUE.equals(user.getIsBlocked())) throw new BadRequestException("Your account is blocked. Please contact your admin");
        String accessToken = jwtService.generateAccessToken(loginVm.email(), listRoles);
        String refreshToken = jwtService.generateRefreshToken(loginVm.email());
        revokeAllUserTokens(user);
        Token token = saveUserToken(user, accessToken);
        return new JwtResponse(accessToken, refreshToken, user.getEmail(), user.getId(), token.getTokenType());
    }

    @Override
    public JwtResponse loginBackOffice(LoginVm loginVm) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginVm.email(),
                loginVm.password()
        );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        List<String> listRoles = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .toList();
        boolean isAdmin = listRoles.stream()
                .anyMatch(role -> role.equals(POST_FIX + roleAdmin));
        if (!isAdmin) throw new UnAuthorizedException("You are not admin");
        String accessToken = jwtService.generateAccessToken(loginVm.email(), listRoles);
        String refreshToken = jwtService.generateRefreshToken(loginVm.email());
        User user = findByEmail(loginVm.email());
        revokeAllUserTokens(user);
        Token token = saveUserToken(user, accessToken);
        return new JwtResponse(accessToken, refreshToken, loginVm.email(), user.getId(), token.getTokenType());
    }

    @Transactional
    @Override
    public String register(RegisterPostVm registerPostVm) {
        User user = UserMapper.INSTANCE.toUser(registerPostVm);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByRoleName(POST_FIX + roleUser).orElseThrow(() -> new NotFoundException("No role was found"));
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setListRole(roles);
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

    private Token saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .value(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        return tokenRepository.save(token);
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

    @Override
    public PaginationVm getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pageUser = userRepository.findAll(pageable);
        Stream<UserVm> userVms = pageUser.stream().map(user -> {
            boolean isAdmin = user.getListRole().stream()
                    .anyMatch(role -> role.getRoleName().equals(POST_FIX + roleAdmin));
            return new UserVm(
                    user.getId(),
                    user.getEmail(),
                    user.getListRole().stream().map(Role::getRoleName).toList(),
                    isAdmin,
                    user.getIsBlocked()
            );
        });
        return new PaginationVm(
                pageUser.getTotalPages(),
                pageUser.getTotalElements(),
                pageUser.getSize(),
                pageUser.getNumber(),
                userVms
        );
    }

    @Override
    public JwtResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) throw new UnAuthorizedException("Cannot find access token");
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractEmail(refreshToken);
        if (userEmail == null) throw new UnAuthorizedException("Invalid refresh token");
        User user = findByEmail(userEmail);
        if (Boolean.TRUE.equals(user.getIsBlocked())) throw new UnAuthorizedException("User is blocked");
        if (Boolean.TRUE.equals(jwtService.isTokenValid(refreshToken, userEmail))) {
            revokeAllUserTokens(user);
            String accessToken = jwtService.generateAccessToken(
                    userEmail,
                    user.getListRole().stream().map(Role::getRoleName).toList()
            );
            String newRefreshToken = jwtService.generateRefreshToken(userEmail);
            Token token = saveUserToken(
                    user,
                    accessToken
            );
            return new JwtResponse(accessToken, newRefreshToken, user.getEmail(), user.getId(), token.getTokenType());
        }
        throw new BadRequestException("Invalid refresh token");
    }

    @Transactional
    @Override
    public void updateUser(UserVm userVm) {
        User user = findByEmail(userVm.email());
        user.setIsBlocked(userVm.isBlock());
        revokeAllUserTokens(user);
        userRepository.save(user);
    }
}
