package com.example.demo.entity;




import com.example.demo.Enum.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_applications")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ApplicationEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String applicationId;
    private String name;
    private String registerNo;
    private String email;
    private String phoneNumber;
    private String branch;
    private String course;
    private String Year;
    private String graduation;
    private String resumeURL;
    

    
    
}
    
    