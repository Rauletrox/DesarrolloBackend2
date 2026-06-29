# Documentacion tecnica - Semana 6

## 1. Resumen tecnico del avance

En las semanas previas se construyo la base del backend de Minimarket Plus con sus entidades, repositorios, servicios y controladores REST. Sobre esa base, en esta semana se incorporo autenticacion y control de acceso con Spring Security, usando `@PreAuthorize` para restringir operaciones criticas en producto, inventario y venta segun el rol del usuario.

Las entidades y relaciones mas relevantes para esta etapa son:

- `Usuario` y `Rol`, para representar perfiles autenticados.
- `Producto` y `Categoria`, para la gestion de catalogo.
- `Inventario`, para registrar movimientos asociados a productos.
- `Venta` y `DetalleVenta`, para consolidar transacciones y su detalle.
- `Carrito`, para el flujo previo a la venta.

## 2. Configuracion del entorno de pruebas

El proyecto utiliza Maven y mantiene la estructura estandar:

- `src/main/java` para codigo productivo.
- `src/test/java` para pruebas unitarias y de integracion de capa web.

La configuracion de pruebas usa:

- JUnit 5 para ejecutar los casos.
- Mockito para simular repositorios y servicios.
- `spring-security-test` para validar acceso por rol.
- H2 en memoria para levantar el contexto de Spring en pruebas.
- JaCoCo para generar reporte de cobertura.

El archivo `application.properties` usa H2 en memoria y configura JPA para levantar el esquema automaticamente en pruebas.

## 3. Diseno y justificacion de las pruebas

Las pruebas se organizaron por capa:

### 3.1 Servicios

Se probaron servicios de:

- `ProductoService`
- `InventarioService`
- `VentaService`
- `UsuarioService`
- `CarritoService`
- `DetalleVentaService`
- `CategoriaService`
- `RolService`

Justificacion:

- Verifican la interaccion con repositorios.
- Cubren escenarios de retorno exitoso y ausencia de registros.
- Confirman que el servicio responde con `null` o `Optional.empty()` cuando corresponde.

### 3.2 Controladores

Se agregaron pruebas MVC para:

- `UsuarioController`
- `CategoriaController`
- `CarritoController`
- `DetalleVentaController`
- `ProductoController`
- `InventarioController`
- `VentaController`

Justificacion:

- Validan respuestas HTTP `200`, `204`, `404`, `403` segun el caso.
- Comprueban que las rutas protegidas respeten autenticacion y autorizacion.
- Reflejan mejor la pauta, porque combinan comportamiento funcional y seguridad.

### 3.3 Entidades y seguridad

Se agregaron pruebas sobre entidades y `CustomUserDetails` para asegurar:

- Asignacion correcta de atributos.
- Relaciones entre objetos.
- Mapeo de roles a authorities de Spring Security.

## 4. Resultados de ejecucion

Ejecucion realizada con:

```bash
mvn test
```

Resultados esperados para documentar:

- Todos los tests ejecutados sin fallas.
- Reportes generados en `target/surefire-reports`.
- Reporte de cobertura generado por JaCoCo en `target/site/jacoco/index.html`.

### 4.1 Endpoints protegidos

Los endpoints protegidos por autorizacion de rol son:

- `POST /api/productos` y operaciones de modificacion/eliminacion para `ADMIN`.
- `POST /api/inventario` y operaciones de modificacion/eliminacion para `ADMIN`.
- `POST /api/ventas` para `CAJERO`.

### 4.2 Casos de error cubiertos

Las pruebas incluyen:

- Recursos existentes y no existentes.
- Acceso permitido con rol correcto.
- Acceso denegado con rol incorrecto.
- Respuesta `404` cuando el registro no existe.
- Respuesta `403` cuando el rol no tiene permiso.

## 5. Analisis de cobertura

Las pruebas agregadas apuntan a aumentar cobertura de:

- ramas `if/else` en controladores,
- retornos vacios en servicios,
- metodos de seguridad y modelos de usuario,
- clases de entidad con getters, setters y relaciones.

Con JaCoCo se debe revisar:

- cobertura de lineas,
- cobertura de ramas,
- clases no cubiertas,
- metodos con cobertura parcial.

Meta sugerida:

- alcanzar al menos 80% de cobertura lineal global o la mayor cobertura posible en las capas de dominio, servicio y controller.

## 6. Evidencia a adjuntar

Pegue aqui sus capturas de:

- ejecucion de `mvn test`,
- resumen de Surefire,
- reporte HTML de JaCoCo,
- respuesta de endpoints protegidos con distintos roles,
- vista del archivo `target/site/jacoco/index.html`.

## 7. Como estas pruebas mejoran la calidad

Las pruebas aportan valor porque:

- reducen regresiones en endpoints protegidos,
- validan reglas de autorizacion por rol,
- facilitan detectar fallos de configuracion en seguridad,
- documentan el comportamiento esperado de cada capa,
- respaldan la entrega con evidencia reproducible.

## 8. Recomendaciones de mejora

- Agregar pruebas de integracion para flujos completos de venta.
- Incorporar validaciones de stock y reglas reales en carrito e inventario.
- Publicar `README.md` con comandos de ejecucion y cobertura.
- Mantener JaCoCo en el pipeline para revisar cobertura en cada cambio.
- Revisar mensajes y codificacion UTF-8 en archivos del proyecto.
