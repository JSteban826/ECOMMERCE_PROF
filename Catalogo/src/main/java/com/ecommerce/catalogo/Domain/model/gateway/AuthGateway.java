package com.ecommerce.catalogo.Domain.model.gateway;

import java.math.BigDecimal;

public interface AuthGateway {

    void validarUsuario(String idUsuario);

    BigDecimal consultarSaldo(String idUsuario);

    void descontarSaldo(
            String idUsuario,
            BigDecimal valor,
            String referenciaCompra
    );

    void revertirDescuento(
            String idUsuario,
            String referenciaCompra
    );
}