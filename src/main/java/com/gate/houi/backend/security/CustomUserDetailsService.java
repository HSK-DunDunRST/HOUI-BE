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
        // OAuth ID로 사용자를 찾습니다 (username에는 OAuth ID가 전달됨)
        AccountEntity accountEntity = accountRepository.findByOauthId(username)
                .orElseThrow(() -> new UsernameNotFoundException("구글 토큰으로 사용자 조회 실패: " + username));

        return new org.springframework.security.core.userdetails.User(
                accountEntity.getOauthId(),
                "", // OAuth 인증이므로 비밀번호는 필요 없음
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}