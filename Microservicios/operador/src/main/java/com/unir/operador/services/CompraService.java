package com.unir.operador.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.unir.operador.dto.CompraRequest;
import com.unir.operador.dto.CompraResponse;
import com.unir.operador.models.Compra;
import com.unir.operador.models.Pelicula;

@Service
public class CompraService {
    @Autowired
    Environment environment;

    @Autowired
    private RestTemplate restTemplate;
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
    
    public CompraResponse crearCompra(CompraRequest request){
        Compra nuevaCompra = new Compra();
        CompraResponse response = new CompraResponse();
        Pelicula pelicula = consultarPelicula(request.getIdPelicula());
        if(pelicula==null || pelicula.getDisponibilidad()==false){
            //throw new PeliculaNoEncontradaException("Película no encontrada");
            return null;
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

        return response;
    }

    public Pelicula consultarPelicula(Long id){
        Pelicula pelicula;
        RestTemplate restTemplate = null;
        pelicula = restTemplate.getForObject(environment.getProperty("urlBuscador") + id, Pelicula.class);
        if(pelicula==null){
            return null;
        }
        return pelicula;
    }
}