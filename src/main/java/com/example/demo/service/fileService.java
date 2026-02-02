package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.io.FileResponse;



public interface fileService {

   FileResponse add(MultipartFile file );

   

  
    
}