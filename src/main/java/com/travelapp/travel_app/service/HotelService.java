package com.travelapp.travel_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.repository.hotel.HotelRepository;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Optional<Hotel> getHotelById(Integer id) {
        return hotelRepository.findById(id);
    }

    public Hotel saveHotel(Hotel hotel) {
        // Anda bisa menambahkan logika validasi di sini
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Integer id) {
        hotelRepository.deleteById(id);
    }

    public long getHotelCount() {
        return hotelRepository.count();
    }
}