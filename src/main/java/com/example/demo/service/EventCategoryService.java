package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.EventCategory;
import com.example.demo.repository.EventCategoryRepository;


@Service
public class EventCategoryService {

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    public List<EventCategory> findAll() {
        return eventCategoryRepository.findAll();
    }
}