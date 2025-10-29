package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransportRepository extends JpaRepository<Transport, Integer> {}