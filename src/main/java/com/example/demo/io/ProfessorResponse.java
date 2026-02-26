package com.example.demo.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfessorResponse {
    
    private String imageUrl;
    private String name;
    private String registerNo;
    private String Occupation;
}
