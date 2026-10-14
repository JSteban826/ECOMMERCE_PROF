package com.ecommerce.catalogo.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.catalogo.Infraestructure.driver_adapter.ItemCarritoData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemCarritoDataJpaRepository
        extends JpaRepository<ItemCarritoData, Long> {

    List<ItemCarritoData> findByCarritoIdCarrito(
            Long idCarrito
    );

    Optional<ItemCarritoData>
    findByCarritoIdCarritoAndProductoIdProducto(
            Long idCarrito,
            Long idProducto
    );

    void deleteByCarritoIdCarritoAndProductoIdProducto(
            Long idCarrito,
            Long idProducto
    );
}