package com.travelapp.travel_app.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import com.travelapp.travel_app.repository.AttractionTicketRepository;
import com.travelapp.travel_app.repository.HotelRoomRepository;
import com.travelapp.travel_app.repository.OrderDetailRepository;
import com.travelapp.travel_app.repository.OrderRepository;
import com.travelapp.travel_app.repository.TransportTicketRepository;
import com.travelapp.travel_app.repository.UserRepository;

@Service
public class OrderService {

    // Service ini meng-inject semua repository yang berhubungan dengan pemesanan
    // (Ini tidak berubah)
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderDetailRepository orderDetailRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private HotelRoomRepository hotelRoomRepository;
    @Autowired private TransportTicketRepository transportTicketRepository;
    @Autowired private AttractionTicketRepository attractionTicketRepository;

    /**
     * Logika bisnis utama untuk membuat pesanan baru.
     * @Transactional memastikan semua operasi database berhasil,
     * atau semua akan dibatalkan (rollback) jika terjadi error.
     * * --- INI ADALAH METHOD YANG DIMODIFIKASI ---
     */
    @Transactional
    public Order createNewOrder(ItemType itemType, Integer itemId, int quantity, String userEmail) {
        
        // 1. Dapatkan User (Tidak berubah)
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Buat Order (Tidak berubah)
        Order newOrder = new Order();
        newOrder.setUser(currentUser);
        newOrder.setOrderDate(new Date());
        newOrder.setStatus(OrderStatus.Pending);
        Order savedOrder = orderRepository.save(newOrder);

        // 3. Ambil harga (Tidak berubah, kita tetap perlu memanggil helper getPrice)
        BigDecimal pricePerItem = getPrice(itemType, itemId);
        if (pricePerItem == null) {
            throw new RuntimeException("Item (kamar/tiket) dengan ID " + itemId + " tidak ditemukan.");
        }
        BigDecimal totalPrice = pricePerItem.multiply(new BigDecimal(quantity));

        // 4. Buat OrderDetail (--- INI BAGIAN YANG BERUBAH ---)
        OrderDetail newDetail = new OrderDetail();
        newDetail.setOrder(savedOrder);
        newDetail.setQuantity(quantity);
        newDetail.setTotalPrice(totalPrice);

        // Logika baru: set entity yang sesuai berdasarkan itemType, bukan ID mentah
        switch (itemType) {
            case Hotel:
                // Ambil seluruh object HotelRoom
                HotelRoom room = hotelRoomRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Kamar hotel tidak ditemukan"));
                // Set relasinya
                newDetail.setHotelRoom(room);
                break;
            case Transport:
                // Ambil seluruh object TransportTicket
                TransportTicket transportTicket = transportTicketRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Tiket transport tidak ditemukan"));
                // Set relasinya
                newDetail.setTransportTicket(transportTicket);
                break;
            case Attraction:
                 // Ambil seluruh object AttractionTicket
                AttractionTicket attractionTicket = attractionTicketRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Tiket atraksi tidak ditemukan"));
                // Set relasinya
                newDetail.setAttractionTicket(attractionTicket);
                break;
            default:
                throw new RuntimeException("Tipe item tidak diketahui");
        }

        // Simpan OrderDetail yang sudah dimodifikasi
        orderDetailRepository.save(newDetail);

        return savedOrder;
    }

    /**
     * Logika bisnis untuk mengambil riwayat pesanan user.
     * (Ini tidak berubah)
     */
    public List<Order> findOrdersByUser(String userEmail) {
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Memanggil method repository
        return orderRepository.findByUser(currentUser);
    }

    /**
     * Fungsi helper privat untuk mendapatkan harga dari berbagai repository.
     * (Ini tidak berubah)
     */
    private BigDecimal getPrice(ItemType itemType, Integer itemId) {
        switch (itemType) {
            case Hotel:
                return hotelRoomRepository.findById(itemId)
                        .map(HotelRoom::getPrice)
                        .orElse(null);
            case Transport:
                return transportTicketRepository.findById(itemId)
                        .map(TransportTicket::getPrice)
                        .orElse(null);
            case Attraction:
                return attractionTicketRepository.findById(itemId)
                        .map(AttractionTicket::getPrice)
                        .orElse(null);
            default:
                throw new RuntimeException("Tipe item tidak diketahui");
        }
    }
}