package com.unir.buscador.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unir.buscador.model.Pelicula;
import com.unir.buscador.repository.PeliculaRepository;

@Service
public class PeliculaService {
    
    @Autowired
    private PeliculaRepository peliculaRepository;

    public List<Pelicula> obtenerPeliculas() {
        return peliculaRepository.findAll();
    }

    public Pelicula obtenerPeliculaId(Long id) {
        return peliculaRepository.findById(id).orElseThrow();
    }

    public Pelicula obtenerPeliculaNombre(String nombre) {
        return peliculaRepository.findByNombre(nombre);
    }

    public Pelicula agregarPelicula(Pelicula Pelicula) {
        return peliculaRepository.save(Pelicula);
    }

    public Pelicula actualizarPelicula(Long id, Pelicula newPelicula) {
        Pelicula Pelicula = peliculaRepository.findById(id).orElseThrow();

        Pelicula.setNombre(newPelicula.getNombre());
        Pelicula.setDescripcion(newPelicula.getDescripcion());
        Pelicula.setDisponibilidad(newPelicula.getDisponibilidad());

        return peliculaRepository.save(Pelicula);
    }

    public String eliminarPelicula(Long id) {
        peliculaRepository.deleteById(id);
        return "La pelicula con " + id + " ha sido eliminada satisfactoriamente.";
    }
}