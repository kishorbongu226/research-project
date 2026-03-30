package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.io.EventRequest;
import com.example.demo.io.EventResponse;
import com.example.demo.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1.0")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/events/add")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public EventResponse addEvent(
            @RequestPart("event") String eventString,
            @RequestPart("file") MultipartFile file,
            Principal principal) {

        ObjectMapper objectMapper = new ObjectMapper();
        EventRequest request = null;

        try {

            request = objectMapper.readValue(eventString, EventRequest.class);

            return eventService.createEvent(request, file, principal);

        } catch (JsonProcessingException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Exception occurred while parsing JSON " + e.getMessage());

        }
    }

    @PutMapping("/events/edit/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public EventResponse editEvent(
            @PathVariable String eventId,
            @RequestPart("event") String eventString,
            @RequestPart(value = "file", required = false) MultipartFile file,
            Principal principal) {

        ObjectMapper objectMapper = new ObjectMapper();
        EventRequest request = null;

        try {

            request = objectMapper.readValue(eventString, EventRequest.class);

            return eventService.updateEvent(eventId, request, file, principal);

        } catch (JsonProcessingException e) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Exception occurred while parsing JSON " + e.getMessage());
        }
    }

    @GetMapping("/events")
    public List<EventResponse> fetchEvents() {

        return eventService.readEvents();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void remove(@PathVariable String eventId) {

        try {

            eventService.deleteEvent(eventId);

        } catch (Exception e) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    e.getMessage());
        }
    }
}