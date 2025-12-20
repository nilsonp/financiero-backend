package com.prueba.tecnica.financiero.dto;

import com.prueba.tecnica.financiero.model.Cliente;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDTO toDto(Cliente entidad);
    List<ClienteDTO> toListDto(List<Cliente> listClientes);

    Cliente toEntity(ClienteDTO cliente);
    List<Cliente> toListEntity(List<ClienteDTO> listClientes);
}
