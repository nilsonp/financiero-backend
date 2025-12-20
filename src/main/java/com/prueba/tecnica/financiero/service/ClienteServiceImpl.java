package com.prueba.tecnica.financiero.service;

import com.prueba.tecnica.financiero.dto.ClienteDTO;
import com.prueba.tecnica.financiero.dto.ClienteMapper;
import com.prueba.tecnica.financiero.exception.ResourceNotFoundException;
import com.prueba.tecnica.financiero.model.Cliente;
import com.prueba.tecnica.financiero.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public List<ClienteDTO> buscarTodos() {
        log.debug("consultar todos los clientes");
        List<Cliente> clientes = clienteRepository.findAll();
        log.debug("total clientes todos: {}", clientes.size());

        return clienteMapper.toListDto(clientes);
    }

    @Override
    public Optional<ClienteDTO> buscarPorId(Integer id) {
        log.debug("consultar cliente por id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("cliente no encontrado por id: {}", id);
                    return new ResourceNotFoundException("Cliente", "id", id);
                });

        return Optional.of(clienteMapper.toDto(cliente));
    }

    @Override
    public ClienteDTO crear(ClienteDTO dto) {
        log.debug("crear cliente: {}", dto.getNumeroIdentificacion());
        Cliente entity = clienteRepository.save(clienteMapper.toEntity(dto));
        log.debug("cliente creado id:", entity.getIdCliente());
        return clienteMapper.toDto(entity);
    }

    @Override
    public ClienteDTO actualizar(ClienteDTO dto, Integer id) {
        log.debug("actualizar cliente: {}", id);
        Cliente entity = clienteRepository.findById(dto.getIdCliente()).orElseThrow(() -> {
            log.error("actualizar cliente no encontrado: {}", id);
            return new ResourceNotFoundException("Cliente", "id", id);
        });
        clienteRepository.save(clienteMapper.toEntity(dto));
        log.debug("cliente actualizado id: {}", entity.getIdCliente());
        return clienteMapper.toDto(entity);

    }

    @Override
    public void borrarPorId(Integer id) {
        log.debug("eliminar cliente id: {}", id);
        clienteRepository.findById(id).orElseThrow(() -> {
            log.error("eliminar cliente no encontrado: {}", id);
            return new ResourceNotFoundException("Cliente", "id", id);
        });
        clienteRepository.deleteById(id);
        log.debug("cliente eliminado id: {}", id);
    }
}
