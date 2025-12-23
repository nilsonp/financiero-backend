package com.prueba.tecnica.financiero.controller;

import com.prueba.tecnica.financiero.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private TransaccionService transaccionService;
}
