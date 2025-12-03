package com.travelapp.travel_app.service.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.midtrans.httpclient.SnapApi;
import com.midtrans.httpclient.error.MidtransError;
import com.travelapp.travel_app.model.Order;
import com.travelapp.travel_app.model.OrderDetail;
import com.travelapp.travel_app.repository.order.OrderRepository;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    // GANTI NAMA METHOD JADI: getSnapToken
    public String getSnapToken(List<Integer> orderIds) throws MidtransError {
        
        // 1. Ambil data order
        List<Order> orders = orderRepository.findAllById(orderIds);
        if (orders.isEmpty()) throw new RuntimeException("Order tidak ditemukan.");

        // 2. Hitung Total
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Order order : orders) {
            for (OrderDetail detail : order.getOrderDetails()) {
                totalAmount = totalAmount.add(detail.getTotalPrice());
            }
        }

        // 3. Buat ID Transaksi Unik
        String uniqueTransactionId = "PAY-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4);

        // 4. Siapkan Parameter
        Map<String, Object> params = new HashMap<>();

        // A. Transaction Details
        Map<String, Object> transactionDetails = new HashMap<>();
        transactionDetails.put("order_id", uniqueTransactionId);
        transactionDetails.put("gross_amount", totalAmount);
        params.put("transaction_details", transactionDetails);

        // B. Customer Details
        Order firstOrder = orders.get(0);
        Map<String, Object> customerDetails = new HashMap<>();
        customerDetails.put("first_name", firstOrder.getUser().getName());
        customerDetails.put("email", firstOrder.getUser().getEmail());
        customerDetails.put("phone", firstOrder.getUser().getPhone());
        params.put("customer_details", customerDetails);
        
        // C. Item Details
        List<Map<String, Object>> itemDetails = new ArrayList<>();
        for (Order order : orders) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Map<String, Object> item = new HashMap<>();
                String itemName = "Item Travel"; 
                if (detail.getHotelRoom() != null) itemName = "Hotel: " + detail.getHotelRoom().getHotel().getName();
                else if (detail.getAttractionTicket() != null) itemName = "Tiket: " + detail.getAttractionTicket().getAttraction().getName();
                else if (detail.getTransportTicket() != null) itemName = "Tiket: " + detail.getTransportTicket().getTransport().getName();
                
                if (itemName.length() > 50) itemName = itemName.substring(0, 50);

                item.put("id", String.valueOf(detail.getOrderDetailId()));
                item.put("price", detail.getTotalPrice().divide(new BigDecimal(detail.getQuantity()))); 
                item.put("quantity", detail.getQuantity());
                item.put("name", itemName);
                itemDetails.add(item);
            }
        }
        params.put("item_details", itemDetails);

        // D. Custom Field
        String orderIdsString = orderIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        params.put("custom_field1", orderIdsString);

        // --- PERUBAHAN PENTING DISINI ---
        // Gunakan createTransactionToken, BUKAN createTransactionRedirectUrl
        return SnapApi.createTransactionToken(params);
    }
}