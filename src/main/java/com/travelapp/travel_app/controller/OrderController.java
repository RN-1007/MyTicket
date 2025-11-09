package com.travelapp.travel_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // <-- Hanya perlu 1 service
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelapp.travel_app.model.ItemType;
import com.travelapp.travel_app.service.user.OrderService;

@Controller
public class OrderController {

    // Controller CUKUP inject SATU service
    @Autowired
    private OrderService orderService;

    @PostMapping("/order/create")
    public String createOrder(
            @RequestParam("itemType") ItemType itemType,
            @RequestParam("itemId") Integer itemId,
            @RequestParam("quantity") int quantity,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            // Semua logika bisnis kompleks dipanggil HANYA dengan 1 baris ini
            orderService.createNewOrder(itemType, itemId, quantity, authentication.getName());
            
            redirectAttributes.addFlashAttribute("successMessage", "Pesanan berhasil dibuat!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal membuat pesanan: " + e.getMessage());
        }

        return "redirect:/my-orders"; 
    }
}