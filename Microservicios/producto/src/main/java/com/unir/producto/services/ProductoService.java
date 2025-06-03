package com.unir.producto.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.producto.model.Producto;
import com.unir.producto.repository.ProductoRepository;
import com.unir.producto.exception.ProductoNoDisponibleException;
import com.unir.producto.exception.ProductoNoEncontradoException;
import com.unir.producto.exception.ProductoValidadoException;

@Service
public class ProductoService {

    private static final Logger logger = Logger.getLogger(ProductoService.class.getName());

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerProductos() {
        logger.info("Obteniendo todos los productos.");
        return productoRepository.findAll();
    }

    public Producto obtenerProductoId(Long id) {
        logger.info("Buscando producto con ID: " + id);
        Producto producto = productoRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.", null));
        return producto;
    }

    public Producto obtenerProductoNombre(String nombre) {
        logger.info("Buscando producto con nombre: " + nombre);
        return productoRepository.findByNombre(nombre);
    }

    public Producto agregarProducto(Producto producto) {
        logger.info("Agregando nuevo producto: " + producto.getNombre());
        // validarProducto(producto);
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto prod) {
        logger.info("Actualizando producto con ID: " + id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.", null));

        // validarProducto(prod);

        producto.setNombre(prod.getNombre());
        producto.setDescripcion(prod.getDescripcion());
        producto.setPrecioVenta(prod.getPrecioVenta());
        producto.setStock(prod.getStock());

        return productoRepository.save(producto);
    }

    public String eliminarProducto(Long id) {
        logger.info("Eliminando producto con ID: " + id);
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.", null);
        }
        productoRepository.deleteById(id);
        return "El producto con ID " + id + " ha sido eliminado satisfactoriamente.";
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            logger.warning("Validación fallida: nombre vacío.");
            throw new ProductoValidadoException("El nombre del producto es obligatorio.", null);
        }
        if (producto.getPrecioVenta() == null || producto.getPrecioVenta().doubleValue() < 0) {
            logger.warning("Validación fallida: precio negativo.");
            throw new ProductoValidadoException("El precio del producto no puede ser negativo.", null);
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            logger.warning("Validación fallida: stock negativo.");
            throw new ProductoValidadoException("El stock no puede ser negativo.", null);
        }
    }

    public Producto agregarStock(Long id, int cantidad) {
        logger.info("Agregando stock al producto con ID: " + id + ", cantidad: " + cantidad);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.", null));

        if (cantidad <= 0) {
            logger.warning("Cantidad inválida para agregar stock: " + cantidad);
            throw new ProductoValidadoException("La cantidad a agregar debe ser mayor que cero.", null);
        }

        producto.setStock((short) (producto.getStock() + cantidad));
        return productoRepository.save(producto);
    }

    public Producto quitarStock(Long id, int cantidad) {
        logger.info("Quitando stock al producto con ID: " + id + ", cantidad: " + cantidad);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.", null));

        if (cantidad <= 0) {
            logger.warning("Cantidad inválida para quitar stock: " + cantidad);
            throw new ProductoValidadoException("La cantidad a quitar debe ser mayor que cero.", null);
        }

        if (producto.getStock() < cantidad) {
            logger.warning("Stock insuficiente. Disponible: " + producto.getStock() + ", solicitado: " + cantidad);
            throw new ProductoNoDisponibleException("No hay suficiente stock para quitar " + cantidad + " unidades.");
        }

        producto.setStock((short) (producto.getStock() - cantidad));
        return productoRepository.save(producto);
    }

}
