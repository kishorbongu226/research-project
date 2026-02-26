package com.example.demo.io;

import com.example.demo.Enum.ProjectStatus;

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
    private ProjectStatus projectStatus;
    
}
