package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tbl_students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String registerNo;

    private String name;
    private String branch;
    private String year;
    private String course;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String profileImageUrl;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<ApplicationEntity> applications;
}