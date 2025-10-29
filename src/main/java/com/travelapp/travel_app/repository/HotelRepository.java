package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {}