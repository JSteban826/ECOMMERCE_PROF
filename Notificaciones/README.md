# Microservicio Notificaciones

Implementación propuesta para JSteban826/ECOMMERCE_PROF, tomando como referencia la rama mejora/errores-microservicios.
Agregar esta carpeta al mismo nivel que Auth y Catalogo. No modifica ninguno de esos servicios.

## Estado real de entrega
Código generado y revisado estáticamente; NO compilado ni ejecutado en el entorno del asistente.
El entorno no dispone de java, javac, mvn ni docker. Los tests están incluidos, no reportados como aprobados.
No se conectó con SMSGate, no se instaló James y no se hicieron envíos reales.
No es una afirmación de disponibilidad productiva ni de entrega garantizada.

## Alcance implementado
- API REST síncrona de SMS y correo de texto plano.
- Puertos de entrada/salida, casos de uso y dominio sin frameworks, separados por módulos Maven.
- SMSGate modo Local Server mediante POST /message.
- JavaMailSender conectado a SMTP autogestionado (Apache James).
- Validación HTTP y de dominio; errores ProblemDetail; autenticación API key.
- Timeouts de HTTP/SMTP y configuración externa para local/desplegado.
- Tests unitarios, contratos simulados, integración API con fakes y restricciones ArchUnit.
- Dockerfile multistage, Compose de la aplicación, ejemplos de entorno y scripts Windows/Linux.
- Colección Postman y contrato OpenAPI.

## Arranque resumido
1. Instalar JDK 25 y Maven 3.9+; verificar que Spring Boot 4.1.1 del repositorio sea resoluble.
2. Preparar SMSGate y James siguiendo docs/PROVEEDORES.md.
3. Ejecutar mvn -B clean verify desde esta carpeta.
4. Copiar .env.example a .env y completar SIN comillas los valores reales.
5. Ejecutar scripts/run-local.ps1 (PowerShell) o bash scripts/run-local.sh (Linux/macOS).
6. Importar la colección Postman, establecer apiKey y destinatarios autorizados y probar.

En Docker: ajustar los hosts para la red de contenedores y ejecutar docker compose up --build -d.

## Contrato
GET /health: estado del proceso, público, sin detalles de proveedores.
POST /api/notificaciones/sms: telefono y mensaje.
POST /api/notificaciones/email: destinatario, asunto y mensaje.
Ambos POST requieren X-API-Key. HTTP 200 significa aceptación reportada por el proveedor, NO entrega.
La política de teléfono es + seguido de 8 a 15 dígitos; números reales dependen de país/operador.
Email se valida con una política simple de destinatario individual; no listas ni display names.

## No incluido intencionalmente
Base de datos, historial, cola durable, outbox, idempotencia, plantillas, adjuntos, webhooks y reintentos automáticos.
No son requisitos universales de hexagonal. Si el negocio exige entrega durable, se debe ampliar este alcance.
No hay cambios al flujo de compra; no colocar fallos de notificaciones en la compensación de saldo.
No se incluye un proxy, servidor VPN o James preconfigurado; requieren infraestructura y secretos reales.
La API key no reemplaza cuotas, multi-tenant, autorización por usuario ni rate limiting.

## Lectura
- docs/ARQUITECTURA.md: responsabilidades, dependencias, decisiones y limitaciones.
- docs/PROVEEDORES.md: preparación de SMSGate y James.
- docs/DESPLIEGUE.md: ejecución, Docker, seguridad y checklist de publicación.
- docs/VERIFICACION.md: controles estáticos hechos y pruebas todavía pendientes.
- openapi.json: contrato de entrada/salida.
- postman_collection.json: solicitudes de ejemplo.

## Fuentes oficiales
https://docs.sms-gate.app/getting-started/local-server/
https://docs.sms-gate.app/integration/api/
https://james.apache.org/server/install.html
https://james.apache.org/server/manage-cli.html
https://docs.spring.io/spring-boot/reference/io/email.html
https://alistair.cockburn.us/hexagonal-architecture
https://12factor.net/config
