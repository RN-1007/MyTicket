package com.travelapp.travel_app.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelapp.travel_app.model.ItemType;
import com.travelapp.travel_app.model.Order;
import com.travelapp.travel_app.repository.order.OrderRepository;
import com.travelapp.travel_app.service.user.OrderService;
import com.travelapp.travel_app.service.user.PaymentService;

@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private PaymentService paymentService;
    @Autowired private OrderRepository orderRepository; 

    // --- CREATE ORDER (FITUR LAMA TETAP ADA) ---
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

    // --- CHECKOUT (FITUR LAMA TETAP ADA) ---
    @PostMapping("/order/checkout")
    public String processCheckout(@RequestParam(value = "orderIds", required = false) List<Integer> orderIds, 
                                  RedirectAttributes redirectAttributes) {
        if (orderIds == null || orderIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pilih setidaknya satu item untuk di-checkout.");
            return "redirect:/my-orders";
        }

        try {
            String snapToken = paymentService.getSnapToken(orderIds);
            redirectAttributes.addFlashAttribute("snapToken", snapToken);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memproses pembayaran: " + e.getMessage());
        }
        
        return "redirect:/my-orders";
    }

    // --- CANCEL ORDER (FITUR LAMA TETAP ADA) ---
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

    // --- FITUR BARU: INVOICE PAGE ---
    @GetMapping("/order/invoice/{id}")
    public String showInvoice(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        // 1. Cari Order
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order tidak ditemukan: " + id));

        // 2. Security: Cek kepemilikan
        String currentEmail = authentication.getName();
        if (!order.getUser().getEmail().equals(currentEmail)) {
            return "redirect:/my-orders"; 
        }

        // 3. Hitung Total
        BigDecimal grandTotal = order.getOrderDetails().stream()
                .map(detail -> detail.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("order", order);
        model.addAttribute("grandTotal", grandTotal);

        return "user/invoice"; 
    }
}