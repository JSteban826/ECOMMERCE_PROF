package com.ecommerce.auth.Domain.model.gateway;

import com.ecommerce.auth.Domain.model.MovimientoSaldo;

public interface MovimientoSaldoGateway {

    MovimientoSaldo guardarMovimiento(
            MovimientoSaldo movimientoSaldo
    );

    MovimientoSaldo buscarPorReferenciaCompra(
            String referenciaCompra
    );

    boolean existeReferenciaCompra(
            String referenciaCompra
    );
}