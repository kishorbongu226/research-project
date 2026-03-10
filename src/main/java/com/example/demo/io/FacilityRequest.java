package com.example.demo.io;

import com.example.demo.Enum.FacilityType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityRequest {

    private String centerId;

    private FacilityType facilityType;

    private String facilityName;
}