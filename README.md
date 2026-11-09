# Directorio de funcionarios

Taller Spring Boot (Campus Lands): API REST + página HTML/CSS/JS vanilla. Java 21, Spring Boot, patrón MVC (controller / service / repository / model) y MySQL.

## Ejecutar con MySQL local

1. Tener MySQL en `localhost:3306` (la base `directorio` se crea sola).
2. Credenciales por defecto: usuario `root` sin contraseña. Se cambian con las variables `DB_URL`, `DB_USER` y `DB_PASSWORD`.
3. `./mvnw spring-boot:run` y abrir <http://localhost:8080>.

## Ejecutar con MySQL en la nube (TiDB Cloud Serverless, gratis)

1. Crear una cuenta en <https://tidbcloud.com> y un cluster **Starter** compatible con MySQL.
2. En **Connect** generar la contraseña y copiar Host, Port y User.
3. Crear `src/main/resources/application-local.properties` (está ignorado por Git, no se sube):

```properties
spring.datasource.url=jdbc:mysql://HOST:4000/directorio?createDatabaseIfNotExist=true&sslMode=VERIFY_IDENTITY
spring.datasource.username=USUARIO
spring.datasource.password=CONTRASEÑA
```

4. `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`

## Pruebas

`./mvnw test` (usan H2 en memoria, no requieren MySQL).

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
