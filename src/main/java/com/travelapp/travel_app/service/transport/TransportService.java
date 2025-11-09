package com.travelapp.travel_app.service.transport;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.Transport;
import com.travelapp.travel_app.repository.transport.TransportRepository; 

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

    // --- METHOD BARU ---
    public Optional<Transport> getTransportById(Integer id) {
        return transportRepository.findById(id);
    }

    // --- METHOD BARU ---
    public Transport saveTransport(Transport transport) {
        return transportRepository.save(transport);
    }

    // --- METHOD BARU ---
    public void deleteTransport(Integer id) {
        transportRepository.deleteById(id);
    }
}