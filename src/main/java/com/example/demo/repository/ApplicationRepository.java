package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ApplicationEntity;


public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long>{

    List<ApplicationEntity> findByProject_ProjectId(String projectId);
    List<ApplicationEntity> findByProject_Director_Id(Long professorId);
    
}