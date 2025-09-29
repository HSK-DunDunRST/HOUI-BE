package com.gate.houi.be.repository;

import com.gate.houi.be.entity.SignatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureRepository extends JpaRepository<SignatureEntity, Long> {

}
