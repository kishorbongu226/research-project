package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.CenterEntity;

public interface CenterRepository extends JpaRepository<CenterEntity, Long>{

    Optional<CenterEntity> findByCenterId(String centerId);
    
}
