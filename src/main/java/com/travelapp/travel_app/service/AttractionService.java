package com.travelapp.travel_app.service;

import com.travelapp.travel_app.model.Attraction;
import com.travelapp.travel_app.repository.AttractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttractionService {

    @Autowired
    private AttractionRepository attractionRepository;

    public List<Attraction> getAllAttractions() {
        return attractionRepository.findAll();
    }

    public long getAttractionCount() {
        return attractionRepository.count();
    }
}