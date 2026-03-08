package com.example.demo.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.ProfessorEntity;
import com.example.demo.io.AdminProfileResponse;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.io.ProfessorRequest;
import com.example.demo.io.ProfessorResponse;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.ProfessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;
    private final ApplicationService applicationService;
    private final ProfessorRepository professorRepository;

    @PostMapping("/professor/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ProfessorResponse addProfessor(@RequestPart("professor") String professorString,
            @RequestPart("file") MultipartFile file) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProfessorRequest request = null;
        try {
            request = objectMapper.readValue(professorString, ProfessorRequest.class);
            return professorService.createProfessor(request, file);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Exception occured while parsing the json" + e.getMessage());
        }

    }

    @PutMapping("/application/{applicationId}/APPROVE")
    public String approveApplication(
            @PathVariable String applicationId,
            Principal principal) {
        ProfessorEntity professor = professorRepository
                .findByRegisterNo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        Long professorID = professor.getId();
        applicationService.approveApplication(applicationId, professorID);
        return "Application Approved Successfully";
    }

    @PutMapping("/application/{applicationId}/DECLINE")
    public String declineApplication(
            @PathVariable String applicationId,
            Principal principal) {
        ProfessorEntity professor = professorRepository
                .findByRegisterNo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        Long professorID = professor.getId();
        applicationService.declineApplication(applicationId, professorID);
        return "Application Declined Successfully";
    }

    @GetMapping("/applications/pending/")
    public List<ApplicationResponse> getPendingApplications(
            Principal principal) {
        ProfessorEntity professor = professorRepository
                .findByRegisterNo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        Long professorID = professor.getId();
        return applicationService.getPendingApplications(professorID);
    }

    @GetMapping("/applications/approved/")
    public List<ApplicationResponse> getApprovedApplications(
            Principal principal) {
        ProfessorEntity professor = professorRepository
                .findByRegisterNo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        Long professorID = professor.getId();
        return applicationService.getApprovedApplications(professorID);
    }

    @GetMapping("/admins")
    public List<AdminProfileResponse> getAdminProfessors() {
        return professorService.getAdminProfessors();
    }
    @GetMapping("/admins/{professorID}")
    public AdminProfileResponse getAdminProfessors(@PathVariable  String professorID) {
        Optional<ProfessorEntity> optionalprofessorEntity = professorRepository.findByRegisterNo(professorID);
        ProfessorEntity professor = null;
        if(optionalprofessorEntity.isPresent()){
            professor = optionalprofessorEntity.get();
        }
        return convertToProfileResponse(professor);
    }

      private AdminProfileResponse convertToProfileResponse(ProfessorEntity newProfessor) {
        return AdminProfileResponse.builder()
                .imageURL(newProfessor.getImageUrl())
                .name(newProfessor.getName())
                .Occupation(newProfessor.getOccupation())
                .registerNo(newProfessor.getRegisterNo())
                .build();
    }


    
}
// Done..........