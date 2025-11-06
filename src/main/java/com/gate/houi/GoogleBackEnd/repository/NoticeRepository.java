package com.gate.houi.GoogleBackEnd.repository;

import com.gate.houi.GoogleBackEnd.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity,Integer> {
    Optional<NoticeEntity> findFirstByOrderByCreatedAtDesc();
}
