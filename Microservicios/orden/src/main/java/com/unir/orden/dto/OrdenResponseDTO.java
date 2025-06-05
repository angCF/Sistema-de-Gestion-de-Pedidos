package com.unir.orden.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrdenResponseDTO {
    private Long id;
    private String numDocumentoComprador;
    private String nombreComprador;
    private BigDecimal precioCompra;
    private LocalDate fechaCompra;
    private List<OrdenItemResponseDTO> itemsOrden;

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
