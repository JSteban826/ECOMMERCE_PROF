package com.ecommerce.notificaciones.application.port.out;
import com.ecommerce.notificaciones.domain.model.MensajeSms;
public interface SmsGateway { String enviar(MensajeSms mensaje); }
