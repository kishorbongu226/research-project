package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.io.FileResponse;
import com.example.demo.service.fileService;

import lombok.RequiredArgsConstructor;




@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0")
public class FileController {
    
    private  final fileService fileService;

    @PostMapping("/resume/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public FileResponse addCategory(
            @RequestPart("file") MultipartFile file) {

                
                try{
                    return fileService.add(file);

                }
                catch(Exception e){
                    throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Invalid JSON format", e);
                       
                }
     
        
    }
}
// DONE..........