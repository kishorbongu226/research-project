package com.example.demo.service.implementation;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ApplicationsServiceImpl implements ApplicationService{
    
    private final ApplicationRepository applicationRepository;
   
    private final FileUploadService fileUploadService;

    @Override
    public ApplicationResponse createApplication(ApplicationRequest request,MultipartFile file) {
        String resumeURL = fileUploadService.uploadFile(file);

      

        ApplicationEntity application = convertToEntity(request);

        application.setResumeURL(resumeURL);

        application = applicationRepository.save(application);

        return convertToResponse(application);
    }


   
    // 🔹 Convert Request → Entity
    private ApplicationEntity convertToEntity(ApplicationRequest request) {
        String uniqueID = UUID.randomUUID().toString();
        return ApplicationEntity.builder()
                .applicationId(uniqueID)
                .name(request.getName())
                .registerNo(request.getRegisterNo())
                .branch(request.getBranch())
                .course(request.getCourse())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .graduation(request.getGraduation())
                .Year(request.getYear())
                .build();
    }

    // 🔹 Convert Entity → Response
    private ApplicationResponse convertToResponse(ApplicationEntity application) {
        return ApplicationResponse.builder()
                .Year(application.getYear())
                .branch(application.getBranch())
                .course(application.getCourse())
                .email(application.getEmail())
                .graduation(application.getGraduation())
                .name(application.getName())
                .resumeURL(application.getResumeURL())
                .registerNo(application.getRegisterNo())
                .phoneNumber(application.getPhoneNumber())
                .build();
    }
}



