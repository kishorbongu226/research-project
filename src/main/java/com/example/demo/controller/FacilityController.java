package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.io.FacilityRequest;
import com.example.demo.io.FacilityResponse;
import com.example.demo.service.FacilityService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/v1.0/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    // ADD FACILITY
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public FacilityResponse addFacility(@RequestBody FacilityRequest request) {
        return facilityService.addFacility(request);
    }

    // DELETE FACILITY
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFacility(@PathVariable Long id) {
        facilityService.deleteFacility(id);
    }

    // GET ALL FACILITIES BY CENTER
@GetMapping("/center/{centerId}")
public List<FacilityResponse> getFacilities(@PathVariable String centerId) {
    return facilityService.getFacilitiesByCenter(centerId);
}


}