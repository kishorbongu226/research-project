package com.example.demo.service.implementation;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Enum.ApplicationStatus;
import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.ProjectRepository;

import com.example.demo.service.ApplicationService;
import com.example.demo.service.FileUploadService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor

public class ApplicationsServiceImpl implements ApplicationService{
    
    private final ApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;
   
    private final FileUploadService fileUploadService;
    private static final Logger logger =
        LoggerFactory.getLogger(ApplicationsServiceImpl.class);

    @Override
public ApplicationResponse createApplication(
        ApplicationRequest request,
        MultipartFile file) {

    logger.info("Starting application creation for student: {}", request.getRegisterNo());

    String resumeURL = fileUploadService.uploadFile(file);
    logger.info("Resume uploaded successfully. URL: {}", resumeURL);

    ProjectEntity project = projectRepository
            .findByProjectId(request.getProjectId())
            .orElseThrow(() -> {
                logger.error("Project not found with projectId: {}", request.getProjectId());
                return new RuntimeException("Project not found");
            });

    logger.info("Project found: {}", project.getProjectId());

    ApplicationEntity application = convertToEntity(request);

    application.setResumeURL(resumeURL);
    application.setProject(project);
    application.setStatus(ApplicationStatus.PENDING);

    application = applicationRepository.save(application);

    logger.info("Application saved successfully with ID: {}", application.getId());

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


    @Override
public void approveApplication(Long applicationId, Long professorId) {

    ApplicationEntity application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found"));

    // 🔥 Only project creator can approve
    if (!application.getProject()
            .getDirector()
            .getId()
            .equals(professorId)) {

        throw new RuntimeException("Only project creator can approve this application");
    }

    application.setStatus(ApplicationStatus.APPROVED);

    applicationRepository.save(application);
}


@Override
public void declineApplication(Long applicationId, Long professorId) {

    ApplicationEntity application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found"));

    // 🔥 Only project creator can decline
    if (!application.getProject()
            .getDirector()
            .getId()
            .equals(professorId)) {

        throw new RuntimeException("Only project creator can decline this application");
    }

    application.setStatus(ApplicationStatus.REJECTED);

    applicationRepository.save(application);
}
}



