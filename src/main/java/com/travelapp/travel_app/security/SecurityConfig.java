package com.travelapp.travel_app.security; 

import com.travelapp.travel_app.service.user.CustomUserDetailsService; // <-- Path ini sudah benar
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
            .authorizeHttpRequests(authz -> authz
                
                .requestMatchers("/css/**", "/js/**", "/vendor/**", "/img/**").permitAll() 
                .requestMatchers("/login", "/register").permitAll() 
                
                // --- PERBAIKAN DI SINI ---
                .requestMatchers("/admin/**").hasRole("ADMIN") // Hapus "ROLE_"
                .requestMatchers("/", "/my-orders", "/order/create").hasRole("USER") // Hapus "ROLE_"
                // --- AKHIR PERBAIKAN ---

                .anyRequest().authenticated()
            )
            
            .formLogin(form -> form
                .loginPage("/login") 
                .loginProcessingUrl("/login") 
                .successHandler((request, response, authentication) -> {
                    // Cek di sini tetap menggunakan .getAuthority() yang berisi nama lengkap
                    if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/dashboard");
                    } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
                        response.sendRedirect("/"); 
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll() 
            )
            
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
                .logoutSuccessUrl("/login?logout=true") 
                .permitAll() 
            )
            .userDetailsService(customUserDetailsService)
            .csrf(csrf -> csrf.disable()); 

        return http.build();
    }
}