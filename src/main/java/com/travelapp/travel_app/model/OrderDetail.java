package com.travelapp.travel_app.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderDetailId;

    private Integer quantity;
    private BigDecimal totalPrice;

    // --- TAMBAHAN BARU: TANGGAL MENGINAP ---
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkInDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkOutDate;
    // ---------------------------------------

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = true)
    private HotelRoom hotelRoom;

    @ManyToOne
    @JoinColumn(name = "attraction_ticket_id", nullable = true)
    private AttractionTicket attractionTicket;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = true)
    private TransportTicket transportTicket;

    // --- GETTERS AND SETTERS ---

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

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

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