package com.gate.houi.backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gate.houi.backend.data.entityType.AccountEntity;
import com.gate.houi.backend.data.entityType.RefreshTokenEntity;
import com.gate.houi.backend.dto.auth.JwtTokenResponse;
import com.gate.houi.backend.exception.AuthenticationException;
import com.gate.houi.backend.exception.TokenExpiredException;
import com.gate.houi.backend.exception.UserNotFoundException;
import com.gate.houi.backend.repository.AccountRepository;
import com.gate.houi.backend.repository.RefreshTokenRepository;
import com.gate.houi.backend.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    private final AccountRepository accountRepository;

    @Transactional
    public void saveRefreshToken(AccountEntity accountEntity, String refreshToken) {
        // 기존 리프레시 토큰이 있으면 업데이트, 없으면 새로 생성
        Optional<RefreshTokenEntity> existingToken = refreshTokenRepository.findByAccountUuid(accountEntity.getAccountUuid());
        
        if (existingToken.isPresent()) {
            // 기존 토큰 업데이트
            existingToken.get().update(refreshToken);
            refreshTokenRepository.save(existingToken.get());
        } else {
            // 새로운 토큰 생성
            RefreshTokenEntity newRefreshToken = RefreshTokenEntity.builder()
                    .accountUuid(accountEntity.getAccountUuid())
                    .refreshToken(refreshToken)
                    .build();
            refreshTokenRepository.save(newRefreshToken);
        }
    }

    @Transactional
    public JwtTokenResponse refreshAccessToken(String refreshToken) {
        // 리프레시 토큰으로 DB에서 조회
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenExpiredException());

        // 리프레시 토큰이 만료되었는지 확인
        if (jwtTokenProvider.isTokenExpired(refreshToken)) {
            // 만료된 토큰 삭제
            refreshTokenRepository.delete(refreshTokenEntity);
            throw new AuthenticationException();
        }

        // UUID로 AccountEntity 조회
        AccountEntity accountEntity = accountRepository.findByAccountUuid(refreshTokenEntity.getAccountUuid())
                .orElseThrow(() -> new UserNotFoundException());
        
        // 보안을 위해 OAuth ID를 JWT 토큰에 사용
        String newAccessToken = jwtTokenProvider.generateAccessToken(accountEntity.getOauthId());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(accountEntity.getOauthId());

        // 새로운 리프레시 토큰을 DB에 저장
        refreshTokenEntity.update(newRefreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        return JwtTokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional
    public void deleteRefreshToken(AccountEntity accountEntity) {
        refreshTokenRepository.deleteByAccountUuid(accountEntity.getAccountUuid());
    }

    @Transactional(readOnly = true)
    public RefreshTokenEntity findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken).orElse(null);
    }
}
