package com.unir.operador.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.unir.operador.dto.AlquilerRequest;
import com.unir.operador.dto.AlquilerResponse;
import com.unir.operador.models.Alquiler;
import com.unir.operador.models.Pelicula;

@Service
public class AlquilerService {
    @Autowired
    private Environment environment;

    @Autowired
    private RestTemplate restTemplate;

    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
   
    public AlquilerResponse crearAlquiler(AlquilerRequest request){
        Alquiler nuevoAlquiler = new Alquiler();
        AlquilerResponse response = new AlquilerResponse();
        Pelicula pelicula = consultarPelicula(request.getIdPelicula());
        if(pelicula==null){
            return null;
        }

        nuevoAlquiler.setIdPelicula(request.getIdPelicula());
        nuevoAlquiler.setNombreUsuario(request.getNombreUsuario());
        nuevoAlquiler.setPrecioAlquiler(pelicula.getPrecioAlquiler());
        nuevoAlquiler.setFechaInicio(LocalDate.now());
        nuevoAlquiler.setFechaFin(LocalDate.now().plusDays(5));
        
        //Guardar alquiler en BD

        response.setNombrePelicula(pelicula.getNombre());
        response.setNombreUsuario(request.getNombreUsuario());
        response.setPrecioAlquiler(pelicula.getPrecioAlquiler());
        response.setFechaInicio(LocalDate.now());
        response.setFechaFin(LocalDate.now().plusDays(5));

        return response;
    }

    //Evaluar crear en Utils
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
