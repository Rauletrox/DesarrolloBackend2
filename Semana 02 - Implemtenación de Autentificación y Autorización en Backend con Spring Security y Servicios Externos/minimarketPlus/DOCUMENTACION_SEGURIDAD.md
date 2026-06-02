# Informe de implementacion de seguridad JWT

## Contexto

MiniMarket Plus requiere proteger su backend con autenticacion segura, sesiones stateless mediante JWT y autorizacion basada en roles para clientes, empleados y gerentes.

El proyecto se adapto sobre la estructura existente de Spring Boot, JPA, H2, entidades `Usuario` y `Rol`, servicios y controladores REST.

## Paso 1: Configuracion JWT del proyecto

### Dependencias agregadas

En `pom.xml` se mantuvo `spring-boot-starter-security` y se agrego JJWT:

- `jjwt-api`
- `jjwt-impl`
- `jjwt-jackson`

Estas dependencias permiten crear, firmar, serializar y validar tokens JWT desde la aplicacion.

### Configuracion de propiedades

En `application.properties` se agregaron:

- `app.jwt.secret`: clave Base64 usada para firmar los tokens.
- `app.jwt.expiration-ms`: duracion del token. En esta entrega se dejo en 1 hora.

En produccion, el secreto debe cargarse desde variables de entorno o un gestor de secretos, no quedar fijo en el archivo de configuracion.

## Paso 2: Implementacion de autenticacion con JWT

### Configuracion de Spring Security

La clase `SecurityConfig` define un `SecurityFilterChain` personalizado:

- Deshabilita CSRF porque la API trabaja con JWT y no con cookies de sesion.
- Usa `SessionCreationPolicy.STATELESS` para evitar sesiones del lado servidor.
- Deshabilita `httpBasic`, `formLogin` y `logout`, ya que la autenticacion se realiza por endpoint REST.
- Registra `JwtAuthenticationFilter` antes de `UsernamePasswordAuthenticationFilter`.
- Usa `DaoAuthenticationProvider` con `CustomUserDetailsService` y `BCryptPasswordEncoder`.

### Modelo de usuario y roles

La entidad `Usuario` contiene:

- `id`
- `username`
- `password`
- `roles`

La contrasena se marca como `WRITE_ONLY` con Jackson para que pueda recibirse en solicitudes, pero no se exponga en respuestas JSON. Los roles se guardan en una relacion muchos-a-muchos con la entidad `Rol`.

### Almacenamiento seguro de contrasenas

Las contrasenas se cifran con BCrypt:

- Al crear usuarios iniciales en `DataInitializer`.
- Al guardar usuarios desde `UsuarioServiceImpl`.
- Al registrar nuevos clientes desde `/api/auth/registro`.

### Servicio de usuario

`CustomUserDetailsService` implementa `UserDetailsService` y carga usuarios desde `UsuarioRepository.findByUsername`. Luego entrega un `CustomUserDetails` con username, password cifrada y authorities.

`CustomUserDetails` normaliza roles con prefijo `ROLE_`, lo que permite usar reglas como `hasRole("GERENTE")` o `hasAnyRole("CLIENTE", "EMPLEADO", "GERENTE")`.

### Controlador de autenticacion

`AuthController` expone:

- `POST /api/auth/registro`: registra un usuario cliente con rol `CLIENTE`.
- `POST /api/auth/login`: valida credenciales y devuelve un JWT.

Ejemplo de login:

```json
{
  "username": "gerente",
  "password": "Gerente123"
}
```

Respuesta esperada:

```json
{
  "token": "eyJ...",
  "tipo": "Bearer",
  "username": "gerente",
  "roles": ["ROLE_GERENTE"]
}
```

### Generacion y validacion de JWT

`JwtUtil` se encarga de:

- Generar tokens firmados con HMAC.
- Incluir el username como `subject`.
- Incluir roles como claim.
- Agregar fecha de emision y expiracion.
- Extraer claims.
- Validar que el token corresponda al usuario y no este expirado.

### Filtro de seguridad

`JwtAuthenticationFilter` intercepta cada solicitud:

1. Lee el encabezado `Authorization`.
2. Verifica que use el formato `Bearer <token>`.
3. Extrae el username desde el token.
4. Carga el usuario desde la base de datos.
5. Valida firma, expiracion y correspondencia del token.
6. Crea la autenticacion en `SecurityContextHolder`.

Si el token falta o es invalido, la solicitud continua sin autenticacion y Spring Security responde 401 o 403 segun el recurso solicitado.

## Paso 3: Autorizacion basada en roles

### Roles definidos

`DataInitializer` crea los roles base:

- `CLIENTE`
- `EMPLEADO`
- `GERENTE`

Tambien crea usuarios de prueba:

- `cliente` / `Cliente123`
- `empleado` / `Empleado123`
- `gerente` / `Gerente123`

### Reglas de acceso

Las reglas principales son:

- `/public/**`, `/api/auth/**` y `/h2-console/**`: acceso publico.
- `GET /api/productos/**` y `GET /api/categorias/**`: CLIENTE, EMPLEADO o GERENTE.
- `/api/carrito/**`: CLIENTE, EMPLEADO o GERENTE.
- `/api/ventas/**` y `/api/detalle-ventas/**`: EMPLEADO o GERENTE.
- `/api/inventario/**`: EMPLEADO o GERENTE.
- `/api/usuarios/**`: solo GERENTE.
- Cualquier otro endpoint requiere autenticacion.

### Prueba de acceso

1. Hacer login con un usuario:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"gerente\",\"password\":\"Gerente123\"}"
```

2. Copiar el token y usarlo:

```bash
curl http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer TOKEN_AQUI"
```

Un `GERENTE` puede acceder a `/api/usuarios`. Un `CLIENTE` no deberia poder acceder a ese endpoint y recibira una respuesta de acceso denegado.

## Respuestas a preguntas de apoyo

### 1. Como funciona JWT en una aplicacion stateless

El usuario envia sus credenciales a `/api/auth/login`. Si son correctas, el backend genera un JWT firmado y lo devuelve al cliente. En las siguientes solicitudes, el cliente envia el token en el encabezado `Authorization: Bearer ...`. El servidor valida el token en cada request sin guardar una sesion en memoria.

### 2. Ventajas de JWT frente a sesiones tradicionales

JWT facilita arquitecturas distribuidas y microservicios porque cada servicio puede validar el token sin consultar una sesion central. Esto reduce acoplamiento, mejora escalabilidad horizontal y evita depender de memoria local del servidor.

### 3. Como se evita la manipulacion del JWT

El token se firma con una clave secreta HMAC. Si un atacante modifica claims como username o roles, la firma deja de coincidir y el token es rechazado. Ademas, el token tiene expiracion y se valida contra el usuario cargado desde la base de datos.

### 4. Como se implemento la autorizacion basada en roles

Los roles se almacenan en la base de datos y se transforman en authorities de Spring Security con prefijo `ROLE_`. Luego `SecurityConfig` restringe endpoints segun `CLIENTE`, `EMPLEADO` y `GERENTE`. Como medida adicional, el registro publico solo crea usuarios `CLIENTE`; los usuarios con roles superiores deben gestionarse por endpoints protegidos.

### 5. Buenas practicas aplicadas

- Contrasenas cifradas con BCrypt.
- API stateless con JWT.
- Tokens firmados y con expiracion.
- Password oculto en respuestas JSON.
- Principio de minimo privilegio en endpoints.
- Roles normalizados para evitar errores de autorizacion.
- Registro de eventos de autenticacion con `SecurityAuditService`.

## Como la configuracion protege el backend

La configuracion protege los componentes del backend porque separa autenticacion y autorizacion. Primero valida identidad con credenciales y JWT firmado. Luego controla permisos por rol antes de ejecutar operaciones sensibles.

Esto reduce riesgos de:

- Acceso anonimo a endpoints internos.
- Lectura de contrasenas por respuestas JSON.
- Manipulacion de roles dentro del token.
- Uso de sesiones persistentes en un entorno que debe ser stateless.
- Acceso de clientes a operaciones administrativas o de inventario.

## Verificacion realizada

Se ejecuto:

```bash
.\mvnw.cmd test
```

Resultado:

```text
BUILD SUCCESS
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
```
