package com.travelapp.travel_app.security; // Pastikan nama package ini sesuai dengan struktur folder Anda

import com.travelapp.travel_app.service.CustomUserDetailsService; // Pastikan file ini ada
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    // Spring akan otomatis memasukkan (inject) CustomUserDetailsService ke sini
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Bean ini memberi tahu Spring cara mengenkripsi (hash) password.
     * Kita menggunakan BCrypt, yang merupakan standar modern.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean ini adalah inti dari konfigurasi keamanan Anda.
     * Ini menentukan halaman mana yang dilindungi, siapa yang bisa mengaksesnya,
     * di mana halaman login-nya, dan bagaimana proses logout bekerja.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Mengatur otorisasi untuk setiap request HTTP
            .authorizeHttpRequests(authz -> authz
                
                // 1. Izinkan akses publik (tanpa login) ke file-file statis.
                // Ini penting agar CSS dan JavaScript bisa dimuat di halaman login.
                .requestMatchers("/css/**", "/js/**", "/vendor/**", "/img/**").permitAll() 
                
                // 2. Izinkan akses publik ke halaman login.
                .requestMatchers("/login").permitAll()
                
                // 3. Halaman Admin (semua URL di bawah /admin/)
                // Hanya bisa diakses oleh user dengan role 'ADMIN'.
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 4. Halaman User (Landing page, halaman pesanan, dan aksi membuat pesanan)
                // Hanya bisa diakses oleh user dengan role 'USER'.
                .requestMatchers("/", "/my-orders", "/order/create").hasRole("USER")
                
                // 5. Semua request lain harus diautentikasi (harus login).
                .anyRequest().authenticated()
            )
            
            // Mengatur form login kustom
            .formLogin(form -> form
                // Tentukan URL yang menampilkan halaman login kustom kita
                .loginPage("/login") 
                // Tentukan URL yang akan memproses login (ditangani otomatis oleh Spring Security)
                .loginProcessingUrl("/login") 
                
                // Tentukan handler kustom untuk redirect setelah login sukses
                .successHandler((request, response, authentication) -> {
                    // Cek role user dan redirect ke halaman yang sesuai
                    if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/dashboard");
                    } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                        response.sendRedirect("/"); // User biasa ke landing page
                    }
                })
                // Jika login gagal, kembali ke halaman login dengan parameter error
                .failureUrl("/login?error=true")
                .permitAll() // Izinkan semua orang mengakses halaman login
            )
            
            // Mengatur proses logout
            .logout(logout -> logout
                // Tentukan URL yang akan memicu logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
                // Setelah logout sukses, arahkan ke halaman login dengan parameter logout
                .logoutSuccessUrl("/login?logout=true") 
                .permitAll() // Izinkan semua orang melakukan logout
            )
            
            // Beri tahu Spring Security untuk menggunakan service kustom kita
            // untuk mengambil detail user dari database.
            .userDetailsService(customUserDetailsService)
            
            // Nonaktifkan CSRF untuk kemudahan. 
            // Dalam aplikasi produksi, ini harus dikonfigurasi dengan benar.
            .csrf(csrf -> csrf.disable()); 

        return http.build();
    }
}