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

import com.travelapp.travel_app.model.Hotel;
import com.travelapp.travel_app.model.HotelRoom;
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

    // --- PERBAIKAN DI SINI ---
    @GetMapping("/hotel/detail/{id}")
    public String hotelDetailPage(@PathVariable("id") Integer id, Model model) {
        // 1. Ambil data hotel
        Hotel hotel = hotelService.getHotelById(id)
                                  .orElseThrow(() -> new IllegalArgumentException("Invalid hotel Id:" + id));
        
        // 2. Masukkan hotel ke model
        model.addAttribute("hotel", hotel);

        // 3. Konversi Set<HotelRoom> menjadi List agar bisa diakses dengan index [0] di HTML
        List<HotelRoom> roomList = new ArrayList<>();
        if (hotel.getRooms() != null) {
            roomList.addAll(hotel.getRooms());
        }
        
        // 4. Kirim variable 'rooms' ke HTML sebagai List
        model.addAttribute("rooms", roomList);

        return "user/detail-order-hotel";
    }
}