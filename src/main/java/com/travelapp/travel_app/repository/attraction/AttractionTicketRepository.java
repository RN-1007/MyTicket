package com.travelapp.travel_app.repository.attraction;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.AttractionTicket;

// Repository ini WAJIB ada untuk mengambil harga tiket atraksi
public interface AttractionTicketRepository extends JpaRepository<AttractionTicket, Integer> {
}