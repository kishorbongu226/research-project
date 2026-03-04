package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // disable CSRF for simplicity (for APIs)
                .cors(cors -> {}) // enable CORS if needed (e.g., localhost:3000)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()  // login endpoint open
                        .anyRequest().authenticated()           // all other endpoints protected
                )
                .httpBasic(basic -> basic
                        .authenticationEntryPoint((req, res, authEx) -> {
                            // send 401 if not authenticated
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        })
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Basic Auth: stateless
                )
                .build();
    }
}