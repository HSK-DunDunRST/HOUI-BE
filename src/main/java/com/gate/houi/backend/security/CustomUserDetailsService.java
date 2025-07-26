package com.gate.houi.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gate.houi.backend.data.entityType.AccountEntity;
import com.gate.houi.backend.repository.AccountRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // JWT 토큰에서는 OAuth ID가 전달되므로 OAuth ID로 사용자를 찾습니다
        AccountEntity accountEntity = accountRepository.findByOauthId(username)
                .orElseThrow(() -> new UsernameNotFoundException("OAuth ID로 사용자 조회 실패: " + username));

        // 사용자 권한 설정 (ROLE_ 접두사 추가)
        String role = "ROLE_" + accountEntity.getRole().name();
        
        // 일관성을 위해 OAuth ID를 그대로 사용 (보안상 더 안전)
        return new org.springframework.security.core.userdetails.User(
                accountEntity.getOauthId(), // OAuth ID 사용 (JWT 토큰과 일치)
                "", // OAuth 인증이므로 비밀번호는 필요 없음
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}