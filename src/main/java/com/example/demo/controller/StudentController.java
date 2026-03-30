package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.io.ApplicationRequest;
import com.example.demo.io.ApplicationResponse;
import com.example.demo.io.StudentProfileResponse;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1.0")
public class StudentController {

    private final ApplicationService applicationService;
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PostMapping("/student/apply")
@ResponseStatus(HttpStatus.CREATED)
public ApplicationResponse apply(
        @RequestPart("details") String details,
        @RequestPart("file") MultipartFile file) {

    logger.info("Received application request: {}", details);

    ObjectMapper objectMapper = new ObjectMapper();

    try {
        ApplicationRequest request =
                objectMapper.readValue(details, ApplicationRequest.class);

        return applicationService.createApplication(request, file);

    } catch (JsonProcessingException e) {

        logger.error("JSON parsing failed: {}", e.getMessage());

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Exception occurred while parsing the json");
    }
}

private final StudentService studentService;

    @GetMapping("/student/profile/{registerNo}")
    public StudentProfileResponse getStudentProfile(
            @PathVariable String registerNo) {

        return studentService.getStudentProfile(registerNo);
    }
}
