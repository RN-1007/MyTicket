package com.travelapp.travel_app.service.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.travelapp.travel_app.model.User;
import com.travelapp.travel_app.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public long getUserCount() {
        return userRepository.count();
    }

    // --- METHOD BARU: TOGGLE STATUS ---
    public void toggleUserStatus(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));
        
        // Jika null, anggap true, lalu dibalik jadi false. Jika true jadi false, dst.
        boolean currentStatus = user.getIsActive();
        user.setIsActive(!currentStatus);
        
        userRepository.save(user);
    }
    // ----------------------------------

    public void saveUser(User user) {
        if (user.getUserId() == null) {
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password wajib diisi untuk user baru.");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // Pastikan status default di-set saat create
            if (user.getIsActive() == null) user.setIsActive(true);
            
            userRepository.save(user);
        } else {
            User existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan untuk di-update"));

            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            // Jaga status lama jika tidak dikirim dari form (opsional, tapi aman)
            if (user.getIsActive() == null) {
                user.setIsActive(existingUser.getIsActive());
            }
            
            userRepository.save(user);
        }
    }
}