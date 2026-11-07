package com.ecommerce.notificaciones.infrastructure.adapter.in.rest;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.net.URI;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
 private static final Logger LOG=LoggerFactory.getLogger(GlobalExceptionHandler.class);
 @ExceptionHandler(ErrorNotificacion.class)
 public ResponseEntity<Object> conocido(ErrorNotificacion ex) {
  int status=switch(ex.tipo()) {
   case VALIDACION -> 422;
   case RECHAZADO, RESPUESTA_INVALIDA -> 502;
   case NO_DISPONIBLE -> 503;
   case TIEMPO_ESPERA, INCIERTO -> 504;
  };
  LOG.warn("Fallo de notificación: {}",ex.codigo());
  return problema(status,ex.codigo(),ex.getMessage(),HttpHeaders.EMPTY);
 }
 @Override protected ResponseEntity<Object> handleExceptionInternal(Exception ex,Object body,HttpHeaders headers,HttpStatusCode status,WebRequest request) {
  return problema(status.value(),"SOLICITUD_HTTP_INVALIDA","No fue posible procesar la solicitud HTTP",headers);
 }
 @ExceptionHandler(Exception.class)
 public ResponseEntity<Object> inesperado(Exception ex) {
  LOG.error("Error interno: {}",ex.getClass().getName());
  return problema(500,"ERROR_INTERNO","Ocurrió un error interno",HttpHeaders.EMPTY);
 }
 private ResponseEntity<Object> problema(int status,String code,String detail,HttpHeaders originales) {
  var body=ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(status),detail);
  body.setType(URI.create("about:blank")); body.setProperty("code",code);
  var headers=new HttpHeaders(); headers.putAll(originales);
  headers.setContentType(MediaType.APPLICATION_PROBLEM_JSON); headers.setCacheControl("no-store");
  return new ResponseEntity<>(body,headers,HttpStatusCode.valueOf(status));
 }
}
