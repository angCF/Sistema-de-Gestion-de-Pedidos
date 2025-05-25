package com.unir.producto.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.unir.producto.model.Producto;
import com.unir.producto.services.ProductoService;

@RestController
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    Environment environment;

    @Autowired
    private ProductoService productoService;

    @GetMapping()
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    @GetMapping("/{id}")
    public Producto obtenerServicioId(@PathVariable Long id) {
        return productoService.obtenerProductoId(id);
    }

    // http://localhost:9999/pelicula/nombre?nombre=
    @GetMapping("/nombre")
    public Producto obtenerProductoNombre(@RequestParam String name) {
        System.out.println("REQUEST: " + name);
        return productoService.obtenerProductoNombre(name);
    }

    @PostMapping()
    public Producto agregarProducto(@RequestBody Producto pelicula) {
        return productoService.agregarProducto(pelicula);
    }

    @PutMapping("/{id}")
    public Producto actualizaProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    } 
}