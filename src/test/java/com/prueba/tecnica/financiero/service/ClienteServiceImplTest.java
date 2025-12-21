package com.prueba.tecnica.financiero.service;

import com.prueba.tecnica.financiero.dto.ClienteDTO;
import com.prueba.tecnica.financiero.dto.ClienteMapper;
import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.model.Cliente;
import com.prueba.tecnica.financiero.repository.ClienteRepository;
import lombok.extern.log4j.Log4j2;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Log4j2
@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper = Mappers.getMapper(ClienteMapper.class);

    private ClienteServiceImpl clienteService;

    private List<Cliente> listDeClientes;

    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() throws Exception {
        clienteService = new ClienteServiceImpl(clienteRepository, clienteMapper);
        this.listDeClientes = Instancio.ofList(Cliente.class).size(11).create();

        cliente = Instancio.of(Cliente.class)
                .create();

        clienteDTO = clienteMapper.toDto(cliente);
    }

    @Test
    void buscarTodos_DebeRetornar_ListDeClientes() {
        // Arrange
        given(clienteRepository.findAll()).willReturn(this.listDeClientes);

        // Act
        List<ClienteDTO> result = clienteService.buscarTodos();

        // Assert
        assertNotNull(result);
        assertEquals(11, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_Debe_ReturnarCliente() {
        // Arrange
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.of(cliente));

        // Act
        Optional<ClienteDTO> result = Optional.ofNullable(clienteService.buscarPorId(cliente.getIdCliente()));

        // Assert
        assertTrue(result.isPresent());
        assertEquals(clienteDTO.getIdCliente(), result.get().getIdCliente());
        verify(clienteRepository, times(1)).findById(anyInt());
    }

    @Test
    void buscarPorId_NoEncontrado_LanzarResourceNotFoundException() {
        // Arrange
        when(clienteRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarPorId(999));
        verify(clienteRepository, times(1)).findById(999);
    }

    @Test
    void crear_ReturnaClienteCreado() {
        // Arrange
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteDTO result = clienteService.crear(clienteDTO);

        // Assert
        assertNotNull(result);
        assertEquals(clienteDTO.getIdCliente(), result.getIdCliente());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void actualizar_ReturnaClienteActualizado() {
        // Arrange
        when(clienteRepository.existsById(anyInt())).thenReturn(Boolean.TRUE);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        // Act
        ClienteDTO result = clienteService.actualizar(clienteDTO, cliente.getIdCliente());

        // Assert
        assertNotNull(result);
        assertEquals(clienteDTO.getIdCliente(), result.getIdCliente());
        verify(clienteRepository, times(1)).existsById(anyInt());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void actualizar_NoEncontrado_LanzarResourceNotFoundException() {
        // Arrange
        when(clienteRepository.existsById(anyInt())).thenReturn(Boolean.FALSE);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.actualizar(clienteDTO, 999));
        verify(clienteRepository, times(1)).existsById(anyInt());
    }

    @Test
    void borrarPorId_EliminaCliente() {
        // Arrange
        when(clienteRepository.existsById(anyInt())).thenReturn(Boolean.TRUE);
        doNothing().when(clienteRepository).deleteById(anyInt());

        // Act
        clienteService.borrarPorId(cliente.getIdCliente());

        // Assert
        verify(clienteRepository, times(1)).existsById(anyInt());
        verify(clienteRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void borrarPorId_NoEncontrado_LanzarResourceNotFoundException() {
        // Arrange
        when(clienteRepository.existsById(anyInt())).thenReturn(Boolean.FALSE);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.borrarPorId(999));
        verify(clienteRepository, times(1)).existsById(anyInt());
    }
}
