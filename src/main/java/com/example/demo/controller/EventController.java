package com.example.demo.controller;

import com.example.demo.model.Event;

import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/list")
    public String getAllEvents(@RequestParam(defaultValue = "0") int page, 
                                @RequestParam(defaultValue = "5") int size, 
                                Model model) {
        Page<Event> eventsPage = eventService.getAllEventsPagination(page, size);
        model.addAttribute("eventsPage", eventsPage);
        
        return "list";  
    }


    @GetMapping("/add")
    public String showAddEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "add";
    }

    @PostMapping("/add")
    public String addEvent(
    		@ModelAttribute Event event, @RequestParam("imageFile") MultipartFile imageFile) {
        String uploadDir = "C://Users//USER//Desktop//events//uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            File fileToSave = new File(uploadDir + fileName);
            try {
                imageFile.transferTo(fileToSave);
                event.setImage("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du téléchargement de l'image", e);
            }
        }
        eventService.addEvent(event);
        return "redirect:/list";
    }


    @GetMapping("/edit/{id}")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id).orElseThrow();
        model.addAttribute("event", event);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id,
                            @ModelAttribute Event event,
                            @RequestParam("imageFile") MultipartFile imageFile) {
        Event existingEvent = eventService.getEventById(id).orElseThrow(() -> new RuntimeException("Événement introuvable"));
        existingEvent.setName(event.getName());
        existingEvent.setDate(event.getDate());
        existingEvent.setLocation(event.getLocation());
        existingEvent.setCategory(event.getCategory());
        if (!imageFile.isEmpty()) {
            String uploadDir = "C://Users//USER//Desktop//events//uploads/";
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            File fileToSave = new File(uploadDir + fileName);

            try {
                imageFile.transferTo(fileToSave);
                existingEvent.setImage("/uploads/" + fileName);
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors du téléchargement de l'image", e);
            }
        }

        eventService.updateEvent(id, existingEvent);
        return "redirect:/list";
    }



    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/list";
    }

    @GetMapping("/détails/{id}")
    public String getEventDetails(@PathVariable Long id, Model model) {
        Event event = eventService.getEventById(id).orElseThrow();
        model.addAttribute("event", event);
        return "détails";
    }
    
    @GetMapping("/home")
    public String showEventHome(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                @RequestParam(value = "category", required = false) String category,
                                @RequestParam(value = "location", required = false) String location,
                                @RequestParam(value = "date", required = false) String date, 
                                Model model) {
        Page<Event> eventsPage;
        if (category != null || location != null || date != null) {
            eventsPage = eventService.findEventsMulCritere(category, location, date, page, size);
        } else {
            eventsPage = eventService.getAllEventsPagination(page, size);
        }
        model.addAttribute("eventsPage", eventsPage);

        return "home";  
    }

}
