package com.travelapp.travel_app.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelapp.travel_app.service.user.OrderService;

@RestController
@RequestMapping("/api/midtrans")
public class NotificationController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/notification")
    public ResponseEntity<String> handleNotification(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("🔔 WEBHOOK MIDTRANS DITERIMA: " + payload); // Log Payload Masuk

            String transactionStatus = (String) payload.get("transaction_status");
            String orderIdsString = (String) payload.get("custom_field1");

            if (orderIdsString == null) {
                System.out.println("❌ Order ID tidak ditemukan di custom_field1");
                return new ResponseEntity<>("Order IDs not found", HttpStatus.OK);
            }

            List<Integer> orderIds = Arrays.stream(orderIdsString.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            if (transactionStatus.equals("capture") || transactionStatus.equals("settlement")) {
                orderService.payMultipleOrders(orderIds);
                System.out.println("✅ PEMBAYARAN SUKSES! Order IDs: " + orderIdsString + " diupdate jadi PAID.");
            } else if (transactionStatus.equals("deny") || transactionStatus.equals("cancel") || transactionStatus.equals("expire")) {
                System.out.println("⚠️ Pembayaran Gagal/Batal untuk Order IDs: " + orderIdsString);
            }

            return new ResponseEntity<>("OK", HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}