package com.travelapp.travel_app.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {}