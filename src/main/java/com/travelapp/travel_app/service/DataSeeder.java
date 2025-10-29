package com.travelapp.travel_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.travelapp.travel_app.model.Role;
import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.repository.UserRepository;

/**
 * Component ini akan otomatis berjalan satu kali saat aplikasi Spring Boot startup.
 * Ini akan mengisi database dengan data awal (seed) jika data tersebut belum ada.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    // Kita butuh UserRepository untuk menyimpan data user
    @Autowired
    private UserRepository userRepository;

    // Kita butuh PasswordEncoder untuk MENG-HASH password
    // Ini diambil dari bean yang Anda buat di SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // --- SEEDER UNTUK ADMIN ---
        // Kita cek berdasarkan email agar tidak membuat user duplikat setiap kali restart
        if (userRepository.findByEmail("admin@travel.app").isEmpty()) {
            
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@travel.app");
            
            // PENTING: Jangan pernah simpan password plain text.
            // Gunakan encoder untuk mengenkripsinya.
            admin.setPassword(passwordEncoder.encode("admin123")); 
            
            admin.setPhone("081200001111");
            admin.setRole(Role.ROLE_ADMIN); // Set rolenya sebagai ADMIN
            
            userRepository.save(admin);
        }

        // --- SEEDER UNTUK USER BIASA ---
        if (userRepository.findByEmail("user@travel.app").isEmpty()) {
            
            User user = new User();
            user.setName("user");
            user.setEmail("user@travel.app");
            
            // Enkripsi passwordnya
            user.setPassword(passwordEncoder.encode("user123"));
            
            user.setPhone("081200002222");
            user.setRole(Role.ROLE_USER); // Set rolenya sebagai USER
            
            userRepository.save(user);
        }
    }
}