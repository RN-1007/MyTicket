package com.travelapp.travel_app.model;

import java.math.BigDecimal;
import java.util.Comparator; // Import penting
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer hotelId;
    
    private String name;
    private String location;
    private Integer stars;
    
    // Saya ubah jadi 255 agar aman untuk nama file panjang
    @Column(nullable = true, length = 255)
    private String image;

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

    public String getImage() { 
        return image; 
    }

    public void setImage(String image) { 
        this.image = image; 
    }

    public Set<HotelRoom> getRooms() {
        return rooms;
    }

    public void setRooms(Set<HotelRoom> rooms) {
        this.rooms = rooms;
    }

    @Transient
    public String getImagePath() {
        if (image == null || hotelId == null) return null;
        return "/hotel-photos/" + hotelId + "/" + image;
    }

    // --- METHOD BARU UNTUK MENGHITUNG HARGA TERENDAH ---
    @Transient
    public BigDecimal getStartingPrice() {
        if (rooms == null || rooms.isEmpty()) {
            return BigDecimal.ZERO;
        }
        // Cari harga terendah dari daftar kamar
        return rooms.stream()
                .map(HotelRoom::getPrice)
                .filter(price -> price != null)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
    }
}