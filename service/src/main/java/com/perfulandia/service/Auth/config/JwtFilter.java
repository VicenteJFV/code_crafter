package com.perfulandia.service.Auth.config;

import com.perfulandia.service.config.JwtConfig;
import com.perfulandia.service.Auth.service.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔓 Saltar autenticación para rutas públicas
        if (path.startsWith("/api/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String correo = null;
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // quitar "Bearer "

            try {
                // Extraer correo desde token (subject)
                correo = jwtUtil.obtenerCorreoDesdeToken(token);

                // Extraer claims desde token
                Claims claims = jwtUtil.obtenerClaims(token);
                String rol = (String) claims.get("rol");

                // Crear autoridad con el rol
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(rol));

                // Validar token y setear autenticación
                if (correo != null && jwtUtil.validarToken(token)
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            correo,
                            null,
                            authorities);

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    System.out.println("Autenticación seteada con correo: " + correo + " y rol: " + rol);
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
