package com.team.incidentresponse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1️⃣ Define the in-memory user (admin)
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        String adminPassword = System.getenv("ADMIN_PASSWORD");
        if (adminPassword == null || adminPassword.isEmpty()) {
            adminPassword = "admin123"; // Default for development
        }
        
        UserDetails admin = User.withUsername("admin")
                                .password(passwordEncoder.encode(adminPassword))
                                .roles("ADMIN")
                                .build();
        return new InMemoryUserDetailsManager(admin);
    }

    // 2️⃣ Password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3️⃣ Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Allow dashboard and static resources
                .requestMatchers("/", "/dashboard", "/dashboard/**", "/css/**", "/js/**", "/static/**", "/webjars/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                .requestMatchers("/api/webhook", "/api/test/**", "/api/scan/**", "/api/cleanup/**").permitAll()
                .requestMatchers("/api/incidents/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.permitAll())
            .httpBasic();

        return http.build();
    }
}
