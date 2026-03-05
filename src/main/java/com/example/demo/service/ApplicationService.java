package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;

public interface ApplicationService {
    
        ApplicationResponse createApplication(ApplicationRequest request,MultipartFile file);
        void approveApplication(String applicationId, Long professorId);

void declineApplication(String applicationId, Long professorId);
List<ApplicationResponse> getPendingApplications(Long professorId);
List<ApplicationResponse> getApprovedApplications(Long professorId);
List<ApplicationResponse> getStudentsByProject(String projectId);

}
