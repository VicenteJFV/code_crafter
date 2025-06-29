package com.perfulandia.service.Auth.config;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import com.perfulandia.service.Auth.service.JwtUtil;
import com.perfulandia.service.user.config.JwtConfig;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Claims claims;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldBypassAuthRoutes() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/auth/login");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUtil);
    }

    @Test
    void doFilterInternal_shouldAuthenticateForProtectedRoutes() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer validtoken");
        when(jwtUtil.obtenerCorreoDesdeToken("validtoken")).thenReturn("correo@dominio.com");
        when(jwtUtil.obtenerClaims("validtoken")).thenReturn(claims);
        when(claims.get("rol")).thenReturn("ADMIN");
        when(jwtUtil.validarToken("validtoken")).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil).obtenerCorreoDesdeToken("validtoken");
        verify(jwtUtil).obtenerClaims("validtoken");
        // verify(jwtUtil).validarToken("validtoken");
        verify(jwtUtil, times(2)).validarToken("validtoken");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldHandleInvalidToken() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/protected");
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalidtoken");
        when(jwtUtil.obtenerCorreoDesdeToken("invalidtoken")).thenThrow(new RuntimeException("Token inválido"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(jwtUtil).obtenerCorreoDesdeToken("invalidtoken");
        verify(filterChain).doFilter(request, response);
    }
}