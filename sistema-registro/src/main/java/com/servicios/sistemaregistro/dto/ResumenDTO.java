package com.servicios.sistemaregistro.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResumenDTO {
    // Sub-totales por origen (informativos).
    private BigDecimal totalCorporativo;
    private BigDecimal totalB4;
    private BigDecimal peajesKusi;

    // Ganancia de la semana:
    //   base  = suma de montoServicio de los servicios Kusi, CMV o con "ID" en el codigo
    //   ganancia = base - 25% de la base
    private BigDecimal totalBruto;
    private BigDecimal ganancia;
}
