package com.example.demo.io;

import com.example.demo.Enum.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    private String projectId;
    private String title;
    private String description;
    private ProjectStatus projectStatus;

    private String responsibilities;
    private String skillRequirements;

    private String centerId;
    private String professorId;
}