package com.servicios.sistemaregistro.exception;

/** Cuando se encuentra que ya existe un nombre de usuario nos lanzara una excepcion
 * Para esto creamos la clase UsuarioYaExisteException, super(mensaje) le dara el
 * mensaje a RuntimeException para que lo maneje
 */
public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String mensaje) {
        super(mensaje);
    }
}
