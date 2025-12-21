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

    @NotEmpty(message = "Tipo identificacion es obligatorio")
    private String tipoIdentificacion;

    @NotEmpty(message = "Numero de identificacion es obligatorio")
    private String numeroIdentificacion;

    @NotEmpty(message = "Nombres es obligatorio")
    private String nombres;

    @NotEmpty(message = "Apellidos es obligatorio")
    private String apellidos;

    @NotEmpty(message = "Correo electronico es obligatorio")
    private String correoElectronico;

    @NotEmpty(message = "Fecha nacimiento es obligatorio")
    private LocalDate fechaNacimiento;

}
