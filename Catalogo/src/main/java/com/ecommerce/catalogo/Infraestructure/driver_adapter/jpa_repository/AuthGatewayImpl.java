package com.ecommerce.catalogo.Infraestructure.driver_adapter.jpa_repository;

import com.ecommerce.catalogo.Domain.model.gateway.AuthGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class AuthGatewayImpl implements AuthGateway {

    private final RestClient restClient;

    public AuthGatewayImpl(
            @Value("${auth.service.url}")
            String authServiceUrl
    ) {

        this.restClient = RestClient
                .builder()
                .baseUrl(authServiceUrl)
                .build();
    }

    @Override
    public void validarUsuario(String idUsuario) {

        restClient
                .get()
                .uri("/{idUsuario}", idUsuario)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            throw new IllegalArgumentException(
                                    "El usuario no existe en Auth"
                            );
                        }
                )
                .toBodilessEntity();
    }

    @Override
    public BigDecimal consultarSaldo(
            String idUsuario
    ) {

        return restClient
                .get()
                .uri(
                        "/{idUsuario}/saldo",
                        idUsuario
                )
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            throw new IllegalArgumentException(
                                    "No fue posible consultar el saldo"
                            );
                        }
                )
                .body(BigDecimal.class);
    }

    @Override
    public void descontarSaldo(
            String idUsuario,
            BigDecimal valor,
            String referenciaCompra
    ) {

        Map<String, Object> cuerpo = Map.of(
                "valor",
                valor,
                "referenciaCompra",
                referenciaCompra
        );

        restClient
                .put()
                .uri(
                        "/{idUsuario}/saldo/descontar",
                        idUsuario
                )
                .body(cuerpo)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            throw new IllegalArgumentException(
                                    "No fue posible descontar el saldo"
                            );
                        }
                )
                .toBodilessEntity();
    }

    @Override
    public void revertirDescuento(
            String idUsuario,
            String referenciaCompra
    ) {

        Map<String, Object> cuerpo = Map.of(
                "referenciaCompra",
                referenciaCompra
        );

        restClient
                .put()
                .uri(
                        "/{idUsuario}/saldo/revertir",
                        idUsuario
                )
                .body(cuerpo)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        (request, response) -> {
                            throw new IllegalArgumentException(
                                    "No fue posible revertir el saldo"
                            );
                        }
                )
                .toBodilessEntity();
    }
}