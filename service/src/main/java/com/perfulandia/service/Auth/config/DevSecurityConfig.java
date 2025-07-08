// package com.perfulandia.service.Auth.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;
// import
// org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @Profile("dev") // Esta clase solo se carga en el perfil "dev"
// public class DevSecurityConfig {
// @Value("${spring.profiles.active:}")
// private String activeProfile;
//
// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws
// Exception {
//
// if ("dev".equals(activeProfile)) {
// Configuración para dev: permitir todo
// http.csrf().disable()
// .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
// } else {
// // Configuración para otros perfiles: seguridad normal
// http.csrf().disable()
// .authorizeHttpRequests(authz -> authz
// .requestMatchers("/api/auth/**", "/swagger-ui/**",
// "/v3/api-docs/**").permitAll()
// .anyRequest().authenticated());
// }

// return http.build();
// }

// }
