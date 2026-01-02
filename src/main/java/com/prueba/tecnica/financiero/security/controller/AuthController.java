package com.prueba.tecnica.financiero.security.controller;

import com.prueba.tecnica.financiero.security.dto.AuthRequest;
import com.prueba.tecnica.financiero.security.dto.AuthResponse;
import com.prueba.tecnica.financiero.security.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

   @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody @Valid AuthRequest request) {
       log.info("Generar token para cliente id: {}", request.getClientId());

        jwtService.generateToken(request.getClientId());

       return ResponseEntity.ok(AuthResponse.builder().build());
    }
}
