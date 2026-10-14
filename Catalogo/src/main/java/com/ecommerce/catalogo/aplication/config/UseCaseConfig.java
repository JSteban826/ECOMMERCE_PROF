package com.ecommerce.catalogo.aplication.config;

import com.ecommerce.catalogo.Domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.Domain.usecase.ProductoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProductoUseCase productoUseCase(
            ProductoGateway productoGateway
    ) {

        return new ProductoUseCase(productoGateway);
    }
}