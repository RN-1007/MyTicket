package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository ini WAJIB ada untuk mengambil harga kamar
public interface HotelRoomRepository extends JpaRepository<HotelRoom, Integer> {
}