package com.prueba.tecnica.financiero.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto_financiero")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoFinanciero {

    @Id
    @Column(name = "numero_producto", nullable = false, length = 10, unique = true)
    private BigDecimal numeroProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @Column(name = "tipo_producto", nullable = false, length = 2)
    private String tipoProducto;

    @Column(name = "estado_producto", nullable = false, length = 1)
    private String estadoProducto;

    @Column(name = "saldo", nullable = false)
    private Double saldo;

    @Column(name = "execto_gmf", nullable = false)
    private Boolean exectoGmf;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
}
