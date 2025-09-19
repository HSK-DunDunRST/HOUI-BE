package com.gate.houi.be.repository;

import com.gate.houi.be.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReopository extends JpaRepository<UserEntity,Integer> {

}
