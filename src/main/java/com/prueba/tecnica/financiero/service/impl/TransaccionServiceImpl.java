package com.prueba.tecnica.financiero.service.impl;

import com.prueba.tecnica.financiero.dto.TransaccionDTO;
import com.prueba.tecnica.financiero.dto.TransaccionMapper;
import com.prueba.tecnica.financiero.model.Transaccion;
import com.prueba.tecnica.financiero.repository.TransaccionRepository;
import com.prueba.tecnica.financiero.service.ProductoFinancieroNegocioService;
import com.prueba.tecnica.financiero.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final TransaccionMapper transaccionMapper;
    private final ProductoFinancieroNegocioService productoFinancieroNegocioService;

    public static final String CONSIGNACION = "CONSIGNACION";
    public static final String RETIRO = "RETIRO";
    public static final String TRANSFERENCIA = "TRANSFERENCIA";
    private static final List<String> ALLOWED_TRANSACTION_TYPES = Arrays.asList(CONSIGNACION, RETIRO, TRANSFERENCIA);

    @Override
    @Transactional
    public TransaccionDTO crearTransaccion(TransaccionDTO transaccionDTO) {
        log.info("crear transaccion tipo: {}", transaccionDTO.getTipoTransaccion());

        if (!ALLOWED_TRANSACTION_TYPES.contains(transaccionDTO.getTipoTransaccion())) {
            throw new IllegalArgumentException("Tipo de transacción no válido: " + transaccionDTO.getTipoTransaccion());
        }

        switch (transaccionDTO.getTipoTransaccion()) {
            case CONSIGNACION ->
                    productoFinancieroNegocioService.consignar(transaccionDTO.getCuentaOrigen(), transaccionDTO.getMonto());
            case RETIRO ->
                    productoFinancieroNegocioService.retirar(transaccionDTO.getCuentaOrigen(), transaccionDTO.getMonto());
            case TRANSFERENCIA ->
                    productoFinancieroNegocioService.transferir(transaccionDTO.getCuentaOrigen(), transaccionDTO.getCuentaDestino(), transaccionDTO.getMonto());
        };

        Transaccion transaccion = transaccionMapper.toEntity(transaccionDTO);
        log.debug("transaccion mapeda a entidad - tipo: {}", transaccion.getTipoTransaccion());
        transaccion = transaccionRepository.save(transaccion);
        log.info("transaccion generada correctamente id: {}", transaccion.getTransaccionId());

        transaccionDTO = transaccionMapper.toDto(transaccion);
        log.debug("transaccion mapeada a dto - id:{}", transaccionDTO.getTransaccionId());

        return transaccionDTO;
    }
}
