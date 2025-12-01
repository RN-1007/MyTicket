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

@Entity
@Table(name = "attraction_tickets")
public class AttractionTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attractionTicketId;
    
    // Field baru untuk jenis tiket (Ex: "Regular", "Fast Track", "VIP")
    private String ticketType;
    
    private BigDecimal price;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validDate; // (Opsional)

    @ManyToOne
    @JoinColumn(name = "attraction_id")
    private Attraction attraction;

    // Constructors, Getters, Setters...

    public Integer getAttractionTicketId() {
        return attractionTicketId;
    }

    public void setAttractionTicketId(Integer attractionTicketId) {
        this.attractionTicketId = attractionTicketId;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }
}