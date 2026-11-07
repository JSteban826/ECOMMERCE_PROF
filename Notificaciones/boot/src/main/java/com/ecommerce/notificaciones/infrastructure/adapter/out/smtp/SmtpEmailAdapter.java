package com.ecommerce.notificaciones.infrastructure.adapter.out.smtp;
import com.ecommerce.notificaciones.application.port.out.EmailGateway;
import com.ecommerce.notificaciones.domain.model.MensajeEmail;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import com.ecommerce.notificaciones.infrastructure.config.NotificacionesProperties;
import static com.ecommerce.notificaciones.domain.exception.ErrorNotificacion.Tipo.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
@Component
public final class SmtpEmailAdapter implements EmailGateway {
 private final JavaMailSender sender;
 private final String remitente;
 public SmtpEmailAdapter(JavaMailSender sender, NotificacionesProperties props) {
  this.sender=sender; this.remitente=props.remitente();
 }
 @Override public String enviar(MensajeEmail email) {
  var message=new SimpleMailMessage();
  message.setFrom(remitente); message.setTo(email.destinatario());
  message.setSubject(email.asunto()); message.setText(email.mensaje());
  try {
   sender.send(message);
   return null;
  } catch (MailAuthenticationException ex) {
   throw new ErrorNotificacion(NO_DISPONIBLE,"EMAIL_CONFIGURACION_INVALIDA","El servicio de correo no está disponible");
  } catch (MailParseException ex) {
   throw new ErrorNotificacion(RECHAZADO,"EMAIL_RECHAZADO","No se pudo preparar el correo");
  } catch (MailException ex) {
   throw new ErrorNotificacion(INCIERTO,"EMAIL_RESULTADO_INCIERTO","No se pudo confirmar el envío del correo; podría haber sido aceptado. No reintentes automáticamente");
  }
 }
}
