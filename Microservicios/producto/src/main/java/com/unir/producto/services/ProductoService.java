package com.unir.producto.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.producto.model.Producto;
import com.unir.producto.repository.ProductoRepository;
import com.unir.producto.exception.ProductoNoEncontradoException;
import com.unir.producto.mapper.ProductoMapper;
import com.unir.producto.dto.ProductoRequestDTO;
import com.unir.producto.dto.ProductoResponseDTO;
import com.unir.producto.exception.ProductoInvalidoException;

@Service
public class ProductoService {

    private static final Logger logger = Logger.getLogger(ProductoService.class.getName());

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper mapper;

    public List<Producto> obtenerProductos() {
        logger.info("Obteniendo todos los productos.");
        return productoRepository.findAll();
    }

    public Producto obtenerProductoId(Long id) {
        logger.info("Buscando producto con ID: " + id);
        Producto producto = productoRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado."));
        return producto;
    }

    public Producto obtenerProductoNombre(String nombre) {
        logger.info("Buscando producto con nombre: " + nombre);
        return productoRepository.findByNombre(nombre);
    }

    private void validarProducto(ProductoRequestDTO productoReq) {
        if (productoReq.getNombre() == null || productoReq.getNombre().isBlank()) {
            throw new ProductoInvalidoException("El nombre del producto es obligatorio.");
        }

        if (productoReq.getPrecioVenta() == null || productoReq.getPrecioVenta().doubleValue() < 0) {
            throw new ProductoInvalidoException("El precio del producto no puede ser negativo ni nulo.");
        }

        if (productoReq.getStock() == null || productoReq.getStock() < 0) {
            throw new ProductoInvalidoException("El stock no puede ser negativo ni nulo.");
        }
    }

    public ProductoResponseDTO agregarProducto(ProductoRequestDTO productoReq) {
        validarProducto(productoReq);
        Producto producto = mapper.requestToProducto(productoReq);
        productoRepository.save(producto);
        return mapper.productoToResponse(producto);
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO productoReq) {
        logger.info("Actualizando producto con ID: " + id);

        validarProducto(productoReq);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado."));

        producto.setNombre(productoReq.getNombre());
        producto.setDescripcion(productoReq.getDescripcion());
        producto.setPrecioVenta(productoReq.getPrecioVenta());
        producto.setStock(productoReq.getStock());

        productoRepository.save(producto);

        return mapper.productoToResponse(producto);
    }

    public String eliminarProducto(Long id) {
        logger.info("Eliminando producto con ID: " + id);
        if (!productoRepository.existsById(id)) {
            throw new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.");
        }
        productoRepository.deleteById(id);
        return "El producto con ID " + id + " ha sido eliminado satisfactoriamente.";
    }

    public ProductoResponseDTO agregarStock(Long id, int cantidad) {
        logger.info("Agregando stock al producto con ID: " + id + ", cantidad: " + cantidad);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado."));
        
        if (cantidad <= 0) {
            logger.warning("Cantidad inválida para agregar stock: " + cantidad);
            throw new ProductoInvalidoException("La cantidad a agregar debe ser mayor que cero.");
        }

        producto.setStock((short) (producto.getStock() + cantidad));
        productoRepository.save(producto);

        return mapper.productoToResponse(producto);
    }

    public ProductoResponseDTO quitarStock(Long id, int cantidad) {
        logger.info("Quitando stock al producto con ID: " + id + ", cantidad: " + cantidad);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado."));

        if (cantidad <= 0) {
            logger.warning("Cantidad inválida para quitar stock: " + cantidad);
            throw new ProductoInvalidoException("La cantidad a quitar debe ser mayor que cero.");
        }

        producto.setStock((short) (producto.getStock() - cantidad));
        productoRepository.save(producto);

        return mapper.productoToResponse(producto);
    }
}
