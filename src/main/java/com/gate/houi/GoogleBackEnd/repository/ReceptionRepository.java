package com.gate.houi.GoogleBackEnd.repository;

import com.gate.houi.GoogleBackEnd.entity.ReceptionEntity;
import com.gate.houi.GoogleBackEnd.entity.UserEntity;
import com.gate.houi.GoogleBackEnd.entity.enums.Reception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReceptionRepository extends JpaRepository<ReceptionEntity, Long> {
    // 대기중인 접수 개수 조회
    int countByReceptionType(Reception reception);
    List<ReceptionEntity> findAllByUserEntityAndReceptionTypeOrderByCreatedAtDesc(UserEntity userEntity, Reception receptionType);
}
