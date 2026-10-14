package com.ecommerce.catalogo.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.catalogo.Domain.model.EstadoCarrito;
import com.ecommerce.catalogo.Infraestructure.driver_adapter.CarritoData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoDataJpaRepository
        extends JpaRepository<CarritoData, Long> {

    Optional<CarritoData> findByIdUsuarioAndEstado(
            String idUsuario,
            EstadoCarrito estado
    );
}