# Decisiones de arquitectura

## Dependencias verificables
- domain: Java estándar; cero dependencias productivas de Spring/Jakarta/Lombok.
- application: depende exclusivamente de domain y del JDK.
- boot: adaptadores REST, SMSGate, SMTP, seguridad y composición Spring.
- Los POM separan físicamente el núcleo. ArchUnit agrega controles sobre las importaciones.
- Los puertos de salida pertenecen a application porque son necesidades de sus casos de uso.
- Los casos implementan puertos de entrada y no llevan @Service ni @Transactional.
- Los DTO de SMSGate no cruzan hacia application; SimpleMailMessage tampoco.
- No hay JPA ni PostgreSQL: esta versión no almacena historial, no inventa repositorios sin necesidad.
- Spring conecta puertos y adaptadores mediante beans externos al núcleo.
- Clock inyectable permite controlar fechas de las pruebas.

## Contrato síncrono
POST espera el reporte de aceptación de SMSGate/SMTP y responde 200; no es una cola durable.
La respuesta ACEPTADO_POR_PROVEEDOR NO confirma entrega al destinatario.
proveedorId contiene el id de SMSGate; en SMTP es null porque SimpleMailMessage no devuelve un recibo rastreable.
solicitudId identifica solamente el resultado de esta operación. No es una clave de idempotencia ni un registro persistente.
No existe endpoint de consulta por id ni webhook de entrega en este alcance.

## Errores
400: JSON/DTO inválido. 401: clave ausente o incorrecta. 422: regla de dominio inválida.
502: rechazo del proveedor o respuesta ilegible/incompleta. Un 4xx de SMSGate no se traslada como 4xx del cliente automáticamente.
503: indisponibilidad o configuración SMTP inválida. 504: resultado incierto de comunicación.
500: fallo inesperado. No se retornan errores crudos del proveedor.
Un 5xx del proveedor tampoco garantiza que no haya procesado el mensaje.
No reintentar automáticamente ninguna operación cuyo resultado sea incierto.
El enum TIEMPO_ESPERA queda reservado; los fallos de I/O se clasifican conservadoramente INCIERTO.

## Una implementación, varios entornos
El JAR y los casos de uso no cambian. URL, puertos, credenciales y TLS son configuración externa.
Para SMSGate local el servidor remoto necesita ruta hacia Android, por ejemplo mediante VPN autogestionada.
No se utiliza modo Cloud/Private ni se presupone independencia de FCM en esos modos.
HTTP Basic solo debe viajar en LAN confiable o túnel cifrado. No abrir el puerto del teléfono al Internet público.
No se sigue una redirección HTTP del proveedor con las credenciales.

## Seguridad y límites
API key compartida para la primera integración entre servicios; rotarla y almacenarla como secreto.
No sustituye autorización por usuario, multi-tenant, cuotas, rate limiting ni auditoría durable.
El reverse proxy debe imponer TLS, límites de petición/concurrencia, rate limiting y política de red.
No habilitar CORS público: este API es de servidor a servidor.
Los mensajes se envían como texto plano. Sin HTML ni adjuntos en esta versión.
El límite SMS de 1600 caracteres es una política de la aplicación, no equivale a un solo segmento ni costo.
La política de email es conservadora, no una implementación completa de todos los formatos RFC.
No hay reintentos de aplicación. JavaMail/HTTP pueden tener comportamiento propio del protocolo; verificar en el entorno final.

## Integración posterior con compra
No insertar el envío dentro del try/catch que compensa saldo/inventario en CarritoUseCase.
Si se exige no perder notificaciones tras el commit, diseñar un outbox transaccional en el servicio que genera el evento,
consumidor durable e idempotencia con restricciones únicas. Un POST posterior al commit deja una ventana de pérdida.
No se incluyen esos cambios a Auth/Catalogo para evitar alterar compras sin una decisión explícita.

## Requisitos que no provienen de hexagonal
Colas, event sourcing, CQRS, base de datos y microservicios no son obligaciones de puertos y adaptadores.
La arquitectura aísla el núcleo, no proporciona por sí sola disponibilidad o entrega exactamente una vez.
