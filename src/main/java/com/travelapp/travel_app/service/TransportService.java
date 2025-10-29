package com.travelapp.travel_app.service;

import com.travelapp.travel_app.model.Transport;
import com.travelapp.travel_app.repository.TransportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportService {

    @Autowired
    private TransportRepository transportRepository;

    public List<Transport> getAllTransports() {
        return transportRepository.findAll();
    }

    public long getTransportCount() {
        return transportRepository.count();
    }
}