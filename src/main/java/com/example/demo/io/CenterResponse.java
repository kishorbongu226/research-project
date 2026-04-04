package com.example.demo.io;

import com.example.demo.Enum.CenterStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CenterResponse {


    private Long id;
    private String name;
    private String centerId;
    private String imgUrl;
    private String description;
    private CenterStatus projectStatus;
    private String professorName;
    private String professorOccupation;
    private String professorImageUrl;
    
}
