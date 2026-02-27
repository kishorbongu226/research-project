package com.example.demo.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.ApplicationEntity;
import com.example.demo.entity.CenterEntity;
import com.example.demo.entity.ProfessorEntity;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.io.ProjectRequest;
import com.example.demo.io.ProjectResponse;
import com.example.demo.repository.CenterRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ApplicationsServiceImpl implements ApplicationService{
    
    private final ProjectRepository projectRepository;
    private final CenterRepository centerRepository;
    private final ProfessorRepository professorRepository;
    private final FileUploadService fileUploadService;

    @Override
    public ApplicationResponse createApplication(ApplicationRequest request,MultipartFile file) {
        String imgUrl = fileUploadService.uploadFile(file);

      

        ApplicationEntity project = convertToEntity(request);
        project.setCenter(center);
        project.setImageUrl(imgUrl);
        project.setDirector(professor);

        project = projectRepository.save(project);

        return convertToResponse(project);
    }

    @Override
    public void deleteProject(Long projectId, Long professorId) {

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getDirector().getId().equals(professorId)) {
            throw new RuntimeException("Only the director can delete this project");
        }

        projectRepository.delete(project);
    }

    @Override
    public List<ProjectResponse> getProjectsByCenter(String centerId) {

        List<ProjectEntity> projects =
                projectRepository.findByCenter_CenterId(centerId);

        return projects.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 🔹 Convert Request → Entity
    private ApplicationEntity convertToEntity(ApplicationRequest request) {
        
        return ApplicationEntity.builder()
                .applicationId(request.getApplicationId())
                .resumeURL(request.getResumeURL())
                .branch(request.getBranch())
                .course(request.getCourse())
                .email(request.getEmail())
                .phoneNo(request.getPhoneNumber())
                .graduation(request.getGraduation())
                .year(request.getYear())
                .build();
    }

    // 🔹 Convert Entity → Response
    private ProjectResponse convertToResponse(ProjectEntity project) {
        return ProjectResponse.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .description(project.getDescription())
                .imageUrl(project.getImageUrl())
                .projectStatus(project.getProjectStatus())
                .responsibilities(project.getResponsibilities())
                .skillRequirements(project.getSkillRequirements())
                .centerName(project.getCenter().getName())
                .directorName(project.getDirector().getName())
                .build();
    }
}



