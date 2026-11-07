package com.ecommerce.notificaciones.application.usecase;
import com.ecommerce.notificaciones.application.port.in.EnviarEmail;
import com.ecommerce.notificaciones.application.port.out.EmailGateway;
import com.ecommerce.notificaciones.application.result.ResultadoEnvio;
import com.ecommerce.notificaciones.domain.model.MensajeEmail;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
public final class EnviarEmailUseCase implements EnviarEmail {
 private final EmailGateway gateway;
 private final Clock clock;
 public EnviarEmailUseCase(EmailGateway gateway, Clock clock) { this.gateway=Objects.requireNonNull(gateway); this.clock=Objects.requireNonNull(clock); }
 @Override public ResultadoEnvio ejecutar(String destinatario, String asunto, String mensaje) {
  var solicitudId=UUID.randomUUID();
  String proveedorId=gateway.enviar(new MensajeEmail(destinatario, asunto, mensaje));
  return new ResultadoEnvio(solicitudId,ResultadoEnvio.Canal.EMAIL,ResultadoEnvio.Estado.ACEPTADO_POR_PROVEEDOR,proveedorId,Instant.now(clock));
 }
}
