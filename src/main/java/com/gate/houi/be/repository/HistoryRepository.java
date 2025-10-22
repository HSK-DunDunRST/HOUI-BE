package com.gate.houi.be.repository;

import com.gate.houi.be.entity.HistoryEntity;
import com.gate.houi.be.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
    List<HistoryEntity> findAllByUserEntityOrderByCreatedAtDesc(UserEntity userEntity);
}
