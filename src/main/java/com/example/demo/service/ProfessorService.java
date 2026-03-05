package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.ProfessorEntity;
import com.example.demo.io.ProfessorRequest;
import com.example.demo.io.ProfessorResponse;
import java.util.List;

public interface ProfessorService {
     ProfessorResponse createProfessor(ProfessorRequest request,MultipartFile multipartFile);
     List<ProfessorEntity> getAdminProfessors();
}
