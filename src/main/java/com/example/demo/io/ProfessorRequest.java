package com.example.demo.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfessorRequest {
    private String registerNo;
    private String name;
    private String occupation;
    private String designation;
    private String highestQualification;
    private String personalEmail;
    private String officialEmail;
    private String phoneNumber;
    private String password;
}
