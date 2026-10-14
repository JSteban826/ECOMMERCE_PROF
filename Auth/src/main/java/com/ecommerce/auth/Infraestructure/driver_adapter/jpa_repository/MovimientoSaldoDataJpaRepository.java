package com.ecommerce.auth.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.auth.Infraestructure.driver_adapter.MovimientoSaldoData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovimientoSaldoDataJpaRepository
        extends JpaRepository<MovimientoSaldoData, Long> {

    Optional<MovimientoSaldoData> findByReferenciaCompra(
            String referenciaCompra
    );

    boolean existsByReferenciaCompra(
            String referenciaCompra
    );
}