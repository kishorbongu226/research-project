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
public class CenterRequest {

    private String name;
    private String centerId;
    private String description;
    // private ProjectStatus projectStatus;
   
    
}
