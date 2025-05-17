package com.immi.system.config;

import com.immi.system.utils.JwtAuthenticationFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    private final UserDetailsService userDetailsService;
    
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Enable CORS
            .csrf(csrf -> csrf.disable()) // Disable CSRF if not needed
            .authorizeHttpRequests(auth -> auth
                // 🔓 Public endpoints for login and registration
                .requestMatchers("/users/login", "/register").permitAll()

                // 🌐 Public APIs
                .requestMatchers(
                    "/api/immigration/**",
                    "/api/travel/**",
                    "/api/common/**",
                    "/api/tax/**",
                    "/api/uscis/**",
                    "/api/contact/**"                        
                ).permitAll()

                // 📋 Application-specific frontend paths
                .requestMatchers(
                    "/customer/**",
                    "/forms/**",
                    "/invoice/**",
                    "/petition/**",
                    "/service/**",
                    "/visa-application/**"
                ).permitAll()

                // 🧳 Travel module
                .requestMatchers(
                    "/travel/api/**",        // Covers /travel/api/amenity/**
                    "/travel/forms/hotels/**"
                ).permitAll()

                // 📂 Reference data and supporting endpoints
                .requestMatchers(
                    "/category/*",
                    "/dependent/*",
                    "/document/*",
                    "/gender/**",
                    "/immigrationStatus/**",
                    "/maritalStatus/**",
                    "/petitionStatus/*",
                    "/serviceType/**",
                    "/unitofmeasure/*",
                    "/uscisOffice/*",
                    "/generic/**"
                ).permitAll()

                // 🔐 All other routes require authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Only allow the specific frontend origin
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        // Define the allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Specify allowed headers including Authorization for JWT token
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Allow credentials for cross-origin requests (important for passing cookies or authorization headers)
        configuration.setAllowCredentials(true);

        // Handle preflight requests by allowing OPTIONS
        configuration.addExposedHeader("Authorization"); // Expose Authorization header to the frontend

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }


//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                   .userDetailsService(userDetailsService)
//                   .passwordEncoder(passwordEncoder())
//                   .and()
//                   .build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
