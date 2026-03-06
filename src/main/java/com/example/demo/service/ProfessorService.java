package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.io.AdminProfileResponse;
import com.example.demo.io.ProfessorRequest;
import com.example.demo.io.ProfessorResponse;

public interface ProfessorService {
     ProfessorResponse createProfessor(ProfessorRequest request,MultipartFile multipartFile);
     List<AdminProfileResponse> getAdminProfessors();
}
