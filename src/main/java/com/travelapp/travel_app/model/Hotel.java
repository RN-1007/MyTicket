package com.travelapp.travel_app.model;

import jakarta.persistence.*;
import java.util.Set;
// Jika Anda pakai Lombok, tambahkan @Data, @NoArgsConstructor, @AllArgsConstructor
@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hotelId;
    
    private String name;
    private String location;
    private Integer stars;

    @OneToMany(mappedBy = "hotel")
    private Set<HotelRoom> rooms;

    // Constructors, Getters, Setters...

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
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

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Set<HotelRoom> getRooms() {
        return rooms;
    }

    public void setRooms(Set<HotelRoom> rooms) {
        this.rooms = rooms;
    }
}