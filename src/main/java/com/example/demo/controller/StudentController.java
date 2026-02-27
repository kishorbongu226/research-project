package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import tools.jackson.databind.ObjectMapper;

public class StudentController {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(@RequestPart("details") String details , @RequestPart("file") String file){

        ObjectMapper objectMapper = new ObjectMapper();
        ApplicationRequest request = null;
        try{
            request = objectMapper.readValue(details, ApplicationRequest.class);
            applicationService.createApplication(request,file);
        }
          catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occured while parsing the json"+e.getMessage());
        }
        
    }
}
