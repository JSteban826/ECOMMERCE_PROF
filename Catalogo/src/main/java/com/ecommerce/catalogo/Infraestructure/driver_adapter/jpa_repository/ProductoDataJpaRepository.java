package com.ecommerce.catalogo.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.catalogo.Infraestructure.driver_adapter.ProductoData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoDataJpaRepository
        extends JpaRepository<ProductoData, Long> {
}