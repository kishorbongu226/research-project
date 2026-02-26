package com.example.demo.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.CenterEntity;
import com.example.demo.entity.ProfessorEntity;
import com.example.demo.io.CenterRequest;
import com.example.demo.io.CenterResponse;
import com.example.demo.io.ProfessorRequest;
import com.example.demo.io.ProfessorResponse;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.service.FileUploadService;
import com.example.demo.service.ProfessorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessorServiceImpl implements  ProfessorService {

       private final ProfessorRepository professorRepository;
    private final FileUploadService fileUploadService;

   
   
    @Override
    public ProfessorResponse createProfessor(ProfessorRequest request,MultipartFile file){
 String imgUrl = fileUploadService.uploadFile(file);
        ProfessorEntity newProfessor = convertToEntity(request);
        newProfessor.setImageUrl(imgUrl);
        newProfessor = professorRepository.save(newProfessor);
        return convertToResponse(newProfessor);
    }

    private ProfessorEntity convertToEntity(ProfessorRequest request) {
        return ProfessorEntity.builder()
                .registerNo(request.getRegisterNo())
                .name(request.getName())
                .Occupation(request.getOccupation())

                .build();

    }

    private ProfessorResponse convertToResponse(ProfessorEntity newProfessor) {
        return ProfessorResponse.builder()
                      .imageUrl(newProfessor.getImageUrl())
                      .name(newProfessor.getName())
                      .Occupation(newProfessor.getOccupation())
                      .registerNo(newProfessor.getRegisterNo())
                      .build();
    }
}
