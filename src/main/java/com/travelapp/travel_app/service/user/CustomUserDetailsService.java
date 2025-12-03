package com.travelapp.travel_app.service.user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.Role;
import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Kita gunakan CustomUserDetails (Inner Class di bawah) agar bisa bawa Nama Lengkap
        return new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())),
                user.getName() // <-- Masukkan Nama Asli di sini
        );
    }

    public void registerUser(User user) throws Exception {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email " + user.getEmail() + " sudah terdaftar.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setIsActive(true);
        userRepository.save(user);
    }

    // --- CLASS TAMBAHAN (CUSTOM USER DETAILS) ---
    // Ini memungkan kita mengakses 'principal.fullName' di HTML
    public static class CustomUserDetails implements UserDetails {
        private String username; // Email
        private String password;
        private Collection<? extends GrantedAuthority> authorities;
        private String fullName; // Field Baru

        public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String fullName) {
            this.username = username;
            this.password = password;
            this.authorities = authorities;
            this.fullName = fullName;
        }

        public String getFullName() { return fullName; } // Getter ini dipanggil di HTML

        @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
        @Override public String getPassword() { return password; }
        @Override public String getUsername() { return username; }
        @Override public boolean isAccountNonExpired() { return true; }
        @Override public boolean isAccountNonLocked() { return true; }
        @Override public boolean isCredentialsNonExpired() { return true; }
        @Override public boolean isEnabled() { return true; }
    }
}