package com.travelapp.travel_app.repository;

import com.travelapp.travel_app.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {}