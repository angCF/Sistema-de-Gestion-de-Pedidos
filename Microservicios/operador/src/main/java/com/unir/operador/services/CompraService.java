package com.unir.operador.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

import org.apache.tomcat.util.net.jsse.PEMFile;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//mport org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.unir.operador.dto.CompraRequest;
import com.unir.operador.dto.CompraResponse;
import com.unir.operador.exception.PeliculaNoEncontradaException;
import com.unir.operador.models.Compra;
import com.unir.operador.models.Pelicula;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CompraService {

    private static final Logger logger = Logger.getLogger(CompraService.class.getName());
    
    Environment environment;

    private final RestTemplate restTemplate;
    
    //@Bean
    //public RestTemplate restTemplate(RestTemplateBuilder builder) {
      //  return builder.build();
    //}
    
    public String crearCompra(CompraRequest request){
        Compra nuevaCompra = new Compra();
        CompraResponse response = new CompraResponse();
        Pelicula pelicula = consultarPelicula(request.getIdPelicula());
        //Pelicula pelicula = new Pelicula(1L, "TEST", "TEST", new BigDecimal("19.99"), new BigDecimal("4.99"),true);
        if(pelicula==null){
            //throw new PeliculaNoEncontradaException("Película no encontrada"); || pelicula.getId()!=request.getIdPelicula()
            return "Pelicula no encontrada";
        }else if(pelicula.getDisponibilidad()==false){
            return "Pelicula no disponible";
        }
        nuevaCompra.setId(null);
        nuevaCompra.setIdPelicula(request.getIdPelicula());
        nuevaCompra.setNombreComprador(request.getNombreComprador());
        nuevaCompra.setPrecioCompra(pelicula.getPrecioVenta());
        nuevaCompra.setFechaCompra(LocalDate.now());

        //Guardar compra BD

        response.setNombrePelicula(pelicula.getNombre());
        response.setNombreComprador(request.getNombreComprador());
        response.setPrecio(pelicula.getPrecioVenta());
        response.setFechaCompra(LocalDate.now());

        return response.toString();
    }

    public Pelicula consultarPelicula(Long id){
        Pelicula pelicula = null;
        try {
            String url = environment.getProperty("urlBuscador") + id;
            pelicula = restTemplate.getForObject(url, Pelicula.class);
        } catch (HttpClientErrorException.NotFound e) {
            String errorMessage = "La película con ID " + id + " no fue encontrada.";
            logger.severe(errorMessage);
            throw new PeliculaNoEncontradaException(errorMessage, e);
        } /*catch (HttpClientErrorException. e){
            String errorMessage = "El servidor de productos no está disponible.";
            logger.severe(errorMessage);
            throw new PeliculaNoEncontradaException(errorMessage, e);
        } */
       catch (RestClientException e) {
            logger.severe("Error al consultar la película con ID " + id + ": " + e.getMessage());
            throw new PeliculaNoEncontradaException("No se pudo obtener la película con ID: " + id, e);
        }
        return pelicula;
    }
}