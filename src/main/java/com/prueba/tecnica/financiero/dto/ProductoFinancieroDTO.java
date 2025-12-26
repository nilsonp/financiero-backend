package com.prueba.tecnica.financiero.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoFinancieroDTO {
    private BigInteger numeroProducto;

    @NotEmpty(message = "Cliente es obligatorio")
    private Integer idCliente;

    @NotEmpty(message = "Tipo de producto es obligatorio")
    private String tipoProducto;

    private String estadoProducto;

    @Min(value = 0, message = "Saldo valor minimo cero")
    private BigDecimal saldo;

    @NotEmpty(message = "Excento GMF es obligatorio")
    private Boolean exectoGmf;
}
