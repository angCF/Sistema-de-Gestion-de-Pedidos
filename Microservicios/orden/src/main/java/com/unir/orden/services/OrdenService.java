package com.unir.orden.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.orden.client.ClienteProductos;
import com.unir.orden.dto.OrdenRequest;
import com.unir.orden.dto.OrdenResponse;
import com.unir.orden.dto.ProductoDTO;
import com.unir.orden.dto.ProductoRequest;
import com.unir.orden.dto.ProductoResponse;
import com.unir.orden.exception.ClienteNoEncontradoException;
import com.unir.orden.exception.ProductoErrorServerException;
import com.unir.orden.exception.ProductoNoDisponibleException;
import com.unir.orden.exception.ProductoNoEncontradoException;
import com.unir.orden.models.Orden;
import com.unir.orden.repository.OrdenRepository;

import feign.FeignException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrdenService {

    private static final Logger logger = Logger.getLogger(OrdenService.class.getName());

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    ClienteProductos clienteProductos;

    public List<Orden> obtenerOrdenes() {
        return ordenRepository.findAll();
    }

    public Orden obtenerOrdenId(Long id) {
        return ordenRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrada.", null));
    }

    public List<Orden> obtenerOrdenCliente(String cedula) {
        if(ordenRepository.existsByNumDocumentoComprador(cedula)){
           return ordenRepository.findByNumDocumentoComprador(cedula);
        }
        throw new ClienteNoEncontradoException("No se encontraron ordenes asociadas al cliente con documento:" + cedula);
    }

    public String crearOrden(OrdenRequest request) {
        BigDecimal totalOrden = BigDecimal.ZERO;
        List<ProductoResponse> productosValidados = new ArrayList<>();
        for (ProductoRequest p : request.getIdProductos()) {
            ProductoDTO producto = consultarProducto(p.getIdProducto());
            if (producto.getStock() < p.getCantidad()) {
                throw new ProductoNoDisponibleException("Cantidad del producto no disponible en stock");
            }
            clienteProductos.quitarStock(p.getIdProducto(), p.getCantidad());
            BigDecimal subtotal = producto.getPrecioVenta().multiply(BigDecimal.valueOf(p.getCantidad()));
            totalOrden = totalOrden.add(subtotal);

            ProductoResponse productoOrdenado = new ProductoResponse();
            productoOrdenado.setIdProducto(p.getIdProducto());
            productoOrdenado.setNombreProducto(producto.getNombre());
            productoOrdenado.setCantidad(p.getCantidad());
            productosValidados.add(productoOrdenado);
        }
        Orden nuevaCompra = new Orden();
        nuevaCompra.setIdProductos(productosValidados);
        nuevaCompra.setNombreComprador(request.getNombreComprador());
        nuevaCompra.setNumDocumentoComprador(request.getNumDocumentoComprador());
        nuevaCompra.setPrecioCompra(totalOrden);
        nuevaCompra.setFechaCompra(LocalDate.now());

        // Guarda el BD
        ordenRepository.save(nuevaCompra);

        OrdenResponse response = new OrdenResponse();
        response.setNumeroOrden(nuevaCompra.getId());
        response.setProductosOrden(productosValidados);
        response.setNombreComprador(request.getNombreComprador());
        response.setNumDocumentoComprador(request.getNumDocumentoComprador());
        response.setPrecio(nuevaCompra.getPrecioCompra());
        response.setFechaCompra(LocalDate.now());

        return response.toString();
    }

    public Orden actualizarOrden(Long id, Orden newOrden) {
        BigDecimal totalOrden = BigDecimal.ZERO;
        Orden orden = ordenRepository.findById(id).orElseThrow(
        () -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrada.", null));

        orden.setNombreComprador(newOrden.getNombreComprador());
        orden.setNumDocumentoComprador(newOrden.getNumDocumentoComprador());
        for (ProductoResponse p : newOrden.getIdProductos()) {
            ProductoDTO producto = consultarProducto(p.getIdProducto());
            clienteProductos.quitarStock(p.getIdProducto(), p.getCantidad());
            BigDecimal subtotal = producto.getPrecioVenta().multiply(BigDecimal.valueOf(p.getCantidad()));
            totalOrden = totalOrden.add(subtotal);
        }
        
        orden.setIdProductos(newOrden.getIdProductos());
        orden.setPrecioCompra(totalOrden);
        orden.setFechaCompra(LocalDate.now());

        return ordenRepository.save(orden);
    }

    public String eliminarOrden(Long id) {
        Orden orden = ordenRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrada.", null));
        for (ProductoResponse producto : orden.getIdProductos()) {
            clienteProductos.agregarStock(producto.getIdProducto(), producto.getCantidad());
        }
        ordenRepository.deleteById(id);
        return "La orden con " + id + " ha sido cancelada satisfactoriamente.";
    }

    public ProductoDTO consultarProducto(Long id) {
        ProductoDTO producto = null;
        try {
            producto = clienteProductos.obtenerProducto(id);
        } catch (FeignException.NotFound e) {
            String errorMessage = "El producto con ID " + id + " no fue encontrado.";
            logger.severe(errorMessage);
            throw new ProductoNoEncontradoException(errorMessage, e);
        } catch (FeignException e) {
            //logger.severe("Error al consultar el producto con ID " + id + ": " + e.getMessage());
            throw new ProductoErrorServerException(e.getMessage(),e.getCause());
        }
        return producto;
    }
}