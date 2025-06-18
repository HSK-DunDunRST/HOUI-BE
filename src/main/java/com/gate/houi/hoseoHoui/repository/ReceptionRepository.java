package com.gate.houi.hoseoHoui.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gate.houi.hoseoHoui.domain.entitySet.ReceptionEntity;

@Repository
public interface ReceptionRepository extends JpaRepository<ReceptionEntity, Long> {

    // ReceptionEntity 객체의 studentId 항목으로 조회
    List<ReceptionEntity> findByStudentEntity_StudentId(Long studentId);

}
