package com.example.demo.service;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Événement introuvable"));

        event.setName(eventDetails.getName());
        event.setDate(eventDetails.getDate());
        event.setLocation(eventDetails.getLocation());
        event.setCategory(eventDetails.getCategory());
        if (eventDetails.getImage() != null && !eventDetails.getImage().isEmpty()) {
            event.setImage(eventDetails.getImage());
        }

        return eventRepository.save(event);
    }
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    public List<Event> findEventsCritere(String category, String location, String date) {
        if (category != null && !category.isEmpty()) {
            return eventRepository.findByCategory(category);
        } else if (location != null && !location.isEmpty()) {
            return eventRepository.findByLocation(location);
        } else if (date != null && !date.isEmpty()) {
            return eventRepository.findByDate(date);
        }
       
        return eventRepository.findAll();
    }
    public Page<Event> getAllEventsPagination(int page, int size) {
        
        return eventRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Event> findEventsByCategory(String category, int page, int size) {
        
        if (category != null && !category.isEmpty()) {
            return eventRepository.findByCategory(category, PageRequest.of(page, size));
        }
        return eventRepository.findAll(PageRequest.of(page, size)); 
    }
    public Page<Event> findEventsByLocation(String location, int page, int size) {
        if (location != null && !location.isEmpty()) {
            return eventRepository.findByLocation(location, PageRequest.of(page, size));
        }
        return eventRepository.findAll(PageRequest.of(page, size)); 
    }
    public Page<Event> findEventsByDate(String date, int page, int size) {
        if (date != null && !date.isEmpty()) {
            return eventRepository.findByDate(date, PageRequest.of(page, size));
        }
        return eventRepository.findAll(PageRequest.of(page, size)); 
    }
    public Page<Event> findEventsMulCritere(String category, String location, String date, int page, int size) {
        
        if (category != null && !category.isEmpty()) {
            return eventRepository.findByCategory(category, PageRequest.of(page, size));
        } else if (location != null && !location.isEmpty()) {
            return eventRepository.findByLocation(location, PageRequest.of(page, size));
        } else if (date != null && !date.isEmpty()) {
            return eventRepository.findByDate(date, PageRequest.of(page, size));
        }
        return eventRepository.findAll(PageRequest.of(page, size)); 
    }
}
