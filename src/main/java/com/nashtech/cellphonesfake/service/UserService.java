package com.nashtech.cellphonesfake.service;

import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.view.JwtResponse;
import com.nashtech.cellphonesfake.view.LoginVm;


public interface UserService {
    JwtResponse login(LoginVm loginVm);

    void register(LoginVm userRequest);

    User findByEmail(String email);

    User findById (int id);
//    Boolean changePassword(ChangePasswordRequest changePasswordRequest);

//    void update(int id, UserInfoRequest userInfoRequest);
}
