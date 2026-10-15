package com.ecommerce.catalogo.Domain.model.gateway;

import com.ecommerce.catalogo.Domain.model.ItemCarrito;

import java.util.List;

public interface ItemCarritoGateway {

    ItemCarrito guardarItem(
            ItemCarrito itemCarrito
    );

    ItemCarrito actualizarItem(
            ItemCarrito itemCarrito
    );

    ItemCarrito buscarPorCarritoYProducto(
            Long idCarrito,
            Long idProducto
    );

    List<ItemCarrito> listarPorCarrito(
            Long idCarrito
    );

    void eliminarPorCarritoYProducto(
            Long idCarrito,
            Long idProducto
    );
}