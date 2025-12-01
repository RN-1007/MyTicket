package com.travelapp.travel_app.repository;

import java.util.List; // Import List
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.travelapp.travel_app.model.Role; // Import Role
import com.travelapp.travel_app.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    
    // Method baru untuk filter by Role
    List<User> findByRole(Role role);
}