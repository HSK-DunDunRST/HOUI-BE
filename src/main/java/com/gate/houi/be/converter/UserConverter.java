package com.gate.houi.be.converter;

import com.gate.houi.be.dto.res.UserDtoRes;
import com.gate.houi.be.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public static UserDtoRes.UserLoginRes signInRes(UserEntity userEntity, String accessToken, String refreshToken, String name) {
        return UserDtoRes.UserLoginRes.builder()
                .id(userEntity.getId())
                .email(userEntity.getUserEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(name)
                .build();
    }
}
