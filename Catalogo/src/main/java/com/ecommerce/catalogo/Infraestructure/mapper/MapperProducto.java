package com.ecommerce.catalogo.Infraestructure.mapper;

import com.ecommerce.catalogo.Domain.model.Producto;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.ProductoData;
import org.springframework.stereotype.Component;

@Component
public class MapperProducto {

    public ProductoData toData(Producto producto) {

        ProductoData productoData = new ProductoData();

        productoData.setIdProducto(producto.getIdProducto());
        productoData.setNombre(producto.getNombre());
        productoData.setDescripcion(producto.getDescripcion());
        productoData.setPrecio(producto.getPrecio());
        productoData.setStock(producto.getStock());
        productoData.setImagenUrl(producto.getImagenUrl());

        return productoData;
    }

    public Producto toDomain(ProductoData productoData) {

        Producto producto = new Producto();

        producto.setIdProducto(productoData.getIdProducto());
        producto.setNombre(productoData.getNombre());
        producto.setDescripcion(productoData.getDescripcion());
        producto.setPrecio(productoData.getPrecio());
        producto.setStock(productoData.getStock());
        producto.setImagenUrl(productoData.getImagenUrl());

        return producto;
    }
}