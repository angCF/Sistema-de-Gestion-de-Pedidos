package com.unir.operador.exception;

public class PeliculaNoEncontradaException extends RuntimeException{
    public PeliculaNoEncontradaException(String message, Throwable cause) {
            super(message, cause);
        }
}