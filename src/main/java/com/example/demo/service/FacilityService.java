package com.example.demo.service;

import java.util.List;

import com.example.demo.Enum.FacilityType;
import com.example.demo.io.FacilityRequest;
import com.example.demo.io.FacilityResponse;

public interface FacilityService {

    FacilityResponse addFacility(FacilityRequest request);

    void deleteFacility(Long id);

    List<FacilityResponse> getFacilitiesByCenter(String centerId);

    List<FacilityResponse> getFacilitiesByType(String centerId, FacilityType type);

}