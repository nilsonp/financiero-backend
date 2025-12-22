package com.prueba.tecnica.financiero.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.tecnica.financiero.dto.ClienteDTO;
import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.service.ClienteService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    private ClienteDTO clienteDTO;
    private List<ClienteDTO> clientesDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = Instancio.of(ClienteDTO.class)
                .set(Select.field(ClienteDTO::getIdCliente), 1)
                .set(Select.field(ClienteDTO::getTipoIdentificacion), "CC")
                .set(Select.field(ClienteDTO::getNumeroIdentificacion), "123456789")
                .set(Select.field(ClienteDTO::getNombres), "John")
                .set(Select.field(ClienteDTO::getApellidos), "Doe")
                .set(Select.field(ClienteDTO::getCorreoElectronico), "john.doe@example.com")
                .create();

        clientesDTO = List.of(clienteDTO);
    }

    @Test
    void obtenerTodosLosClientes_Debe_RetornarListOfClientes() throws Exception {
        // Arrange
        when(clienteService.buscarTodos()).thenReturn(clientesDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].idCliente").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nombres").value("John"));

        verify(clienteService, times(1)).buscarTodos();
    }

    @Test
    void obtenerClientePorId_Debe_RetornarCliente() throws Exception {
        // Arrange
        when(clienteService.buscarPorId(anyInt())).thenReturn(clienteDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idCliente").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombres").value("John"));

        verify(clienteService, times(1)).buscarPorId(1);
    }

    @Test
    void obtenerClientePorId_NoEncontrado_Retornar404() throws Exception {
        // Arrange
        when(clienteService.buscarPorId(anyInt())).thenThrow(new ResourceNotFoundException("Cliente", "id", 999));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clientes/999"))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).buscarPorId(anyInt());
    }

    @Test
    void crearCliente_Debe_RetornarClienteCreado() throws Exception {
        // Arrange
        when(clienteService.crear(any(ClienteDTO.class))).thenReturn(clienteDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idCliente").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombres").value("John"));

        verify(clienteService, times(1)).crear(any(ClienteDTO.class));
    }

    @Test
    void actualizarCliente_Debe_RetornarClienteActualizado() throws Exception {
        // Arrange
        when(clienteService.actualizar(any(ClienteDTO.class), anyInt())).thenReturn(clienteDTO);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idCliente").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombres").value("John"));

        verify(clienteService, times(1)).actualizar(any(ClienteDTO.class), eq(1));
    }

    @Test
    void actualizarCliente_NoEncontrado_Retornar404() throws Exception {
        // Arrange
        when(clienteService.actualizar(any(ClienteDTO.class), anyInt()))
                .thenThrow(new ResourceNotFoundException("Cliente", "id", 999));

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/clientes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteDTO)))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).actualizar(any(ClienteDTO.class), anyInt());
    }

    @Test
    void eliminarCliente_Debe_RetornarNoContent() throws Exception {
        // Arrange
        doNothing().when(clienteService).borrarPorId(anyInt());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).borrarPorId(anyInt());
    }

    @Test
    void eliminarCliente_NoEncontrado_Retornar404() throws Exception {
        // Arrange
        doThrow(new ResourceNotFoundException("Cliente", "id", 999))
                .when(clienteService).borrarPorId(anyInt());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clientes/999"))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).borrarPorId(anyInt());
    }
}
