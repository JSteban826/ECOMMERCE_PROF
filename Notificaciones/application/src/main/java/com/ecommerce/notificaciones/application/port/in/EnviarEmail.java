package com.ecommerce.notificaciones.application.port.in;
import com.ecommerce.notificaciones.application.result.ResultadoEnvio;
public interface EnviarEmail { ResultadoEnvio ejecutar(String destinatario, String asunto, String mensaje); }
