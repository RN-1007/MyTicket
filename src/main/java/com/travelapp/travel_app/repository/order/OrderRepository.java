package com.travelapp.travel_app.repository.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; // <-- Import User

import com.travelapp.travel_app.model.Order;
import com.travelapp.travel_app.model.User; // <-- Import List

public interface OrderRepository extends JpaRepository<Order, Integer> {
    

    List<Order> findByUser(User user);
    
}