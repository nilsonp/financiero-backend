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
        List<ClienteDTO> listDto = clienteMapper.toListDto(clientes);
        log.debug("total clientes dto: {}",listDto.size());
        return listDto;
    }

    @Override
    public ClienteDTO buscarPorId(Integer id) {
        log.debug("consultar cliente por id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("cliente no encontrado por id: {}", id);
                    return new ResourceNotFoundException("Cliente", "id", id);
                });

        return clienteMapper.toDto(cliente);
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
        this.validaSiExisteClientePorId(id);
        Cliente entity = clienteRepository.save(clienteMapper.toEntity(dto));
        log.debug("cliente actualizado id: {}", entity.getIdCliente());
        return clienteMapper.toDto(entity);

    }

    @Override
    public void borrarPorId(Integer id) {
        log.debug("eliminar cliente id: {}", id);
        this.validaSiExisteClientePorId(id);
        clienteRepository.deleteById(id);
        log.debug("cliente eliminado id: {}", id);
    }

    private void validaSiExisteClientePorId(Integer idCliente) {
        if(!clienteRepository.existsById(idCliente)) {
            log.error("cliente no encontrado id: {}", idCliente);
            throw new ResourceNotFoundException("Cliente", "id", idCliente);
        }
    }
}
