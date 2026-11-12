# Plan: autenticación HTTP Basic

## Alcance

Proteger la API del directorio con Spring Security y HTTP Basic, y agregar a la interfaz un formulario de usuario y contraseña. Las credenciales se configuran mediante variables de entorno y no se guardan en el repositorio.

## Fases

1. **Seguridad del servidor**
   - Añadir el starter de Spring Security.
   - Crear una `SecurityFilterChain`: recursos estáticos públicos y API protegida con HTTP Basic.
   - Cargar usuario y contraseña desde variables de entorno; comparar la contraseña con BCrypt.
   - Skills: `security-and-hardening`, `test-driven-development`.

2. **Interfaz de acceso**
   - Añadir formulario accesible de usuario y contraseña.
   - Validar las credenciales con una llamada protegida; enviar Basic en las llamadas API y olvidar las credenciales al rechazarlas.
   - Mantener credenciales solo en memoria durante la sesión de la página.
   - Skills: `security-and-hardening`, `test-driven-development`, `ponytail`.

3. **Configuración y documentación**
   - Documentar variables requeridas y ejecución.
   - Skills: `security-and-hardening`.

## Criterios de aceptación

- Sin credenciales, la API rechaza solicitudes y la página muestra el formulario.
- Credenciales válidas habilitan el directorio; las inválidas no lo habilitan.
- Todas las operaciones API usan HTTP Basic.
- No hay contraseñas reales ni valores inseguros en el repositorio.

## Riesgo

HTTP Basic requiere HTTPS fuera de desarrollo local porque el encabezado puede recuperarse fácilmente si el tráfico no está cifrado.
