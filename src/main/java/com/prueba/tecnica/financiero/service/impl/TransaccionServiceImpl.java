package com.prueba.tecnica.financiero.service.impl;

import com.prueba.tecnica.financiero.dto.TransaccionDTO;
import com.prueba.tecnica.financiero.dto.TransaccionMapper;
import com.prueba.tecnica.financiero.repository.TransaccionRepository;
import com.prueba.tecnica.financiero.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;

    @Override
    public TransaccionDTO crearTransaccion(TransaccionDTO transaccionDto) {
        return TransaccionDTO.builder().build();
    }
}
