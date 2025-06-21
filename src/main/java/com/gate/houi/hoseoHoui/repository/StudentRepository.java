package com.gate.houi.hoseoHoui.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gate.houi.hoseoHoui.domain.entity.StudentEntity;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByOauthId(String oauthId);
}