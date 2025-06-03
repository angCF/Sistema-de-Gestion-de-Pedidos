package com.unir.producto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<?> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ex.getMessage());
    }

    @ExceptionHandler(ProductoValidadoException.class)
    public ResponseEntity<?> handleProductoValidado(ProductoValidadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body( ex.getMessage());
    }

    @ExceptionHandler(ProductoNoDisponibleException.class)
    public ResponseEntity<?> handleProductoNoDisponible(ProductoNoDisponibleException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Error interno:" + ex.getMessage());
    }
}
