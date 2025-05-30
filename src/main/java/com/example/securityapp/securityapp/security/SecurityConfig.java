package com.example.securityapp.securityapp.security;

import com.example.securityapp.securityapp.repository.UserRepository;
import com.example.securityapp.securityapp.service.JWTService;
import com.example.securityapp.securityapp.service.RateLimiterService;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final RateLimiterService rateLimiterService; // Inject via constructor

    public SecurityConfig(JWTService jwtService, UserRepository userRepository,
                          RateLimiterService rateLimiterService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.rateLimiterService = rateLimiterService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(
                        sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new LoginRateLimitingFilter(rateLimiterService), UsernamePasswordAuthenticationFilter.class) // run before JWT check
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public Filter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(jwtService, userRepository);
    }
}
