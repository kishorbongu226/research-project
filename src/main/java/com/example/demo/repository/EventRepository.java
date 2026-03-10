package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.EventEntity;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    Optional<EventEntity> findByEventId(String eventId);

}