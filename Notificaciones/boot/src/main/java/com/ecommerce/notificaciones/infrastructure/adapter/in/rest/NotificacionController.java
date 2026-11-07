package com.ecommerce.notificaciones.infrastructure.adapter.in.rest;
import com.ecommerce.notificaciones.application.port.in.*;
import com.ecommerce.notificaciones.application.result.ResultadoEnvio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/notificaciones")
public final class NotificacionController {
 private final EnviarSms sms;
 private final EnviarEmail email;
 public NotificacionController(EnviarSms sms, EnviarEmail email) { this.sms=sms; this.email=email; }
 @PostMapping(value="/sms",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
 public ResultadoEnvio sms(@Valid @RequestBody SmsRequest req) { return sms.ejecutar(req.telefono(),req.mensaje()); }
 @PostMapping(value="/email",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
 public ResultadoEnvio email(@Valid @RequestBody EmailRequest req) { return email.ejecutar(req.destinatario(),req.asunto(),req.mensaje()); }
 public record SmsRequest(@NotBlank @Size(max=16) String telefono, @NotBlank @Size(max=1600) String mensaje) {}
 public record EmailRequest(@NotBlank @Email @Size(max=254) String destinatario,
  @NotBlank @Size(max=200) String asunto,@NotBlank @Size(max=100000) String mensaje) {}
}
