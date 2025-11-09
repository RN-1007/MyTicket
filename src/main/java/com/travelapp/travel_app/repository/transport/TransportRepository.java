package com.travelapp.travel_app.repository.transport;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.Transport;

public interface TransportRepository extends JpaRepository<Transport, Integer> {}