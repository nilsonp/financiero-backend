package com.prueba.tecnica.financiero.dto;

import com.prueba.tecnica.financiero.model.ProductoFinanciero;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoFinancieroMapper {
    @Mapping(target = "idCliente", source = "cliente.idCliente")
    ProductoFinancieroDTO toDto(ProductoFinanciero entidad);

    @Mapping(target = "cliente.idCliente", source = "idCliente")
    ProductoFinanciero toEntity(ProductoFinancieroDTO dto);

    List<ProductoFinancieroDTO> toListDto(List<ProductoFinanciero> listProductos);
    List<ProductoFinanciero> toListEntity(List<ProductoFinancieroDTO> listProductos);
}
