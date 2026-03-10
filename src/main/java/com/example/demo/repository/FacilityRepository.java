package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.FacilityEntity;
import com.example.demo.Enum.FacilityType;

public interface FacilityRepository extends JpaRepository<FacilityEntity, Long>{

    List<FacilityEntity> findByCenterId(String centerId);

    List<FacilityEntity> findByCenterIdAndFacilityType(String centerId, FacilityType facilityType);

}