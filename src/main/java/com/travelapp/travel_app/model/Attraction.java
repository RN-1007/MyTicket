package com.travelapp.travel_app.model;

import java.util.Set;

import jakarta.persistence.Column;
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
    @Column(nullable = true, length = 64)
    private String image;
    
    @Enumerated(EnumType.STRING)
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
        if (image == null || attractionId == null) return null;
        return "/attraction-photos/" + attractionId + "/" + image;
    }
}