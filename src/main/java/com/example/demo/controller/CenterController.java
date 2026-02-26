package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.CenterEntity;
import com.example.demo.entity.ProfessorEntity;
import com.example.demo.io.CenterDetailsResponse;
import com.example.demo.io.CenterRequest;
import com.example.demo.io.CenterResponse;
import com.example.demo.repository.CenterRepository;
import com.example.demo.repository.ProfessorRepository;
import com.example.demo.service.CenterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CenterController {

    private final CenterService centerService;
    private final CenterRepository centerRepository;
    private final ProfessorRepository professorRepository;

    @PostMapping("/centers/add")
    @ResponseStatus(HttpStatus.CREATED)
    public CenterResponse addCategory(@RequestPart("center") String centerString,
                                        @RequestPart("file") MultipartFile file)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        CenterRequest request=null;
        try {
            request=objectMapper.readValue(centerString, CenterRequest.class);
            return centerService.createCenter(request, file);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Exception occured while parsing the json"+e.getMessage());
        }
        

    }

    @GetMapping("/centers")
    public List<CenterResponse> fetchCategories()
    {
        return centerService.readCenters();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/centers/{centerId}")
    public void remove(@PathVariable String centerId)
    {
        try {
            centerService.deleteCenter(centerId);
        } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
    }
    
    @GetMapping("/centers/{centerId}")

    public CenterDetailsResponse fetchCategory(@PathVariable String centerId){

        CenterEntity center = null;
        Optional<CenterEntity> optionalcenter = centerRepository.findByCenterId(centerId);
        if(optionalcenter.isPresent()){
            center = optionalcenter.get(); 
        }

        ProfessorEntity professor = null;
          Optional<ProfessorEntity> optionalprofessor =
            professorRepository.findByRegisterNo(center.getProfessor().getRegisterNo());


            if(optionalprofessor.isPresent()){
                professor = optionalprofessor.get();
            }

        CenterDetailsResponse  centerDetails= centerService.getCenterDetails(center,professor);

        return centerDetails;
    }

    
    
}
