package com.prueba.tecnica.financiero.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotBlank(message = "client_id es requerido")
    private String clientId;

    @NotBlank(message = "client_secret es requerido")
    private String clientSecret;

}
