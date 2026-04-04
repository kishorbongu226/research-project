package com.example.demo.entity;

import java.util.List;

import com.example.demo.Enum.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_students")
@Getter
@Setter
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
    @Column(length = 4000)
    private String profileDescription;
    @Column(length = 4000)
    private String achievements;

    private String password;

     @Enumerated(EnumType.STRING) // VERY IMPORTANT
    private Role role = Role.USER;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<ApplicationEntity> applications;
}
