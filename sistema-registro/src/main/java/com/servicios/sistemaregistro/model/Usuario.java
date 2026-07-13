package com.servicios.sistemaregistro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity    // Establecemos que lo que viene es una tabla de BD
@Table(name = "usuarios")   // La tabla de la base de datos se llama "usuarios"
public class Usuario {

    @Id  // Llave foranea
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // GeneratedValue necesita saber como generar el ID
    // IDENTITY le dice le dice a "Hibernate" deja que la BD genere el ID por mi
    // esto es lo que hace SERIAL en PostgreSQL
    private Long id;

    private String nombreUsuario;

    // @JsonIgnore: le dice a Jackson que NUNCA incluya este campo al convertir
    // el objeto a JSON, sin importar desde dónde se esté serializando
    // (directamente, o indirectamente como parte de otro objeto, como pasa
    // ahora con Servicio.usuario). Protege el hash del PIN de filtrarse
    // en cualquier respuesta HTTP, presente o futura.
    @JsonIgnore
    private String pinHash;

}

