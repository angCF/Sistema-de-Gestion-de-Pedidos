package com.unir.orden.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrdenRequest {
    private List<ProductoRequest> idProductos;    
    private String numDocumentoComprador;
    private String nombreComprador;
}
