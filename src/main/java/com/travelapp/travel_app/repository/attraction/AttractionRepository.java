
package com.travelapp.travel_app.repository.attraction;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.Attraction;

public interface AttractionRepository extends JpaRepository<Attraction, Integer> {}