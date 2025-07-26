package com.gate.houi.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.houi.backend.data.entityType.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    
    // 학생 UUID로 리프레시 토큰 찾기
    Optional<RefreshTokenEntity> findByAccountUuid(UUID accountUuid);
    
    // 리프레시 토큰 문자열로 찾기
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
    
    // 학생 UUID로 리프레시 토큰 삭제
    void deleteByAccountUuid(UUID accoutnUuid);
}
