package com.ecommerce.catalogo.Infraestructure.driver_adapter.jpa_repository;
import com.ecommerce.catalogo.Domain.exception.ErrorAplicacion;
import com.ecommerce.catalogo.Domain.exception.ErrorAplicacion.Tipo;
import com.ecommerce.catalogo.Domain.model.gateway.AuthGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class AuthGatewayImpl implements AuthGateway {
    private final RestClient restClient;
    public AuthGatewayImpl(@Value("${auth.service.url}") String url) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);
        restClient = RestClient.builder().baseUrl(url).requestFactory(factory).build();
    }
    @Override public void validarUsuario(String idUsuario) {
        ejecutar(() -> {
            restClient.get().uri("/{id}", idUsuario).retrieve().toBodilessEntity();
            return null;
        });
    }
    @Override public BigDecimal consultarSaldo(String idUsuario) {
        return ejecutar(() -> {
            BigDecimal saldo = restClient.get().uri("/{id}/saldo", idUsuario)
                    .retrieve().body(BigDecimal.class);
            if (saldo == null || saldo.signum() < 0) {
                throw new ErrorAplicacion(Tipo.RESPUESTA_INVALIDA, "AUTH_RESPUESTA_INVALIDA",
                        "Auth devolvió un saldo ausente o inválido");
            }
            return saldo;
        });
    }
    @Override public void descontarSaldo(String idUsuario, BigDecimal valor, String referenciaCompra) {
        ejecutar(() -> {
            restClient.put().uri("/{id}/saldo/descontar", idUsuario)
                    .body(Map.of("valor", valor, "referenciaCompra", referenciaCompra))
                    .retrieve().toBodilessEntity();
            return null;
        });
    }
    @Override public void revertirDescuento(String idUsuario, String referenciaCompra) {
        ejecutar(() -> {
            restClient.put().uri("/{id}/saldo/revertir", idUsuario)
                    .body(Map.of("referenciaCompra", referenciaCompra))
                    .retrieve().toBodilessEntity();
            return null;
        });
    }
    private <T> T ejecutar(Supplier<T> operacion) {
        try {
            return operacion.get();
        } catch (RestClientResponseException ex) {
            throw traducir(ex);
        } catch (ResourceAccessException ex) {
            for (Throwable causa = ex; causa != null; causa = causa.getCause()) {
                if (causa instanceof SocketTimeoutException) {
                    throw new ErrorAplicacion(Tipo.TIEMPO_ESPERA, "AUTH_TIEMPO_ESPERA",
                            "Auth no respondió a tiempo. Si era una compra, su resultado no está confirmado; no la repitas automáticamente", ex);
                }
                if (causa instanceof ConnectException) {
                    throw new ErrorAplicacion(Tipo.NO_DISPONIBLE, "AUTH_NO_DISPONIBLE",
                            "No se pudo establecer conexión con Auth", ex);
                }
            }
            throw new ErrorAplicacion(Tipo.INTERNO, "AUTH_ERROR_COMUNICACION",
                    "No fue posible completar la comunicación con Auth", ex);
        } catch (RestClientException ex) {
            throw new ErrorAplicacion(Tipo.RESPUESTA_INVALIDA, "AUTH_RESPUESTA_INVALIDA",
                    "No fue posible interpretar la respuesta de Auth", ex);
        }
    }
    private ErrorAplicacion traducir(RestClientResponseException ex) {
        String codigo = "";
        try {
            Map<?, ?> problema = ex.getResponseBodyAs(Map.class);
            if (problema != null && problema.get("code") instanceof String valor) codigo = valor;
        } catch (RuntimeException ignored) {
            // Una respuesta no estructurada no se interpreta como un error conocido del usuario.
        }
        int estado = ex.getStatusCode().value();
        if (estado == 404 && "USUARIO_NO_ENCONTRADO".equals(codigo)) {
            return new ErrorAplicacion(Tipo.NO_ENCONTRADO, codigo, "Usuario no encontrado en Auth");
        }
        if (estado == 404 && "MOVIMIENTO_NO_ENCONTRADO".equals(codigo)) {
            return new ErrorAplicacion(Tipo.NO_ENCONTRADO, codigo, "Movimiento de saldo no encontrado");
        }
        if (estado == 409) {
            String mensaje = switch(codigo) {
                case "SALDO_INSUFICIENTE" -> "Saldo insuficiente para realizar la compra";
                case "REFERENCIA_YA_PROCESADA" -> "La referencia de compra ya fue procesada";
                case "MOVIMIENTO_YA_REVERTIDO" -> "El descuento ya fue revertido";
                case "MOVIMIENTO_USUARIO_INCOMPATIBLE" -> "La referencia no corresponde al usuario solicitado";
                default -> null;
            };
            if (mensaje != null) return new ErrorAplicacion(Tipo.CONFLICTO, codigo, mensaje);
        }
        if (estado == 503) {
            return new ErrorAplicacion(Tipo.NO_DISPONIBLE, "AUTH_NO_DISPONIBLE",
                    "Auth informó que no está disponible temporalmente", ex);
        }
        return new ErrorAplicacion(Tipo.INTERNO, "AUTH_ERROR_INTEGRACION",
                "Auth devolvió una respuesta de error no prevista por el contrato", ex);
    }
}
