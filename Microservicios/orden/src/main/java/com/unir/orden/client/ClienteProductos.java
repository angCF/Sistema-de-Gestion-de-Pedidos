package com.unir.orden.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unir.orden.dto.ProductoDTO;

@FeignClient(name = "productos")
public interface ClienteProductos {
    
    @GetMapping("/api/producto/{id}")
    ProductoDTO obtenerProducto(@PathVariable Long id);

    @PutMapping("/api/producto/{id}/agregar-stock")
    ProductoDTO agregarStock(@PathVariable Long id, @RequestParam int cantidad);

    @PutMapping("/api/producto/{id}/quitar-stock")
    ProductoDTO quitarStock(@PathVariable Long id, @RequestParam int cantidad);
}