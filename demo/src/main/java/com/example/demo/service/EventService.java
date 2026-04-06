package com.example.demo.service;

import com.example.demo.dto.EventDTO;
import com.example.demo.model.Event;
import com.example.demo.model.Location;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    public List<Event> getEventsToday() {
        LocalDate today = LocalDate.now();
        return eventRepository.findByDate(today);
    }

    public List<Event> searchEvents(String name, String type, String address, Double maxPrice, LocalDate date) {
        return eventRepository.searchEvents(name, type, address, maxPrice, date);
    }

    public List<Event> getEventsByLocation(Long locationId) {
        return eventRepository.findByLocationId(locationId);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event createEvent(EventDTO eventDTO) {
        Location location = locationRepository.findById(eventDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        Event event = Event.builder()
                .name(eventDTO.getName())
                .date(eventDTO.getDate())
                .price(eventDTO.getPrice())
                .address(eventDTO.getAddress())
                .type(eventDTO.getType())
                .recurrent(eventDTO.isRecurrent())
                .location(location)
                .build();

        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, EventDTO eventDTO) {
        Event event = getEventById(id);

        if (eventDTO.getLocationId() != null) {
            Location location = locationRepository.findById(eventDTO.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            event.setLocation(location);
        }

        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setPrice(eventDTO.getPrice());
        event.setRecurrent(eventDTO.isRecurrent());
        event.setAddress(eventDTO.getAddress());
        event.setType(eventDTO.getType());

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
