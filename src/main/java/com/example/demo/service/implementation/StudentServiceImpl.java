package com.example.demo.service.implementation;

import org.springframework.stereotype.Service;

import com.example.demo.entity.StudentEntity;
import com.example.demo.io.StudentProfileResponse;
import com.example.demo.repository.StudentRepository;
import com.example.demo.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentProfileResponse getStudentProfile(String registerNo) {

        StudentEntity student = studentRepository.findByRegisterNo(registerNo)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return StudentProfileResponse.builder()
                .registerNo(student.getRegisterNo())
                .name(student.getName())
                .branch(student.getBranch())
                .year(student.getYear())
                .course(student.getCourse())
                .email(student.getEmail())
                .phoneNumber(student.getPhoneNumber())
                .profileImageUrl(student.getProfileImageUrl())
                .applicationCount(
                        student.getApplications() != null
                                ? student.getApplications().size()
                                : 0
                )
                .build();
    }
}