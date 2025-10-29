package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.TransportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository ini WAJIB ada untuk mengambil harga tiket transport
public interface TransportTicketRepository extends JpaRepository<TransportTicket, Integer> {
}