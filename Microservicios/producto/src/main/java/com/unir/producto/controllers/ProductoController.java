package com.unir.producto.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.producto.dto.ProductoRequestDTO;
import com.unir.producto.dto.ProductoResponseDTO;
import com.unir.producto.model.Producto;
import com.unir.producto.services.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping()
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    @GetMapping("/{id}")
    public Producto obtenerProductoId(@PathVariable Long id) {
        return productoService.obtenerProductoId(id);
    }

    @GetMapping("/nombre")
    public Producto obtenerProductoNombre(@RequestParam String name) {
        return productoService.obtenerProductoNombre(name);
    }

    @PostMapping()
    public ProductoResponseDTO agregarProducto(@Valid @RequestBody ProductoRequestDTO producto) {
        return productoService.agregarProducto(producto);
    }

    @PutMapping("/{id}")
    public ProductoResponseDTO actualizaProducto(@PathVariable Long id, @RequestBody ProductoRequestDTO Producto) {
        return productoService.actualizarProducto(id, Producto);
    }

    @PutMapping("/{id}/agregar-stock")
    public ProductoResponseDTO agregarStock(@PathVariable Long id, @RequestParam int cantidad) {
        return productoService.agregarStock(id, cantidad);
    }

    @PutMapping("/{id}/quitar-stock")
    public ProductoResponseDTO quitarStock(@PathVariable Long id, @RequestParam int cantidad) {
        return productoService.quitarStock(id, cantidad);
    }

    @DeleteMapping("/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        return productoService.eliminarProducto(id);
    } 
}
