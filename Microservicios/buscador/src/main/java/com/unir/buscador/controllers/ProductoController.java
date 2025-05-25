package com.unir.buscador.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.unir.buscador.model.Pelicula;
import com.unir.buscador.services.PeliculaService;

@RestController
@RequestMapping("/producto")
public class ProductoController {
    @Autowired
    Environment environment;

    @Autowired
    private PeliculaService peliculaService;

    @GetMapping()
    public List<Pelicula> obtenerPeliculas() {
        return peliculaService.obtenerPeliculas();
    }

    @GetMapping("/{id}")
    public Pelicula obtenerServicioId(@PathVariable Long id) {
        return peliculaService.obtenerPeliculaId(id);
    }

    // http://localhost:9999/pelicula/nombre?nombre=
    @GetMapping("/nombre")
    public Pelicula obtenerPeliculaNombre(@RequestParam String name) {
        System.out.println("REQUEST: " + name);
        return peliculaService.obtenerPeliculaNombre(name);
    }

    @PostMapping()
    public Pelicula agregarPelicula(@RequestBody Pelicula pelicula) {
        return peliculaService.agregarPelicula(pelicula);
    }

    @PutMapping("/{id}")
    public Pelicula actualizaPelicula(@PathVariable Long id, @RequestBody Pelicula nuevaPelicula) {
        Pelicula pelicula = peliculaService.obtenerPeliculaId(id);
        return peliculaService.agregarPelicula(pelicula);
    }

    @DeleteMapping("/{id}")
    public void eliminarPelicula(@PathVariable Long id) {
        peliculaService.eliminarPelicula(id);
    } 
}