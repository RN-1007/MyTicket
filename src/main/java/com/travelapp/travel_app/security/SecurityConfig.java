package com.travelapp.travel_app.security; 

import com.travelapp.travel_app.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// HAPUS IMPORT BCRYPT DAN PASSWORDENCODER JIKA HANYA MEREKA YANG DIPAKAI DI SINI
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * HAPUS BEAN PASSWORD ENCODER DARI SINI
     *
     * @Bean
     * public PasswordEncoder passwordEncoder() {
     * return new BCryptPasswordEncoder();
     * }
     */
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // (Sisa dari kode ini tidak perlu diubah, biarkan apa adanya)
        http
            .authorizeHttpRequests(authz -> authz
                
                .requestMatchers("/css/**", "/js/**", "/vendor/**", "/img/**").permitAll() 
                .requestMatchers("/login", "/register").permitAll() 
                
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/", "/my-orders", "/order/create").hasRole("USER")
                
                .anyRequest().authenticated()
            )
            
            .formLogin(form -> form
                .loginPage("/login") 
                .loginProcessingUrl("/login") 
                .successHandler((request, response, authentication) -> {
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