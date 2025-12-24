package com.prueba.tecnica.financiero.exception;

import lombok.Getter;

@Getter
public class NumeroProductoNoEncontradoException extends RuntimeException {

    public NumeroProductoNoEncontradoException(String message) {
        super(message);
    }
}
