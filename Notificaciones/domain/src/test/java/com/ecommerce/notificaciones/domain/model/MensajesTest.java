package com.ecommerce.notificaciones.domain.model;
import org.junit.jupiter.api.Test;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import static org.junit.jupiter.api.Assertions.*;
class MensajesTest {
 @Test void smsValido() { assertEquals("Hola",new MensajeSms("+573001234567","Hola").mensaje()); }
 @Test void telefonoInvalido() { assertThrows(ErrorNotificacion.class,()->new MensajeSms("3001234567","Hola")); }
 @Test void mensajeVacio() { assertThrows(ErrorNotificacion.class,()->new MensajeSms("+573001234567"," ")); }
 @Test void mensajeExcesivo() { assertThrows(ErrorNotificacion.class,()->new MensajeSms("+573001234567","a".repeat(1601))); }
 @Test void emailValido() { assertDoesNotThrow(()->new MensajeEmail("persona@example.com","Compra","Confirmada")); }
 @Test void emailInvalido() { assertThrows(ErrorNotificacion.class,()->new MensajeEmail("sin-arroba","Compra","Confirmada")); }
 @Test void sinInyeccionCabeceras() { assertThrows(ErrorNotificacion.class,()->new MensajeEmail("persona@example.com","Compra\r\nBcc: x@example.com","Hola")); }
}
