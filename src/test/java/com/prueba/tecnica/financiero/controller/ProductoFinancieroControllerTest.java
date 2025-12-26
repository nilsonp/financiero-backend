package com.prueba.tecnica.financiero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.tecnica.financiero.dto.ProductoFinancieroDTO;
import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.service.ProductoFinancieroService;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoFinancieroController.class)
class ProductoFinancieroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoFinancieroService productoFinancieroService;

    private ProductoFinancieroDTO productoFinancieroDTO;
    private List<ProductoFinancieroDTO> productosFinancierosDTO;

    private final BigInteger numeroCuentaCorriente = new BigInteger("3334567890");
    private final BigInteger numeroCuentaAhorros = new BigInteger("5334567890");

    @BeforeEach
    void setUp() {
        productoFinancieroDTO = Instancio.of(ProductoFinancieroDTO.class)
                .set(Select.field(ProductoFinancieroDTO::getNumeroProducto), numeroCuentaCorriente)
                .set(Select.field(ProductoFinancieroDTO::getIdCliente), 1)
                .set(Select.field(ProductoFinancieroDTO::getTipoProducto), "CC")
                .set(Select.field(ProductoFinancieroDTO::getEstadoProducto), "A")
                .set(Select.field(ProductoFinancieroDTO::getSaldo), new BigDecimal("1000.0"))
                .set(Select.field(ProductoFinancieroDTO::getExectoGmf), false)
                .create();

        productosFinancierosDTO = List.of(productoFinancieroDTO);
    }

    @Test
    void obtenerTodosLosProductosFinancieros_Debe_RetornarListOfProductos() throws Exception {
        // Arrange
        when(productoFinancieroService.buscarTodos()).thenReturn(productosFinancierosDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productos-financieros"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numeroProducto").value("3334567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].tipoProducto").value("CC"));

        verify(productoFinancieroService, times(1)).buscarTodos();
    }

    @Test
    void obtenerProductoFinancieroPorNumero_Debe_RetornarProducto() throws Exception {
        // Arrange
        when(productoFinancieroService.buscarPorNumeroProducto(any())).thenReturn(productoFinancieroDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productos-financieros/3334567890"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroProducto").value("3334567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipoProducto").value("CC"));

        verify(productoFinancieroService, times(1)).buscarPorNumeroProducto(any());
    }

    @Test
    void obtenerProductoFinancieroPorNumero_NoEncontrado_Retornar404() throws Exception {
        // Arrange
        when(productoFinancieroService.buscarPorNumeroProducto(any()))
                .thenThrow(new ResourceNotFoundException("ProductoFinanciero", "numeroProducto", numeroCuentaAhorros));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/productos-financieros/3334567890"))
                .andExpect(status().isNotFound());

        verify(productoFinancieroService, times(1)).buscarPorNumeroProducto(any());
    }

    @Test
    void crearProductoFinanciero_Debe_RetornarProductoCreado() throws Exception {
        // Arrange
        when(productoFinancieroService.crear(any(ProductoFinancieroDTO.class))).thenReturn(productoFinancieroDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/productos-financieros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoFinancieroDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroProducto").value("3334567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipoProducto").value("CC"));

        verify(productoFinancieroService, times(1)).crear(any(ProductoFinancieroDTO.class));
    }

    @Test
    void actualizarProductoFinanciero_Debe_RetornarProductoActualizado() throws Exception {
        // Arrange
        when(productoFinancieroService.actualizar(any(ProductoFinancieroDTO.class), any())).thenReturn(productoFinancieroDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/productos-financieros/3334567890")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoFinancieroDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.numeroProducto").value("3334567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tipoProducto").value("CC"));

        verify(productoFinancieroService, times(1)).actualizar(any(ProductoFinancieroDTO.class), eq(numeroCuentaCorriente));
    }

    @Test
    void actualizarProductoFinanciero_NoEncontrado_Retornar404() throws Exception {
        // Arrange
        when(productoFinancieroService.actualizar(any(ProductoFinancieroDTO.class), any()))
                .thenThrow(new ResourceNotFoundException("ProductoFinanciero", "numeroProducto", numeroCuentaAhorros));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/productos-financieros/5334567890")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoFinancieroDTO)))
                .andExpect(status().isNotFound());

        verify(productoFinancieroService, times(1)).actualizar(any(ProductoFinancieroDTO.class), eq(numeroCuentaAhorros));
    }

    @Test
    void eliminarProductoFinanciero_Debe_RetornarNoContent() throws Exception {
        // Arrange
        doNothing().when(productoFinancieroService).borrarPorNumeroProducto(any());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productos-financieros/3334567890"))
                .andExpect(status().isNoContent());

        verify(productoFinancieroService, times(1)).borrarPorNumeroProducto(numeroCuentaCorriente);
    }

    @Test
    void eliminarProductoFinanciero_NoEncontrado_Retornar404() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("ProductoFinanciero", "numeroProducto", numeroCuentaAhorros))
                .when(productoFinancieroService).borrarPorNumeroProducto(any());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/productos-financieros/5334567890"))
                .andExpect(status().isNotFound());

        verify(productoFinancieroService, times(1)).borrarPorNumeroProducto(numeroCuentaAhorros);
    }
}
