package com.example.demo.service;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.CenterEntity;
import com.example.demo.entity.ProfessorEntity;
import com.example.demo.io.CenterDetailsResponse;
import com.example.demo.io.CenterRequest;
import com.example.demo.io.CenterResponse;

public interface CenterService {

    CenterResponse createCenter(CenterRequest request,MultipartFile multipartFile,Principal principal);
    void deleteCenter(String id);
    List<CenterResponse> readCenters();
    void assignProfessorToCenter(String centerId, String professorRegisterNo);
    CenterDetailsResponse getCenterDetails(CenterEntity center,ProfessorEntity professorEntity);
    
}
