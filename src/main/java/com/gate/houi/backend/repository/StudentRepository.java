package com.gate.houi.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gate.houi.backend.data.entityType.StudentEntity;

import java.util.Optional;
import java.util.UUID;


public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
    Optional<StudentEntity> findByOauthId(String oauthId);
    Optional<StudentEntity> findByStudentUuid(UUID studentUuid);
}