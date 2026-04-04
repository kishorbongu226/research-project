package com.example.demo.io;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminProfileResponse {

    private String registerNo;
    private String name;
    private String Occupation;
    private String designation;
    private String highestQualification;
    private String personalEmail;
    private String officialEmail;
    private String phoneNumber;
    private String imageURL;
}
