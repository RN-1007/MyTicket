package com.travelapp.travel_app.security; 

import com.travelapp.travel_app.service.user.CustomUserDetailsService;
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
                // --- 1. PUBLIC ACCESS (Halaman Depan & Aset) ---
                .requestMatchers("/", "/index", "/about-us").permitAll() 
                .requestMatchers("/hotel/detail/**", "/transport/detail/**", "/attraction/detail/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/vendor/**", "/img/**", "/hotel-photos/**", "/transport-photos/**", "/attraction-photos/**").permitAll() 
                .requestMatchers("/login", "/register").permitAll() 
                
                // --- 2. MIDTRANS WEBHOOK ---
                .requestMatchers("/api/midtrans/**").permitAll() 

                // --- 3. ROLE BASED ACCESS ---
                .requestMatchers("/admin/**").hasRole("ADMIN") 
                .requestMatchers("/my-orders", "/order/**").hasRole("USER") 

                .anyRequest().authenticated()
            )
            
            .formLogin(form -> form
                .loginPage("/login") 
                .loginProcessingUrl("/login") 
                .successHandler((request, response, authentication) -> {
                    // Redirect sesuai Role
                    if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/dashboard");
                    } else {
                        // User kembali ke Home setelah login
                        response.sendRedirect("/"); 
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll() 
            )
            
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
                .logoutSuccessUrl("/") // Logout kembali ke Home Publik
                .permitAll() 
            )
            .userDetailsService(customUserDetailsService)
            
            // Matikan CSRF khusus untuk endpoint Midtrans
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/midtrans/**")
            ); 

        return http.build();
    }
}