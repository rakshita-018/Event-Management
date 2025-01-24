package com.example.demo.contoller;

import com.example.demo.model.Event;
import com.example.demo.model.EventCategory;
import com.example.demo.service.EventCategoryService;
import com.example.demo.service.EventService;
import com.example.demo.service.PhotoStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventCategoryService eventCategoryService;
    
    @Autowired
    private PhotoStorageService photoStorageService;

    // Display form to add event
    @GetMapping("/addEvent")
    public String showAddEventForm(Model model) {
        List<EventCategory> categories = eventCategoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/addEvent";
    }

    // Handle adding event
    @PostMapping("/addEvent")
    public String addEvent(@RequestParam String title,
                           @RequestParam String description,
                           @RequestParam Long categoryId,
                           @RequestParam LocalDate startDate,
                           @RequestParam String startTime,
                           @RequestParam String endTime,
                           @RequestParam int teamSize,
                           @RequestParam int amount,
                           @RequestParam String location,
                           @RequestParam("photo") MultipartFile photo,
                           RedirectAttributes redirectAttributes) {
        try {
            // Save the photo and get the URL or path
            String photoUrl = photoStorageService.save(photo);

            // Create the event and save it
            eventService.addEvent(title, description, categoryId, startDate, startTime, endTime, teamSize, amount, location, photoUrl);

            redirectAttributes.addFlashAttribute("message", "Event added successfully!");
            return "redirect:/admin/eventList";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload photo.");
            return "redirect:/admin/addEvent";
        }
    }
    
    @GetMapping("/eventList")
    public String listUsers(Model model) {
    	List<EventCategory> categories = eventCategoryService.findAll();
    	model.addAttribute("categories", categories);

        List<Event> allEvents = eventService.findAllEvents();
        model.addAttribute("events", allEvents);
        return "admin/eventList"; 
    }

    @PostMapping("/eventList")
    public String showEvents(@RequestParam("categoryId") Long categoryId,
                             @RequestParam("eventDate") String eventDate,
                             Model model) {
    	
        // Convert eventDate String to LocalDate
        LocalDate selectedDate = LocalDate.parse(eventDate);

        // Fetch the events based on categoryId and selectedDate
        List<Event> events = eventService.findEventsByCategoryAndDate(categoryId, selectedDate);
        
        // Log the retrieved events to verify   
        if (events.isEmpty()) {
            System.out.println("No events found for the given category and date.");
        } 
        
        model.addAttribute("events", events);
        
        List<EventCategory> categories = eventCategoryService.findAll();
        model.addAttribute("categories", categories);
        return "/admin/eventList"; // The name of your HTML template
    }
    
    // Delete user by ID
    @PostMapping("/deleteEvent/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = eventService.deleteUserById(id);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "User not found or couldn't be deleted.");
        }
        return "redirect:/admin/eventList";
    }
    
    
}
