package com.example.demo.entity;

import com.example.demo.Enum.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_projects")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String projectId;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    // Store as long text (No separate table)
    @Column(columnDefinition = "TEXT")
    private String responsibilities;

    @Column(columnDefinition = "TEXT")
    private String skillRequirements;

    //  Many Projects → One Center
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id", nullable = false)
    private CenterEntity center;

    //  Many Projects → One Professor (Creator)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private ProfessorEntity director;
}