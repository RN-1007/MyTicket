package com.travelapp.travel_app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelapp.travel_app.model.Attraction;        // Import Baru
import com.travelapp.travel_app.model.AttractionTicket;  // Import Baru
import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.model.HotelRoom;
import com.travelapp.travel_app.model.Transport;         // Import Baru
import com.travelapp.travel_app.model.TransportTicket;   // Import Baru
import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.service.attraction.AttractionService;
import com.travelapp.travel_app.service.hotel.HotelService;
import com.travelapp.travel_app.service.transport.TransportService;
import com.travelapp.travel_app.service.user.CustomUserDetailsService;
import com.travelapp.travel_app.service.user.OrderService;

@Controller
public class PageController {

    @Autowired private HotelService hotelService;
    @Autowired private TransportService transportService;
    @Autowired private AttractionService attractionService;
    @Autowired private OrderService orderService;
    @Autowired private CustomUserDetailsService customUserDetailsService; 

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            customUserDetailsService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registrasi berhasil! Silakan login.");
            return "redirect:/login"; 
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register"; 
        }
    }

    @GetMapping("/")
    public String userLandingPage(Model model) {
        model.addAttribute("hotels", hotelService.getAllHotels());
        model.addAttribute("transports", transportService.getAllTransports());
        model.addAttribute("attractions", attractionService.getAllAttractions());
        return "user/index"; 
    }

    @GetMapping("/my-orders")
    public String myOrdersPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        model.addAttribute("orders", orderService.findOrdersByUser(email));
        return "user/my-orders";
    }

    @GetMapping("/about-us")
    public String aboutUsPage() {
        return "user/about-us";
    }

    // --- DETAIL HOTEL ---
    @GetMapping("/hotel/detail/{id}")
    public String hotelDetailPage(@PathVariable("id") Integer id, Model model) {
        Hotel hotel = hotelService.getHotelById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("Invalid hotel Id:" + id));
        
        model.addAttribute("hotel", hotel);

        List<HotelRoom> roomList = new ArrayList<>();
        if (hotel.getRooms() != null) {
            roomList.addAll(hotel.getRooms());
        }
        model.addAttribute("rooms", roomList);

        return "user/detail/detail-order-hotel";
    }

    // --- DETAIL ATTRACTION (BARU) ---
    @GetMapping("/attraction/detail/{id}")
    public String attractionDetailPage(@PathVariable("id") Integer id, Model model) {
        Attraction attraction = attractionService.getAttractionById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid attraction Id:" + id));
        
        model.addAttribute("attraction", attraction);

        // Konversi Set ke List agar mudah diakses index-nya di frontend
        List<AttractionTicket> ticketList = new ArrayList<>();
        if (attraction.getTickets() != null) {
            ticketList.addAll(attraction.getTickets());
        }
        model.addAttribute("tickets", ticketList);

        return "user/detail/detail-order-attraction"; // Path file template baru
    }

    // --- DETAIL TRANSPORT (BARU) ---
    @GetMapping("/transport/detail/{id}")
    public String transportDetailPage(@PathVariable("id") Integer id, Model model) {
        Transport transport = transportService.getTransportById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid transport Id:" + id));
        
        model.addAttribute("transport", transport);

        // Konversi Set ke List
        List<TransportTicket> ticketList = new ArrayList<>();
        if (transport.getTickets() != null) {
            ticketList.addAll(transport.getTickets());
        }
        model.addAttribute("tickets", ticketList);

        return "user/detail/detail-order-transport"; // Path file template baru
    }
}