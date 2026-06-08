package com.shkvlnc.ShoeApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register").permitAll()   // ✅ allow registration
                        .requestMatchers("/api/products/**").permitAll()      // public products
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {}); // ✅ Basic Auth enabled

        return http.build();
    }
}
