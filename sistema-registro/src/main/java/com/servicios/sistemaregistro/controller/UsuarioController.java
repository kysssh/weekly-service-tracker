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
//Le decimos a Spring cual es la ruta base del controlador
@RequestMapping("/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    // Creamos nuestra variable usuarioService por constructor
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    //Con el RequestBody, tomamos el JSON que viene del body de la peticion y lo convertimos a un objeto UsuarioDTO
    public ResponseEntity<Void> registrarUsuario (@RequestBody UsuarioDTO usuarioDto) {
        usuarioService.registrarUsuario(usuarioDto.getNombreUsuario(), usuarioDto.getPin());
        //Crea una respuesta HTTP, con codigo de estado 201, sin body y enviala
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
