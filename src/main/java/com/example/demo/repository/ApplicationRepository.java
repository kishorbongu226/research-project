package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Enum.ApplicationStatus;
import com.example.demo.entity.ApplicationEntity;


public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long>{

    List<ApplicationEntity> findByProject_ProjectId(String projectId);
    List<ApplicationEntity> findByProject_Director_Id(Long professorId);
    List<ApplicationEntity> findByProject_Director_IdAndStatus(Long professorId,ApplicationStatus status);
    Optional<ApplicationEntity> findByApplicationId(String applicationId);
    List<ApplicationEntity> findByProject_ProjectIdAndStatus(String projectId,ApplicationStatus status);
    List<ApplicationEntity> findByStudent_RegisterNoAndStatus(String registerNo, ApplicationStatus status);
    
}