package com.prueba.tecnica.financiero.service.impl;

import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.repository.ProductoFinancieroRepository;
import com.prueba.tecnica.financiero.service.ProductoFinancieroNegocioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductoFinancieroNegocioServiceImpl implements ProductoFinancieroNegocioService {

    private final ProductoFinancieroRepository productoFinancieroRepository;

    @Override
    public void validaSiExisteProductoPorNumero(BigInteger id) {
        if (!productoFinancieroRepository.existsById(id)) {
            log.error("producto financiero no encontrado: {}", id);
            throw new ResourceNotFoundException("ProductoFinanciero", "numeroProducto", id);
        }
    }
}
