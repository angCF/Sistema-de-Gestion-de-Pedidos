package com.unir.orden.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class OrdenResponse {
    private Long numeroOrden;
    private String nombreProducto;
    private String numDocumentoComprador;
    private String nombreComprador;
    private Integer cantidad;
    private BigDecimal precio;
    private LocalDate fechaCompra;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Registra el módulo JavaTimeModule para manejar LocalDate, LocalDateTime, etc.
        objectMapper.registerModule(new JavaTimeModule());

        try {
            return objectMapper.writeValueAsString(this); // Convierte el objeto en JSON
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}"; // Si ocurre un error, retorna un JSON vacío
        }
    }
}
