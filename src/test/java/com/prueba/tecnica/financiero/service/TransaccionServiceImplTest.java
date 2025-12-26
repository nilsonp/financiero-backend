package com.prueba.tecnica.financiero.service;

import com.prueba.tecnica.financiero.dto.TransaccionDTO;
import com.prueba.tecnica.financiero.dto.TransaccionMapper;
import com.prueba.tecnica.financiero.model.ProductoFinanciero;
import com.prueba.tecnica.financiero.model.Transaccion;
import com.prueba.tecnica.financiero.repository.TransaccionRepository;
import com.prueba.tecnica.financiero.service.impl.TransaccionServiceImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private TransaccionMapper transaccionMapper;

    @Mock
    private ProductoFinancieroNegocioService productoFinancieroNegocioService;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    private TransaccionDTO transaccionDTO;
    private Transaccion transaccion;
    private ProductoFinanciero productoOrigen;
    private ProductoFinanciero productoDestino;

    @BeforeEach
    void setUp() {

        productoOrigen = Instancio.of(ProductoFinanciero.class)
                .set(field(ProductoFinanciero::getNumeroProducto), BigInteger.valueOf(1234567890L))
                .set(field(ProductoFinanciero::getSaldo), BigDecimal.valueOf(1000.00))
                .create();

        productoDestino = Instancio.of(ProductoFinanciero.class)
                .set(field(ProductoFinanciero::getNumeroProducto), BigInteger.valueOf(9876543210L))
                .set(field(ProductoFinanciero::getSaldo), BigDecimal.valueOf(500.00))
                .create();

        transaccion = Instancio.of(Transaccion.class)
                .set(field(Transaccion::getTransaccionId), UUID.randomUUID())
                .set(field(Transaccion::getTipoTransaccion), "CONSIGNACION")
                .set(field(Transaccion::getCuentaOrigen), productoOrigen)
                .set(field(Transaccion::getCuentaDestino), productoDestino)
                .set(field(Transaccion::getMonto), 100.00)
                .create();

        transaccionDTO = Instancio.of(TransaccionDTO.class)
                .set(field(TransaccionDTO::getTipoTransaccion), "CONSIGNACION")
                .set(field(TransaccionDTO::getCuentaOrigen), productoOrigen.getNumeroProducto())
                .set(field(TransaccionDTO::getCuentaDestino), productoDestino.getNumeroProducto())
                .set(field(TransaccionDTO::getMonto), BigDecimal.valueOf(100.00))
                .create();
    }

    @Test
    void crearTransaccion_Consignacion_DebeRetornarTransaccionDTO() {
        // Arrange
        transaccionDTO.setTipoTransaccion(TransaccionServiceImpl.CONSIGNACION);
        transaccionDTO.setCuentaDestino(null); // No es necesario cuando es Consignacion

        when(transaccionMapper.toEntity(any(TransaccionDTO.class))).thenReturn(transaccion);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);
        when(transaccionMapper.toDto(any(Transaccion.class))).thenReturn(transaccionDTO);

        // Act
        TransaccionDTO result = transaccionService.crearTransaccion(transaccionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(TransaccionServiceImpl.CONSIGNACION, result.getTipoTransaccion());
        verify(productoFinancieroNegocioService, times(1))
                .consignar(transaccionDTO.getCuentaOrigen(), transaccionDTO.getMonto());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void crearTransaccion_Retiro_DebeRetornarTransaccionDTO() {
        // Arrange
        transaccionDTO.setTipoTransaccion(TransaccionServiceImpl.RETIRO);
        transaccionDTO.setCuentaDestino(null); // No es necesario cuando es retiro

        when(transaccionMapper.toEntity(any(TransaccionDTO.class))).thenReturn(transaccion);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);
        when(transaccionMapper.toDto(any(Transaccion.class))).thenReturn(transaccionDTO);

        // Act
        TransaccionDTO result = transaccionService.crearTransaccion(transaccionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(TransaccionServiceImpl.RETIRO, result.getTipoTransaccion());
        verify(productoFinancieroNegocioService, times(1))
                .retirar(transaccionDTO.getCuentaOrigen(), transaccionDTO.getMonto());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void crearTransaccion_Transferencia_DebeRetornarTransaccionDTO() {
        // Arrange
        transaccionDTO.setTipoTransaccion(TransaccionServiceImpl.TRANSFERENCIA);

        when(transaccionMapper.toEntity(any(TransaccionDTO.class))).thenReturn(transaccion);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);
        when(transaccionMapper.toDto(any(Transaccion.class))).thenReturn(transaccionDTO);

        // Act
        TransaccionDTO result = transaccionService.crearTransaccion(transaccionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(TransaccionServiceImpl.TRANSFERENCIA, result.getTipoTransaccion());
        verify(productoFinancieroNegocioService, times(1))
                .transferir(transaccionDTO.getCuentaOrigen(),
                           transaccionDTO.getCuentaDestino(),
                           transaccionDTO.getMonto());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void crearTransaccion_InvalidType_DebeLanzarIllegalArgumentException() {
        // Arrange
        transaccionDTO.setTipoTransaccion("INVALID_TYPE");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transaccionService.crearTransaccion(transaccionDTO));

        assertTrue(exception.getMessage().contains("Tipo de transacción no válido"));
        verify(productoFinancieroNegocioService, never()).consignar(any(), any());
        verify(productoFinancieroNegocioService, never()).retirar(any(), any());
        verify(productoFinancieroNegocioService, never()).transferir(any(), any(), any());
        verify(transaccionRepository, never()).save(any());
    }

    @Test
    void crearTransaccion_NullType_DebeLanzarIllegalArgumentException() {
        // Arrange
        transaccionDTO.setTipoTransaccion(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transaccionService.crearTransaccion(transaccionDTO));

        assertTrue(exception.getMessage().contains("Tipo de transacción no válido"));
        verifyNoInteractions(productoFinancieroNegocioService);
        verify(transaccionRepository, never()).save(any());
    }

    @Test
    void crearTransaccion_EmptyType_DebeLanzarIllegalArgumentException() {
        // Arrange
        transaccionDTO.setTipoTransaccion("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transaccionService.crearTransaccion(transaccionDTO));

        assertTrue(exception.getMessage().contains("Tipo de transacción no válido"));
        verifyNoInteractions(productoFinancieroNegocioService);
        verify(transaccionRepository, never()).save(any());
    }
}
