package com.travelapp.travel_app.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "attractions")
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attractionId;
    
    private String name;
    private String location;
    @Column(nullable = true, length = 255)
    private String image;
    
    @Convert(converter = CategoryConverter.class)
    private Category category;

    @OneToMany(mappedBy = "attraction")
    private Set<AttractionTicket> tickets;

    // Constructors, Getters, Setters...

    public Integer getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(Integer attractionId) {
        this.attractionId = attractionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<AttractionTicket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<AttractionTicket> tickets) {
        this.tickets = tickets;
    }
    public String getImage() { 
        return image; 
    }

    public void setImage(String image) {
        this.image = image; 
    }
    @Transient
    public String getImagePath() {
        if (image != null && !image.trim().isEmpty()) {
            if (image.startsWith("http://") || image.startsWith("https://")) {
                return image;
            }
            if (attractionId != null) {
                return "/attraction-photos/" + attractionId + "/" + image;
            }
        }

        if (name != null) {
            String lower = name.toLowerCase();
            if (lower.contains("borobudur") || lower.contains("temple") || lower.contains("prambanan") || lower.contains("candi")) {
                return "https://images.unsplash.com/photo-1596402184320-417e7178b2cd?q=80&w=1200&auto=format&fit=crop";
            }
            if (lower.contains("ancol") || lower.contains("dufan") || lower.contains("park") || lower.contains("dreamland")) {
                return "https://images.unsplash.com/photo-1513889961551-628c1e5e2ee9?q=80&w=1200&auto=format&fit=crop";
            }
            if (lower.contains("beach") || lower.contains("pantai") || lower.contains("sea") || lower.contains("bali")) {
                return "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=1200&auto=format&fit=crop";
            }
        }

        if (category != null) {
            switch (category) {
                case Culture:
                    return "https://images.unsplash.com/photo-1596402184320-417e7178b2cd?q=80&w=1200&auto=format&fit=crop";
                case Park:
                    return "https://images.unsplash.com/photo-1513889961551-628c1e5e2ee9?q=80&w=1200&auto=format&fit=crop";
                case Nature:
                    return "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=1200&auto=format&fit=crop";
                case Museum:
                    return "https://images.unsplash.com/photo-1565008447742-97f6f38c985c?q=80&w=1200&auto=format&fit=crop";
            }
        }

        return "https://images.unsplash.com/photo-1516483638261-f4dbaf036963?q=80&w=1200&auto=format&fit=crop";
    }
}