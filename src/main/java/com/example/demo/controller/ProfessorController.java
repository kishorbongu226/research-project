package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import com.example.demo.io.ApplicationResponse;


import com.example.demo.io.ApplicationResponse;
import com.example.demo.io.ProfessorRequest;
import com.example.demo.io.ProfessorResponse;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.ProfessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class ProfessorController {
    
    private final ProfessorService professorService;
    private final ApplicationService applicationService;
    

    @PostMapping("/professor/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfessorResponse addProfessor(@RequestPart("professor") String professorString,
                                        @RequestPart("file") MultipartFile file)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        ProfessorRequest request=null;
        try {
            request=objectMapper.readValue(professorString, ProfessorRequest.class);
            return professorService.createProfessor(request, file);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occured while parsing the json"+e.getMessage());
        }
        

    }



    @PutMapping("/application/{applicationId}/APPROVE")
public String approveApplication(
        @PathVariable Long applicationId,
        @RequestParam Long professorId) {

    applicationService.approveApplication(applicationId, professorId);
    return "Application Approved Successfully";
}


@PutMapping("/application/{applicationId}/DECLINE")
public String declineApplication(
        @PathVariable Long applicationId,
        @RequestParam Long professorId) {

    applicationService.declineApplication(applicationId, professorId);
    return "Application Declined Successfully";
}

@GetMapping("/applications/pending/{professorId}")
public List<ApplicationResponse> getPendingApplications(
        @PathVariable Long professorId) {

    return applicationService.getPendingApplications(professorId);
}

@GetMapping("/applications/approved/{professorId}")
public List<ApplicationResponse> getApprovedApplications(
        @PathVariable Long professorId) {

    return applicationService.getApprovedApplications(professorId);
}
}
