package com.servicios.sistemaregistro.controller;

import com.servicios.sistemaregistro.dto.TokenResponseDTO;
import com.servicios.sistemaregistro.dto.UsuarioDTO;
import com.servicios.sistemaregistro.security.JwtService;
import com.servicios.sistemaregistro.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//Le decimos a Spring que /auth es la ruta de este controlador
@RequestMapping("/auth")
public class AuthController {
    //Inyectamos UsuarioService por constructor
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    public AuthController(UsuarioService usuarioService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.jwtService = jwtService;
    }

    @PostMapping
    //El ResponseEntity representa una respuesta HTTP completa
    //No tiene cuerpo, pero aun asi queremos controlar el codigo de estado
    //El cliente no necesita recibir datos de vuelta
    public ResponseEntity<TokenResponseDTO> autenticarUsuario(@RequestBody UsuarioDTO usuarioDto) {
        //Verificamos si es true o false, de existir el usuario devuelve true (200)
        //en caso contrario devuelve false (401)
        if(usuarioService.validarAutenticacion(usuarioDto.getNombreUsuario(), usuarioDto.getPin())) {
            // Generamos el token para este usuario ya autenticado.
            String token = jwtService.generarToken(usuarioDto.getNombreUsuario());
            // Lo envolvemos en el DTO y lo devolvemos con código 200.
            return ResponseEntity.ok(new TokenResponseDTO(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
