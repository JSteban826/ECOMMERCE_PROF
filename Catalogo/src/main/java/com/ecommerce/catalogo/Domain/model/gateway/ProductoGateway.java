package com.ecommerce.catalogo.Domain.model.gateway;

import com.ecommerce.catalogo.Domain.model.Producto;

import java.util.List;

public interface ProductoGateway {

    Producto guardarProducto(Producto producto);

    Producto actualizarProducto(Producto producto);

    Producto buscarPorIdProducto(Long idProducto);

    List<Producto> listarProductos();

    void eliminarPorIdProducto(Long idProducto);
}