package com.prueba.tecnica.financiero.controller;

import com.prueba.tecnica.financiero.dto.TransaccionDTO;
import com.prueba.tecnica.financiero.service.TransaccionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
@Tag(name = "Transacciones", description = "API para gestionar transacciones de productos finacieros")
public class TransaccionController {

    private final TransaccionService transaccionService;

    @PostMapping
    public ResponseEntity<TransaccionDTO> crearTransaccion(@RequestBody @Valid TransaccionDTO transaccionDTO) {
        log.info("crear transaccion tipo: {} - numeroProducto Origen: {}", transaccionDTO.getTipoTransaccion(),
                transaccionDTO.getCuentaOrigen());
        TransaccionDTO nuevaTransaccion = transaccionService.crearTransaccion(transaccionDTO);
        log.info("generada nueva transaccion tipo: {} - id: {}", transaccionDTO.getTipoTransaccion(),
                nuevaTransaccion.getTransaccionId());

        return new ResponseEntity<>(nuevaTransaccion, HttpStatus.CREATED);
    }
}
