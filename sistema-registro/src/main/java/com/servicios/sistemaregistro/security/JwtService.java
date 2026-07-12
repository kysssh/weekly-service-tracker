package com.servicios.sistemaregistro.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service // Le dice a Spring: "gestiona esta clase como un bean, puedo inyectarla donde la necesite"
public class JwtService {

    // Inyecta el valor de la propiedad jwt.secret desde application.properties
    // Este texto NUNCA se escribe directamente aquí en el código (por eso @Value, no un literal)
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Tiempo de expiración: 1 hora en milisegundos.
    // Escrito como operación explícita (1000 * 60 * 60) para que sea autoexplicativo,
    // en vez de escribir directamente 3600000.
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // Convierte el String de la clave secreta en un objeto SecretKey,
    // que es lo que la librería jjwt necesita para firmar/validar con HMAC-SHA256.
    private SecretKey obtenerClaveFirma() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Genera un nuevo token para un usuario dado su nombre de usuario.
    // No recibe el objeto Usuario completo (evitamos exponer datos sensibles como el hash del password).
    public String generarToken(String nombreUsuario) {
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(nombreUsuario)           // el "dueño" del token
                .issuedAt(fechaActual)             // cuándo se generó
                .expiration(fechaExpiracion)       // cuándo deja de ser válido
                .signWith(obtenerClaveFirma())     // firma con nuestra clave secreta
                .compact();                        // construye el string final del JWT
    }

    // Extrae el nombre de usuario (subject) almacenado dentro del token.
    public String extraerNombreUsuario(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // Método genérico reutilizable: extrae cualquier "claim" (dato) del payload del token.
    // Function<Claims, T> permite pasarle, por ejemplo, Claims::getSubject o Claims::getExpiration
    private <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extraerTodosLosClaims(token);
        return resolver.apply(claims);
    }

    // Descompone el token: valida la firma con nuestra clave y devuelve el payload (claims) si es válido.
    // Si la firma no coincide o el token fue alterado, esto lanza una excepción automáticamente.
    private Claims extraerTodosLosClaims(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Valida si el token es válido: pertenece al usuario esperado y no ha expirado.
    public boolean validarToken(String token, String nombreUsuario) {
        String nombreUsuarioDelToken = extraerNombreUsuario(token);
        return nombreUsuarioDelToken.equals(nombreUsuario) && !tokenExpirado(token);
    }

    // Verifica si la fecha de expiración del token ya pasó respecto al momento actual.
    private boolean tokenExpirado(String token) {
        Date fechaExpiracion = extraerClaim(token, Claims::getExpiration);
        return fechaExpiracion.before(new Date());
    }
}