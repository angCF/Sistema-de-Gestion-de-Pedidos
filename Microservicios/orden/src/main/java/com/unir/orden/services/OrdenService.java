package com.unir.orden.services;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        return ordenRepository.findById(id).orElseThrow(() -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrado.", null));
    }

    public List<Orden> obtenerOrdenCliente(String cedula) {
        return ordenRepository.findByNumDocumentoComprador(cedula);
    }

    public String crearOrden(OrdenRequest request){
        Orden nuevaCompra = new Orden();
        OrdenResponse response = new OrdenResponse();
        ProductoDTO producto = consultarProducto(request.getIdProducto());

        if(producto.getStock()<request.getCantidad()){
            throw new ProductoNoDisponibleException("Cantidad del producto no disponible en stock");
        }

        nuevaCompra.setIdProducto(request.getIdProducto());
        nuevaCompra.setNombreComprador(request.getNombreComprador());
        nuevaCompra.setNumDocumentoComprador(request.getNumDocumentoComprador());
        nuevaCompra.setPrecioCompra(producto.getPrecioVenta().multiply(BigDecimal.valueOf(request.getCantidad())));
        nuevaCompra.setCantidad(request.getCantidad());
        nuevaCompra.setFechaCompra(LocalDate.now());

        //Guarda el BD
        ordenRepository.save(nuevaCompra);

        response.setNumeroOrden(nuevaCompra.getId());
        response.setNombreProducto(producto.getNombre());
        response.setNombreComprador(request.getNombreComprador());
        response.setNumDocumentoComprador(request.getNumDocumentoComprador());
        response.setPrecio(nuevaCompra.getPrecioCompra());
        response.setCantidad(request.getCantidad());
        response.setFechaCompra(LocalDate.now());

        return response.toString();
    }

    public ProductoDTO consultarProducto(Long id){
        ProductoDTO producto = null;
        try {
            String url = environment.getProperty("urlBuscador") + id;
            producto = restTemplate.getForObject(url, ProductoDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            String errorMessage = "El producto con ID " + id + " no fue encontrado.";
            logger.severe(errorMessage);
            throw new ProductoNoEncontradoException(errorMessage, e);
       } /*catch (HttpClientErrorException. e){
            String errorMessage = "El servidor de productos no está disponible.";
            logger.severe(errorMessage);
            throw new PeliculaNoEncontradaException(errorMessage, e);
        } */
       catch (RestClientException e) {
            logger.severe("Error al consultar el producto con ID " + id + ": " + e.getMessage());
            throw new ProductoErrorServerException("No se pudo obtener el producto con ID: " + id, e);
        }
        return producto;
    }
}