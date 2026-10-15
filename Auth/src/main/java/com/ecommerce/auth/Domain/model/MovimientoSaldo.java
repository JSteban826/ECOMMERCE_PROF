package com.ecommerce.auth.Domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MovimientoSaldo {

    private Long idMovimiento;
    private String idUsuario;
    private BigDecimal valor;
    private String referenciaCompra;
    private EstadoMovimientoSaldo estado;
}