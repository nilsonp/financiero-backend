package com.prueba.tecnica.financiero.service;

import com.prueba.tecnica.financiero.dto.ProductoFinancieroDTO;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface ProductoFinancieroService {
    List<ProductoFinancieroDTO> buscarTodos();
    ProductoFinancieroDTO buscarPorNumeroProducto(BigInteger id);
    ProductoFinancieroDTO crear(ProductoFinancieroDTO dto);
    ProductoFinancieroDTO actualizar(ProductoFinancieroDTO dto, BigInteger id);
    void borrarPorNumeroProducto(BigInteger id);
}
