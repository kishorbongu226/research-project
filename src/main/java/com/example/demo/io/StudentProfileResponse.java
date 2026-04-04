package com.example.demo.io;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentProfileResponse {

    private String registerNo;
    private String name;
    private String branch;
    private String year;
    private String course;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private String profileDescription;
    private String achievements;
    private int applicationCount;
}
