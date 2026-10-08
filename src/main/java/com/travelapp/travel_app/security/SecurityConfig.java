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
                // --- PUBLIC ACCESS ---
                .requestMatchers("/", "/index", "/about-us").permitAll() 
                .requestMatchers("/hotel/detail/**", "/transport/detail/**", "/attraction/detail/**").permitAll()
                .requestMatchers("/css/**", "/js/**", "/vendor/**", "/img/**", "/hotel-photos/**", "/transport-photos/**", "/attraction-photos/**").permitAll() 
                .requestMatchers("/login", "/register").permitAll() 
                
                // --- ANDROID PUBLIC ACCESS ---
                .requestMatchers(
                    "/android", "/android/", "/android/home", "/android/login",
                    "/android/hotel/detail/**", 
                    "/android/transport/detail/**", 
                    "/android/attraction/detail/**"

                ).permitAll()
                
                // --- MIDTRANS ---
                .requestMatchers("/api/midtrans/**").permitAll() 

                // --- PROTECTED ROUTES ---
                .requestMatchers("/admin/**").hasRole("ADMIN") 
                .requestMatchers("/my-orders", 
                                    "/order/**",   
                                    "/android/my-orders", 
                                    "/android/order/**",
                                    "/android/profile",
                                    "/android/order/invoice/**").hasRole("USER") 

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .loginProcessingUrl("/login") 
                .successHandler((request, response, authentication) -> {
                    if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/dashboard");
                    } else {
                        // Redirect cerdas berdasarkan asal device/URL
                        String referer = request.getHeader("referer");
                        if (referer != null && referer.contains("/android")) {
                            response.sendRedirect("/android");
                        } else {
                            response.sendRedirect("/");
                        }
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll() 
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
                .logoutSuccessUrl("/") 
                .permitAll() 
            )
            .userDetailsService(customUserDetailsService)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/midtrans/**")); 

        return http.build();
    }
}