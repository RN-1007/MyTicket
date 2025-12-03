package com.travelapp.travel_app.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelapp.travel_app.model.ItemType;
import com.travelapp.travel_app.service.user.OrderService;
import com.travelapp.travel_app.service.user.PaymentService;

@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private PaymentService paymentService;

    // Create Order (Tidak berubah)
    @PostMapping("/order/create")
    public String createOrder(
            @RequestParam("itemType") ItemType itemType,
            @RequestParam("itemId") Integer itemId,
            @RequestParam("quantity") int quantity,
            @RequestParam(value = "checkIn", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkIn,
            @RequestParam(value = "checkOut", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkOut,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.createNewOrder(itemType, itemId, quantity, authentication.getName(), checkIn, checkOut);
            redirectAttributes.addFlashAttribute("successMessage", "Item berhasil ditambahkan ke keranjang!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal: " + e.getMessage());
        }
        return "redirect:/my-orders"; 
    }

    // --- UPDATE: CHECKOUT MENGGUNAKAN POPUP ---
    @PostMapping("/order/checkout")
    public String processCheckout(@RequestParam(value = "orderIds", required = false) List<Integer> orderIds, 
                                  RedirectAttributes redirectAttributes) {
        if (orderIds == null || orderIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pilih setidaknya satu item untuk di-checkout.");
            return "redirect:/my-orders";
        }

        try {
            // 1. Minta TOKEN ke Midtrans (bukan URL lagi)
            String snapToken = paymentService.getSnapToken(orderIds);
            
            // 2. Kirim Token ke Frontend via FlashAttribute
            // Token ini akan ditangkap oleh JavaScript di halaman my-orders
            redirectAttributes.addFlashAttribute("snapToken", snapToken);
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memproses pembayaran: " + e.getMessage());
        }
        
        // Tetap di halaman yang sama, nanti JS yang akan buka popup
        return "redirect:/my-orders";
    }

    // Cancel Order (Tidak berubah)
    @PostMapping("/order/cancel/{id}")
    public String cancelOrder(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Item berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus item: " + e.getMessage());
        }
        return "redirect:/my-orders";
    }
}