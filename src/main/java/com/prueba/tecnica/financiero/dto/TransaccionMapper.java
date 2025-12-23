package com.prueba.tecnica.financiero.dto;

import com.prueba.tecnica.financiero.model.Transaccion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransaccionMapper {

    @Mapping(source = "cuentaOrigen.numeroProducto", target = "cuentaOrigen")
    @Mapping(source = "cuentaDestino.numeroProducto", target = "cuentaDestino")
    TransaccionDTO toDto(Transaccion entity);

    @Mapping(source = "cuentaOrigen", target = "cuentaOrigen.numeroProducto")
    @Mapping(source = "cuentaDestino", target = "cuentaDestino.numeroProducto")
    Transaccion toEntity(TransaccionDTO dto);

    List<TransaccionDTO> toListDto(List<Transaccion> listEntities);

}
