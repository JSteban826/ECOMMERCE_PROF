package com.ecommerce.catalogo.Domain.model.gateway;

import com.ecommerce.catalogo.Domain.model.Carrito;

public interface CarritoGateway {

    Carrito guardarCarrito(Carrito carrito);

    Carrito actualizarCarrito(Carrito carrito);

    Carrito buscarPorIdCarrito(Long idCarrito);

    Carrito buscarCarritoActivoPorUsuario(
            String idUsuario
    );
}