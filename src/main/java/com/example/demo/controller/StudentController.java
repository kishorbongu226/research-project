package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.service.ApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class StudentController {

    private final ApplicationService applicationService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PostMapping("/student/apply")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(@RequestPart("details") String details, @RequestPart("file") MultipartFile file) {

        ObjectMapper objectMapper = new ObjectMapper();
        ApplicationRequest request = null;
        logger.info(details);
        try {
            request = objectMapper.readValue(details, ApplicationRequest.class);
            return applicationService.createApplication(request, file);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Exception occured while parsing the json" + e.getMessage());
        }

    }
}
