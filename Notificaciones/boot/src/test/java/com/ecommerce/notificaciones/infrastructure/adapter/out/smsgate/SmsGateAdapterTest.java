package com.ecommerce.notificaciones.infrastructure.adapter.out.smsgate;
import org.junit.jupiter.api.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.http.*;
import com.ecommerce.notificaciones.domain.model.MensajeSms;
import com.ecommerce.notificaciones.domain.exception.ErrorNotificacion;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.junit.jupiter.api.Assertions.*;
class SmsGateAdapterTest {
 private MockRestServiceServer server;
 private SmsGateAdapter adapter;
 @BeforeEach void setup() {
  var builder=RestClient.builder().baseUrl("http://gateway");
  server=MockRestServiceServer.bindTo(builder).build();adapter=new SmsGateAdapter(builder.build());
 }
 @Test void contratoHttp() {
  server.expect(requestTo("http://gateway/message")).andExpect(method(HttpMethod.POST))
   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
   .andExpect(content().json("{\"textMessage\":{\"text\":\"Hola\"},\"phoneNumbers\":[\"+573001234567\"]}"))
   .andRespond(withSuccess("{\"id\":\"sms-1\",\"state\":\"Pending\"}",MediaType.APPLICATION_JSON));
  assertEquals("sms-1",adapter.enviar(new MensajeSms("+573001234567","Hola")));server.verify();
 }
 @Test void proveedorCaido() {
  server.expect(requestTo("http://gateway/message")).andRespond(withStatus(HttpStatus.SERVICE_UNAVAILABLE));
  var ex=assertThrows(ErrorNotificacion.class,()->adapter.enviar(new MensajeSms("+573001234567","Hola")));
  assertEquals(ErrorNotificacion.Tipo.NO_DISPONIBLE,ex.tipo());server.verify();
 }
 @Test void respuestaSinId() {
  server.expect(requestTo("http://gateway/message")).andRespond(withSuccess("{}",MediaType.APPLICATION_JSON));
  var ex=assertThrows(ErrorNotificacion.class,()->adapter.enviar(new MensajeSms("+573001234567","Hola")));
  assertEquals(ErrorNotificacion.Tipo.RESPUESTA_INVALIDA,ex.tipo());server.verify();
 }
}
