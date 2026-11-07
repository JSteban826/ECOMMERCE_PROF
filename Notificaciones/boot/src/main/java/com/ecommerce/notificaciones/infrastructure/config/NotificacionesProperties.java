package com.ecommerce.notificaciones.infrastructure.config;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.net.URI;
@ConfigurationProperties(prefix="notificaciones")
@Validated
public record NotificacionesProperties(
 @NotBlank @Size(min=32,max=256) String apiKey,
 @NotBlank @Email String remitente,
 @NotNull @Valid Sms sms) {
 public record Sms(@NotNull URI baseUrl, @NotBlank String username, @NotBlank String password,
                   @Min(100) @Max(120000) int connectTimeoutMs, @Min(100) @Max(120000) int readTimeoutMs) {
  @AssertTrue(message="SMS base-url debe usar http/https, incluir host y no contener credenciales, query ni fragment")
  public boolean isUrlValida() {
   return baseUrl!=null && ("http".equals(baseUrl.getScheme()) || "https".equals(baseUrl.getScheme()))
    && baseUrl.getHost()!=null && baseUrl.getUserInfo()==null && baseUrl.getQuery()==null && baseUrl.getFragment()==null;
  }
 }
}
