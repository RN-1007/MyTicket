
package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttractionRepository extends JpaRepository<Attraction, Integer> {}