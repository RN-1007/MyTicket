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
    @Column(nullable = true, length = 64)
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
        if (image == null || transportId == null) return null;
        return "/transport-photos/" + transportId + "/" + image;
    }
}