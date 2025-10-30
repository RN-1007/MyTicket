package com.travelapp.travel_app.controller;

import org.springframework.beans.factory.annotation.Autowired; // <-- IMPORT BARU
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller; // <-- IMPORT BARU
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.service.AttractionService;
import com.travelapp.travel_app.service.CustomUserDetailsService;
import com.travelapp.travel_app.service.HotelService; // <-- IMPORT BARU
import com.travelapp.travel_app.service.OrderService; // <-- IMPORT BARU
import com.travelapp.travel_app.service.TransportService; // <-- IMPORT BARU

@Controller
public class PageController {

    // Inject Service
    @Autowired private HotelService hotelService;
    @Autowired private TransportService transportService;
    @Autowired private AttractionService attractionService;
    @Autowired private OrderService orderService;
    @Autowired private CustomUserDetailsService customUserDetailsService; // <-- TAMBAHKAN INI

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // --- METHOD BARU (GET /register) ---
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Kirim object User kosong ke form
        model.addAttribute("user", new User());
        return "register";
    }

    // --- METHOD BARU (POST /register) ---
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            customUserDetailsService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "Registrasi berhasil! Silakan login.");
            return "redirect:/login"; // Redirect ke halaman login jika sukses
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register"; // Kembali ke form registrasi jika gagal
        }
    }


    @GetMapping("/")
    public String userLandingPage(Model model) {
        // ... (sisanya tetap sama)
        model.addAttribute("hotels", hotelService.getAllHotels());
        model.addAttribute("transports", transportService.getAllTransports());
        model.addAttribute("attractions", attractionService.getAllAttractions());
        return "index"; 
    }

    @GetMapping("/my-orders")
    public String myOrdersPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        model.addAttribute("orders", orderService.findOrdersByUser(email));
        return "my-order"; // Pastikan ini "my-order" (singular)
    }
}