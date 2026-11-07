package com.ecommerce.notificaciones.application.usecase;
import org.junit.jupiter.api.Test;
import com.ecommerce.notificaciones.application.result.ResultadoEnvio;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import java.time.*;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;
class CasosDeUsoTest {
 private final Clock clock=Clock.fixed(Instant.parse("2026-11-07T00:00:00Z"),ZoneOffset.UTC);
 @Test void smsDelegaYRetornaAceptacionNoEntrega() {
  var calls=new AtomicInteger();
  var useCase=new EnviarSmsUseCase(m->{calls.incrementAndGet();assertEquals("Hola",m.mensaje());return "sms-1";},clock);
  var result=useCase.ejecutar("+573001234567","Hola");
  assertEquals(1,calls.get()); assertEquals("sms-1",result.proveedorId());
  assertEquals(ResultadoEnvio.Estado.ACEPTADO_POR_PROVEEDOR,result.estado()); assertEquals(clock.instant(),result.fecha());
 }
 @Test void datosInvalidosNoLlamanAlProveedor() {
  var calls=new AtomicInteger();
  var useCase=new EnviarSmsUseCase(m->{calls.incrementAndGet();return "id";},clock);
  assertThrows(ErrorNotificacion.class,()->useCase.ejecutar("incorrecto","Hola"));assertEquals(0,calls.get());
 }
 @Test void falloNoSeReintenta() {
  var calls=new AtomicInteger();
  var error=new ErrorNotificacion(ErrorNotificacion.Tipo.INCIERTO,"INCIERTO","Verificar");
  var useCase=new EnviarSmsUseCase(m->{calls.incrementAndGet();throw error;},clock);
  assertSame(error,assertThrows(ErrorNotificacion.class,()->useCase.ejecutar("+573001234567","Hola")));
  assertEquals(1,calls.get());
 }
 @Test void emailDelega() {
  var useCase=new EnviarEmailUseCase(m->{assertEquals("Compra",m.asunto());return null;},clock);
  var result=useCase.ejecutar("persona@example.com","Compra","Confirmada");
  assertEquals(ResultadoEnvio.Canal.EMAIL,result.canal());assertNull(result.proveedorId());
 }
}
