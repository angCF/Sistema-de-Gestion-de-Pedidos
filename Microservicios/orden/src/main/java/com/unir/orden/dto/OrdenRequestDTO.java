package com.unir.orden.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenRequestDTO {
    @NotEmpty(message = "Debe incluir al menos un producto en la orden.")
    private List<@Valid OrdenItemDTO> idProductos;

    @NotNull(message = "El numero de documento es obligatorio.")
    @Min(value = 5, message = "El numero de documento debe ser de minimo 5 digitos.")
    private String numDocumentoComprador;
    
    @NotBlank(message = "El nombre del cliente es obligatorio.")
    private String nombreComprador;
}
