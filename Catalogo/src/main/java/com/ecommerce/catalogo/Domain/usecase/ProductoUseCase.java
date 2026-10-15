package com.ecommerce.catalogo.Domain.usecase;

import com.ecommerce.catalogo.Domain.model.Producto;
import com.ecommerce.catalogo.Domain.model.gateway.ProductoGateway;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductoUseCase {

    private final ProductoGateway productoGateway;

    private void validarProducto(Producto producto) {

        Producto productoValidado = Optional.ofNullable(producto)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Los datos del producto son obligatorios"
                        )
                );

        validarNombre(productoValidado.getNombre());
        validarDescripcion(productoValidado.getDescripcion());
        validarPrecio(productoValidado.getPrecio());
        validarStock(productoValidado.getStock());
        validarImagenUrl(productoValidado.getImagenUrl());
    }

    public Producto guardarProducto(Producto producto) {

        validarProducto(producto);

        return productoGateway.guardarProducto(producto);
    }

    public Producto actualizarProducto(Producto producto) {

        Producto productoValidado = Optional.ofNullable(producto)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Los datos del producto son obligatorios"
                        )
                );

        validarIdProducto(
                productoValidado.getIdProducto()
        );

        validarProducto(productoValidado);

        buscarProducto(
                productoValidado.getIdProducto()
        );

        return productoGateway.actualizarProducto(
                productoValidado
        );
    }

    public Producto buscarProducto(Long idProducto) {

        validarIdProducto(idProducto);

        return Optional.ofNullable(
                        productoGateway.buscarPorIdProducto(
                                idProducto
                        )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe un producto con el ID: "
                                        + idProducto
                        )
                );
    }

    public List<Producto> listarProductos() {

        return productoGateway.listarProductos();
    }

    public void eliminarProducto(Long idProducto) {

        validarIdProducto(idProducto);

        buscarProducto(idProducto);

        productoGateway.eliminarPorIdProducto(
                idProducto
        );
    }

    private void validarIdProducto(Long idProducto) {

        Optional.ofNullable(idProducto)
                .filter(valor -> valor > 0)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El ID del producto debe ser mayor que cero"
                        )
                );
    }

    private void validarNombre(String nombre) {

        Optional.ofNullable(nombre)
                .filter(valor -> !valor.isBlank())
                .filter(valor -> valor.length() >= 3)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El nombre debe tener mínimo 3 caracteres"
                        )
                );
    }

    private void validarDescripcion(String descripcion) {

        Optional.ofNullable(descripcion)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La descripción del producto es obligatoria"
                        )
                );
    }

    private void validarPrecio(BigDecimal precio) {

        Optional.ofNullable(precio)
                .filter(valor ->
                        valor.compareTo(BigDecimal.ZERO) > 0
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El precio debe ser mayor que cero"
                        )
                );
    }

    private void validarStock(Integer stock) {

        Optional.ofNullable(stock)
                .filter(valor -> valor >= 0)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El stock no puede ser negativo"
                        )
                );
    }

    private void validarImagenUrl(String imagenUrl) {

        Optional.ofNullable(imagenUrl)
                .filter(valor -> !valor.isBlank())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La URL de la imagen es obligatoria"
                        )
                );
    }
}