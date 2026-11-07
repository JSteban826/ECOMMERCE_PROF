package com.ecommerce.notificaciones.application.result;
import java.time.Instant;
import java.util.UUID;
public record ResultadoEnvio(UUID solicitudId, Canal canal, Estado estado, String proveedorId, Instant fecha) {
 public enum Canal { SMS, EMAIL }
 public enum Estado { ACEPTADO_POR_PROVEEDOR }
}
