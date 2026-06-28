package com.servicios.sistemaregistro.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ServicioDTO {
    private String codigo;
    private String distrito;
    //El tipo de servicio: Kusi, CMV, 365, Otros
    private String tipoServicio;
    //True: Hay peaje, False: No hay peaje
    private Boolean peaje;
    private BigDecimal montoServicio;

    //Solo se usa si peaje es true, puede llegar null si es false
    private BigDecimal montoPeaje;
    //La fecha del servicio selecionada en el calendario
    private LocalDate fechaServicio;
}
