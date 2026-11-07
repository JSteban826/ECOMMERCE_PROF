package com.ecommerce.notificaciones.application.usecase;
import com.ecommerce.notificaciones.application.port.in.EnviarSms;
import com.ecommerce.notificaciones.application.port.out.SmsGateway;
import com.ecommerce.notificaciones.application.result.ResultadoEnvio;
import com.ecommerce.notificaciones.domain.model.MensajeSms;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
public final class EnviarSmsUseCase implements EnviarSms {
 private final SmsGateway gateway;
 private final Clock clock;
 public EnviarSmsUseCase(SmsGateway gateway, Clock clock) { this.gateway=Objects.requireNonNull(gateway); this.clock=Objects.requireNonNull(clock); }
 @Override public ResultadoEnvio ejecutar(String telefono, String mensaje) {
  var solicitudId=UUID.randomUUID();
  String proveedorId=gateway.enviar(new MensajeSms(telefono, mensaje));
  return new ResultadoEnvio(solicitudId,ResultadoEnvio.Canal.SMS,ResultadoEnvio.Estado.ACEPTADO_POR_PROVEEDOR,proveedorId,Instant.now(clock));
 }
}
