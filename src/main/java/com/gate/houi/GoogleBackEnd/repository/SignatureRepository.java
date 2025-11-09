package com.gate.houi.GoogleBackEnd.repository;

import com.gate.houi.GoogleBackEnd.entity.SignatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignatureRepository extends JpaRepository<SignatureEntity, Long> {
    Optional<SignatureEntity> findByStudentId(String studentId);
}