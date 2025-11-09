package com.travelapp.travel_app.service.hotel;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.HotelRoom;
import com.travelapp.travel_app.repository.hotel.HotelRoomRepository;

@Service
public class HotelRoomService {

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    public List<HotelRoom> findAll() {
        return hotelRoomRepository.findAll();
    }

    public Optional<HotelRoom> findById(Integer id) {
        return hotelRoomRepository.findById(id);
    }

    public HotelRoom save(HotelRoom hotelRoom) {
        return hotelRoomRepository.save(hotelRoom);
    }

    public void deleteById(Integer id) {
        hotelRoomRepository.deleteById(id);
    }
}