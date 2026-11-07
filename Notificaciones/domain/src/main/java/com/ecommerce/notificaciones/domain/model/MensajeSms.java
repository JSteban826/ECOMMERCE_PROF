package com.ecommerce.notificaciones.domain.model;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import static com.ecommerce.notificaciones.domain.exception.ErrorNotificacion.Tipo.VALIDACION;
public record MensajeSms(String telefono, String mensaje) {
 public MensajeSms {
  if (telefono==null || !telefono.matches("\\+[1-9][0-9]{7,14}"))
   throw new ErrorNotificacion(VALIDACION,"TELEFONO_INVALIDO","Usa un teléfono internacional, por ejemplo +573001234567");
  Reglas.texto(mensaje,"Mensaje",1600);
 }
}
