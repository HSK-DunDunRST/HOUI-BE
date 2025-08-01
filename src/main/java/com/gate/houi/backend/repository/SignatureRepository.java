package com.gate.houi.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.houi.backend.data.entityType.SignatureEntity;


@Repository
public interface SignatureRepository extends JpaRepository<SignatureEntity, Long> {
    SignatureEntity findByStudentUuid(UUID studentUuid);
}
