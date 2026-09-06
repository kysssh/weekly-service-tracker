package com.servicios.sistemaregistro.dto;

/**
 * Cuerpo de respuesta para errores que sí llevan un mensaje pensado para
 * mostrarle al usuario (por ejemplo las validaciones de un servicio).
 */
public record ErrorDTO(String mensaje) {
}
