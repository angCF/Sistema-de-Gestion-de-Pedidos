package com.unir.orden.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrdenItemDTO {
    @NotNull(message = "El ID del producto no puede ser nulo.")
    private Long idProducto;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer cantidad;
}
