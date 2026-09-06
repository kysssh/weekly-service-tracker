package com.servicios.sistemaregistro.config;

import com.servicios.sistemaregistro.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Origenes permitidos por CORS. En produccion se define APP_CORS_ORIGINS
    // (coma-separado); si no, vale el default para desarrollo local.
    @Value("${app.cors.allowed-origins:http://localhost:*,http://127.0.0.1:*,https://weekly-service-tracker-front.onrender.com}")
    private List<String> allowedOrigins;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // define qué orígenes, métodos y headers están permitidos
    // cuando el navegador hace peticiones "cross-origin" hacia este backend.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracion = new CorsConfiguration();

        // Origenes permitidos. En local, el default de app.cors.allowed-origins
        // cubre cualquier puerto de localhost/127.0.0.1. En produccion se pone
        // aqui el dominio del frontend desplegado (APP_CORS_ORIGINS).
        configuracion.setAllowedOriginPatterns(allowedOrigins);

        // Métodos HTTP que tu frontend va a necesitar usar contra la API,
        // incluyendo OPTIONS para que el navegador pueda hacer el preflight.
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permitimos cualquier header en la petición — en particular necesitamos
        // "Authorization" (para el JWT) y "Content-Type" (para el JSON del body).
        configuracion.setAllowedHeaders(List.of("*"));

        // Aplicamos esta configuración a TODAS las rutas de la API ("/**").
        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);
        return fuente;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                //le decimos a Spring Security que use el bean de CORS
                // que acabamos de definir arriba, en vez de bloquear todo por defecto.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Las peticiones OPTIONS (preflight de CORS) no llevan
                        // credenciales; se permiten sin autenticacion para que
                        // el navegador pueda negociar la peticion real.
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth", "/usuarios").permitAll()
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}