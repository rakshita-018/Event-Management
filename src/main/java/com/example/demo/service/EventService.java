package com.example.demo.service;

import com.example.demo.model.Event;
import com.example.demo.model.EventCategory;
import com.example.demo.model.EventDate;
import com.example.demo.model.Teams;
import com.example.demo.model.UserDetail;
import com.example.demo.repository.EventCategoryRepository;
import com.example.demo.repository.EventDateRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.repository.userRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private EventDateRepository eventDateRepository;
   
    public void addEvent(String title, String description, Long categoryId, LocalDate startDate, String startTime, String endTime, int teamSize, int amount,String location, String photoUrl) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);
        event.setTeamSze(teamSize);
        event.setAmount(amount);
        event.setPhotoUrl(photoUrl); 

        EventCategory category = eventCategoryRepository.findById(categoryId).orElseThrow();
        event.setCategory(category);

        eventRepository.save(event);

        EventDate eventDate = new EventDate();
        
        eventDate.setEvent(event);
        eventDate.setStartDate((startDate));
        eventDate.setStartTime((startTime));
        eventDate.setEndTime((endTime));
        eventDate.setLocation(location);

        System.out.println("EventDate to be saved: " + eventDate);
        eventDateRepository.save(eventDate);
    }

	public List<Event> findEventsByCategoryAndDate(Long categoryId, LocalDate eventDate) {
        return eventRepository.findByCategoryIdAndStartDate(categoryId, eventDate);
	}

    public List<EventCategory> getAllCategories() {
        return eventRepository.findAllCategories(); // Assuming this method is defined
    }
    
	public long getTotalEvents() {
		return eventRepository.count();
	}

	public boolean deleteUserById(Long id) {
		if(eventRepository.existsById(id)) {
			eventRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
    public List<Event> findAllCulturalEvents() {
        return eventRepository.findAll().stream()
                .filter(event -> event.getCategory().getName().equalsIgnoreCase("Cultural"))
                .collect(Collectors.toList());
    }

    public List<Event> findAllTechnicalEvents() {
        return eventRepository.findAll().stream()
                .filter(event -> event.getCategory().getName().equalsIgnoreCase("Technical"))
                .collect(Collectors.toList());
    }
    public List<Event> findAllSportsEvents() {
        return eventRepository.findAll().stream()
                .filter(event -> event.getCategory().getName().equalsIgnoreCase("Sports"))
                .collect(Collectors.toList());
    }

	public Event findById(Long eventId) {	
		 Optional<Event> event = eventRepository.findById(eventId);
	        return event.orElse(null);	}

	public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
	}

    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

	public List<Event> findEventsByDate(LocalDate selectedDate) {
		return eventDateRepository.findByStartDate(selectedDate);
	}
	

}