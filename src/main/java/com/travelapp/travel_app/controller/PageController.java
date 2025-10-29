package com.travelapp.travel_app.controller;

import com.travelapp.travel_app.service.AttractionService;
import com.travelapp.travel_app.service.HotelService;
import com.travelapp.travel_app.service.OrderService;
import com.travelapp.travel_app.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Inject Service
    @Autowired private HotelService hotelService;
    @Autowired private TransportService transportService;
    @Autowired private AttractionService attractionService;
    @Autowired private OrderService orderService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String userLandingPage(Model model) {
        // Mengambil data dari service
        model.addAttribute("hotels", hotelService.getAllHotels());
        model.addAttribute("transports", transportService.getAllTransports());
        model.addAttribute("attractions", attractionService.getAllAttractions());
        return "index"; 
    }

    @GetMapping("/my-orders")
    public String myOrdersPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        // Mengambil data order dari OrderService
        model.addAttribute("orders", orderService.findOrdersByUser(email));
        return "my-orders";
    }
}