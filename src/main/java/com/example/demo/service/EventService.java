package com.example.demo.service;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.io.EventRequest;
import com.example.demo.io.EventResponse;

public interface EventService {

    EventResponse createEvent(EventRequest request, MultipartFile file, Principal principal);

    EventResponse updateEvent(String eventId, EventRequest request, MultipartFile file,Principal principal);

    void deleteEvent(String eventId);

    List<EventResponse> readEvents();

}