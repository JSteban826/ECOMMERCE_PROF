package com.ecommerce.auth.Infraestructure.driver_adapter;

import com.ecommerce.auth.Domain.model.EstadoMovimientoSaldo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovimientoSaldoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private UsuarioData usuario;

    @Column(
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal valor;

    @Column(
            nullable = false,
            unique = true
    )
    private String referenciaCompra;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMovimientoSaldo estado;
}
