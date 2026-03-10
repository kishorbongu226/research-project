package com.example.demo.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.Enum.FacilityType;
import com.example.demo.entity.FacilityEntity;
import com.example.demo.io.FacilityRequest;
import com.example.demo.io.FacilityResponse;
import com.example.demo.repository.FacilityRepository;
import com.example.demo.service.FacilityService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;

    @Override
    public FacilityResponse addFacility(FacilityRequest request) {

        FacilityEntity facility = FacilityEntity.builder()
                .centerId(request.getCenterId())
                .facilityType(request.getFacilityType())
                .facilityName(request.getFacilityName())
                .build();

        facility = facilityRepository.save(facility);

        return convertToResponse(facility);
    }

    @Override
    public void deleteFacility(Long id) {

        FacilityEntity facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Facility not found"));

        facilityRepository.delete(facility);
    }

    @Override
    public List<FacilityResponse> getFacilitiesByCenter(String centerId) {

        return facilityRepository.findByCenterId(centerId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FacilityResponse> getFacilitiesByType(String centerId, FacilityType type) {

        return facilityRepository.findByCenterIdAndFacilityType(centerId, type)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private FacilityResponse convertToResponse(FacilityEntity facility) {

        return FacilityResponse.builder()
                .id(facility.getId())
                .centerId(facility.getCenterId())
                .facilityType(facility.getFacilityType())
                .facilityName(facility.getFacilityName())
                .build();
    }
}