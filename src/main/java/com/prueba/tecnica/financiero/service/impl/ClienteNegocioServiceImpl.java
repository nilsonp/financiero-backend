package com.prueba.tecnica.financiero.service.impl;

import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.repository.ClienteRepository;
import com.prueba.tecnica.financiero.service.ClienteNegocioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClienteNegocioServiceImpl implements ClienteNegocioService {

    private final ClienteRepository clienteRepository;

    @Override
    public void validaSiExisteClientePorId(Integer idCliente) {
        if(!clienteRepository.existsById(idCliente)) {
            log.error("cliente no encontrado id: {}", idCliente);
            throw new ResourceNotFoundException("Cliente", "id", idCliente);
        }
    }
}
