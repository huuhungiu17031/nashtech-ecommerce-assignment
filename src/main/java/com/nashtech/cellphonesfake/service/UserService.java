package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.view.JwtResponse;
import com.nashtech.cellphonesfake.view.LoginVm;
import com.nashtech.cellphonesfake.view.RegisterPostVm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;


public interface UserService {
    JwtResponse login(LoginVm loginVm);

    String register(RegisterPostVm userRequest);

    User findByEmail(String email);

    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
