package com.gate.houi.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gate.houi.backend.data.entityType.AccountEntity;

import java.util.Optional;
import java.util.UUID;


public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByOauthId(String oauthId);
    Optional<AccountEntity> findByAccountEmail(String studentEmail);
    Optional<AccountEntity> findByAccountUuid(UUID accountUuid);
}