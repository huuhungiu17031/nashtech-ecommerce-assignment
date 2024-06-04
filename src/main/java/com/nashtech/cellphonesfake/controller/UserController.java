package com.nashtech.cellphonesfake.controller;

import com.nashtech.cellphonesfake.service.UserService;
import com.nashtech.cellphonesfake.view.JwtResponse;
import com.nashtech.cellphonesfake.view.LoginVm;
import com.nashtech.cellphonesfake.view.PaginationVm;
import com.nashtech.cellphonesfake.view.RegisterPostVm;
import com.nashtech.cellphonesfake.view.UserVm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login/backoffice")
    public ResponseEntity<JwtResponse> loginBackOffice(@Valid @RequestBody LoginVm loginVm) {
        JwtResponse jwtResponse = userService.loginBackOffice(loginVm);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        userService.logout(request, response, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(userService.refreshToken(request, response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PaginationVm> refreshToken(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return new ResponseEntity<>(userService.getUsers(page, size)
                , HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserVm userVm) {
        userService.updateUser(userVm);
        return new ResponseEntity<>("User updated", HttpStatus.OK);
    }
}
