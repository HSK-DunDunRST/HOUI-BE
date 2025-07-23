package com.gate.houi.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.houi.backend.data.entityType.ReceptionEntity;
import com.gate.houi.backend.data.enumType.ReceptionType;

@Repository
public interface ReceptionRepository extends JpaRepository<ReceptionEntity, Long> {

    // ReceptionEntity 객체의 studentUuid 항목으로 조회
    List<ReceptionEntity> findByStudentUuid(UUID studentUuid);
    
    // studentUuid로 조회하되 생성시간 내림차순으로 정렬 (최신순)
    List<ReceptionEntity> findByStudentUuidOrderByCreatedAtDesc(UUID studentUuid);
    
    // 대기중인 접수들을 생성시간 순으로 조회
    List<ReceptionEntity> findByReceptionTypeOrderByCreatedAtDesc(ReceptionType receptionStatus);
    
    // 대기중인 접수 개수 조회
    long countByReceptionType(ReceptionType receptionStatus);

}
