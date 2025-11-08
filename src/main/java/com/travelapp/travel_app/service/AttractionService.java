package com.travelapp.travel_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.Attraction;
import com.travelapp.travel_app.repository.attraction.AttractionRepository; 

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

    // --- METHOD BARU ---
    public Optional<Attraction> getAttractionById(Integer id) {
        return attractionRepository.findById(id);
    }

    // --- METHOD BARU ---
    public Attraction saveAttraction(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    // --- METHOD BARU ---
    public void deleteAttraction(Integer id) {
        attractionRepository.deleteById(id);
    }
}