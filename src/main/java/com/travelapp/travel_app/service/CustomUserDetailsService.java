package com.travelapp.travel_app.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.Role; // <-- IMPORT BARU
import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // <-- TAMBAHKAN INI

    // --- MODIFIKASI CONSTRUCTOR ---
    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder; // <-- TAMBAHKAN INI
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    // --- METHOD BARU UNTUK REGISTRASI ---
    public void registerUser(User user) throws Exception {
        // 1. Cek apakah email sudah terdaftar
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email " + user.getEmail() + " sudah terdaftar.");
        }

        // 2. Enkripsi password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 3. Set role default sebagai USER
        user.setRole(Role.ROLE_USER);

        // 4. Simpan user baru
        userRepository.save(user);
    }
}