package com.perfulandia.service.user.config;

import com.perfulandia.service.Auth.service.JwtUtil;
import com.perfulandia.service.user.config.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private UserDetailsServiceImpl usuarioDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("JwtFilter: procesando path: " + path);

        if (path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String correo = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);

            try {
                correo = jwtUtil.obtenerCorreoDesdeToken(token);
                Claims claims = jwtUtil.obtenerClaims(token);
                String rol = (String) claims.get("rol");

                System.out.println("¿Correo extraído?: " + correo);
                System.out.println("¿Token válido?: " + jwtUtil.validarToken(token));

                if (correo != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = usuarioDetailsService.loadUserByUsername(correo);
                    if (jwtUtil.validarToken(token)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("JwtFilter: usuario autenticado " + correo);
                    }
                }

            } catch (ExpiredJwtException e) {
                System.out.println("Token expirado: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error al procesar el token: " + e.getMessage());
            }
        }

        chain.doFilter(request, response);
    }
}
