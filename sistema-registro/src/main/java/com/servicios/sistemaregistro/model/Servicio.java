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

    @Column(name = "created_at") // usamos @Column para ubicar la columna en la BD
    private LocalDateTime createdAt;



}
