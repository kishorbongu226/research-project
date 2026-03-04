package com.example.demo.service.implementation;

import java.util.UUID;
import java.security.Principal;
import java.util.List;

import com.example.demo.service.ApplicationService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Enum.ApplicationStatus;
import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.entity.StudentEntity;
import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.StudentRepository;

import com.example.demo.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ApplicationsServiceImpl implements ApplicationService{
    
    private final ApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;
   
    private final FileUploadService fileUploadService;
    private static final Logger logger =
        LoggerFactory.getLogger(ApplicationsServiceImpl.class);

   @Override
public ApplicationResponse createApplication(
        ApplicationRequest request,
        MultipartFile file) {

    logger.info("Starting application creation for registerNo: {}", request.getRegisterNo());



    // 2️⃣ Upload Resume
    String resumeURL = fileUploadService.uploadFile(file);
    logger.info("Resume uploaded successfully. URL: {}", resumeURL);

    // 3️⃣ Get Project
    ProjectEntity project = projectRepository
            .findByProjectId(request.getProjectId())
            .orElseThrow(() -> {
                logger.error("Project not found: {}", request.getProjectId());
                return new RuntimeException("Project not found");
            });

    // 4️⃣ Find or Create Student (UPSERT)
    StudentEntity student = studentRepository
            .findByRegisterNo(request.getRegisterNo())
            .orElseGet(() -> {

                logger.info("Student not found. Creating new student.");

                StudentEntity newStudent = StudentEntity.builder()
                        .registerNo(request.getRegisterNo())
                        .name(request.getName())
                        .branch(request.getBranch())
                        .year(request.getYear())
                        .course(request.getCourse())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
                        .build();

                return studentRepository.save(newStudent);
            });

    // 5️⃣ Create Application
    ApplicationEntity application = ApplicationEntity.builder()
            .applicationId(UUID.randomUUID().toString())
            .student(student)
            .project(project)
            .graduation(request.getGraduation())
            .resumeURL(resumeURL)
            .status(ApplicationStatus.PENDING)
            .build();

    application = applicationRepository.save(application);

    logger.info("Application saved successfully with ID: {}", application.getId());

    return convertToResponse(application);
}

   
    // 🔹 Convert Request → Entity
    // private ApplicationEntity convertToEntity(ApplicationRequest request) {
    //     String uniqueID = UUID.randomUUID().toString();
    //     return ApplicationEntity.builder()
    //             .applicationId(uniqueID)
    //             .name(request.getName())
    //             .registerNo(request.getRegisterNo())
    //             .branch(request.getBranch())
    //             .course(request.getCourse())
    //             .email(request.getEmail())
    //             .phoneNumber(request.getPhoneNumber())
    //             .graduation(request.getGraduation())
    //             .Year(request.getYear())
    //             .build();
    // }

    // 🔹 Convert Entity → Response
    private ApplicationResponse convertToResponse(ApplicationEntity application) {

    return ApplicationResponse.builder()
            .id(application.getId())
            .applicationId(application.getApplicationId())

            .name(application.getStudent().getName())
            .registerNo(application.getStudent().getRegisterNo())
            .branch(application.getStudent().getBranch())
            .course(application.getStudent().getCourse())
            .email(application.getStudent().getEmail())
            .phoneNumber(application.getStudent().getPhoneNumber())
            .year(application.getStudent().getYear())
            .graduation(application.getGraduation())
            .resumeURL(application.getResumeURL())
            .status(application.getStatus())

            .centerName(application.getProject().getCenter().getName())
            .projectName(application.getProject().getTitle())

            .build();
}


    @Override
public void approveApplication(String applicationId, Long professorID) {

    ApplicationEntity application = applicationRepository.findByApplicationId(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found"));

    // 🔥 Only project creator can approve
    if (!application.getProject()
            .getDirector()
            .getId()
            .equals(professorID)) {

        throw new RuntimeException("Only project creator can approve this application");
    }

    application.setStatus(ApplicationStatus.APPROVED);

    applicationRepository.save(application);
}


@Override
public void declineApplication(String applicationId, Long professorID) {

    ApplicationEntity application = applicationRepository.findByApplicationId(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found"));

    // 🔥 Only project creator can decline
    if (!application.getProject()
            .getDirector()
            .getId()
            .equals(professorID)) {

        throw new RuntimeException("Only project creator can decline this application");
    }

    application.setStatus(ApplicationStatus.REJECTED);

    applicationRepository.save(application);
}

@Override
public List<ApplicationResponse> getPendingApplications(Long professorId) {

    List<ApplicationEntity> applications =
            applicationRepository.findByProject_Director_IdAndStatus(
                    professorId,
                    ApplicationStatus.PENDING
            );

    return applications.stream()
            .map(this::convertToResponse)
            .toList();
}

@Override
public List<ApplicationResponse> getApprovedApplications(Long professorId) {

    List<ApplicationEntity> applications =
            applicationRepository.findByProject_Director_IdAndStatus(
                    professorId,
                    ApplicationStatus.APPROVED
            );

    return applications.stream()
            .map(this::convertToResponse)
            .toList();
}

}



