package com.gate.houi.be.repository;

import com.gate.houi.be.entity.ReceptionEntity;
import com.gate.houi.be.entity.enums.Reception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReceptionRepository extends JpaRepository<ReceptionEntity, Long> {
    // 대기중인 접수 개수 조회
    int countByReceptionType(Reception reception);
}
