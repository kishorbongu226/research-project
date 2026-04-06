package com.example.demo.service.implementation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Enum.ApplicationStatus;
import com.example.demo.Enum.ProjectStatus;
import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.ProfessorEntity;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.entity.StudentEntity;
import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationsServiceImpl implements ApplicationService {

    private static final String MORNING_SLOT = "10:15 AM to 11:00 AM";
    private static final String AFTERNOON_SLOT = "2:00 PM to 2:40 PM";
    private static final int DEFAULT_TEAM_SIZE = 6;

    private final ApplicationRepository applicationRepository;
    private final ProjectRepository projectRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final FileUploadService fileUploadService;

    private static final Logger logger =
            LoggerFactory.getLogger(ApplicationsServiceImpl.class);

    @Override
    public ApplicationResponse createApplication(
            ApplicationRequest request,
            MultipartFile file) {

        logger.info("Starting application creation for registerNo: {}", request.getRegisterNo());

        String resumeURL = fileUploadService.uploadFile(file);
        logger.info("Resume uploaded successfully. URL: {}", resumeURL);

        ProjectEntity project = projectRepository
                .findByProjectId(request.getProjectId())
                .orElseThrow(() -> {
                    logger.error("Project not found: {}", request.getProjectId());
                    return new RuntimeException("Project not found");
                });

        if (applicationRepository.existsByStudent_RegisterNoAndProject_ProjectId(
                request.getRegisterNo(),
                request.getProjectId()
        )) {
            throw new RuntimeException("You have already applied for this project");
        }

        validateProjectCanAcceptApplications(project);

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
                .projectId(application.getProject().getProjectId())
                .decisionDate(application.getDecisionDate())
                .slotDate(application.getSlotDate())
                .morningSlot(application.getMorningSlot())
                .afternoonSlot(application.getAfternoonSlot())
                .status(application.getStatus())
                .profileImageUrl(application.getStudent().getProfileImageUrl())
                .centerName(application.getProject().getCenter().getName())
                .projectName(application.getProject().getTitle())
                .projectStatus(application.getProject().getProjectStatus())
                .projectImageUrl(application.getProject().getImageUrl())
                .build();
    }

    @Override
    public void approveApplication(String applicationId, Long professorID) {

        ApplicationEntity application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getProject().getDirector().getId().equals(professorID)) {
            throw new RuntimeException("Only project creator can approve this application");
        }

        if (application.getStatus() == ApplicationStatus.APPROVED) {
            return;
        }

        List<ApplicationEntity> existingApprovedEntries =
                applicationRepository.findByStudent_RegisterNoAndProject_ProjectIdAndStatus(
                        application.getStudent().getRegisterNo(),
                        application.getProject().getProjectId(),
                        ApplicationStatus.APPROVED
                );
        boolean studentAlreadyInProject = existingApprovedEntries.stream()
                .anyMatch(existing -> !existing.getId().equals(application.getId()));

        if (studentAlreadyInProject) {
            throw new RuntimeException("Student is already added to this project");
        }

        ensureTeamCapacity(application.getProject());

        LocalDate decisionDate = LocalDate.now();
        LocalDate slotDate = getNextWorkingDay(decisionDate.plusDays(1));

        application.setStatus(ApplicationStatus.APPROVED);
        application.setDecisionDate(decisionDate);
        application.setSlotDate(slotDate);
        application.setMorningSlot(MORNING_SLOT);
        application.setAfternoonSlot(AFTERNOON_SLOT);

        applicationRepository.save(application);
    }

    @Override
    public void declineApplication(String applicationId, Long professorID) {

        ApplicationEntity application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getProject().getDirector().getId().equals(professorID)) {
            throw new RuntimeException("Only project creator can decline this application");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        application.setDecisionDate(LocalDate.now());
        application.setSlotDate(null);
        application.setMorningSlot(null);
        application.setAfternoonSlot(null);

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

    @Override
    public List<ApplicationResponse> getStudentsByProject(String projectId) {

        List<ApplicationEntity> applications =
                applicationRepository.findByProject_ProjectIdAndStatus(
                        projectId,
                        ApplicationStatus.APPROVED
                );

        return applications.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<ApplicationResponse> getApplicationsByStudent(String registerNo) {

        List<ApplicationEntity> applications =
                applicationRepository.findByStudent_RegisterNo(registerNo);

        return applications.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<ApplicationResponse> getProjectsByStudent(String registerNo) {

        List<ApplicationEntity> applications =
                applicationRepository.findByStudent_RegisterNoAndStatus(
                        registerNo,
                        ApplicationStatus.APPROVED
                );

        return applications.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public void removeStudentFromProject(String projectId, String applicationId, String professorRegisterNo) {

        ApplicationEntity application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getProject().getProjectId().equals(projectId)) {
            throw new RuntimeException("Application does not belong to this project");
        }

        ProfessorEntity professor = findProfessorByIdentifier(professorRegisterNo)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        if (!application.getProject().getDirector().getId().equals(professor.getId())) {
            throw new RuntimeException("Only the project owner can remove students from this project");
        }

        applicationRepository.delete(application);
    }

    private LocalDate getNextWorkingDay(LocalDate date) {
        LocalDate nextDate = date;

        while (nextDate.getDayOfWeek() == DayOfWeek.SATURDAY
                || nextDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            nextDate = nextDate.plusDays(1);
        }

        return nextDate;
    }

    private void validateProjectCanAcceptApplications(ProjectEntity project) {
        if (project.getProjectStatus() == ProjectStatus.COMPLETED) {
            throw new RuntimeException("Completed projects do not accept new applications");
        }

        ensureTeamCapacity(project);
    }

    private void ensureTeamCapacity(ProjectEntity project) {
        int allowedTeamSize = project.getTeamSize() != null ? project.getTeamSize() : DEFAULT_TEAM_SIZE;
        long approvedCount = applicationRepository.countByProject_ProjectIdAndStatus(
                project.getProjectId(),
                ApplicationStatus.APPROVED
        );

        if (approvedCount >= allowedTeamSize) {
            throw new RuntimeException("This project team is already full");
        }
    }

    private java.util.Optional<ProfessorEntity> findProfessorByIdentifier(String identifier) {
        return professorRepository.findByOfficialEmail(identifier)
                .or(() -> professorRepository.findByRegisterNo(identifier));
    }
}
