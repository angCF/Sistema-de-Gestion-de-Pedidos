package com.unir.orden.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unir.orden.dto.OrdenRequest;
import com.unir.orden.exception.*;
import com.unir.orden.models.Orden;
import com.unir.orden.services.OrdenService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController // Controlador REST que recibe y procesa peticiones
@RequestMapping("/orden") // Endpoint de acceso
@CrossOrigin(origins = "*") // Permite acceder desde cualquier IP a los endpoint
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @GetMapping()
    public ResponseEntity<List<Orden>> obtenerOdenes() {
        List<Orden> ordenes = ordenService.obtenerOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerOrdenId(@PathVariable Long id) {
        try {
            Orden orden = ordenService.obtenerOrdenId(id);
            return ResponseEntity.ok(orden);
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Orden>> obtenerOrdenCliente(@RequestParam String numCedula) {
        List<Orden> ordenesCliente = ordenService.obtenerOrdenCliente(numCedula);
        return ResponseEntity.ok(ordenesCliente);
    }

    @PostMapping()
    public ResponseEntity<?> crearOrden(@RequestBody OrdenRequest request) {
        try {
            String response = ordenService.crearOrden(request);
            return ResponseEntity.ok(response);
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ProductoErrorServerException e){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        } catch (ProductoNoDisponibleException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
