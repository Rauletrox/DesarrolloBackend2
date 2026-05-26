# Documentacion de implementacion de seguridad

## Contexto

Este documento registra los cambios realizados en el backend del proyecto `minimarket` para cumplir las instrucciones de la actividad formativa sobre estrategias de seguridad con Spring Security.

El caso solicita revisar el backend de "LETRAS & PAPELES" y aplicar una implementacion inicial basada en autenticacion con nombre de usuario y contrasena. En este proyecto se adapto esa estrategia al dominio existente de minimarket, manteniendo usuarios, roles, productos, categorias, carrito, ventas e inventario.

## Paso 1: Analisis de seguridad

### Puntos criticos identificados

- Los endpoints `/api/**` exponian datos de negocio sin reglas de autorizacion diferenciadas.
- Las operaciones de usuarios podian entregar informacion sensible, incluida la contrasena.
- El guardado de usuarios no aseguraba el cifrado de contrasenas antes de persistir.
- No existia una respuesta REST clara para validar usuario y contrasena desde clientes como Postman.
- No habia monitoreo basico de intentos exitosos o fallidos de autenticacion.

### Amenazas consideradas

- Accesos no autorizados a datos de clientes, ventas, usuarios e inventario.
- Robo o exposicion accidental de contrasenas.
- Uso indebido de endpoints administrativos por usuarios sin privilegios.
- Intentos repetidos de autenticacion fallida.
- Riesgos comunes como SQL injection, XSS y CSRF.

### Requerimientos de seguridad abordados

- Autenticacion segura con nombre de usuario y contrasena.
- Autorizacion por tipos de usuario.
- Cifrado de contrasenas con BCrypt.
- Registro de eventos de autenticacion para monitorear actividad sospechosa.
- Proteccion de endpoints segun nivel de responsabilidad.

## Tipos de usuarios definidos

### CLIENTE

Nivel de seguridad: basico autenticado.

Puede consultar catalogo y usar carrito. No puede administrar usuarios, ventas internas ni inventario.

### EMPLEADO

Nivel de seguridad: medio.

Puede operar ventas, detalle de ventas, inventario, catalogo y carrito. No puede administrar usuarios.

### GERENTE

Nivel de seguridad: alto.

Puede acceder a funciones administrativas, incluida la gestion de usuarios. Es el rol con mayor privilegio.

## Metodos de autenticacion revisados

- En memoria: util para pruebas rapidas, pero no adecuado para usuarios reales persistidos.
- Base de datos JDBC/JPA: adecuado para este proyecto porque ya existen entidades `Usuario` y `Rol`.
- LDAP: util para organizaciones con directorio corporativo, pero excesivo para esta implementacion inicial.
- JWT: util para APIs stateless, pero no fue seleccionado porque la instruccion inicial pide usuario y contrasena.
- Nombre de usuario y contrasena: seleccionado para esta etapa.

## Estrategia seleccionada

Se implemento autenticacion con nombre de usuario y contrasena usando Spring Security, `UserDetailsService`, roles desde base de datos y contrasenas cifradas con BCrypt.

### Justificacion

La estrategia seleccionada esta alineada con el proyecto de Desarrollo Backend I porque reutiliza el modelo existente de usuarios y roles. Tambien responde a los objetivos de Desarrollo Backend II al incorporar Spring Security, control de acceso por rol, cifrado de contrasenas y registro basico de actividad sospechosa.

## Paso 2: Configuracion del framework

### Dependencias

El proyecto ya tenia incorporadas las dependencias principales:

- `spring-boot-starter-security`
- `spring-security-test`
- `spring-boot-starter-data-jpa`
- `spring-boot-starter-web`
- `h2`

No fue necesario agregar nuevas dependencias al `pom.xml`.

### Reglas de autorizacion implementadas

- `/public/**`: acceso publico.
- `/api/auth/**`: acceso publico para login.
- `/h2-console/**`: acceso publico para pruebas locales.
- `GET /api/productos/**` y `GET /api/categorias/**`: CLIENTE, EMPLEADO, GERENTE.
- `/api/carrito/**`: CLIENTE, EMPLEADO, GERENTE.
- `/api/ventas/**` y `/api/detalle-ventas/**`: EMPLEADO, GERENTE.
- `/api/inventario/**`: EMPLEADO, GERENTE.
- `/api/usuarios/**`: GERENTE.
- Cualquier otro endpoint requiere autenticacion.

## Cambios realizados por archivo

### `src/main/java/com/minimarket/security/config/SecurityConfig.java`

- Se configuro `SecurityFilterChain`.
- Se definieron reglas de autorizacion por endpoint y rol.
- Se habilito `httpBasic` para pruebas con usuario y contrasena.
- Se mantuvo `formLogin` para pruebas desde navegador.
- Se agrego `DaoAuthenticationProvider` para autenticar usuarios desde base de datos.
- Se configuro `BCryptPasswordEncoder`.
- Se permitio la consola H2 para pruebas locales.

### `src/main/java/com/minimarket/security/controller/AuthController.java`

- Se creo el endpoint `POST /api/auth/login`.
- Recibe `username` y `password`.
- Usa `AuthenticationManager` para validar credenciales.
- Devuelve un mensaje de autenticacion exitosa, el username y los roles.
- No devuelve contrasenas ni informacion sensible.

### `src/main/java/com/minimarket/security/model/LoginRequest.java`

- Se agregaron los campos `username` y `password`.
- Se agregaron getters y setters para recibir el JSON de login.

### `src/main/java/com/minimarket/security/model/CustomUserDetails.java`

- Se normalizaron roles con prefijo `ROLE_`.
- Esto permite que Spring Security use correctamente `hasRole` y `hasAnyRole`.

### `src/main/java/com/minimarket/security/service/CustomUserDetailsService.java`

- Se marco la carga de usuario como transaccion de solo lectura.
- Se documento que la autenticacion consulta usuarios desde base de datos.

### `src/main/java/com/minimarket/security/service/SecurityAuditService.java`

- Se creo un servicio de auditoria basica.
- Registra logins exitosos con nivel `INFO`.
- Registra intentos fallidos con nivel `WARN`.
- Esto permite monitorear actividad sospechosa.

### `src/main/java/com/minimarket/security/config/DataInitializer.java`

- Se agregaron roles iniciales: `CLIENTE`, `EMPLEADO`, `GERENTE`.
- Se agregaron usuarios de prueba:
  - `cliente` / `Cliente123`
  - `empleado` / `Empleado123`
  - `gerente` / `Gerente123`
- Las contrasenas se guardan cifradas con BCrypt.

### `src/main/java/com/minimarket/service/impl/UsuarioServiceImpl.java`

- Se agrego `PasswordEncoder`.
- Antes de guardar un usuario, la contrasena se cifra si viene en texto plano.

### `src/main/java/com/minimarket/entity/Usuario.java`

- Se agrego `@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)` al campo `password`.
- La API puede recibir contrasenas, pero no las devuelve en respuestas JSON.

### `src/main/resources/application.properties`

- Se normalizo la codificacion del archivo a UTF-8/ASCII.
- Esto corrigio un error de Maven al copiar recursos: `MalformedInputException`.

## Como probar

### Login REST

Enviar `POST` a:

```text
http://localhost:8080/api/auth/login
```

Body:

```json
{
  "username": "gerente",
  "password": "Gerente123"
}
```

Respuesta esperada:

```json
{
  "mensaje": "Autenticacion exitosa",
  "username": "gerente",
  "roles": ["ROLE_GERENTE"]
}
```

### Prueba con HTTP Basic

Ejemplo:

```bash
curl -u gerente:Gerente123 http://localhost:8080/api/usuarios
```

Un usuario `cliente` no deberia poder acceder a `/api/usuarios`, porque ese endpoint esta reservado para `GERENTE`.

## Observaciones

- CSRF fue deshabilitado para simplificar el consumo REST en esta actividad. Si luego se construye un frontend con sesiones y formularios, conviene habilitar proteccion CSRF.
- JWT no fue implementado porque la instruccion especifica solicita una primera implementacion con nombre de usuario y contrasena.
- Para ambientes productivos se recomienda mover credenciales iniciales a migraciones controladas o variables seguras, no dejarlas como datos fijos de prueba.
