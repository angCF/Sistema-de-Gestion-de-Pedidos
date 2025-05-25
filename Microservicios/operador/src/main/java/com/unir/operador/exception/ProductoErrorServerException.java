package com.unir.operador.exception;

public class ProductoErrorServerException extends RuntimeException{
        public ProductoErrorServerException(String message, Throwable cause) {
            super(message, cause);
        }
}
