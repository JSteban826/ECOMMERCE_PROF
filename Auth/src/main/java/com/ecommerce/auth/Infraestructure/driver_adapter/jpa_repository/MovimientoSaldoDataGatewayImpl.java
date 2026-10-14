package com.ecommerce.auth.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.auth.Domain.model.MovimientoSaldo;
import com.ecommerce.auth.Domain.model.gateway.MovimientoSaldoGateway;
import com.ecommerce.auth.Infraestructure.driver_adapter.MovimientoSaldoData;
import com.ecommerce.auth.Infraestructure.driver_adapter.UsuarioData;
import com.ecommerce.auth.Infraestructure.mapper.MapperMovimientoSaldo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovimientoSaldoDataGatewayImpl
        implements MovimientoSaldoGateway {

    private final MovimientoSaldoDataJpaRepository
            movimientoSaldoDataJpaRepository;

    private final UsuarioDataJpaRepository
            usuarioDataJpaRepository;

    private final MapperMovimientoSaldo
            mapperMovimientoSaldo;

    @Override
    public MovimientoSaldo guardarMovimiento(
            MovimientoSaldo movimientoSaldo
    ) {

        UsuarioData usuarioData =
                obtenerUsuarioData(
                        movimientoSaldo.getIdUsuario()
                );

        MovimientoSaldoData movimientoSaldoData =
                mapperMovimientoSaldo.toData(
                        movimientoSaldo,
                        usuarioData
                );

        MovimientoSaldoData movimientoGuardado =
                movimientoSaldoDataJpaRepository.save(
                        movimientoSaldoData
                );

        return mapperMovimientoSaldo.toDomain(
                movimientoGuardado
        );
    }

    @Override
    public MovimientoSaldo buscarPorReferenciaCompra(
            String referenciaCompra
    ) {

        return movimientoSaldoDataJpaRepository
                .findByReferenciaCompra(
                        referenciaCompra
                )
                .map(mapperMovimientoSaldo::toDomain)
                .orElse(null);
    }

    @Override
    public boolean existeReferenciaCompra(
            String referenciaCompra
    ) {

        return movimientoSaldoDataJpaRepository
                .existsByReferenciaCompra(
                        referenciaCompra
                );
    }

    private UsuarioData obtenerUsuarioData(
            String idUsuario
    ) {

        return usuarioDataJpaRepository
                .findById(idUsuario)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un usuario con el ID: "
                                        + idUsuario
                        )
                );
    }
}