package com.prueba.tecnica.financiero.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@Getter
@Setter
public class TransaccionDTO {

    private UUID transaccionId;

    @NotEmpty(message = "Tipo de transaccion es obligatorio")
    private String tipoTransaccion;

    @NotEmpty(message = "Cuenta Origen es obligatorio")
    private BigInteger cuentaOrigen;

    private BigInteger cuentaDestino;

    @NotEmpty(message = "Monto es obligatorio")
    private BigDecimal monto;

    private LocalDateTime fechaTransaccion;
}
