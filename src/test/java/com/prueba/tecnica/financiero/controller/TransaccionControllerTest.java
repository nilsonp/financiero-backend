package com.prueba.tecnica.financiero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.tecnica.financiero.dto.TransaccionDTO;
import com.prueba.tecnica.financiero.service.TransaccionService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransaccionController.class)
class TransaccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransaccionService transaccionService;

    private TransaccionDTO transaccionDTO;

    @BeforeEach
    void setUp() {
        transaccionDTO = Instancio.of(TransaccionDTO.class)
                .set(field(TransaccionDTO::getTransaccionId), UUID.randomUUID())
                .set(field(TransaccionDTO::getTipoTransaccion), "CONSIGNACION")
                .set(field(TransaccionDTO::getCuentaOrigen), BigInteger.valueOf(1234567890L))
                .set(field(TransaccionDTO::getCuentaDestino), BigInteger.valueOf(9876543210L))
                .set(field(TransaccionDTO::getMonto), BigDecimal.valueOf(100.00))
                .set(field(TransaccionDTO::getFechaTransaccion), LocalDateTime.now())
                .create();
    }

    @Test
    void crearTransaccion_DebeRetornarCreated() throws Exception {
        // Arrange
        when(transaccionService.crearTransaccion(any(TransaccionDTO.class))).thenReturn(transaccionDTO);

        // Act
        ResultActions result = mockMvc.perform(post("/api/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccionDTO)));

        // Assert
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transaccionId").exists())
                .andExpect(jsonPath("$.tipoTransaccion").value("CONSIGNACION"))
                .andExpect(jsonPath("$.cuentaOrigen").value(1234567890L))
                .andExpect(jsonPath("$.cuentaDestino").value(9876543210L))
                .andExpect(jsonPath("$.monto").value(100.00))
                .andExpect(jsonPath("$.fechaTransaccion").exists());

        Mockito.verify(transaccionService, Mockito.times(1)).crearTransaccion(any(TransaccionDTO.class));
    }

    @Test
    void crearTransaccion_SinCamposRequeridos_DebeRetornarBadRequest() throws Exception {
        // Arrange
        TransaccionDTO invalidDTO = Instancio.of(TransaccionDTO.class)
                .set(field(TransaccionDTO::getTipoTransaccion), "")
                .set(field(TransaccionDTO::getCuentaOrigen), null)
                .set(field(TransaccionDTO::getMonto), null)
                .create();

        // Act
        ResultActions result = mockMvc.perform(post("/api/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)));

        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists());

        Mockito.verify(transaccionService, Mockito.never()).crearTransaccion(any());
    }

    @Test
    void crearTransaccion_conTransaccionInvalida_DebeRetornarBadRequest() throws Exception {
        // Arrange
        TransaccionDTO invalidDTO = Instancio.of(TransaccionDTO.class)
                .set(field(TransaccionDTO::getTipoTransaccion), "INVALID_TYPE")
                .create();

        when(transaccionService.crearTransaccion(any(TransaccionDTO.class)))
                .thenThrow(new IllegalArgumentException("Tipo de transacción no válido"));

        // Act
        ResultActions result = mockMvc.perform(post("/api/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)));

        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Tipo de transacción no válido"));

        Mockito.verify(transaccionService, Mockito.times(1)).crearTransaccion(any(TransaccionDTO.class));
    }

    @Test
    void crearTransaccion_ConTransaccionNull_DebeRetornarBadRequest() throws Exception {
        // Arrange
        TransaccionDTO invalidDTO = Instancio.of(TransaccionDTO.class)
                .set(field(TransaccionDTO::getTipoTransaccion), null)
                .create();

        // Act
        ResultActions result = mockMvc.perform(post("/api/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)));

        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors").exists());

        Mockito.verify(transaccionService, Mockito.never()).crearTransaccion(any());
    }

    @Test
    void crearTransaccion_MontoNegativo_DebeRetornarBadRequest() throws Exception {
        // Arrange
        TransaccionDTO invalidDTO = Instancio.of(TransaccionDTO.class)
                .set(field(TransaccionDTO::getMonto), BigDecimal.valueOf(-100.00))
                .create();

        when(transaccionService.crearTransaccion(any(TransaccionDTO.class)))
                .thenThrow(new IllegalArgumentException("El monto debe ser positivo"));

        // Act
        ResultActions result = mockMvc.perform(post("/api/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)));

        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("El monto debe ser positivo"));

        Mockito.verify(transaccionService, Mockito.times(1)).crearTransaccion(any(TransaccionDTO.class));
    }

    @Test
    void crearTransaccion_MontoCero_DebeRetornarBadRequest() throws Exception {
        // Arrange
        TransaccionDTO invalidDTO = Instancio.of(TransaccionDTO.class)
                .set(field(TransaccionDTO::getMonto), BigDecimal.ZERO)
                .create();

        when(transaccionService.crearTransaccion(any(TransaccionDTO.class)))
                .thenThrow(new IllegalArgumentException("El monto debe ser mayor que cero"));

        // Act
        ResultActions result = mockMvc.perform(post("/api/transacciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)));

        // Assert
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("El monto debe ser mayor que cero"));

        Mockito.verify(transaccionService, Mockito.times(1)).crearTransaccion(any(TransaccionDTO.class));
    }
}
