package com.unir.producto.exception;

public class ProductoValidadoException extends IllegalArgumentException{
    public ProductoValidadoException(String message, Throwable cause) {
        super(message, cause);
    }
}