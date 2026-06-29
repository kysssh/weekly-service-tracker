package com.servicios.sistemaregistro.exception;

public class ServicioNoExisteException extends RuntimeException {
    public ServicioNoExisteException(String mensaje) {
        super(mensaje);
    }
}
