package com.unir.producto.services;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.producto.dto.ProductoDTO;
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

    public List<ProductoDTO> obtenerProductos() {
        logger.info("Obteniendo todos los productos.");
        return productoRepository.findAll().stream()
                .map(this::convertirAProductoDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO obtenerProductoId(Long id) {
        logger.info("Buscando producto con ID: " + id);
        Producto producto = productoRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.", null));
        return convertirAProductoDTO(producto);
    }

    public ProductoDTO obtenerProductoNombre(String nombre) {
        logger.info("Buscando producto con nombre: " + nombre);
        Producto producto = productoRepository.findByNombre(nombre);
        return convertirAProductoDTO(producto);
    }

    public ProductoDTO agregarProducto(ProductoDTO dto) {
        logger.info("Agregando nuevo producto: " + dto.getNombre());
        Producto producto = convertirAProducto(dto);
        validarProducto(producto);
        Producto guardado = productoRepository.save(producto);
        return convertirAProductoDTO(guardado);
    }

    public ProductoDTO actualizarProducto(Long id, ProductoDTO dto) {
        logger.info("Actualizando producto con ID: " + id);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.", null));

        validarProducto(convertirAProducto(dto));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStock(dto.getStock());

        Producto actualizado = productoRepository.save(producto);
        return convertirAProductoDTO(actualizado);
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

    public ProductoDTO agregarStock(Long id, int cantidad) {
        logger.info("Agregando stock al producto con ID: " + id + ", cantidad: " + cantidad);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("El producto con ID " + id + " no fue encontrado.", null));

        if (cantidad <= 0) {
            logger.warning("Cantidad inválida para agregar stock: " + cantidad);
            throw new ProductoValidadoException("La cantidad a agregar debe ser mayor que cero.", null);
        }

        producto.setStock((short) (producto.getStock() + cantidad));
        return convertirAProductoDTO(productoRepository.save(producto));
    }

    public ProductoDTO quitarStock(Long id, int cantidad) {
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
        return convertirAProductoDTO(productoRepository.save(producto));
    }

    private ProductoDTO convertirAProductoDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecioVenta(),
                producto.getStock()
        );
    }

    private Producto convertirAProducto(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setId(dto.getId());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setStock(dto.getStock());
        return producto;
    }
}
