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

        return new CustomUserDetails(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())),
                user.getName()
        );
    }

    // --- METHOD REGISTRASI (UPDATED: Validasi Password) ---
    public void registerUser(User user) throws Exception {
        
        // 1. VALIDASI WAJIB GMAIL
        if (user.getEmail() == null || !user.getEmail().toLowerCase().endsWith("@gmail.com")) {
            throw new Exception("Registrasi gagal! Harap gunakan alamat email Google (@gmail.com).");
        }

        // 2. VALIDASI PASSWORD MINIMAL 8 KARAKTER (BARU)
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new Exception("Password harus memiliki minimal 8 karakter.");
        }

        // 3. Cek apakah email sudah terdaftar
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new Exception("Email " + user.getEmail() + " sudah terdaftar.");
        }

        // 4. Enkripsi password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 5. Set role default sebagai USER
        user.setRole(Role.ROLE_USER);
        
        // 6. Set Active
        user.setIsActive(true);

        // 7. Simpan user baru
        userRepository.save(user);
    }

    // --- INNER CLASS (CUSTOM DETAILS) ---
    public static class CustomUserDetails implements UserDetails {
        private String username;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;
        private String fullName;

        public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String fullName) {
            this.username = username;
            this.password = password;
            this.authorities = authorities;
            this.fullName = fullName;
        }

        public String getFullName() { return fullName; }

        @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
        @Override public String getPassword() { return password; }
        @Override public String getUsername() { return username; }
        @Override public boolean isAccountNonExpired() { return true; }
        @Override public boolean isAccountNonLocked() { return true; }
        @Override public boolean isCredentialsNonExpired() { return true; }
        @Override public boolean isEnabled() { return true; }
    }
}