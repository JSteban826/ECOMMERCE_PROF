# Local y despliegue

## Construcción reproducible pendiente de verificación
Las versiones Java 25 y Spring Boot 4.1.1 provienen de los POM del repositorio compartido.
No han sido resueltas en este entorno. Verificar disponibilidad en tu repositorio Maven y compatibilidad de tu JDK.
No bajar silenciosamente versiones si falla; revisar primero el POM que usa tu equipo.
Las imágenes Docker indicadas tampoco se descargaron ni ejecutaron aquí; fijar digest en un despliegue controlado.

## Desarrollo
Requisitos: JDK 25, Maven 3.9+, SMSGate local accesible y James configurado.

Desde Notificaciones:

    mvn -B clean verify

Esto ejecuta pruebas de dominio, casos de uso, contratos simulados SMS, SMTP con mock, API HTTP con adaptadores falsos
(y servidor embebido real), y reglas ArchUnit. No envía SMS ni correos reales.

Linux/macOS:

    cp .env.example .env
    # Editar .env con datos reales
    bash scripts/run-local.sh

PowerShell:

    Copy-Item .env.example .env
    # Editar .env con datos reales
    ./scripts/run-local.ps1

Los scripts cargan pares KEY=value sin evaluar comandos. Usar valores sin comillas ni comentarios en la misma línea.
.env no se carga automáticamente por Spring. No usar mvn spring-boot:run desde el POM agregador.
El JAR está en boot/target/notificaciones-boot-1.0.0-SNAPSHOT.jar.

## Docker

    docker compose up --build -d
    docker compose logs -f notificaciones

Modificar .env para la red de Docker ANTES de ejecutar. El servicio solo se publica en loopback del host.
El build ejecuta verify; si fallan dependencias o pruebas, no se construye la imagen final.
Colocar un reverse proxy HTTPS delante del puerto 8083 y conservarlo restringido a los clientes de confianza.
Configurar proxy con máximo de cuerpo (por ejemplo 256 KiB), rate limiting, timeout > tiempo máximo de la operación,
y límites de concurrencia acordes al teléfono. Los valores de throughput deben medirse.
El código no configura automáticamente dominio, DNS, certificados, proxy o VPN.

## Pruebas manuales
Health (liveness solamente, no comprueba proveedores):

    curl http://localhost:8083/health

SMS real (puede tener costo):

    curl -i -X POST http://localhost:8083/api/notificaciones/sms \
      -H "X-API-Key: TU_CLAVE" -H "Content-Type: application/json" \
      -d '{"telefono":"+573001234567","mensaje":"Prueba autorizada"}'

Email real:

    curl -i -X POST http://localhost:8083/api/notificaciones/email \
      -H "X-API-Key: TU_CLAVE" -H "Content-Type: application/json" \
      -d '{"destinatario":"tu-cuenta@tu-dominio.com","asunto":"Prueba","mensaje":"Prueba autorizada"}'

Usar tus propios destinatarios. No copiar números de ejemplo para envíos.
No hacer que health dependa del teléfono: un proveedor caído no implica que Java deba reiniciarse en bucle.

## Antes de publicar
- verify exitoso con JDK objetivo; guardar reportes Surefire.
- Clave aleatoria >=32 caracteres, no valor de ejemplo, distribuida solo a Auth/Catalogo.
- SMTP protegido y con identidad de certificado comprobada.
- VPN/rutas accesibles desde contenedor hacia Android; firewall mínimo.
- Rate limiting, tamaños máximos, HTTPS y control de acceso en el proxy/red.
- Logs protegidos y política de retención; nunca activar logging de cuerpos o SMTP debug en producción.
- Prueba de indisponibilidad de Android y James: errores controlados y sin repetición automática.
- Prueba real de cada canal y comprobación en destinatario, no solo status HTTP.
- No activar reintentos en clientes/reverse proxy para POST de envío.
- Definir responsable operativo de SIM, cargas, energía, backups, certificados y alertas.
- Para garantías de entrega: diseñar outbox/idempotencia antes de afirmar que es un sistema durable.
