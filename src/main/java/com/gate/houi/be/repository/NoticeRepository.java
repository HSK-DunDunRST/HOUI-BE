package com.gate.houi.be.repository;

import com.gate.houi.be.entity.NoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity,Integer> {
    Optional<NoticeEntity> findFirstByOrderByCreatedAtDesc();
}
