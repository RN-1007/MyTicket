package com.travelapp.travel_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // <-- IMPORT BARU
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // <-- IMPORT BARU
import org.springframework.security.crypto.password.PasswordEncoder; // <-- IMPORT BARU

@SpringBootApplication
public class TravelAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelAppApplication.class, args);
	}

	/**
	 * PINDAHKAN BEAN KE SINI.
	 * Bean ini sekarang independen dan bisa di-inject
	 * ke service atau config mana pun tanpa membuat siklus.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}