package com.ecommerce.notificaciones.integration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.*;
import com.ecommerce.notificaciones.application.port.out.*;
import java.net.*;
import java.net.http.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT,properties={
 "notificaciones.api-key=integration-test-key-32-characters-minimum",
 "notificaciones.remitente=notificaciones@example.com",
 "notificaciones.sms.base-url=http://127.0.0.1:9999",
 "notificaciones.sms.username=test","notificaciones.sms.password=test",
 "spring.mail.host=127.0.0.1","spring.mail.port=9998",
 "spring.mail.username=test","spring.mail.password=test"
})
@Import(ApiIntegrationTest.Fakes.class)
class ApiIntegrationTest {
 @LocalServerPort int port;
 private final HttpClient client=HttpClient.newHttpClient();
 private HttpResponse<String> post(String path,String body,String key) throws Exception {
  var builder=HttpRequest.newBuilder(URI.create("http://127.0.0.1:"+port+path))
   .header("Content-Type","application/json").POST(HttpRequest.BodyPublishers.ofString(body));
  if (key!=null) builder.header("X-API-Key",key);
  return client.send(builder.build(),HttpResponse.BodyHandlers.ofString());
 }
 private static final String KEY="integration-test-key-32-characters-minimum";
 @Test void sinClaveEs401() throws Exception {
  assertEquals(401,post("/api/notificaciones/sms","{}",null).statusCode());
 }
 @Test void claveIncorrectaEs401() throws Exception {
  assertEquals(401,post("/api/notificaciones/sms","{}","incorrecta").statusCode());
 }
 @Test void smsCorrectoEs200() throws Exception {
  var response=post("/api/notificaciones/sms","{\"telefono\":\"+573001234567\",\"mensaje\":\"Hola\"}",KEY);
  assertEquals(200,response.statusCode());assertTrue(response.body().contains("ACEPTADO_POR_PROVEEDOR"));
  assertTrue(response.body().contains("sms-test"));
 }
 @Test void emailCorrectoEs200() throws Exception {
  var response=post("/api/notificaciones/email","{\"destinatario\":\"persona@example.com\",\"asunto\":\"Compra\",\"mensaje\":\"Confirmada\"}",KEY);
  assertEquals(200,response.statusCode());assertTrue(response.body().contains("EMAIL"));
 }
 @Test void formatoJsonInvalidoEs400() throws Exception {
  assertEquals(400,post("/api/notificaciones/sms","{",KEY).statusCode());
 }
 @Test void reglaTelefonoEs422() throws Exception {
  assertEquals(422,post("/api/notificaciones/sms","{\"telefono\":\"123\",\"mensaje\":\"Hola\"}",KEY).statusCode());
 }
 @Test void healthEsPublico() throws Exception {
  var request=HttpRequest.newBuilder(URI.create("http://127.0.0.1:"+port+"/health")).GET().build();
  assertEquals(200,client.send(request,HttpResponse.BodyHandlers.ofString()).statusCode());
 }
 @TestConfiguration static class Fakes {
  @Bean @Primary SmsGateway fakeSms() { return message->"sms-test"; }
  @Bean @Primary EmailGateway fakeEmail() { return message->null; }
 }
}
