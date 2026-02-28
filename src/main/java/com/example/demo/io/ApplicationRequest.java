package com.example.demo.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationRequest {

   
    private String name;
    private String registerNo;
    private String email;
    private String phoneNumber;
    private String branch;
    private String course;
    private String Year;
    private String graduation;
    private String projectId;
   
}
