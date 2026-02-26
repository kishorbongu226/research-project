package com.example.demo.controller;

import com.example.demo.io.CenterRequest;
import com.example.demo.io.CenterResponse;
import com.example.demo.io.ProjectRequest;
import com.example.demo.io.ProjectResponse;
import com.example.demo.service.ProjectService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    //  Create Project (Director Only)
    


    @PostMapping("/projects/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse addCategory(@RequestPart("project") String projectString,
                                        @RequestPart("file") MultipartFile file)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ProjectRequest request=null;
        try {
            request=objectMapper.readValue(projectString, ProjectRequest.class);
            return projectService.createProject(request, file);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occured while parsing the json"+e.getMessage());
        }
        

    }

    //  Delete Project (Director Only)
    @DeleteMapping("/{projectId}")
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
}