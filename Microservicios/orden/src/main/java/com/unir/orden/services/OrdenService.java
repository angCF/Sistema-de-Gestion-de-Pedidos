package com.unir.orden.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//mport org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.unir.orden.dto.OrdenRequest;
import com.unir.orden.dto.OrdenResponse;
import com.unir.orden.dto.ProductoDTO;
import com.unir.orden.dto.ProductoRequest;
import com.unir.orden.exception.ProductoErrorServerException;
import com.unir.orden.exception.ProductoNoDisponibleException;
import com.unir.orden.exception.ProductoNoEncontradoException;
import com.unir.orden.models.Orden;
import com.unir.orden.repository.OrdenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrdenService {

    private static final Logger logger = Logger.getLogger(OrdenService.class.getName());

    @Autowired
    Environment environment;

    @Autowired
    private OrdenRepository ordenRepository;

    private final RestTemplate restTemplate;

    public List<Orden> obtenerOrdenes() {
        return ordenRepository.findAll();
    }

    public Orden obtenerOrdenId(Long id) {
        return ordenRepository.findById(id).orElseThrow(
        () -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrada.", null));
    }

    public List<Orden> obtenerOrdenCliente(String cedula) {
        return ordenRepository.findByNumDocumentoComprador(cedula);
    }

    public String crearOrden(OrdenRequest request) {
        BigDecimal totalOrden = BigDecimal.ZERO;
        List<ProductoRequest> productosValidados = new ArrayList<>();
        for (ProductoRequest p : request.getIdProductos()) {
            ProductoDTO producto = consultarProducto(p.getIdProducto());
            if (producto.getStock() < p.getCantidad()) {
                throw new ProductoNoDisponibleException("Cantidad del producto no disponible en stock");
            }
            BigDecimal subtotal = producto.getPrecioVenta().multiply(BigDecimal.valueOf(p.getCantidad()));
            totalOrden = totalOrden.add(subtotal);

            ProductoRequest productoOrdenado = new ProductoRequest();
            productoOrdenado.setIdProducto(p.getIdProducto());
            productoOrdenado.setNombreProducto(p.getNombreProducto());
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
        Orden orden = ordenRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrada.", null));

        orden.setNombreComprador(newOrden.getNombreComprador());
        orden.setNumDocumentoComprador(newOrden.getNumDocumentoComprador());
        orden.setIdProductos(newOrden.getIdProductos());
        orden.setPrecioCompra(newOrden.getPrecioCompra());
        orden.setFechaCompra(LocalDate.now());

        return ordenRepository.save(orden);
    }

    public String eliminarOrden(Long id) {
        ordenRepository.deleteById(id);
        return "La orden con " + id + " ha sido cancelada satisfactoriamente.";
    }

    public ProductoDTO consultarProducto(Long id) {
        ProductoDTO producto = null;
        try {
            String url = environment.getProperty("urlBuscador") + id;
            producto = restTemplate.getForObject(url, ProductoDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            String errorMessage = "El producto con ID " + id + " no fue encontrado.";
            logger.severe(errorMessage);
            throw new ProductoNoEncontradoException(errorMessage, e);
        } /*
           * catch (HttpClientErrorException. e){
           * String errorMessage = "El servidor de productos no está disponible.";
           * logger.severe(errorMessage);
           * throw new PeliculaNoEncontradaException(errorMessage, e);
           * }
           */
        catch (RestClientException e) {
            logger.severe("Error al consultar el producto con ID " + id + ": " + e.getMessage());
            throw new ProductoErrorServerException("No se pudo obtener el producto con ID: " + id, e);
        }
        return producto;
    }
}