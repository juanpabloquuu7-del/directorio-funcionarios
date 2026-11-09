# Directorio de funcionarios

Taller Spring Boot (Campus Lands): API REST + página HTML/CSS/JS vanilla. Java 21, Spring Boot, patrón MVC (controller / service / repository / model) y MySQL.

## Ejecutar

1. Tener MySQL en `localhost:3306` (la base `directorio` se crea sola).
2. Credenciales por defecto `root`/`root`; cambiarlas con las variables `DB_URL`, `DB_USER`, `DB_PASSWORD`.
3. `./mvnw spring-boot:run` y abrir <http://localhost:8080>.

Pruebas (usan H2 en memoria, no requieren MySQL): `./mvnw test`

## API `/api/v1/funcionarios`

| Operación | Método y ruta | Respuesta |
|---|---|---|
| Listar | `GET /api/v1/funcionarios` | 200 / 204 si vacío |
| Consultar | `GET /{id}` | 200 / 404 |
| Registrar | `POST` | 201 / 400 |
| Actualizar | `PUT /{id}` | 200 / 400 / 404 |
| Eliminar | `DELETE /{id}` | 204 / 404 |
| Buscar por nombre | `GET ?nombre=ana` (parcial, sin mayúsculas) | 200 / 204 |
| Filtrar por departamento | `GET ?departamento=Contabilidad` (exacto, sin mayúsculas) | 200 / 204 |

Si llegan ambos filtros se aplica el de nombre; los vacíos se ignoran.
