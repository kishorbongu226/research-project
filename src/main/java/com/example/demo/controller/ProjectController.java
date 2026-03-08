package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Enum.ProjectStatus;
import com.example.demo.entity.ProjectEntity;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.io.ProjectRequest;
import com.example.demo.io.ProjectResponse;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ApplicationService applicationService;
    private final ProjectRepository projectRepository;

    //  Create Project (Director Only)
    


    @PostMapping("/projects/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProjectResponse addCategory(@RequestPart("project") String projectString,
    @RequestPart("file") MultipartFile file,Principal principal)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ProjectRequest request=null;
        try {
            request=objectMapper.readValue(projectString, ProjectRequest.class);
            return projectService.createProject(request, file,principal);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occured while parsing the json"+e.getMessage());
        }
        
        
    }
    
    //  Delete Project (Director Only)
    @DeleteMapping("/projects/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProject(
            @PathVariable Long projectId,
            @RequestParam Long professorId) {

        projectService.deleteProject(projectId, professorId);
        return "Project deleted successfully";
    }

    //  Get All Projects By Center (using centerId String)
    @GetMapping("/center/{centerId}")
    public List<ProjectResponse> getProjectsByCenter(
            @PathVariable String centerId) {

        return projectService.getProjectsByCenter(centerId);
    }
    
    @GetMapping("/project/{projectId}/students")
public List<ApplicationResponse> getStudentsByProject(
        @PathVariable String projectId) {

    return applicationService.getStudentsByProject(projectId);
}

    @GetMapping("/projects")
public List<ProjectResponse> getAllProjects() {
    return projectService.getAllProjects();
}


    @GetMapping("/project/{projectId}")
public ProjectResponse getProjectById(@PathVariable String projectId) {
    return projectService.getProjectByProjectId(projectId);
}

@PostMapping("/project/completed/{projectId}")
public void  changeProjectStatus(@PathVariable String projectId){
    Optional<ProjectEntity> optionalProject = projectRepository.findByProjectId(projectId);
    ProjectEntity project = null;
    if(optionalProject.isPresent()){
        project = optionalProject.get();
    }
    project.setProjectStatus(ProjectStatus.COMPLETED);
    projectRepository.save(project);
    
}

}

// DONE.........