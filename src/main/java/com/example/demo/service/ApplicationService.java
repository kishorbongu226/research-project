package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import java.util.List;

public interface ApplicationService {
    
        ApplicationResponse createApplication(ApplicationRequest request,MultipartFile file);
        void approveApplication(Long applicationId, Long professorId);

void declineApplication(Long applicationId, Long professorId);
List<ApplicationResponse> getPendingApplications(Long professorId);
List<ApplicationResponse> getApprovedApplications(Long professorId);

}
