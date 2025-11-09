package com.travelapp.travel_app.service.transport;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.TransportTicket;
import com.travelapp.travel_app.repository.transport.TransportTicketRepository;

@Service
public class TransportTicketService {

    @Autowired
    private TransportTicketRepository transportTicketRepository;

    public List<TransportTicket> findAll() {
        return transportTicketRepository.findAll();
    }

    public Optional<TransportTicket> findById(Integer id) {
        return transportTicketRepository.findById(id);
    }

    public TransportTicket save(TransportTicket transportTicket) {
        return transportTicketRepository.save(transportTicket);
    }

    public void deleteById(Integer id) {
        transportTicketRepository.deleteById(id);
    }
}