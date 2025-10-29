package com.travelapp.travel_app.model;

import jakarta.persistence.*;
import java.util.Set;
// Jika Anda pakai Lombok, tambahkan @Data, @NoArgsConstructor, @AllArgsConstructor
@Entity
@Table(name = "transport_providers")
public class TransportProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer providerId;
    
    private String name;
    
    @Enumerated(EnumType.STRING)
    private TransportType type;
    
    private String contact;

    @OneToMany(mappedBy = "provider")
    private Set<Transport> transports;

    // Constructors, Getters, Setters...

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TransportType getType() {
        return type;
    }

    public void setType(TransportType type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<Transport> getTransports() {
        return transports;
    }

    public void setTransports(Set<Transport> transports) {
        this.transports = transports;
    }
}