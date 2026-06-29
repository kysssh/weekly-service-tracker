package com.servicios.sistemaregistro.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResumenDTO {
    private BigDecimal totalCorporativo;
    private BigDecimal totalB4;
    private BigDecimal peajesKusi;
}
