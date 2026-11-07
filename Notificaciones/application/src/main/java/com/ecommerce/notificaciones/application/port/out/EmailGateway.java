package com.ecommerce.notificaciones.application.port.out;
import com.ecommerce.notificaciones.domain.model.MensajeEmail;
public interface EmailGateway { String enviar(MensajeEmail mensaje); }
