package com.travelapp.travel_app.repository.transport;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.TransportTicket;

// Repository ini WAJIB ada untuk mengambil harga tiket transport
public interface TransportTicketRepository extends JpaRepository<TransportTicket, Integer> {
}