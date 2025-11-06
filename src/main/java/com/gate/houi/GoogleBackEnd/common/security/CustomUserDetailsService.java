package com.gate.houi.GoogleBackEnd.common.security;

import com.gate.houi.be.apiPayload.code.exception.BaseException;
import com.gate.houi.be.apiPayload.code.status.ErrorType;
import com.gate.houi.be.entity.UserEntity;
import com.gate.houi.be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userPk)  {
        UserEntity userEntity = userRepository.findById(Long.parseLong(userPk))
                .orElseThrow(() -> new BaseException(ErrorType.USER_NOT_FOUND));
        return new CustomUserDetail(userEntity);
    }	// 위에서 생성한 CustomUserDetails Class
}