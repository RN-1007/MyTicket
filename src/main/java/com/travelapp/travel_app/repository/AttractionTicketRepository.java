package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.AttractionTicket;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository ini WAJIB ada untuk mengambil harga tiket atraksi
public interface AttractionTicketRepository extends JpaRepository<AttractionTicket, Integer> {
}