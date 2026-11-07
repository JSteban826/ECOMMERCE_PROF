package com.ecommerce.notificaciones.infrastructure.adapter.out.smtp;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import com.ecommerce.notificaciones.domain.model.MensajeEmail;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import com.ecommerce.notificaciones.infrastructure.config.NotificacionesProperties;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
class SmtpEmailAdapterTest {
 private NotificacionesProperties props() { return new NotificacionesProperties("x".repeat(32),"notificaciones@example.com",null); }
 @Test void enviaCorreo() {
  var sender=mock(JavaMailSender.class); var adapter=new SmtpEmailAdapter(sender,props());
  assertNull(adapter.enviar(new MensajeEmail("persona@example.com","Compra","Confirmada")));
  var captor=org.mockito.ArgumentCaptor.forClass(SimpleMailMessage.class);
  verify(sender).send(captor.capture());assertEquals("notificaciones@example.com",captor.getValue().getFrom());
  assertEquals("Compra",captor.getValue().getSubject());
 }
 @Test void falloEsInciertoYNoSeReintenta() {
  var sender=mock(JavaMailSender.class);doThrow(new MailSendException("sensible")).when(sender).send(any(SimpleMailMessage.class));
  var adapter=new SmtpEmailAdapter(sender,props());
  var ex=assertThrows(ErrorNotificacion.class,()->adapter.enviar(new MensajeEmail("persona@example.com","Compra","Confirmada")));
  assertEquals(ErrorNotificacion.Tipo.INCIERTO,ex.tipo());assertFalse(ex.getMessage().contains("sensible"));
  verify(sender,times(1)).send(any(SimpleMailMessage.class));
 }
}
