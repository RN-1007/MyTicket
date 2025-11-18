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

    /**
     * Mengambil semua user dari database.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Mencari user berdasarkan ID.
     */
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Menghapus user berdasarkan ID.
     */
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
    /**
     * Menghitung jumlah total user terdaftar.
     */
    public long getUserCount() {
        return userRepository.count();
    }

    /**
     * Menyimpan user (bisa untuk create user baru atau update user lama).
     * Method ini berisi logika penting untuk menangani password.
     */
    public void saveUser(User user) {
        // Cek apakah ini user baru (ID null)
        if (user.getUserId() == null) {
            // Ini user baru, enkripsi password baru mereka
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password wajib diisi untuk user baru.");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else {
            // Ini update user yang sudah ada
            // Ambil data user lama dari database
            User existingUser = userRepository.findById(user.getUserId())
                    .orElseThrow(() -> new RuntimeException("User tidak ditemukan untuk di-update"));

            // Cek apakah admin mengisi password di form
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                // Jika password KOSONG: Admin tidak ingin mengubah password user.
                // Kita set password user dengan password lama yang sudah terenkripsi.
                user.setPassword(existingUser.getPassword());
            } else {
                // Jika password DIISI: Admin ingin me-reset password user.
                // Kita enkripsi password baru yang diinput admin.
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            // Simpan data user yang sudah di-update
            userRepository.save(user);
        }
    }
}