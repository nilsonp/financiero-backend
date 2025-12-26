package com.prueba.tecnica.financiero.service;

import com.prueba.tecnica.financiero.dto.ProductoFinancieroDTO;
import com.prueba.tecnica.financiero.dto.ProductoFinancieroMapper;
import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.model.Cliente;
import com.prueba.tecnica.financiero.model.ProductoFinanciero;
import com.prueba.tecnica.financiero.repository.ClienteRepository;
import com.prueba.tecnica.financiero.repository.ProductoFinancieroRepository;
import com.prueba.tecnica.financiero.service.impl.ClienteNegocioServiceImpl;
import com.prueba.tecnica.financiero.service.impl.ProductoFinancieroNegocioServiceImpl;
import com.prueba.tecnica.financiero.service.impl.ProductoFinancieroServiceImpl;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoFinancieroServiceImplTest {

    @Mock
    private ProductoFinancieroRepository productoFinancieroRepository;

    @Mock
    private ClienteRepository clienteRepository;

    private final ProductoFinancieroMapper productoFinancieroMapper = Mappers.getMapper(ProductoFinancieroMapper.class);

    private ProductoFinancieroServiceImpl productoFinancieroService;

    private List<ProductoFinanciero> listOfProductoFinanciero;
    private ProductoFinanciero productoFinanciero;
    private ProductoFinancieroDTO productoFinancieroDTO;

    private final BigInteger numeroCuentaCorriente = new BigInteger("3334567890");
    private final BigInteger numeroCuentaAhorros = new BigInteger("5334567890");

    @BeforeEach
    void setUp() {

        ClienteNegocioServiceImpl clienteNegocio = new ClienteNegocioServiceImpl(clienteRepository);
        ProductoFinancieroNegocioServiceImpl productoFinancieroNegocio = new ProductoFinancieroNegocioServiceImpl(productoFinancieroRepository);
        productoFinancieroService = new ProductoFinancieroServiceImpl(productoFinancieroRepository, productoFinancieroMapper, clienteNegocio, productoFinancieroNegocio);

        Cliente cliente = Instancio.of(Cliente.class).create();

        productoFinanciero = Instancio.of(ProductoFinanciero.class)
                .set(Select.field(ProductoFinanciero::getNumeroProducto), numeroCuentaCorriente)
                .set(Select.field(ProductoFinanciero::getCliente), cliente)
                .set(Select.field(ProductoFinanciero::getTipoProducto), "CC")
                .set(Select.field(ProductoFinanciero::getEstadoProducto), "A")
                .set(Select.field(ProductoFinanciero::getSaldo), new BigDecimal("1000.0"))
                .set(Select.field(ProductoFinanciero::getExectoGmf), false)
                .create();

        listOfProductoFinanciero = new ArrayList<>();
        listOfProductoFinanciero.add(productoFinanciero);

        listOfProductoFinanciero.add(
                Instancio.of(ProductoFinanciero.class)
                        .set(Select.field(ProductoFinanciero::getNumeroProducto), numeroCuentaAhorros)
                        .set(Select.field(ProductoFinanciero::getCliente), cliente)
                        .set(Select.field(ProductoFinanciero::getTipoProducto), "CA")
                        .set(Select.field(ProductoFinanciero::getEstadoProducto), "A")
                        .set(Select.field(ProductoFinanciero::getSaldo), new BigDecimal("1000.0"))
                        .set(Select.field(ProductoFinanciero::getExectoGmf), false)
                        .create()
        );

        productoFinancieroDTO = Instancio.of(ProductoFinancieroDTO.class)
                .set(Select.field(ProductoFinancieroDTO::getNumeroProducto), numeroCuentaCorriente)
                .set(Select.field(ProductoFinancieroDTO::getIdCliente), 1)
                .set(Select.field(ProductoFinancieroDTO::getTipoProducto), "CC")
                .set(Select.field(ProductoFinancieroDTO::getEstadoProducto), "A")
                .set(Select.field(ProductoFinancieroDTO::getSaldo), new BigDecimal("1000.0"))
                .set(Select.field(ProductoFinancieroDTO::getExectoGmf), false)
                .create();
    }

    @Test
    void buscarTodos_Debe_RetornarListOfProductos() {
        // Arrange
        when(productoFinancieroRepository.findAll()).thenReturn(listOfProductoFinanciero);

        // Act
        List<ProductoFinancieroDTO> result = productoFinancieroService.buscarTodos();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(productoFinancieroRepository, times(1)).findAll();
    }

    @Test
    void buscarPorNumeroProducto_Debe_RetornarProducto() {
        // Arrange
        when(productoFinancieroRepository.findById(any())).thenReturn(Optional.of(productoFinanciero));

        // Act
        ProductoFinancieroDTO result = productoFinancieroService.buscarPorNumeroProducto(numeroCuentaCorriente);

        // Assert
        assertNotNull(result);
        assertEquals(productoFinancieroDTO.getNumeroProducto(), result.getNumeroProducto());
        verify(productoFinancieroRepository, times(1)).findById(any());
    }

    @Test
    void buscarPorNumeroProducto_NoEncontrado_LanzarResourceNotFoundException() {
        // Arrange
        when(productoFinancieroRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                productoFinancieroService.buscarPorNumeroProducto(numeroCuentaCorriente));
        verify(productoFinancieroRepository, times(1)).findById(any());
    }

    @Test
    void crear_Debe_RetornarProductoCreado() {
        // Arrange
        when(clienteRepository.existsById(anyInt())).thenReturn(true);
        when(productoFinancieroRepository.save(any(ProductoFinanciero.class))).thenReturn(productoFinanciero);

        // Act
        ProductoFinancieroDTO result = productoFinancieroService.crear(productoFinancieroDTO);

        // Assert
        assertNotNull(result);
        assertEquals(productoFinancieroDTO.getNumeroProducto(), result.getNumeroProducto());
        verify(clienteRepository, times(1)).existsById(1);
        verify(productoFinancieroRepository, times(1)).save(any(ProductoFinanciero.class));
    }

    @Test
    void crear_ClienteNoEncontrado_LanzarResourceNotFoundException() {
        // Arrange
        when(clienteRepository.existsById(anyInt())).thenReturn(Boolean.FALSE);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                productoFinancieroService.crear(productoFinancieroDTO));
        verify(clienteRepository, times(1)).existsById(1);
    }

    @Test
    void actualizar_Debe_RetornarUpdatedProducto() {
        // Arrange
        when(productoFinancieroRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(clienteRepository.existsById(anyInt())).thenReturn(Boolean.TRUE);
        when(productoFinancieroRepository.save(any(ProductoFinanciero.class))).thenReturn(productoFinanciero);

        // Act
        ProductoFinancieroDTO result = productoFinancieroService.actualizar(productoFinancieroDTO, numeroCuentaCorriente);

        // Assert
        assertNotNull(result);
        assertEquals(productoFinancieroDTO.getNumeroProducto(), result.getNumeroProducto());
        verify(productoFinancieroRepository, times(1)).existsById(any());
        verify(clienteRepository, times(1)).existsById(anyInt());
        verify(productoFinancieroRepository, times(1)).save(any(ProductoFinanciero.class));
    }

    @Test
    void actualizar_ProductoNoEncontrado_LanzarResourceNotFoundException() {
        // Arrange
        when(productoFinancieroRepository.existsById(any())).thenReturn(Boolean.FALSE);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                productoFinancieroService.actualizar(productoFinancieroDTO, numeroCuentaCorriente));
        verify(productoFinancieroRepository, times(1)).existsById(any());
    }

    @Test
    void actualizar_ClienteNoEncontrado_LanzarResourceNotFoundException() {
        // Arrange
        when(productoFinancieroRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(clienteRepository.existsById(anyInt())).thenReturn(Boolean.FALSE);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                productoFinancieroService.actualizar(productoFinancieroDTO, numeroCuentaCorriente));
        verify(productoFinancieroRepository, times(1)).existsById(any());
        verify(clienteRepository, times(1)).existsById(anyInt());
    }

    @Test
    void borrarPorNumeroProducto_Debe_EliminarProducto() {
        // Arrange
        when(productoFinancieroRepository.existsById(any())).thenReturn(Boolean.TRUE);
        doNothing().when(productoFinancieroRepository).deleteById(any());

        // Act
        productoFinancieroService.borrarPorNumeroProducto(numeroCuentaAhorros);

        // Assert
        verify(productoFinancieroRepository, times(1)).existsById(any());
        verify(productoFinancieroRepository, times(1)).deleteById(numeroCuentaAhorros);
    }

    @Test
    void borrarPorNumeroProducto_NoEncontrado_LanzarResourceNotFoundException() {
        // Arrange
        when(productoFinancieroRepository.existsById(any())).thenReturn(Boolean.FALSE);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                productoFinancieroService.borrarPorNumeroProducto(numeroCuentaAhorros));
        verify(productoFinancieroRepository, times(1)).existsById(any());
    }
}
