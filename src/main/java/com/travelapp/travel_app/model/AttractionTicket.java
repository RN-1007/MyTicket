package com.travelapp.travel_app.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
// Jika Anda pakai Lombok, tambahkan @Data, @NoArgsConstructor, @AllArgsConstructor
@Entity
@Table(name = "attraction_tickets")
public class AttractionTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attractionTicketId;
    
    private BigDecimal price;
    private Date validDate;

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