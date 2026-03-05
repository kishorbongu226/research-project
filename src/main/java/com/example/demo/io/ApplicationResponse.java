package com.example.demo.io;

import com.example.demo.Enum.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationResponse {

    private Long id;              // 🔥 DB id (VERY IMPORTANT)
    private String applicationId; // UUID if you want

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

    private String centerName;    // 🔥 Needed for frontend
    private String projectName;   // 🔥 Needed for frontend

    private ApplicationStatus status; // 🔥 Needed
}