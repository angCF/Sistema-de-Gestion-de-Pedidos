package com.unir.orden.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.orden.client.ClienteProductos;
import com.unir.orden.dto.OrdenRequestDTO;
import com.unir.orden.dto.OrdenResponseDTO;
import com.unir.orden.dto.ProductoDTO;
import com.unir.orden.dto.OrdenItemDTO;
import com.unir.orden.dto.OrdenItemResponseDTO;
import com.unir.orden.exception.ClienteNoEncontradoException;
import com.unir.orden.exception.OrdenInvalidaException;
import com.unir.orden.exception.ProductoErrorServerException;
import com.unir.orden.exception.ProductoNoDisponibleException;
import com.unir.orden.exception.ProductoNoEncontradoException;
import com.unir.orden.mappers.OrdenMapper;
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
    private ClienteProductos clienteProductos;

    @Autowired
    private OrdenMapper ordenMapper;

    public List<Orden> obtenerOrdenes() {
        return ordenRepository.findAll();
    }

    public Orden obtenerOrdenId(Long id) {
        return ordenRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrada.", null));
    }

    public List<Orden> obtenerOrdenCliente(String documento) {
        if (ordenRepository.existsByNumDocumentoComprador(documento)) {
            return ordenRepository.findByNumDocumentoComprador(documento);
        }
        throw new ClienteNoEncontradoException(
                "No se encontraron ordenes asociadas al cliente con documento:" + documento);
    }

    public OrdenResponseDTO crearOrden(OrdenRequestDTO request) {
        validarOrden(request);
        BigDecimal totalOrden = BigDecimal.ZERO;
        List<OrdenItemResponseDTO> productosValidados = new ArrayList<>();
        for (OrdenItemDTO p : request.getIdProductos()) {
            ProductoDTO producto = consultarProducto(p.getIdProducto());
            if (producto.getStock() < p.getCantidad()) {
                throw new ProductoNoDisponibleException("Cantidad del producto no disponible en stock");
            }
            clienteProductos.quitarStock(p.getIdProducto(), p.getCantidad());
            BigDecimal subtotal = producto.getPrecioVenta().multiply(BigDecimal.valueOf(p.getCantidad()));
            totalOrden = totalOrden.add(subtotal);

            OrdenItemResponseDTO productoOrdenado = new OrdenItemResponseDTO();
            productoOrdenado.setIdProducto(p.getIdProducto());
            productoOrdenado.setNombreProducto(producto.getNombre());
            productoOrdenado.setCantidad(p.getCantidad());
            productosValidados.add(productoOrdenado);
        }
        Orden nuevaCompra = new Orden();
        nuevaCompra.setItemsOrden(productosValidados);
        nuevaCompra.setNombreComprador(request.getNombreComprador());
        nuevaCompra.setNumDocumentoComprador(request.getNumDocumentoComprador());
        nuevaCompra.setPrecioCompra(totalOrden);
        nuevaCompra.setFechaCompra(LocalDate.now());

        // Guarda el BD
        ordenRepository.save(nuevaCompra);

        return ordenMapper.toResponseDTO(nuevaCompra);
    }

    public OrdenResponseDTO actualizarOrden(Long id, OrdenRequestDTO newOrden) {
        validarOrden(newOrden);
        Orden ordenExistente = ordenRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrada.", null));

        BigDecimal totalOrden = BigDecimal.ZERO;
        List<OrdenItemResponseDTO> productosValidados = new ArrayList<>();

        Map<Long, Integer> cantidadesAntiguas = ordenExistente.getItemsOrden().stream()
                .collect(Collectors.toMap(OrdenItemResponseDTO::getIdProducto, OrdenItemResponseDTO::getCantidad));
        ordenExistente.setNombreComprador(newOrden.getNombreComprador());
        ordenExistente.setNumDocumentoComprador(newOrden.getNumDocumentoComprador());

        for (OrdenItemDTO nuevoItem : newOrden.getIdProductos()) {
            Long idProducto = nuevoItem.getIdProducto();
            Integer nuevaCantidad = nuevoItem.getCantidad();
            Integer cantidadAnterior = cantidadesAntiguas.getOrDefault(idProducto, 0);
            if (!Objects.equals(nuevaCantidad, cantidadAnterior)) {
                if(cantidadAnterior > 0){
                    clienteProductos.agregarStock(idProducto, cantidadAnterior);
                }
                clienteProductos.quitarStock(idProducto, nuevaCantidad);
            }

            ProductoDTO producto = consultarProducto(idProducto);
            BigDecimal subtotal = producto.getPrecioVenta().multiply(BigDecimal.valueOf(nuevaCantidad));
            totalOrden = totalOrden.add(subtotal);

            OrdenItemResponseDTO itemOrdenado = new OrdenItemResponseDTO();
            itemOrdenado.setIdProducto(idProducto);
            itemOrdenado.setNombreProducto(producto.getNombre());
            itemOrdenado.setCantidad(nuevaCantidad);
            productosValidados.add(itemOrdenado);
        }

        ordenExistente.setItemsOrden(productosValidados);
        ordenExistente.setPrecioCompra(totalOrden);
        ordenExistente.setFechaCompra(LocalDate.now());

        // Guarda el BD
        ordenRepository.save(ordenExistente);

        return ordenMapper.toResponseDTO(ordenExistente);
    }

    public String eliminarOrden(Long id) {
        Orden orden = ordenRepository.findById(id).orElseThrow(
                () -> new ProductoNoEncontradoException("La orden con ID " + id + " no fue encontrada.", null));
        for (OrdenItemResponseDTO producto : orden.getItemsOrden()) {
            try{
                clienteProductos.agregarStock(producto.getIdProducto(), producto.getCantidad());
            } catch (FeignException.NotFound e) {
                String errorMessage = "El producto con ID " + id + " no fue encontrado.";
                System.out.println(errorMessage);
            } catch (FeignException e) {
                throw new ProductoErrorServerException(e.getMessage(), e.getCause());
            }
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
            throw new ProductoErrorServerException(e.getMessage(), e.getCause());
        }
        return producto;
    }

    private void validarOrden(OrdenRequestDTO request) {
        Set<Long> idsVistos = new HashSet<>();
        for (OrdenItemDTO producto : request.getIdProductos()) {
            Long idProducto = producto.getIdProducto();
            if (idProducto == null) {
                throw new OrdenInvalidaException("El id del producto es obligatorio.");
            }
            if (producto.getCantidad() == null || producto.getCantidad() < 0) {
                throw new OrdenInvalidaException("La cantidad del producto no puede ser negativo ni nulo.");
            }
            if (!idsVistos.add(idProducto)) {
                throw new OrdenInvalidaException("El producto con id " + idProducto + " está duplicado en la orden.");
            }
        }
        if (request.getNombreComprador() == null || request.getNombreComprador().isBlank()) {
            throw new OrdenInvalidaException("El nombre del comprador no puede ser nulo ni vacío.");
        }
        if (request.getNumDocumentoComprador() == null || request.getNumDocumentoComprador().isBlank()) {
            throw new OrdenInvalidaException("El numero de documento del comprador no puede ser nulo ni vacío.");
        }
    }
}