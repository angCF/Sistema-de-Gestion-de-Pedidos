package com.unir.operador.exception;

public class ProductoNoEncontradoException extends RuntimeException{
    public ProductoNoEncontradoException(String message, Throwable cause) {
            super(message, cause);
        }
}