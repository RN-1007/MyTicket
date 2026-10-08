package com.travelapp.travel_app.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
// Jika Anda pakai Lombok, tambahkan @Data, @NoArgsConstructor, @AllArgsConstructor
@Entity
@Table(name = "transports")
public class Transport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transportId;
    
    private String name;
    private Integer capacity;
    @Column(nullable = true, length = 255)
    private String image;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private TransportProvider provider;
    
    @OneToMany(mappedBy = "transport")
    private Set<TransportTicket> tickets;

    // Constructors, Getters, Setters...

    public Integer getTransportId() {
        return transportId;
    }

    public void setTransportId(Integer transportId) {
        this.transportId = transportId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TransportProvider getProvider() {
        return provider;
    }

    public void setProvider(TransportProvider provider) {
        this.provider = provider;
    }

    public Set<TransportTicket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<TransportTicket> tickets) {
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
            if (transportId != null) {
                return "/transport-photos/" + transportId + "/" + image;
            }
        }

        if (provider != null && provider.getType() != null) {
            switch (provider.getType()) {
                case Plane:
                    return "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=1200&auto=format&fit=crop";
                case Bus:
                    return "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?q=80&w=1200&auto=format&fit=crop";
                case Train:
                    return "https://images.unsplash.com/photo-1474487548417-781cb71495f3?q=80&w=1200&auto=format&fit=crop";
                case Boat:
                    return "https://images.unsplash.com/photo-1505705694340-019e1e335916?q=80&w=1200&auto=format&fit=crop";
            }
        }

        if (name != null) {
            String lower = name.toLowerCase();
            if (lower.contains("plane") || lower.contains("garuda") || lower.contains("air") || lower.contains("flight") || lower.contains("ga-")) {
                return "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=1200&auto=format&fit=crop";
            }
            if (lower.contains("damri") || lower.contains("bus")) {
                return "https://images.unsplash.com/photo-1544620347-c4fd4a3d5957?q=80&w=1200&auto=format&fit=crop";
            }
            if (lower.contains("kereta") || lower.contains("train") || lower.contains("kai")) {
                return "https://images.unsplash.com/photo-1474487548417-781cb71495f3?q=80&w=1200&auto=format&fit=crop";
            }
        }

        return "https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=1200&auto=format&fit=crop";
    }
}