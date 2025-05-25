package com.unir.orden.dto;

import lombok.Data;

@Data
public class OrdenRequest {
    private Long idProducto;    
    private String numDocumentoComprador;
    private String nombreComprador;
    private Integer cantidad;
}
