package com.travelapp.travel_app.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
// Jika Anda pakai Lombok, tambahkan @Data, @NoArgsConstructor, @AllArgsConstructor
@Entity
@Table(name = "transport_tickets")
public class TransportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;
    
    private Date departureDate;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;

    // Constructors, Getters, Setters...

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}