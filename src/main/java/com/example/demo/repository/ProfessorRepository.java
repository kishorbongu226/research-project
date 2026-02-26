package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProfessorEntity;

public interface ProfessorRepository extends JpaRepository<ProfessorEntity, Long>{

    Optional<ProfessorEntity> findByRegisterNo(String professorID);
    
}