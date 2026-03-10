package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.CenterEntity;
import com.example.demo.entity.ProfessorEntity;
import com.example.demo.io.CenterDetailsResponse;
import com.example.demo.io.CenterRequest;
import com.example.demo.io.CenterResponse;
import com.example.demo.repository.CenterRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.service.CenterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@CrossOrigin("https://localhost:3000")
@RestController
@RequiredArgsConstructor
public class CenterController {

    private final CenterService centerService;
    private final CenterRepository centerRepository;
    private final ProfessorRepository professorRepository;

    @PostMapping("/centers/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CenterResponse addCategory(@RequestPart("center") String centerString,
    @RequestPart("file") MultipartFile file,Principal principal)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        CenterRequest request=null;
        try {
            request=objectMapper.readValue(centerString, CenterRequest.class);
            return centerService.createCenter(request, file,principal);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occured while parsing the json"+e.getMessage());
        }
        
        
    }

    @PutMapping("/centers/edit/{id}")
@PreAuthorize("hasRole('ADMIN')")
public CenterResponse editCenter(
        @PathVariable String id,
        @RequestPart("center") String centerString,
        @RequestPart(value = "file", required = false) MultipartFile file,
        Principal principal) {

    ObjectMapper objectMapper = new ObjectMapper();
    CenterRequest request = null;

    try {
        request = objectMapper.readValue(centerString, CenterRequest.class);
        return centerService.updateCenter(id, request, file, principal);

    } catch (JsonProcessingException e) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Exception occurred while parsing JSON " + e.getMessage()
        );
    }
}
    
    @GetMapping("/centers")
    public List<CenterResponse> fetchCategories()
    {
        return centerService.readCenters();
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/centers/{centerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void remove(@PathVariable String centerId)
    {
        try {
            centerService.deleteCenter(centerId);
        } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
    
    @GetMapping("/centers/{centerId}")
    public CenterDetailsResponse fetchCategory(@PathVariable String centerId){
        
        CenterEntity center = centerRepository.findByCenterId(centerId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Center not found"));
        
        ProfessorEntity professor = professorRepository
        .findByRegisterNo(center.getProfessor().getRegisterNo())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));
        
        return centerService.getCenterDetails(center, professor);
    }
    
    
    
}
// DONE.............