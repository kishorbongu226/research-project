package com.example.demo.service.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.CenterEntity;
import com.example.demo.entity.ProfessorEntity;
import com.example.demo.io.CenterDetailsResponse;
import com.example.demo.io.CenterRequest;
import com.example.demo.io.CenterResponse;
import com.example.demo.repository.CenterRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.service.CenterService;
import com.example.demo.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService{

    

    private final CenterRepository centerRepository;
    private final ProfessorRepository professorRepository;
    private final FileUploadService fileUploadService;

    @Override
    public CenterResponse createCenter(CenterRequest request,MultipartFile file) {
        String imgUrl = fileUploadService.uploadFile(file);
        CenterEntity newCenter = convertToEntity(request);
        newCenter.setImgUrl(imgUrl);
        
        Optional<ProfessorEntity> optionalprofessor = professorRepository.findByRegisterNo(request.getProfessorId());
        ProfessorEntity professor = optionalprofessor.get();
        newCenter.setProfessor(professor);
        newCenter = centerRepository.save(newCenter);
        return convertToResponse(newCenter);
    }

    @Override
    public void deleteCenter(String centerId) {
        CenterEntity existingCategory = centerRepository.findByCenterId(centerId)
                            .orElseThrow(() -> new RuntimeException("Category not found : "+centerId));
        fileUploadService.deleteFile(existingCategory.getImgUrl());                    
        centerRepository.delete(existingCategory);
    }

    @Override
    public List<CenterResponse> readCenters() {
        return centerRepository.findAll()
                    .stream()
                    .map(center -> convertToResponse(center))
              
              
                    .collect(Collectors.toList());
    }

    @Override
    public CenterDetailsResponse getCenterDetails(CenterEntity center,ProfessorEntity professor){
        CenterDetailsResponse centerDetails = convertToResponse(center,professor);
        return centerDetails;
    }

    private CenterEntity convertToEntity(CenterRequest request) {
        return CenterEntity.builder()
                .name(request.getName())
                .centerId(request.getCenterId())
                .description(request.getDescription())
                .projectStatus(request.getProjectStatus())
                .build();

    }

    private CenterResponse convertToResponse(CenterEntity newUser) {
        return CenterResponse.builder()
                        .id(newUser.getId())
                        .name(newUser.getName())
                        .centerId(newUser.getCenterId())
                        .imgUrl(newUser.getImgUrl())
                        .description(newUser.getDescription())
                        .projectStatus(newUser.getProjectStatus())
                        .build();
    }

    private CenterDetailsResponse convertToResponse(CenterEntity center, ProfessorEntity professor){
        return CenterDetailsResponse.builder()
        .imageUrl(professor.getImageUrl())
        .professorName(professor.getName())
        .professorOccupation(professor.getOccupation())
        .centerName(center.getName())
        .centerDescription(center.getDescription())
        .build();
    }

    @Override
    public void assignProfessorToCenter(String centerId,String professorRegisterNo) {

        CenterEntity center = centerRepository.findByCenterId(centerId)
                .orElseThrow(() ->
                        new RuntimeException("Center not found"));

        ProfessorEntity professor =
                professorRepository.findByRegisterNo(professorRegisterNo)
                        .orElseThrow(() ->
                                new RuntimeException("Professor not found"));

       
        center.setProfessor(professor);
        centerRepository.save(center);
    }
    
}
