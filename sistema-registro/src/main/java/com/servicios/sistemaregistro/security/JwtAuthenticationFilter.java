package com.servicios.sistemaregistro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Component: le dice a Spring que gestione esta clase como bean,
// igual que @Service, pero se usa @Component para piezas que no son
// estrictamente "lógica de negocio" (como filtros, utilidades, etc.)
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Inyectamos JwtService por constructor (mismo patrón que ya usas en tus otros Services).
    // Lo necesitamos para llamar a validarToken() y extraerNombreUsuario().
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Leemos el header "Authorization" de la petición HTTP entrante.
        // Ejemplo de lo que puede venir: "Bearer eyJhbGciOiJIUzI1NiJ9..."
        String headerAutorizacion = request.getHeader("Authorization");

        // 2. Si no viene el header, o no empieza con "Bearer ", no hay nada que validar.
        // Dejamos pasar la petición SIN autenticar a nadie (podría ir a una ruta pública, como /auth/login).
        if (headerAutorizacion == null || !headerAutorizacion.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // deja pasar, sin tocar el SecurityContext
            return; // cortamos la ejecución de este método aquí, no seguimos abajo
        }

        // 3. Extraemos el token puro, quitando el prefijo "Bearer " (7 caracteres: "Bearer ").
        String token = headerAutorizacion.substring(7);

        // 4. Extraemos el nombre de usuario contenido en el token (sin validar todavía la firma aquí,
        // eso lo hace validarToken más abajo).
        String nombreUsuario = jwtService.extraerNombreUsuario(token);

        // 5. Verificamos dos cosas antes de autenticar:
        //    a) que hayamos podido extraer un nombreUsuario (no sea null)
        //    b) que AÚN NO haya alguien ya autenticado en este contexto
        //       (SecurityContextHolder.getContext().getAuthentication() == null)
        if (nombreUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Validamos el token de verdad: firma correcta + no expirado + pertenece a este usuario.
            if (jwtService.validarToken(token, nombreUsuario)) {

                // 7. Si el token es válido, construimos un objeto de autenticación de Spring Security.
                // UsernamePasswordAuthenticationToken es la implementación estándar que representa
                // "un usuario autenticado" dentro de Spring Security.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                nombreUsuario,  // "principal": quién es el usuario autenticado
                                null,           // "credentials": null porque ya no necesitamos la contraseña/PIN aquí
                                java.util.Collections.emptyList() // "authorities": roles/permisos (vacío por ahora, no usamos roles)
                        );

                // 8. Depositamos ese objeto de autenticación en el SecurityContext.
                // A partir de aquí, CUALQUIER parte de tu código (controller, service) puede preguntar
                // "¿quién es el usuario autenticado de esta petición?" y obtener nombreUsuario.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            // Si el token NO es válido (validarToken devuelve false), simplemente no autenticamos a nadie.
            // La petición sigue su curso, pero sin nadie en el SecurityContext -> más adelante,
            // SecurityConfig la va a rechazar si la ruta requiere autenticación.
        }

        // 9. Dejamos pasar la petición al siguiente filtro/controller de la cadena, autenticada o no.
        filterChain.doFilter(request, response);
    }
}