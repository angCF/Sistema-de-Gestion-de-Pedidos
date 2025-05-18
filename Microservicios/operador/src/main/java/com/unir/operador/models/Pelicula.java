package com.unir.operador.models;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Pelicula {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVenta;
    private BigDecimal precioAlquiler;
    private Boolean disponibilidad; 
}
