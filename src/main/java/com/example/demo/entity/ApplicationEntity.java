package com.example.demo.entity;




import com.example.demo.Enum.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_applications")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ApplicationEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String applicationId;
   
    private String graduation;
    private String resumeURL;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private StudentEntity student;
   
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    
    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private ProjectEntity project;

    
    
}
    
    