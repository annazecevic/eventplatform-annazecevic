package com.example.demo.controller;

import com.example.demo.dto.EventDTO;
import com.example.demo.model.Event;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/today")
    public List<Event> getEventsToday() {
        return eventService.getEventsToday();
    }

    @GetMapping("/location/{locationId}")
    public List<Event> getEventsByLocation(@PathVariable Long locationId) {
        return eventService.getEventsByLocation(locationId);
    }

    @GetMapping("/search")
    public List<Event> searchEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Double maxPrice) {
        LocalDate today = LocalDate.now();
        return eventService.searchEvents(name, type, address, maxPrice, today);
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Event createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.createEvent(eventDTO);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public Event updateEvent(@PathVariable Long id, @RequestBody EventDTO eventDTO) {
        return eventService.updateEvent(id, eventDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}
