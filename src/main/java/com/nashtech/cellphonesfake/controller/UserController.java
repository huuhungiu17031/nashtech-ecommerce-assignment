package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.UserService;
import com.nashtech.cellphonesfake.view.JwtResponse;
import com.nashtech.cellphonesfake.view.LoginVm;
import com.nashtech.cellphonesfake.view.RegisterPostVm;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(@Lazy UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterPostVm registerPostVm) {
        return new ResponseEntity<>(userService.register(registerPostVm), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginVm loginVm) {
        JwtResponse jwtResponse = userService.login(loginVm);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }
}
