package com.example.demo.entity;

import com.example.demo.Enum.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_professors")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String registerNo;
    private String name;
    private String Occupation;
    private String designation;
    private String highestQualification;
    private String personalEmail;
    @Column(unique = true)
    private String officialEmail;
    private String phoneNumber;
    private String imageUrl;
    private String password;

    @Enumerated(EnumType.STRING) // VERY IMPORTANT
    private Role role = Role.ADMIN;

}
