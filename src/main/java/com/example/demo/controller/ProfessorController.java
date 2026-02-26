package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.io.ProfessorRequest;
import com.example.demo.io.ProfessorResponse;
import com.example.demo.service.ProfessorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class ProfessorController {
    
    private final ProfessorService professorService;
    

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
}
