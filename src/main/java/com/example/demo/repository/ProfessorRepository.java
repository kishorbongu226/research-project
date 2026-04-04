package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ProfessorEntity;

import java.util.List;

import com.example.demo.Enum.Role;

public interface ProfessorRepository extends JpaRepository<ProfessorEntity, Long>{

    Optional<ProfessorEntity> findByRegisterNo(String professorID);
    Optional<ProfessorEntity> findByOfficialEmail(String officialEmail);
    List<ProfessorEntity> findByRole(Role role);
}
