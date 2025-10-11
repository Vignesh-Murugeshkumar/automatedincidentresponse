package com.team.incidentresponse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Allow dashboard and static resources
                .requestMatchers("/","/dashboard","/dashboard/**","/css/**","/js/**","/static/**","/webjars/**").permitAll()
                .requestMatchers("/api/incidents/**").authenticated()
                .anyRequest().authenticated()
            )
            // For local/dev runs, enable form login (avoids requiring OAuth2 resource server classes)
            .formLogin(form -> form.permitAll())
            .httpBasic();

        return http.build();
    }
}
