package com.example.demo.service;

import com.example.demo.io.StudentProfileResponse;

public interface StudentService {

    StudentProfileResponse getStudentProfile(String registerNo);
}