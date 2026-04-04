package com.example.demo.io;

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
  

    private String responsibilities;
    private String skillRequirements;
    private Integer teamSize;
    private String leadershipMembers;

    private String centerId;
    
}
