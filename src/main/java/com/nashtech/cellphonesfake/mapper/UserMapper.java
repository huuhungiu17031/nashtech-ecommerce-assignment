package com.nashtech.cellphonesfake.mapper;

import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.view.RegisterPostVm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(RegisterPostVm registerPostVm);
}
