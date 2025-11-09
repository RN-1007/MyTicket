package com.travelapp.travel_app.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {}