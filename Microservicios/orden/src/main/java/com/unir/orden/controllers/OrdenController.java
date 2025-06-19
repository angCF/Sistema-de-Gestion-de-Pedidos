package com.unir.orden.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.orden.dto.OrdenRequestDTO;
import com.unir.orden.dto.OrdenResponseDTO;
import com.unir.orden.models.Orden;
import com.unir.orden.services.OrdenService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/orden")
@CrossOrigin(origins = "*")
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
        Orden orden = ordenService.obtenerOrdenId(id);
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/cliente/{numDocumento}")
    public ResponseEntity<List<Orden>> obtenerOrdenCliente(@PathVariable String numDocumento) {
        List<Orden> ordenesCliente = ordenService.obtenerOrdenCliente(numDocumento);
        return ResponseEntity.ok(ordenesCliente);
    }

    @PostMapping()
    public ResponseEntity<?> crearOrden(@Valid @RequestBody OrdenRequestDTO request) {
        OrdenResponseDTO response = ordenService.crearOrden(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrden(@PathVariable Long id, @Valid @RequestBody OrdenRequestDTO newRequest) {
        OrdenResponseDTO nuevaOrden = ordenService.actualizarOrden(id, newRequest);
        return ResponseEntity.ok(nuevaOrden);
    }

    @DeleteMapping("/{id}")
    public String eliminarOrden(@PathVariable Long id) {
        return ordenService.eliminarOrden(id);
    }
}