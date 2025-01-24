package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Event;
import com.example.demo.model.EventDate;

public interface EventDateRepository extends JpaRepository<EventDate, Long> {
	List<Event> findByStartDate(LocalDate selectedDate);

}
