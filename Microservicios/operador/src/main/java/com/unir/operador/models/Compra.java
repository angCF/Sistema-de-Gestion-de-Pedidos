package com.unir.operador.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Compra {
    private Long id;
    private Long idPelicula;
    private String nombreComprador;          
    private BigDecimal precioCompra;   
    private LocalDate fechaCompra;
}
