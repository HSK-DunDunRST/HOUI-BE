package com.gate.houi.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.houi.backend.data.entityType.UseHistoryEntity;

@Repository
public interface UseHistoryRepository extends JpaRepository<UseHistoryEntity, Long> {

    // UseHistoryEntity 객체의 studentUuid 항목으로 조회
    List<UseHistoryEntity> findByStudentUuid(UUID studentUuid);
}
