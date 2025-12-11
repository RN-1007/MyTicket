package com.travelapp.travel_app.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.travelapp.travel_app.model.ItemType;
import com.travelapp.travel_app.model.Order;
import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.repository.UserRepository;
import com.travelapp.travel_app.repository.order.OrderRepository;
import com.travelapp.travel_app.service.attraction.AttractionService;
import com.travelapp.travel_app.service.hotel.HotelRoomService;
import com.travelapp.travel_app.service.hotel.HotelService;        // <--- TAMBAHAN
import com.travelapp.travel_app.service.transport.TransportService; // <--- TAMBAHAN
import com.travelapp.travel_app.service.user.OrderService;                      // <--- TAMBAHAN
import com.travelapp.travel_app.service.user.PaymentService;                                      // <--- TAMBAHAN
@Controller
@RequestMapping("/android")
public class MobileController {

    @Autowired private HotelService hotelService;
    @Autowired private TransportService transportService;
    @Autowired private AttractionService attractionService;
    @Autowired private OrderService orderService;
    @Autowired private PaymentService paymentService;
    @Autowired private HotelRoomService hotelRoomService;
    @Autowired private UserRepository userRepository;
    @Autowired private OrderRepository orderRepository;

    // --- 1. MOBILE HOME ---
    @GetMapping({"", "/", "/home"})
    public String mobileIndex(Model model) {
        model.addAttribute("hotels", hotelService.getAllHotels());
        model.addAttribute("transports", transportService.getAllTransports());
        model.addAttribute("attractions", attractionService.getAllAttractions());
        return "mobile/index"; 
    }

    // --- 2. MOBILE LOGIN ---
    @GetMapping("/login")
    public String mobileLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "mobile/login";
    }

    // --- 3. MOBILE MY ORDERS ---
    @GetMapping("/my-orders")
    public String mobileMyOrders(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/android/login";
        }
        
        String email = authentication.getName();
        model.addAttribute("pendingOrders", orderService.findPendingOrders(email));
        model.addAttribute("historyOrders", orderService.findHistoryOrders(email));
        
        if (model.containsAttribute("snapToken")) {
            System.out.println("✅ Android: SnapToken dikirim ke View");
        }
        
        return "mobile/my-orders";
    }

    // --- 4. ACTION: CHECKOUT ---
    @PostMapping("/order/checkout")
    public String mobileCheckout(@RequestParam(value = "orderIds", required = false) List<Integer> orderIds, 
                                 RedirectAttributes redirectAttributes) {
        if (orderIds == null || orderIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pilih minimal satu item untuk dibayar.");
            return "redirect:/android/my-orders";
        }

        try {
            String snapToken = paymentService.getSnapToken(orderIds);
            System.out.println("💳 SnapToken Generated: " + snapToken);
            redirectAttributes.addFlashAttribute("snapToken", snapToken);
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memproses pembayaran: " + e.getMessage());
        }
        
        return "redirect:/android/my-orders";
    }

    // --- 5. ACTION: CANCEL ORDER ---
    @PostMapping("/order/cancel/{id}")
    public String mobileCancel(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Item berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal hapus: " + e.getMessage());
        }
        return "redirect:/android/my-orders";
    }
    
    // --- 6. ACTION: CREATE ORDER ---
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
            if (authentication == null) return "redirect:/android/login";
            
            orderService.createNewOrder(itemType, itemId, quantity, authentication.getName(), checkIn, checkOut);
            redirectAttributes.addFlashAttribute("successMessage", "Berhasil ditambahkan ke keranjang!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal: " + e.getMessage());
        }
        return "redirect:/android/my-orders"; 
    }

    // --- 7. DETAIL PAGES ---
    @GetMapping("/hotel/detail/{id}")
    public String mobileHotelDetail(@PathVariable("id") Integer id, Model model) {
        com.travelapp.travel_app.model.Hotel hotel = hotelService.getHotelById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
        model.addAttribute("hotel", hotel);
        model.addAttribute("rooms", new ArrayList<>(hotel.getRooms()));
        return "mobile/detail/detail-hotel";
    }

    @GetMapping("/attraction/detail/{id}")
    public String mobileAttractionDetail(@PathVariable("id") Integer id, Model model) {
        com.travelapp.travel_app.model.Attraction attraction = attractionService.getAttractionById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
        model.addAttribute("attraction", attraction);
        model.addAttribute("tickets", new ArrayList<>(attraction.getTickets()));
        return "mobile/detail/detail-attraction";
    }

    @GetMapping("/transport/detail/{id}")
    public String mobileTransportDetail(@PathVariable("id") Integer id, Model model) {
        com.travelapp.travel_app.model.Transport transport = transportService.getTransportById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid Id"));
        model.addAttribute("transport", transport);
        model.addAttribute("tickets", new ArrayList<>(transport.getTickets()));
        return "mobile/detail/detail-transport";
    }
    // --- 10. MOBILE PROFILE PAGE (BARU) ---
    @GetMapping("/profile")
    public String mobileProfile(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/android/login";
        }
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        return "mobile/profile";
    }

    // --- 11. MOBILE INVOICE PAGE (BARU) ---
    @GetMapping("/order/invoice/{id}")
    public String mobileInvoice(@PathVariable("id") Integer id, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/android/login";
        }

        // 1. Cari Order
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order tidak ditemukan: " + id));

        // 2. Security: Cek kepemilikan
        if (!order.getUser().getEmail().equals(authentication.getName())) {
            return "redirect:/android/my-orders";
        }

        // 3. Hitung Total
        BigDecimal grandTotal = order.getOrderDetails().stream()
                .map(detail -> detail.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("order", order);
        model.addAttribute("grandTotal", grandTotal);

        return "mobile/invoice";
    }
    
}