package com.ecommerce.notificaciones.application.port.in;
import com.ecommerce.notificaciones.application.result.ResultadoEnvio;
public interface EnviarSms { ResultadoEnvio ejecutar(String telefono, String mensaje); }
