package com.prueba.tecnica.financiero.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Data
@Builder
public class ClienteDTO {

    private Integer idCliente;

    @NotEmpty
    private String tipoIdentificacion;

    @NotEmpty
    private String numeroIdentificacion;

    @NotEmpty
    private String nombres;

    @NotEmpty
    private String apellidos;

    @NotEmpty
    private String correoElectronico;

    @NotEmpty
    private LocalDate fechaNacimiento;

}
