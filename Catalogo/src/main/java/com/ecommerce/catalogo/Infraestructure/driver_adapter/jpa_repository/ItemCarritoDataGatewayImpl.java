package com.ecommerce.catalogo.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.catalogo.Domain.model.ItemCarrito;
import com.ecommerce.catalogo.Domain.model.gateway.ItemCarritoGateway;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.CarritoData;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.ItemCarritoData;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.ProductoData;
import com.ecommerce.catalogo.Infraestructure.mapper.MapperItemCarrito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemCarritoDataGatewayImpl
        implements ItemCarritoGateway {

    private final ItemCarritoDataJpaRepository
            itemCarritoDataJpaRepository;

    private final CarritoDataJpaRepository
            carritoDataJpaRepository;

    private final ProductoDataJpaRepository
            productoDataJpaRepository;

    private final MapperItemCarrito
            mapperItemCarrito;

    @Override
    public ItemCarrito guardarItem(
            ItemCarrito itemCarrito
    ) {

        CarritoData carritoData =
                obtenerCarritoData(
                        itemCarrito.getIdCarrito()
                );

        ProductoData productoData =
                obtenerProductoData(
                        itemCarrito.getIdProducto()
                );

        ItemCarritoData itemCarritoData =
                mapperItemCarrito.toData(
                        itemCarrito,
                        carritoData,
                        productoData
                );

        ItemCarritoData itemGuardado =
                itemCarritoDataJpaRepository.save(
                        itemCarritoData
                );

        return mapperItemCarrito.toDomain(
                itemGuardado
        );
    }

    @Override
    public ItemCarrito actualizarItem(
            ItemCarrito itemCarrito
    ) {

        CarritoData carritoData =
                obtenerCarritoData(
                        itemCarrito.getIdCarrito()
                );

        ProductoData productoData =
                obtenerProductoData(
                        itemCarrito.getIdProducto()
                );

        ItemCarritoData itemCarritoData =
                mapperItemCarrito.toData(
                        itemCarrito,
                        carritoData,
                        productoData
                );

        ItemCarritoData itemActualizado =
                itemCarritoDataJpaRepository.save(
                        itemCarritoData
                );

        return mapperItemCarrito.toDomain(
                itemActualizado
        );
    }

    @Override
    public ItemCarrito buscarPorCarritoYProducto(
            Long idCarrito,
            Long idProducto
    ) {

        return itemCarritoDataJpaRepository
                .findByCarritoIdCarritoAndProductoIdProducto(
                        idCarrito,
                        idProducto
                )
                .map(mapperItemCarrito::toDomain)
                .orElse(null);
    }

    @Override
    public List<ItemCarrito> listarPorCarrito(
            Long idCarrito
    ) {

        return itemCarritoDataJpaRepository
                .findByCarritoIdCarrito(idCarrito)
                .stream()
                .map(mapperItemCarrito::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void eliminarPorCarritoYProducto(
            Long idCarrito,
            Long idProducto
    ) {

        itemCarritoDataJpaRepository
                .deleteByCarritoIdCarritoAndProductoIdProducto(
                        idCarrito,
                        idProducto
                );
    }

    private CarritoData obtenerCarritoData(
            Long idCarrito
    ) {

        return carritoDataJpaRepository
                .findById(idCarrito)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un carrito con el ID: "
                                        + idCarrito
                        )
                );
    }

    private ProductoData obtenerProductoData(
            Long idProducto
    ) {

        return productoDataJpaRepository
                .findById(idProducto)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un producto con el ID: "
                                        + idProducto
                        )
                );
    }
}