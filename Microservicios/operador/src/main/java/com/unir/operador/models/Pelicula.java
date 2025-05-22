package com.unir.operador.models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pelicula {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVenta;
    private BigDecimal precioAlquiler;
    private Boolean disponibilidad; 
}
