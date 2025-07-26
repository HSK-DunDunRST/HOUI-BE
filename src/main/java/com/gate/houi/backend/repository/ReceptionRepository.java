package com.gate.houi.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.houi.backend.data.entityType.ReceptionEntity;
import com.gate.houi.backend.data.enumType.ReceptionType;

@Repository
public interface ReceptionRepository extends JpaRepository<ReceptionEntity, Long> {

    // accountUuid로 조회
    List<ReceptionEntity> findByAccountUuid(UUID accountUuid);
    
    // accountUuid로 조회하되 생성시간 오름차순으로 정렬 (가장 먼저 접수한 순)
    List<ReceptionEntity> findByAccountUuidOrderByCreatedAt(UUID accountUuid);
    
    // 대기중인 접수들을 생성시간 오름차순으로 조회 (가장 먼저 접수한 순)
    List<ReceptionEntity> findByReceptionTypeOrderByCreatedAt(ReceptionType receptionStatus);
    
    // 대기중인 접수 개수 조회
    long countByReceptionType(ReceptionType receptionStatus);
    
    // 특정 사용자의 처방 완료된 접수들 조회 (accountUuid 사용)
    List<ReceptionEntity> findByAccountUuidAndReceptionTypeOrderByCreatedAt(UUID accountUuid, ReceptionType receptionType);

}
