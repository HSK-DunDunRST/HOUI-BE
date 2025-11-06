package com.gate.houi.GoogleBackEnd.common.security;

import com.gate.houi.be.entity.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class CustomUserDetail implements UserDetails {
    private final UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 권한 관리가 없으므로 null 또는 빈 리스트 반환
    }

    @Override
    public String getUsername() {
        return String.valueOf(userEntity.getId());
    }
    @Override
    public String getPassword() {
        return String.valueOf(userEntity.getUserPwd());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}