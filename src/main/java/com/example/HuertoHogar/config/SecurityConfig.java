package com.example.HuertoHogar.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            
            .requestMatchers("/api/auth/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**").permitAll()
            
            //categorias
            .requestMatchers(HttpMethod.GET,"/api/categorias/**").permitAll()
            .requestMatchers(HttpMethod.DELETE,"/api/categorias/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.POST,"/api/categorias/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/categorias/**").hasAuthority("ROLE_ADMIN")

            //productos
            .requestMatchers(HttpMethod.GET,"/api/productos/**").permitAll()
            .requestMatchers(HttpMethod.DELETE,"/api/productos/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.POST,"/api/productos/**").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.PUT,"/api/productos/**").hasAuthority("ROLE_ADMIN")

            //pedidos
            .requestMatchers("/api/pedidos/**").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/pedidos/{idPedido}/estado").hasAuthority("ROLE_ADMIN")
            
            //carrito
            .requestMatchers(HttpMethod.GET, "/api/carrito").authenticated()
            .requestMatchers(HttpMethod.POST, "/api/carrito/agregar").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/carrito/producto/**").authenticated() 
            .requestMatchers(HttpMethod.DELETE, "/api/carrito/**").authenticated()

            //formulario de contacto
            .requestMatchers(HttpMethod.POST, "/api/formularios").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/formularios").hasAuthority("ROLE_ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/formularios/**").hasAuthority("ROLE_ADMIN")

            //reseñas
            .requestMatchers(HttpMethod.GET, "/api/resenas").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/resenas").permitAll()
            .requestMatchers(HttpMethod.DELETE, "/api/resenas").hasAuthority("ROLE_ADMIN")

            //perfil de usuario
            .requestMatchers(HttpMethod.GET, "/api/perfil").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/perfil").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()


            
   
            .anyRequest().authenticated()
        )
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
