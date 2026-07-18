# Minimarket Plus

## Requisitos
- Java 17
- Maven

## Ejecución
```bash
mvn spring-boot:run
```

## Swagger UI
Una vez iniciado el proyecto, abrir:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`

## Documentación OpenAPI
El proyecto incluye documentación OpenAPI para los endpoints de:
- `Producto`
- `Carrito`

## Notas
- Swagger UI y el JSON de OpenAPI están habilitados sin autenticación.
- El proyecto usa H2 en memoria.
