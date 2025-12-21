package com.prueba.tecnica.financiero.service;

import com.prueba.tecnica.financiero.dto.ClienteDTO;
import com.prueba.tecnica.financiero.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<ClienteDTO> buscarTodos();
    ClienteDTO buscarPorId(Integer id);
    ClienteDTO crear(ClienteDTO dto);
    ClienteDTO actualizar(ClienteDTO dto, Integer id);
    void borrarPorId(Integer id);
}
