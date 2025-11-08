package com.travelapp.travel_app.repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.HotelRoom;

// Repository ini WAJIB ada untuk mengambil harga kamar
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Integer> {
}