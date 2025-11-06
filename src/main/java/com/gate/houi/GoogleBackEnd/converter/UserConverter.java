package com.gate.houi.GoogleBackEnd.converter;

import com.gate.houi.GoogleBackEnd.dto.res.UserDtoRes;
import com.gate.houi.GoogleBackEnd.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public static UserDtoRes.UserLoginRes signInRes(UserEntity userEntity, String accessToken, String refreshToken, String name) {
        return UserDtoRes.UserLoginRes.builder()
                .name(name)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
