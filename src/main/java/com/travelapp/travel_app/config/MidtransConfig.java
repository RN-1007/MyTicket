package com.travelapp.travel_app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.midtrans.Midtrans;

import jakarta.annotation.PostConstruct; // Ganti import ini

@Configuration
public class MidtransConfig {

    @Value("${midtrans.server-key}")
    private String serverKey;

    @Value("${midtrans.client-key}")
    private String clientKey;

    @Value("${midtrans.is-production}")
    private boolean isProduction;

    // UBAH DARI @Bean MENJADI @PostConstruct
    @PostConstruct
    public void initMidtrans() {
        Midtrans.serverKey = serverKey;
        Midtrans.clientKey = clientKey;
        Midtrans.isProduction = isProduction;
        
        System.out.println("Midtrans Configuration Initialized with ServerKey: " + serverKey);
    }
}