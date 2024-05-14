package com.nashtech.cellphonesfake.service.impl;

import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.service.UserService;
import com.nashtech.cellphonesfake.view.JwtResponse;
import com.nashtech.cellphonesfake.view.LoginVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Override
    public JwtResponse login(LoginVm loginVm) {
        return null;
    }

    @Override
    public void register(LoginVm userRequest) {

    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findById(int id) {
        return null;
    }
}
