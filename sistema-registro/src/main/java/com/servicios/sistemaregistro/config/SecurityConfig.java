package com.servicios.sistemaregistro.config;

import com.servicios.sistemaregistro.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration: le dice a Spring "esta clase declara beans, procésala al arrancar".
// @EnableWebSecurity: activa el sistema de seguridad web de Spring Security en tu aplicación.
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyectamos el filtro que ya construimos, para insertarlo en la cadena más abajo.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Bean que ya tenías: el algoritmo de hasheo para PINs.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Este bean define TODA la política de seguridad HTTP de tu aplicación.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF (protección contra ataques de formularios web tradicionales).
                // No la necesitamos porque no usamos sesiones ni cookies de navegador: usamos JWT sin estado.
                .csrf(AbstractHttpConfigurer::disable)

                // Le decimos a Spring que NO cree ni use sesiones HTTP tradicionales.
                // Cada petición se autentica desde cero usando el token, no se "recuerda" nada entre peticiones.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Aquí definimos qué rutas son públicas y cuáles requieren autenticación.
                .authorizeHttpRequests(auth -> auth
                        // permitAll(): estas rutas NO requieren token válido.
                        .requestMatchers("/auth", "/usuarios").permitAll()
                        // anyRequest().authenticated(): CUALQUIER otra ruta SÍ requiere autenticación.
                        .anyRequest().authenticated()
                )

                // Insertamos nuestro filtro JWT ANTES del filtro estándar de usuario/contraseña de Spring.
                // Esto asegura que nuestro filtro se ejecute primero en la cadena, revisando el token
                // antes de que Spring intente cualquier otro mecanismo de autenticación.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}