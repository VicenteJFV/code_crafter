package com.perfulandia.service.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.micrometer.common.lang.NonNull;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todos los endpoints
                        .allowedOrigins("*") // Permite cualquier origen (ajústalo en producción)
                        .allowedMethods("*") // Permite todos los métodos HTTP
                        .allowedHeaders("*"); // Permite todos los headers
            }
        };
    }
}
