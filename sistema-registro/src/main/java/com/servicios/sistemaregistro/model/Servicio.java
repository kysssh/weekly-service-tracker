package com.servicios.sistemaregistro.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario") // le decimos a Spring como se llama nuestra columna
                                     // en la base de datos
    // usamos @JoinColumn cuando la columna conectara con otra tabla (llave foranea)
    private Usuario usuario;  // Ahora tenemos el objeto completo

    private LocalDate fechaServicio;
    private String codigoServicio;
    private String distrito;
    private String tipoServicio;
    private Boolean peaje;
    private BigDecimal montoPeaje;
    private BigDecimal montoServicio;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // @PrePersist: le dice a JPA "ejecuta este método automáticamente,
// justo antes de hacer el INSERT en la base de datos".
// Así garantizamos que TODO Servicio nuevo tenga su fecha de creación,
// sin depender de que el desarrollador se acuerde de asignarla manualmente.
    @PrePersist
    protected void alCrear() {
        this.createdAt = LocalDateTime.now();
    }



}
