package com.ecommerce.notificaciones.infrastructure.config;
import com.ecommerce.notificaciones.application.port.in.*;
import com.ecommerce.notificaciones.application.port.out.*;
import com.ecommerce.notificaciones.application.usecase.*;
import org.springframework.context.annotation.*;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import java.net.http.HttpClient;
import java.time.*;
@Configuration
public class UseCaseConfig {
 @Bean Clock clock() { return Clock.systemUTC(); }
 @Bean EnviarSms enviarSms(SmsGateway gateway, Clock clock) { return new EnviarSmsUseCase(gateway,clock); }
 @Bean EnviarEmail enviarEmail(EmailGateway gateway, Clock clock) { return new EnviarEmailUseCase(gateway,clock); }
 @Bean RestClient smsRestClient(NotificacionesProperties props) {
  var config=props.sms();
  var http=HttpClient.newBuilder().connectTimeout(Duration.ofMillis(config.connectTimeoutMs()))
   .followRedirects(HttpClient.Redirect.NEVER).build();
  var factory=new JdkClientHttpRequestFactory(http);
  factory.setReadTimeout(Duration.ofMillis(config.readTimeoutMs()));
  return RestClient.builder().requestFactory(factory).baseUrl(config.baseUrl().toString())
   .defaultHeaders(h->h.setBasicAuth(config.username(),config.password())).build();
 }
}
