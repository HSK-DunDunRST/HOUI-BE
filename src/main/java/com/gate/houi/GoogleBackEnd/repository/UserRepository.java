package com.gate.houi.GoogleBackEnd.repository;

import com.gate.houi.GoogleBackEnd.entity.UserEntity;
import com.gate.houi.GoogleBackEnd.entity.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserEmail(String email);
    Optional<UserEntity> findByUserEmailAndOauthProvider(String email, Provider provider);
}
