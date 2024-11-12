package com.ayb.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ayb.demo.filters.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class securityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        return http
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/").permitAll()
            .requestMatchers("/products").permitAll()
            .requestMatchers("/user/save").permitAll()
            .requestMatchers("/user/login").permitAll()
            .requestMatchers("/user/register").permitAll()
            .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()) 
            .httpBasic(basic -> basic.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS
            )).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean 
    public PasswordEncoder passwordEncoder ( ) {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationManager authenticationManager (AuthenticationConfiguration builder)
        throws Exception {
            return builder.getAuthenticationManager();
    }
}
