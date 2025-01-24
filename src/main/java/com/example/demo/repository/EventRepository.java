package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Event;
import com.example.demo.model.EventCategory;

public interface EventRepository extends JpaRepository<Event, Long>{
	 List<Event> findByCategoryId(Long categoryId);


	    @Query("SELECT e FROM Event e JOIN e.eventDates ed WHERE e.category.id = :categoryId AND ed.startDate = :eventDate")
	    List<Event> findByCategoryIdAndStartDate(@Param("categoryId") Long categoryId, @Param("eventDate") LocalDate eventDate);

	    @Query("SELECT ec FROM EventCategory ec")
	    List<EventCategory> findAllCategories(); // To fetch all categories
	
}