package com.servicios.sistemaregistro.controller;

import com.servicios.sistemaregistro.dto.UsuarioDTO;
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
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    //El ResponseEntity representa una respuesta HTTP completa
    //No tiene cuerpo, pero aun asi queremos controlar el codigo de estado
    //El cliente no necesita recibir datos de vuelta
    public ResponseEntity<Void> autenticarUsuario(@RequestBody UsuarioDTO usuarioDto) {
        //Verificamos si es true o false, de existir el usuario devuelve true (200)
        //en caso contrario devuelve false (401)
        if(usuarioService.validarAutenticacion(usuarioDto.getNombreUsuario(), usuarioDto.getPin())) {
            //Una forma:
            // return ResponseEntity.status(HttpStatus.OK).build();
            //Otra forma mas limpia
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
