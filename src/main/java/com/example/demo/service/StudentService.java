package com.example.demo.service;

import com.example.demo.io.StudentProfileResponse;
import com.example.demo.io.StudentProfileUpdateRequest;

public interface StudentService {

    StudentProfileResponse getStudentProfile(String registerNo);
    StudentProfileResponse updateStudentProfile(String registerNo, StudentProfileUpdateRequest request);
}
