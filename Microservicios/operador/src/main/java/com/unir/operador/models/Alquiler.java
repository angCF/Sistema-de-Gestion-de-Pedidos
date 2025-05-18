package com.unir.operador.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Alquiler {
    private Long idPelicula;
    private String nombreUsuario;            
    private BigDecimal precioAlquiler;   
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
