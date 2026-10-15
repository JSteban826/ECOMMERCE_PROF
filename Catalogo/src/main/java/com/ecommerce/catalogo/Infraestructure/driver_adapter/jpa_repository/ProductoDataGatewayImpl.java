package com.ecommerce.catalogo.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.catalogo.Domain.model.Producto;
import com.ecommerce.catalogo.Domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.ProductoData;
import com.ecommerce.catalogo.Infraestructure.mapper.MapperProducto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductoDataGatewayImpl implements ProductoGateway {

    private final ProductoDataJpaRepository productoDataJpaRepository;
    private final MapperProducto mapperProducto;

    @Override
    public Producto guardarProducto(Producto producto) {

        ProductoData productoData =
                mapperProducto.toData(producto);

        ProductoData productoGuardado =
                productoDataJpaRepository.save(productoData);

        return mapperProducto.toDomain(productoGuardado);
    }

    @Override
    public Producto actualizarProducto(Producto producto) {

        ProductoData productoData =
                mapperProducto.toData(producto);

        ProductoData productoActualizado =
                productoDataJpaRepository.save(productoData);

        return mapperProducto.toDomain(productoActualizado);
    }

    @Override
    public Producto buscarPorIdProducto(Long idProducto) {

        return productoDataJpaRepository.findById(idProducto)
                .map(mapperProducto::toDomain)
                .orElse(null);
    }

    @Override
    public List<Producto> listarProductos() {

        return productoDataJpaRepository.findAll()
                .stream()
                .map(mapperProducto::toDomain)
                .toList();
    }

    @Override
    public void eliminarPorIdProducto(Long idProducto) {

        productoDataJpaRepository.deleteById(idProducto);
    }
}