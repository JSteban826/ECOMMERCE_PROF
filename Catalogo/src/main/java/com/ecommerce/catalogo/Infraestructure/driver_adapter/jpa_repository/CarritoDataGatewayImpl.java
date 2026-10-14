package com.ecommerce.catalogo.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.catalogo.Domain.model.Carrito;
import com.ecommerce.catalogo.Domain.model.EstadoCarrito;
import com.ecommerce.catalogo.Domain.model.gateway.CarritoGateway;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.CarritoData;
import com.ecommerce.catalogo.Infraestructure.mapper.MapperCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CarritoDataGatewayImpl
        implements CarritoGateway {

    private final CarritoDataJpaRepository carritoDataJpaRepository;
    private final MapperCarrito mapperCarrito;

    @Override
    public Carrito guardarCarrito(Carrito carrito) {

        CarritoData carritoData =
                mapperCarrito.toData(carrito);

        CarritoData carritoGuardado =
                carritoDataJpaRepository.save(carritoData);

        return mapperCarrito.toDomain(carritoGuardado);
    }

    @Override
    public Carrito actualizarCarrito(Carrito carrito) {

        CarritoData carritoData =
                carritoDataJpaRepository
                        .findById(carrito.getIdCarrito())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe un carrito con el ID: "
                                                + carrito.getIdCarrito()
                                )
                        );

        carritoData.setIdUsuario(
                carrito.getIdUsuario()
        );

        carritoData.setEstado(
                carrito.getEstado()
        );

        carritoData.setTotal(
                carrito.getTotal()
        );

        carritoData.setFechaCreacion(
                carrito.getFechaCreacion()
        );

        carritoData.setFechaCompra(
                carrito.getFechaCompra()
        );

        CarritoData carritoActualizado =
                carritoDataJpaRepository.save(carritoData);

        return mapperCarrito.toDomain(
                carritoActualizado
        );
    }

    @Override
    public Carrito buscarPorIdCarrito(Long idCarrito) {

        return carritoDataJpaRepository
                .findById(idCarrito)
                .map(mapperCarrito::toDomain)
                .orElse(null);
    }

    @Override
    public Carrito buscarCarritoActivoPorUsuario(
            String idUsuario
    ) {

        return carritoDataJpaRepository
                .findByIdUsuarioAndEstado(
                        idUsuario,
                        EstadoCarrito.ACTIVO
                )
                .map(mapperCarrito::toDomain)
                .orElse(null);
    }
}