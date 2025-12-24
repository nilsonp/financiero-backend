package com.prueba.tecnica.financiero.service;

import com.prueba.tecnica.financiero.model.ProductoFinanciero;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface ProductoFinancieroNegocioService {

    void validaSiExisteProductoPorNumero(BigInteger numeroProducto);

    ProductoFinanciero buscarProductoPorNumero(BigInteger numeroProducto);

    ProductoFinanciero consignar(BigInteger numeroProducto, BigDecimal monto);

    ProductoFinanciero retirar(BigInteger numeroProducto, BigDecimal monto);

    ProductoFinanciero transferir(BigInteger numeroProductoOrigen, BigInteger numeroProductoDestino, BigDecimal monto);
}
