package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.io.ProjectResponse;

public interface ApplicationService {
    
        ApplicationResponse createApplication(ApplicationRequest request,MultipartFile file);
    void deleteApplication(Long projectId, Long professorId);
    List<ApplicationResponse> getApplications(String centerId);
}
