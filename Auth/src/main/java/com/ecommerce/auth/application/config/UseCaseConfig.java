package com.ecommerce.auth.application.config;

import com.ecommerce.auth.Domain.model.gateway.EncrypterGateway;
import com.ecommerce.auth.Domain.model.gateway.MovimientoSaldoGateway;
import com.ecommerce.auth.Domain.model.gateway.UsuarioGateway;
import com.ecommerce.auth.Domain.usecase.UsuarioUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public UsuarioUseCase usuarioUseCase(
            UsuarioGateway usuarioGateway,
            EncrypterGateway encrypterGateway,
            MovimientoSaldoGateway movimientoSaldoGateway
    ) {

        return new UsuarioUseCase(
                usuarioGateway,
                encrypterGateway,
                movimientoSaldoGateway
        );
    }
}