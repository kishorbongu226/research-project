package com.example.demo.service.implementation;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.EventEntity;
import com.example.demo.io.EventRequest;
import com.example.demo.io.EventResponse;
import com.example.demo.repository.EventRepository;
import com.example.demo.service.EventService;
import com.example.demo.service.FileUploadService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final FileUploadService fileUploadService;

    @Override
    public EventResponse createEvent(EventRequest request, MultipartFile file, Principal principal) {

        String imgUrl = fileUploadService.uploadFile(file);

        EventEntity newEvent = convertToEntity(request);
        newEvent.setImageUrl(imgUrl);

        newEvent = eventRepository.save(newEvent);

        return convertToResponse(newEvent);
    }

    @Override
    public EventResponse updateEvent(String eventId, EventRequest request, MultipartFile file,Principal principal) {

        Optional<EventEntity> optionalEvent = eventRepository.findByEventId(eventId);

        if(optionalEvent.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Event not found");
        }

        EventEntity event = optionalEvent.get();

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());

        if(file != null && !file.isEmpty()){
            String imgUrl = fileUploadService.uploadFile(file);
            event.setImageUrl(imgUrl);
        }

        event = eventRepository.save(event);

        return convertToResponse(event);
    }

    @Override
    public void deleteEvent(String eventId) {

        EventEntity existingEvent = eventRepository.findByEventId(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found : " + eventId));

        fileUploadService.deleteFile(existingEvent.getImageUrl());

        eventRepository.delete(existingEvent);
    }

    @Override
    public List<EventResponse> readEvents() {

        return eventRepository.findAll()
                .stream()
                .map(event -> convertToResponse(event))
                .collect(Collectors.toList());
    }

    private EventEntity convertToEntity(EventRequest request) {

        return EventEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .build();
    }

    private EventResponse convertToResponse(EventEntity event) {

        return EventResponse.builder()
                .eventId(event.getEventId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .imageUrl(event.getImageUrl())
                .build();
    }
}