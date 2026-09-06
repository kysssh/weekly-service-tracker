package com.servicios.sistemaregistro.exception;

import com.servicios.sistemaregistro.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Intercepta las excepciones de todos los Controllers
@ControllerAdvice
//Aqui estaran todos los metodos del manejo de errores
public class GlobalExceptionHandler {
    //Cuando cualquier controller lance UsuarioYaExisteException ,ejecuta este metodo
    @ExceptionHandler(UsuarioYaExisteException.class)
    //Se ejecuta el metodo, retorna un estado
    public ResponseEntity<Void> manejarUsuarioYaExiste() {
        //Codigo 409 "ya existe un recurso con este identificador"
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(ServicioNoExisteException.class)
    public ResponseEntity<Void> manejarServicioNoExiste() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Datos invalidos al registrar o editar un servicio: 400 con el mensaje
    // concreto, para que el frontend pueda decir qué campo está mal en vez de
    // un 500 opaco.
    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<ErrorDTO> manejarValidacion(ValidacionException ex) {
        return ResponseEntity.badRequest().body(new ErrorDTO(ex.getMessage()));
    }
}
