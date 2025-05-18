package com.unir.operador.controllers;

import com.unir.operador.dto.CompraResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.operador.dto.CompraRequest;
import com.unir.operador.services.CompraService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping()
    public ResponseEntity<CompraResponse> realizarCompra(@RequestBody CompraRequest request) {
        CompraResponse response = compraService.crearCompra(request);
        if(response==null){
            ResponseEntity.notFound();
        }
        return ResponseEntity.ok(response);
    }
    
    
}
