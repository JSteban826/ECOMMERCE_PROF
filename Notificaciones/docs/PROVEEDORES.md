# Preparar los proveedores reales

No se incluyen credenciales ni una imagen no verificada de James. El Compose del proyecto levanta SOLO Notificaciones.
No confundir compilar la aplicación con aprovisionar SMTP o un teléfono.

## SMSGate en modo local
1. Instalar la aplicación oficial SMSGate desde sus canales oficiales.
2. Tener SIM activa, permiso SMS y saldo/plan. Realizar primero un envío manual autorizado.
3. Activar Local Server en la app e iniciarlo; copiar dirección y credenciales.
4. Permitir conectividad desde el equipo donde corre Java (o desde la red del contenedor).
5. Establecer SMSGATE_BASE_URL con la raíz, por ejemplo http://192.168.1.100:8080, sin /message.
6. Colocar usuario y contraseña en .env.
7. Revisar /docs del servidor local: el adaptador implementa POST /message con textMessage y phoneNumbers.
8. Probar con un número propio. SMS largos pueden dividirse en segmentos y generar más cargos.
9. En Android revisar exclusiones de batería, disponibilidad de red, energía y permisos.
10. Desplegado: establecer una VPN privada autogestionada y probar que la IP del Android sea alcanzable desde el contenedor.
    La VPN no está creada por compose.yml. Los caminos de ruta/firewall dependen del hosting y del teléfono.

Documentación:
https://docs.sms-gate.app/getting-started/local-server/
https://docs.sms-gate.app/integration/api/
https://docs.sms-gate.app/pricing/

## Apache James
1. Elegir una distribución soportada desde el proyecto oficial y fijar su versión.
2. Instalarla siguiendo las instrucciones de esa distribución (Spring y Guice usan empaquetados distintos).
3. Crear dominio y cuenta remitente con el CLI de la distribución; restringir administración a red privada.
   Ejemplo para la distribución que incluye bin/james-cli.sh, desde su directorio raíz:

   ./bin/james-cli.sh -h 127.0.0.1 -p 9999 adddomain example.com
   ./bin/james-cli.sh -h 127.0.0.1 -p 9999 adduser notificaciones@example.com CAMBIAR_CLAVE

   El ejemplo no instala James ni genera TLS; el nombre de comando/helper depende de la distribución.
   La documentación de Guice Docker muestra: docker exec -ti james james-cli COMMAND.
   No usar example.com como dominio de envío real: sustituir por un dominio bajo tu control.
4. Configurar listener SMTP con autenticación y TLS. Conservar authRequired; no abrir un relay anónimo.
5. Preparar certificados de confianza cuyo nombre coincida con SMTP_HOST. No desactivar la verificación para producción.
6. Obtener host, puerto, cuenta y contraseña del listener configurado; copiarlos a .env.
7. STARTTLS: SMTP_STARTTLS=true y SMTP_SSL=false. TLS implícito: SMTP_STARTTLS=false y SMTP_SSL=true.
   No activar ambos simultáneamente. Los puertos 587/465 son convenciones, no una configuración automática de James.
8. Solo en una prueba SMTP aislada puedes usar SMTP_STARTTLS=false y SMTP_SSL=false; nunca por Internet sin cifrar.
9. Crear además una cuenta receptora local y comprobar recepción mediante un cliente IMAP adecuado si no hay entrega externa.
10. Para Internet: dominio real, DNS/PTR/SPF/DKIM/DMARC, reputación, salida SMTP permitida, almacenamiento,
    backups, métricas y monitoreo del spool. El hosting puede bloquear el puerto 25 saliente.
11. Probar con buzones autorizados externos y revisar spam/rebotes. Un SMTP aceptado no prueba entrega final.

Documentación:
https://james.apache.org/server/install.html
https://james.apache.org/server/manage-cli.html
https://james.apache.org/server/config-smtp-lmtp.html
https://james.apache.org/server/config-ssl-tls.html

## Contenedores y localhost
SMTP_HOST=127.0.0.1 dentro del contenedor apunta al propio contenedor, NO al host.
Usar DNS privado, dirección de red accesible o nombre del servicio si James comparte una red Docker.
Los valores de .env.example describen Java en el host, no resuelven automáticamente Docker.
No se requiere contratar AWS ni Google para esta topología.
