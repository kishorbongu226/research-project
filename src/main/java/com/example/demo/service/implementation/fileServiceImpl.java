package com.example.demo.service.implementation;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.io.FileResponse;
import com.example.demo.service.FileUploadService;
import com.example.demo.service.fileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class fileServiceImpl implements fileService {


    private final FileUploadService fileUploadService;

    @Override
    public FileResponse add( MultipartFile file) {
        // Upload image
        String imageUrl = fileUploadService.uploadFile(file);
      

        return convertToResponse(imageUrl);
    }

    private FileResponse convertToResponse(String imageUrl) {
        return FileResponse.builder()       
                .fileUrl(imageUrl)
                .build();
    }
}