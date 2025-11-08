package com.travelapp.travel_app.repository.transport;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.TransportProvider;

// Repository ini diperlukan untuk mengambil daftar provider di form admin
public interface TransportProviderRepository extends JpaRepository<TransportProvider, Integer> {
}