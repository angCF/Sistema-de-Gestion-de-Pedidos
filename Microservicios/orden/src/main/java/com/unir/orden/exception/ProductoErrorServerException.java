package com.unir.orden.exception;

public class ProductoErrorServerException extends RuntimeException{
        public ProductoErrorServerException(String message, Throwable cause) {
            super(message, cause);
        }
}
