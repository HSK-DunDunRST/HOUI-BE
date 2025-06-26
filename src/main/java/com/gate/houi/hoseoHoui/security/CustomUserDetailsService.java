package com.gate.houi.hoseoHoui.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gate.houi.hoseoHoui.domain.entity.StudentEntity;
import com.gate.houi.hoseoHoui.repository.StudentRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // OAuth ID로 사용자를 찾습니다 (username에는 OAuth ID가 전달됨)
        StudentEntity studentEntity = studentRepository.findByOauthId(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with oauth id: " + username));

        return new org.springframework.security.core.userdetails.User(
                studentEntity.getOauthId(),
                "", // OAuth 인증이므로 비밀번호는 필요 없음
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}