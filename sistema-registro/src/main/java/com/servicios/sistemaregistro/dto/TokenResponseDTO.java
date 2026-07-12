package com.servicios.sistemaregistro.dto;

// DTO simple que representa la respuesta de login exitoso.
// Solo contiene el token que el cliente deberá guardar y reenviar en futuras peticiones.
public class TokenResponseDTO {

    private String token;

    // Constructor vacío, necesario para que Spring pueda serializar/deserializar este objeto a JSON.
    public TokenResponseDTO() {
    }

    public TokenResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}