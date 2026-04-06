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
    List<ApplicationEntity> findByStudent_RegisterNo(String registerNo);
    List<ApplicationEntity> findByStudent_RegisterNoAndStatus(String registerNo, ApplicationStatus status);
    long countByProject_ProjectIdAndStatus(String projectId, ApplicationStatus status);
    boolean existsByStudent_RegisterNoAndProject_ProjectId(String registerNo, String projectId);
    List<ApplicationEntity> findByStudent_RegisterNoAndProject_ProjectIdAndStatus(
            String registerNo,
            String projectId,
            ApplicationStatus status
    );
    
}
