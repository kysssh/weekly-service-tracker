package com.servicios.sistemaregistro.model;

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
    private String pinHash;

}

