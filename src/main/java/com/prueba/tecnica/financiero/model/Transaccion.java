package com.prueba.tecnica.financiero.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaccion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaccion_id", nullable = false, unique = true)
    private UUID transaccionId;

    @Column(name = "tipo_transaccion", nullable = false)
    private String tipoTransaccion;

    @Column(name = "cuenta_origen", nullable = false, length = 10)
    private BigDecimal cuentaOrigen;

    @Column(name = "cuenta_destino", length = 10)
    private BigDecimal cuentaDestino;

    @Column(name = "monto", nullable = false)
    private Double monto;

    @Column(name = "fecha_transaccion", nullable = false, updatable = false)
    private LocalDateTime fechaTransaccion;

    @PrePersist
    protected void onCreate() {
        fechaTransaccion = LocalDateTime.now();
    }
}
