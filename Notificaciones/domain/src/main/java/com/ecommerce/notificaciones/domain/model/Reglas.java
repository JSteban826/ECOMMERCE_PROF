package com.ecommerce.notificaciones.domain.model;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import static com.ecommerce.notificaciones.domain.exception.ErrorNotificacion.Tipo.VALIDACION;
public final class Reglas {
 private Reglas() {}
 public static String texto(String valor, String campo, int maximo) {
  if (valor == null || valor.isBlank() || valor.length()>maximo)
   throw new ErrorNotificacion(VALIDACION,"DATOS_INVALIDOS",campo+" es obligatorio, no puede estar en blanco y admite hasta "+maximo+" caracteres");
  return valor;
 }
 public static String email(String valor) {
  texto(valor,"Email",254);
  if (!valor.matches("[^\\s@<>;,\\r\\n]+@[^\\s@<>;,\\r\\n]+\\.[^\\s@<>;,\\r\\n]+"))
   throw new ErrorNotificacion(VALIDACION,"EMAIL_INVALIDO","El email no tiene un formato admitido");
  return valor;
 }
}
