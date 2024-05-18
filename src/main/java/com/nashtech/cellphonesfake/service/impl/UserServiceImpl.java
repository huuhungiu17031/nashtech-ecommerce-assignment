package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.constant.Error;
import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.mapper.UserMapper;
import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.repository.UserRepository;
import com.nashtech.cellphonesfake.service.CartService;
import com.nashtech.cellphonesfake.service.JwtService;
import com.nashtech.cellphonesfake.service.UserService;
import com.nashtech.cellphonesfake.view.JwtResponse;
import com.nashtech.cellphonesfake.view.LoginVm;
import com.nashtech.cellphonesfake.view.RegisterPostVm;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CartService cartService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartService = cartService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
        return jwtService.generateToken(loginVm.email(), listRoles);
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

    @Override
    public User findById(int id) {
        return null;
    }
}
