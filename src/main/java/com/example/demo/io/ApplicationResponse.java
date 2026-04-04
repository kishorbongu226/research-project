package com.example.demo.io;

import java.time.LocalDate;

import com.example.demo.Enum.ApplicationStatus;
import com.example.demo.Enum.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse {

    private Long id;
    private String applicationId;

    private String name;
    private String registerNo;
    private String email;
    private String phoneNumber;
    private String branch;
    private String course;
    private String year;
    private String graduation;
    private String profileImageUrl;
    private String resumeURL;
    private String projectImageUrl;
    private String projectId;
    private String centerName;
    private String projectName;
    private LocalDate decisionDate;
    private LocalDate slotDate;
    private String morningSlot;
    private String afternoonSlot;

    private ApplicationStatus status;
    private ProjectStatus projectStatus;
}
