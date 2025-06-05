package com.unir.orden.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.unir.orden.dto.ErrorResponse;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return new ResponseEntity<>(new ErrorResponse(404, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleClienteNoEncontrado(ClienteNoEncontradoException ex) {
        return new ResponseEntity<>(new ErrorResponse(404, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrdenInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleOrdenInvalida(OrdenInvalidaException ex) {
        return new ResponseEntity<>(new ErrorResponse(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductoNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleProductoNoDisponible(ProductoNoDisponibleException ex) {
        return new ResponseEntity<>(new ErrorResponse(409, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductoErrorServerException.class)
    public ResponseEntity<ErrorResponse> handleProductoErrorServer(ProductoErrorServerException ex) {
        return new ResponseEntity<>(new ErrorResponse(500, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeign(FeignException ex) {
        if (ex.status() == 404) {
            return new ResponseEntity<>(new ErrorResponse(404, "El producto no fue encontrado."), HttpStatus.NOT_FOUND);
        } else if (ex.status() == 409) {
            return new ResponseEntity<>(new ErrorResponse(409, "Stock insuficiente."), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ErrorResponse(500, "Error al contactar al servicio de productos."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos.");
        return new ResponseEntity<>(new ErrorResponse(400, mensaje), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenerico(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(500, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
