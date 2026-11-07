package com.ecommerce.notificaciones.domain.exception;
public final class ErrorNotificacion extends RuntimeException {
 public enum Tipo { VALIDACION, RECHAZADO, NO_DISPONIBLE, TIEMPO_ESPERA, RESPUESTA_INVALIDA, INCIERTO }
 private final Tipo tipo;
 private final String codigo;
 public ErrorNotificacion(Tipo tipo, String codigo, String mensaje) { super(mensaje); this.tipo=tipo; this.codigo=codigo; }
 public Tipo tipo() { return tipo; }
 public String codigo() { return codigo; }
}
