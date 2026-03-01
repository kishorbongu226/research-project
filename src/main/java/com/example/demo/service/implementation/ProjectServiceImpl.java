package com.example.demo.service.implementation;

import com.example.demo.entity.CenterEntity;
import com.example.demo.entity.ProfessorEntity;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.io.ProjectRequest;
import com.example.demo.io.ProjectResponse;
import com.example.demo.repository.CenterRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.service.FileUploadService;
import com.example.demo.service.ProjectService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final CenterRepository centerRepository;
    private final ProfessorRepository professorRepository;
    private final FileUploadService fileUploadService;

    @Override
    public ProjectResponse createProject(ProjectRequest request,MultipartFile file) {
        String imgUrl = fileUploadService.uploadFile(file);

        CenterEntity center = centerRepository.findByCenterId(request.getCenterId())
                .orElseThrow(() -> new RuntimeException("Center not found"));

        ProfessorEntity professor = professorRepository.findByRegisterNo(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        //  Validation: Only Center Director Can Create
        if (!center.getProfessor().getId().equals(professor.getId())) {
            throw new RuntimeException("Only the director of this center can create project");
        }

        ProjectEntity project = convertToEntity(request);
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
    private ProjectEntity convertToEntity(ProjectRequest request) {
        return ProjectEntity.builder()
                .projectId(request.getProjectId())
                .title(request.getTitle())
                .description(request.getDescription())
                .projectStatus(request.getProjectStatus())
                .responsibilities(request.getResponsibilities())
                .skillRequirements(request.getSkillRequirements())
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

    @Override
public ProjectResponse getProjectByProjectId(String projectId) {

    ProjectEntity project = projectRepository
            .findByProjectId(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));

    return convertToResponse(project);
}
@Override
public List<ProjectResponse> getAllProjects() {

    return projectRepository.findAll()
            .stream()
            .map(this::convertToResponse)
            .toList();
}
}