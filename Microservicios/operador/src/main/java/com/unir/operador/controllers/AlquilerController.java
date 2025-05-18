package com.unir.operador.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.operador.dto.AlquilerRequest;
import com.unir.operador.dto.AlquilerResponse;
import com.unir.operador.services.AlquilerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/alquiler")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    @PostMapping()
    public ResponseEntity<AlquilerResponse> realizarAlquiler(@RequestBody AlquilerRequest alquilerRequest) {
        AlquilerResponse response = alquilerService.crearAlquiler(alquilerRequest);

        if(response==null){
            ResponseEntity.notFound();
        }
        
        return ResponseEntity.ok(response);
    }
    
    
}
