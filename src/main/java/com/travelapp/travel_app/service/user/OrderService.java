package com.travelapp.travel_app.service.user;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travelapp.travel_app.model.AttractionTicket;
import com.travelapp.travel_app.model.HotelRoom;
import com.travelapp.travel_app.model.ItemType;
import com.travelapp.travel_app.model.Order;
import com.travelapp.travel_app.model.OrderDetail;
import com.travelapp.travel_app.model.OrderStatus;
import com.travelapp.travel_app.model.TransportTicket;
import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.repository.UserRepository;
import com.travelapp.travel_app.repository.attraction.AttractionTicketRepository;
import com.travelapp.travel_app.repository.hotel.HotelRoomRepository;
import com.travelapp.travel_app.repository.order.OrderDetailRepository;
import com.travelapp.travel_app.repository.order.OrderRepository;
import com.travelapp.travel_app.repository.transport.TransportTicketRepository;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private HotelRoomRepository hotelRoomRepository;
    @Autowired private TransportTicketRepository transportTicketRepository;
    @Autowired private AttractionTicketRepository attractionTicketRepository;

    // --- CREATE ORDER (SUPPORT TANGGAL & HITUNG MALAM HOTEL) ---
    @Transactional
    public Order createNewOrder(ItemType itemType, Integer itemId, int quantity, String userEmail, Date checkIn, Date checkOut) {
        
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order newOrder = new Order();
        newOrder.setUser(currentUser);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus(OrderStatus.Pending);
        Order savedOrder = orderRepository.save(newOrder);

        BigDecimal pricePerItem = getPrice(itemType, itemId);
        if (pricePerItem == null) {
            throw new RuntimeException("Item dengan ID " + itemId + " tidak ditemukan.");
        }

        OrderDetail newDetail = new OrderDetail();
        newDetail.setOrder(savedOrder);
        newDetail.setQuantity(quantity);

        BigDecimal totalPrice;

        // Logika Hotel (Hitung Malam)
        if (itemType == ItemType.Hotel) {
            if (checkIn == null || checkOut == null) {
                throw new RuntimeException("Tanggal Check-in dan Check-out wajib diisi.");
            }
            if (!checkOut.after(checkIn)) {
                throw new RuntimeException("Tanggal Check-out harus setelah Check-in.");
            }

            long diffInMillies = Math.abs(checkOut.getTime() - checkIn.getTime());
            long nights = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (nights < 1) nights = 1;

            totalPrice = pricePerItem.multiply(new BigDecimal(quantity)).multiply(new BigDecimal(nights));
            
            newDetail.setCheckInDate(checkIn);
            newDetail.setCheckOutDate(checkOut);
            
            HotelRoom room = hotelRoomRepository.findById(itemId).orElseThrow();
            newDetail.setHotelRoom(room);

        } else {
            // Logika Transport & Attraction (Harga Flat per Tiket)
            if (checkIn == null) {
                throw new RuntimeException("Tanggal Keberangkatan/Kunjungan wajib dipilih.");
            }

            newDetail.setCheckInDate(checkIn); 
            // checkOutDate null untuk tiket
            
            totalPrice = pricePerItem.multiply(new BigDecimal(quantity));
            
            if (itemType == ItemType.Transport) {
                TransportTicket t = transportTicketRepository.findById(itemId).orElseThrow();
                newDetail.setTransportTicket(t);
            } else if (itemType == ItemType.Attraction) {
                AttractionTicket a = attractionTicketRepository.findById(itemId).orElseThrow();
                newDetail.setAttractionTicket(a);
            }
        }

        newDetail.setTotalPrice(totalPrice);
        orderDetailRepository.save(newDetail);

        return savedOrder;
    }

    // --- GET ALL ORDERS ---
    public List<Order> findOrdersByUser(String userEmail) {
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUser(currentUser);
    }

    // --- GET PENDING ORDERS (KERANJANG) ---
    public List<Order> findPendingOrders(String userEmail) {
        return findOrdersByUser(userEmail).stream()
                .filter(o -> o.getStatus() == OrderStatus.Pending)
                .collect(Collectors.toList());
    }

    // --- GET HISTORY ORDERS (SUDAH DIBAYAR/BATAL) ---
    public List<Order> findHistoryOrders(String userEmail) {
        return findOrdersByUser(userEmail).stream()
                .filter(o -> o.getStatus() != OrderStatus.Pending)
                .collect(Collectors.toList());
    }

    // --- PROCESS CHECKOUT (BULK PAYMENT) ---
    @Transactional
    public void payMultipleOrders(List<Integer> orderIds) {
        List<Order> orders = orderRepository.findAllById(orderIds);
        for (Order order : orders) {
            if (order.getStatus() == OrderStatus.Pending) {
                order.setStatus(OrderStatus.Paid);
                orderRepository.save(order);
            }
        }
    }

    // --- CANCEL ORDER (DELETE ITEM) ---
    @Transactional
    public void cancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        if (order.getStatus() == OrderStatus.Pending) {
            order.setStatus(OrderStatus.Cancelled); 
            // Opsional: orderRepository.delete(order); jika ingin hapus permanen
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Tidak bisa membatalkan pesanan yang sudah diproses.");
        }
    }

    // --- HELPER PRICE ---
    private BigDecimal getPrice(ItemType itemType, Integer itemId) {
        switch (itemType) {
            case Hotel:
                return hotelRoomRepository.findById(itemId).map(HotelRoom::getPrice).orElse(null);
            case Transport:
                return transportTicketRepository.findById(itemId).map(TransportTicket::getPrice).orElse(null);
            case Attraction:
                return attractionTicketRepository.findById(itemId).map(AttractionTicket::getPrice).orElse(null);
            default:
                throw new RuntimeException("Tipe item tidak diketahui");
        }
    }
}