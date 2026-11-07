package com.ecommerce.notificaciones.infrastructure.adapter.out.smsgate;
import com.ecommerce.notificaciones.application.port.out.SmsGateway;
import com.ecommerce.notificaciones.domain.model.MensajeSms;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import static com.ecommerce.notificaciones.domain.exception.ErrorNotificacion.Tipo.*;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import org.springframework.web.client.*;
import java.util.List;
@Component
public final class SmsGateAdapter implements SmsGateway {
 private final RestClient client;
 public SmsGateAdapter(RestClient smsRestClient) { this.client=smsRestClient; }
 @Override public String enviar(MensajeSms mensaje) {
  try {
   var body=new Solicitud(new Texto(mensaje.mensaje()),List.of(mensaje.telefono()));
   var response=client.post().uri("/message").contentType(MediaType.APPLICATION_JSON).body(body)
    .retrieve().body(Respuesta.class);
   if (response==null || response.id()==null || response.id().isBlank())
    throw new ErrorNotificacion(RESPUESTA_INVALIDA,"SMS_RESPUESTA_INVALIDA","SMS sin identificador de aceptación; no reintentes automáticamente");
   return response.id();
  } catch (RestClientResponseException ex) {
   int status=ex.getStatusCode().value();
   if (status==429 || status>=500)
    throw new ErrorNotificacion(NO_DISPONIBLE,"SMS_NO_DISPONIBLE","El proveedor SMS no está disponible; verifica antes de reintentar");
   if (status>=400 && status<500)
    throw new ErrorNotificacion(RECHAZADO,"SMS_RECHAZADO","El proveedor SMS rechazó la operación");
   throw new ErrorNotificacion(RESPUESTA_INVALIDA,"SMS_RESPUESTA_INVALIDA","Respuesta HTTP inesperada de SMSGate");
  } catch (ResourceAccessException ex) {
   throw new ErrorNotificacion(INCIERTO,"SMS_RESULTADO_INCIERTO","No se pudo confirmar el envío SMS; podría haber sido aceptado. No reintentes automáticamente");
  } catch (RestClientException ex) {
   throw new ErrorNotificacion(RESPUESTA_INVALIDA,"SMS_RESPUESTA_INVALIDA","No se pudo interpretar la respuesta de SMSGate; verifica antes de reintentar");
  }
 }
 public record Texto(String text) {}
 public record Solicitud(Texto textMessage, List<String> phoneNumbers) {}
 public record Respuesta(String id, String state) {}
}
