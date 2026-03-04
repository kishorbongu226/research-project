package com.example.demo.service;

import com.example.demo.io.ProjectRequest;
import com.example.demo.io.ProjectResponse;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest request,MultipartFile file,Principal principal);
    void deleteProject(Long projectId, Long professorId);
    List<ProjectResponse> getProjectsByCenter(String centerId);
    ProjectResponse getProjectByProjectId(String projectId);
    List<ProjectResponse> getAllProjects();
}