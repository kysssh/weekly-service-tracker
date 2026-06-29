package com.servicios.sistemaregistro.controller;

import com.servicios.sistemaregistro.dto.ServicioDTO;
import com.servicios.sistemaregistro.model.Servicio;
import com.servicios.sistemaregistro.service.ServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicios")
public class ServicioController {
    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @PostMapping
    public ResponseEntity<Void> registrarServicio(@RequestBody ServicioDTO servicioDto) {
        servicioService.registrarServicio(servicioDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/semana-actual")
    public ResponseEntity<List<Servicio>> obtenerSemanaActual() {
        List<Servicio> resultado = servicioService.obtenerSemanaActual(null);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Servicio>> obtenerHistorial() {
        List<Servicio> resultado = servicioService.obtenerHistorial(null);
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> editarServicio(@PathVariable Long id, @RequestBody ServicioDTO servicioDto) {
        Servicio servicioActualizado = servicioService.editarServicio(id, servicioDto);
        return ResponseEntity.ok(servicioActualizado);
    }
}
