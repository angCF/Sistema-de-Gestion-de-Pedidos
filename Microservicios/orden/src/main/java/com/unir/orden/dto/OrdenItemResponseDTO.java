package com.unir.orden.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class OrdenItemResponseDTO {
    private Long idProducto;
    private String nombreProducto;
    private Integer cantidad;
}
