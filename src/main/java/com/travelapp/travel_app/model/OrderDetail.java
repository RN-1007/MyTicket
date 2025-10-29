package com.travelapp.travel_app.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderDetailId;

    private Integer quantity;
    private BigDecimal totalPrice;

    // Relasi FK ke 'orders' (ini tetap sama)
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // --- PERUBAHAN DIMULAI DI SINI ---
    // Kolom itemType dan itemId dihapus.
    // Diganti dengan 3 relasi @ManyToOne yang direct.

    /**
     * Relasi ke HotelRoom.
     * nullable = true berarti kolom ini boleh kosong.
     */
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private HotelRoom hotelRoom;

    /**
     * Relasi ke AttractionTicket.
     */
    @ManyToOne
    @JoinColumn(name = "attraction_ticket_id", nullable = true)
    private AttractionTicket attractionTicket;

    /**
     * Relasi ke TransportTicket.
     */
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = true)
    private TransportTicket transportTicket;

    // --- AKHIR PERUBAHAN ---


    // Getters dan Setters...

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // --- Tambahkan Getters/Setters untuk 3 relasi baru ---

    public HotelRoom getHotelRoom() {
        return hotelRoom;
    }

    public void setHotelRoom(HotelRoom hotelRoom) {
        this.hotelRoom = hotelRoom;
    }

    public AttractionTicket getAttractionTicket() {
        return attractionTicket;
    }

    public void setAttractionTicket(AttractionTicket attractionTicket) {
        this.attractionTicket = attractionTicket;
    }

    public TransportTicket getTransportTicket() {
        return transportTicket;
    }

    public void setTransportTicket(TransportTicket transportTicket) {
        this.transportTicket = transportTicket;
    }
}