package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.view.JwtResponse;
import com.nashtech.cellphonesfake.view.LoginVm;
import com.nashtech.cellphonesfake.view.RegisterPostVm;


public interface UserService {
    JwtResponse login(LoginVm loginVm);

    String register(RegisterPostVm userRequest);

    User findByEmail(String email);

    User findById(int id);
}
