package com.unir.producto.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.producto.model.Producto;
import com.unir.producto.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository ProductoRepository;

    public List<Producto> obtenerProductos() {
        return ProductoRepository.findAll();
    }

    public Producto obtenerProductoId(Long id) {
        return ProductoRepository.findById(id).orElseThrow();
    }

    public Producto obtenerProductoNombre(String nombre) {
        return ProductoRepository.findByNombre(nombre);
    }

    public Producto agregarProducto(Producto Producto) {
        return ProductoRepository.save(Producto);
    }

    public Producto actualizarProducto(Long id, Producto newProducto) {
        Producto producto = ProductoRepository.findById(id).orElseThrow();

        producto.setNombre(newProducto.getNombre());
        producto.setDescripcion(newProducto.getDescripcion());
        producto.setPrecio_venta(newProducto.getPrecio_venta());
        producto.setStock(newProducto.getStock());

        return ProductoRepository.save(producto);
    }

    public String eliminarProducto(Long id) {
        ProductoRepository.deleteById(id);
        return "El producto con " + id + " ha sido eliminada satisfactoriamente.";
    }
}