package com.unir.operador.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class AlquilerResponse {
    private String nombrePelicula;
    private String nombreUsuario;
    private BigDecimal precioAlquiler;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
