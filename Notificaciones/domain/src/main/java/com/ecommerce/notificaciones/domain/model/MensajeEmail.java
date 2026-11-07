package com.ecommerce.notificaciones.domain.model;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import static com.ecommerce.notificaciones.domain.exception.ErrorNotificacion.Tipo.VALIDACION;
public record MensajeEmail(String destinatario, String asunto, String mensaje) {
 public MensajeEmail {
  Reglas.email(destinatario);
  Reglas.texto(asunto,"Asunto",200);
  Reglas.texto(mensaje,"Mensaje",100000);
  if (asunto.indexOf('\r')>=0 || asunto.indexOf('\n')>=0)
   throw new ErrorNotificacion(VALIDACION,"ASUNTO_INVALIDO","El asunto no admite saltos de línea");
 }
}
