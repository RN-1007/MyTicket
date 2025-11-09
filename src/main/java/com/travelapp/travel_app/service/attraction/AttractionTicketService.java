package com.travelapp.travel_app.service.attraction;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.AttractionTicket;
import com.travelapp.travel_app.repository.attraction.AttractionTicketRepository;

@Service
public class AttractionTicketService {

    @Autowired
    private AttractionTicketRepository attractionTicketRepository;

    public List<AttractionTicket> findAll() {
        return attractionTicketRepository.findAll();
    }

    public Optional<AttractionTicket> findById(Integer id) {
        return attractionTicketRepository.findById(id);
    }

    public AttractionTicket save(AttractionTicket attractionTicket) {
        return attractionTicketRepository.save(attractionTicket);
    }

    public void deleteById(Integer id) {
        attractionTicketRepository.deleteById(id);
    }
}