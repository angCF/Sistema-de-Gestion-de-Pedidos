package com.unir.operador.controllers;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.operador.dto.CompraRequest;
import com.unir.operador.services.CompraService;
import com.unir.operador.exception.*;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController // Controlador REST que recibe y procesa peticiones
@RequestMapping("/compra") // Endpoint de acceso
@CrossOrigin(origins = "*") // Permite acceder desde cualquier IP a los endpoint
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping()
    public ResponseEntity<?> realizarCompra(@RequestBody CompraRequest request) {
        try {
            String response = compraService.crearCompra(request);
            return ResponseEntity.ok(response);
        } catch (PeliculaNoEncontradaException e) {
            return ResponseEntity.status(HttpStatus.SC_SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }
}
