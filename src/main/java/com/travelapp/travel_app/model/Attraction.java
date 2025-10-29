package com.travelapp.travel_app.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "attractions")
public class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attractionId;
    
    private String name;
    private String location;
    
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
}