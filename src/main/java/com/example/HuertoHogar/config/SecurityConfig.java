package com.example.HuertoHogar.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(withDefaults())
        .csrf(csrf -> csrf.disable()) // Para APIs stateless, es seguro y más simple deshabilitarlo.
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // 1. REGLAS PÚBLICAS (rutas que no necesitan autenticación, deben ir primero)
            .requestMatchers("/auth/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
            
            // 2. REGLAS DE LECTURA PÚBLICA (cualquiera puede ver productos y categorías)
            .requestMatchers(HttpMethod.GET, "/api/productos/**", "/api/categorias/**").permitAll()
            
            // 3. REGLAS DE ESCRITURA PARA ADMIN (solo un admin puede crear, actualizar o borrar)
            .requestMatchers(HttpMethod.POST, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/pedidos/{idPedido}/estado").hasRole("ADMIN")
            
            // 4. CUALQUIER OTRA PETICIÓN (ej. /api/pedidos/**) debe estar autenticada
            .anyRequest().authenticated()
        )
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }
}
