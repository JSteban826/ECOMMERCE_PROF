package com.ecommerce.catalogo.aplication.config
;

import com.ecommerce.catalogo.Domain.model.gateway.AuthGateway;
import com.ecommerce.catalogo.Domain.model.gateway.CarritoGateway;
import com.ecommerce.catalogo.Domain.model.gateway.ItemCarritoGateway;
import com.ecommerce.catalogo.Domain.model.gateway.ProductoGateway;
import com.ecommerce.catalogo.Domain.usecase.CarritoUseCase;
import com.ecommerce.catalogo.Domain.usecase.ProductoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public ProductoUseCase productoUseCase(
            ProductoGateway productoGateway
    ) {

        return new ProductoUseCase(
                productoGateway
        );
    }

    @Bean
    public CarritoUseCase carritoUseCase(
            CarritoGateway carritoGateway,
            ItemCarritoGateway itemCarritoGateway,
            ProductoGateway productoGateway,
            AuthGateway authGateway) {

        return new CarritoUseCase(
                carritoGateway,
                itemCarritoGateway,
                productoGateway,
                authGateway
        );
    }
}