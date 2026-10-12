package com.ecommerce.auth.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.auth.Infraestructure.driver_adapter.UsuarioData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioDataJpaRepository extends JpaRepository<UsuarioData, String> {

    Optional<UsuarioData> findByCorreo(String email);
}