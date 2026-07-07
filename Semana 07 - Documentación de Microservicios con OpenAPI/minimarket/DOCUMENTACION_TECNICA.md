# Documentación técnica OpenAPI - Minimarket Plus

## 1. Resumen técnico del avance respecto a semanas anteriores

Durante las semanas anteriores se trabajó en la base funcional del backend: autenticación, estructura de servicios, persistencia con JPA, separación por capas y control de acceso. Ese avance permitió llegar a esta semana con controladores y entidades ya definidos, por lo que la documentación OpenAPI pudo construirse sobre contratos REST reales y no sobre un diseño preliminar.

La integración actual fortalece el proyecto porque transforma los endpoints existentes en una interfaz consumible por otros equipos. En particular, la implementación de Swagger UI facilita la verificación visual de los servicios, mientras que el JSON generado por `/v3/api-docs` permite validación externa e interoperabilidad con herramientas como Postman.

## 2. Análisis de la documentación generada con OpenAPI

### Endpoints documentados

Se documentaron los controladores de:
- `Producto`
- `Carrito`

En `Producto` se documentaron los siguientes endpoints:
- `GET /api/productos`
- `GET /api/productos/{id}`
- `POST /api/productos`
- `PUT /api/productos/{id}`
- `DELETE /api/productos/{id}`

En `Carrito` se documentaron los siguientes endpoints:
- `GET /api/carrito`
- `GET /api/carrito/{id}`
- `POST /api/carrito`
- `PUT /api/carrito/{id}`
- `DELETE /api/carrito/{id}`

### Nivel de detalle aplicado

Se incorporaron:
- `@Operation` para resumir y describir cada método.
- `@ApiResponses` para indicar respuestas esperadas como `200`, `204` y `404`.
- `@Schema` en las entidades `Producto` y `Carrito` para explicar campos y ejemplos.
- Configuración general de OpenAPI con título, versión y contacto.

### Problemas encontrados

El principal problema fue que el proyecto no tenía la dependencia de `springdoc-openapi-starter-webmvc-ui`, por lo que Swagger UI no estaba disponible inicialmente. Además, la configuración de seguridad bloqueaba el acceso a `/swagger-ui.html` y `/v3/api-docs`.

Otro punto a considerar fue que las entidades usan relaciones `@ManyToOne`, lo que puede generar documentación más compleja si no se describen adecuadamente los modelos relacionados.

### Mejoras aplicadas

- Se agregó la dependencia de Springdoc en `pom.xml`.
- Se habilitó el acceso público a Swagger UI y al JSON de OpenAPI en la configuración de seguridad.
- Se documentaron las entidades con `@Schema` para hacer más claro el modelo de datos.
- Se mantuvo la estructura original de los controladores, agregando solo metadatos de documentación.

## 3. Evidencia de ejecución

### Swagger UI

Se debe adjuntar captura de:
- `http://localhost:8080/swagger-ui.html`

### JSON exportado

Se debe adjuntar el archivo generado desde:
- `http://localhost:8080/v3/api-docs`

### Validación externa

Se debe importar el JSON en Postman y verificar que:
- Los paths coincidan con la documentación.
- Los métodos respondan según los códigos declarados.
- Los esquemas de request y response sean consistentes.

## 4. Reflexión técnica

OpenAPI aporta calidad al backend porque formaliza los contratos de los servicios y reduce la ambigüedad al integrarlos. También mejora el mantenimiento, ya que los cambios en los endpoints quedan visibles en Swagger UI y pueden validarse de forma rápida.

Para mantener la documentación actualizada en futuras versiones se recomienda:
- Documentar los controladores al mismo tiempo que se implementan.
- Revisar Swagger UI en cada entrega.
- Validar el JSON generado como parte de las pruebas de integración.
- Mantener ejemplos de entrada y salida alineados con los modelos reales.

La experiencia del usuario podría mejorar con:
- Más ejemplos de request body.
- Descripciones más detalladas de validaciones y errores.
- Agrupación por tags o módulos funcionales.
- Inclusión de autenticación JWT en Swagger cuando corresponda.

## 5. Recomendaciones de mejora para futuras versiones

- Crear DTOs para evitar exponer entidades directamente.
- Agregar ejemplos específicos en request/response de todos los endpoints.
- Documentar también los módulos de `Usuario`, `Venta`, `Inventario` y `DetalleVenta`.
- Incorporar seguridad OpenAPI si se desea probar autenticación desde Swagger.
- Automatizar la exportación del JSON de OpenAPI en el pipeline de construcción.
