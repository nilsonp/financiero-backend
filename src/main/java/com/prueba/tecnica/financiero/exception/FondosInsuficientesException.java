package com.prueba.tecnica.financiero.exception;

import lombok.Getter;

@Getter
public class FondosInsuficientesException extends RuntimeException {

    public FondosInsuficientesException(String message) {
        super(message);
    }
}
