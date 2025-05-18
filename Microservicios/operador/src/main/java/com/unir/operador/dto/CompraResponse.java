package com.unir.operador.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CompraResponse {
    private String nombrePelicula;
    private String nombreComprador;
    private BigDecimal precio;
    private LocalDate fechaCompra;
}
