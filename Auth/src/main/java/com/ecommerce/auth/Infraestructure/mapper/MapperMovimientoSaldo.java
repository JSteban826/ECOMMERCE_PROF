package com.ecommerce.auth.Infraestructure.mapper;

import com.ecommerce.auth.Domain.model.MovimientoSaldo;
import com.ecommerce.auth.Infraestructure.driver_adapter.MovimientoSaldoData;
import com.ecommerce.auth.Infraestructure.driver_adapter.UsuarioData;
import org.springframework.stereotype.Component;

@Component
public class MapperMovimientoSaldo {

    public MovimientoSaldoData toData(
            MovimientoSaldo movimientoSaldo,
            UsuarioData usuarioData
    ) {

        MovimientoSaldoData movimientoSaldoData =
                new MovimientoSaldoData();

        movimientoSaldoData.setIdMovimiento(
                movimientoSaldo.getIdMovimiento()
        );

        movimientoSaldoData.setUsuario(
                usuarioData
        );

        movimientoSaldoData.setValor(
                movimientoSaldo.getValor()
        );

        movimientoSaldoData.setReferenciaCompra(
                movimientoSaldo.getReferenciaCompra()
        );

        movimientoSaldoData.setEstado(
                movimientoSaldo.getEstado()
        );

        return movimientoSaldoData;
    }

    public MovimientoSaldo toDomain(
            MovimientoSaldoData movimientoSaldoData
    ) {

        MovimientoSaldo movimientoSaldo =
                new MovimientoSaldo();

        movimientoSaldo.setIdMovimiento(
                movimientoSaldoData.getIdMovimiento()
        );

        movimientoSaldo.setIdUsuario(
                movimientoSaldoData
                        .getUsuario()
                        .getIdUsuario()
        );

        movimientoSaldo.setValor(
                movimientoSaldoData.getValor()
        );

        movimientoSaldo.setReferenciaCompra(
                movimientoSaldoData.getReferenciaCompra()
        );

        movimientoSaldo.setEstado(
                movimientoSaldoData.getEstado()
        );

        return movimientoSaldo;
    }
}