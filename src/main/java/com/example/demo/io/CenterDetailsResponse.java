package com.example.demo.io;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class CenterDetailsResponse {
    
    private String imageUrl;
    private String professorName;
    private String professorOccupation;
    private String centerName;
    private String centerDescription;
}
