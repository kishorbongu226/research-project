package com.example.demo.entity;

import com.example.demo.Enum.FacilityType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_facilities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String centerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType facilityType;

    @Column(nullable = false)
    private String facilityName;
}