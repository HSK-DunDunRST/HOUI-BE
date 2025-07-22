package com.gate.houi.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.houi.backend.data.entityType.NoticeEntity;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {
    Optional<NoticeEntity> findFirstByOrderByCreatedAtDesc();
}
