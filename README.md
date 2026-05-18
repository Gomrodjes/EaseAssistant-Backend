# EaseAssistant Backend

![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

Sistema de servidor que potencia **EaseAssistant**, una plataforma integral para conectar profesionales de servicios con clientes. Gestiona autenticación, perfiles, reservas, pagos, correos electrónicos y documentación a través de una API REST segura y bien estructurada.

---

## Características

- API REST completa y bien documentada
- CRUD de entidades principales: usuarios, reservas, servicios, pagos y categorías
- Autenticación segura con JWT (JSON Web Tokens)
- Autorización basada en roles (cliente, profesional, administrador)
- Validaciones avanzadas con Jakarta Bean Validation
- Manejo global de errores con respuestas consistentes
- Base de datos PostgreSQL con cifrado de contraseñas BCrypt
- Verificación de cuentas por correo electrónico (SendGrid)
- Subida y gestión de archivos (documentos e imágenes)
- Paginación, ordenamiento y filtros dinámicos en listados
- Logging detallado de operaciones

---

## Stack tecnológico

| Categoría     | Tecnología                                          |
|---------------|-----------------------------------------------------|
| Lenguaje      | Java 21                                             |
| Framework     | Spring Boot 4.0.6                                   |
| Seguridad     | Spring Security, JWT (JJWT 0.11.5), BCrypt          |
| Persistencia  | Spring Data JPA, Hibernate                          |
| Base de datos | PostgreSQL 17                                       |
| Validación    | Jakarta Bean Validation (Hibernate Validator)       |
| Mapping       | ModelMapper 2.1.1                                   |
| Email         | SendGrid Java SDK 4.10.3                            |
| Utilidades    | Lombok                                              |
| Build         | Maven                                               |
| Testing       | JUnit, Spring Boot Test, Spring Security Test       |

---

## Instalación

### Prerrequisitos

- JDK 21 o superior
- Maven (incluido en el wrapper)
- PostgreSQL 17 o superior
- Git

### Pasos

**1. Clonar el repositorio**

```bash
git clone https://github.com/Gomrodjes/EaseAssistant-Backend.git
cd EaseAssistant-Backend
```

**2. Configurar la base de datos**

Crea una base de datos en PostgreSQL (ejemplo: `ease_assistant`) y asegúrate de que tu usuario tenga los privilegios necesarios.

**3. Configurar variables de entorno**

Copia el archivo `.env.example` como `.env` en la raíz del proyecto y rellena los valores (ver sección [Variables de entorno](#variables-de-entorno)).

**4. Ejecutar la aplicación**

```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8082`.

---

## Variables de entorno

Crea un archivo `.env` en la raíz del proyecto con las siguientes variables:

```env
# Base de datos PostgreSQL
DB_URL=jdbc:postgresql://localhost:5432/ease_assistant
DB_USER=postgres
DB_PASSWORD=tu_contraseña

# JWT (usa una clave larga y segura)
JWT_SECRET=tu_clave_secreta_muy_larga_y_segura

# SendGrid (para correos electrónicos)
SENDGRID_API_KEY=tu_api_key_de_sendgrid

# Puerto del servidor (opcional)
PORT=8082
```

> **Nota:** Las variables de entorno tienen prioridad sobre `application.properties`. No subas el archivo `.env` al control de versiones.

---

## API — Endpoints principales

La API usa el prefijo `/api/v1`. Para los endpoints protegidos, incluye el token en el header:
`Authorization: Bearer <token>`

### Autenticación

| Método | Endpoint                              | Descripción                          |
|--------|---------------------------------------|--------------------------------------|
| POST   | `/auth/register`                      | Registro de nuevo usuario            |
| POST   | `/auth/login`                         | Inicio de sesión (devuelve JWT)      |
| PUT    | `/auth/select-account-type/{id}`      | Actualizar rol tras el registro      |
| POST   | `/auth/send/verification`             | Enviar correo de verificación        |
| GET    | `/auth/verify/{id}`                   | Verificar cuenta de usuario          |
| GET    | `/auth/verification-status/{id}`      | Consultar estado de verificación     |

### Usuarios

| Método | Endpoint       | Descripción                          |
|--------|----------------|--------------------------------------|
| GET    | `/users`       | Listado de usuarios (paginado)       |
| GET    | `/users/{id}`  | Detalles de un usuario               |
| PUT    | `/users/{id}`  | Actualizar perfil de usuario         |

### Reservas

| Método | Endpoint                  | Descripción                      |
|--------|---------------------------|----------------------------------|
| POST   | `/bookings`               | Crear una nueva reserva          |
| GET    | `/bookings/{id}`          | Detalles de una reserva          |
| PUT    | `/bookings/{id}/status`   | Cambiar estado de una reserva    |

### Pagos

| Método | Endpoint                        | Descripción                      |
|--------|---------------------------------|----------------------------------|
| POST   | `/payments`                     | Procesar un pago                 |
| GET    | `/payments/booking/{bookingId}` | Pagos asociados a una reserva    |

### Servicios y categorías

| Método | Endpoint          | Descripción                                 |
|--------|-------------------|---------------------------------------------|
| GET    | `/jobs`           | Listar servicios disponibles (con filtros)  |
| POST   | `/jobs`           | Crear un nuevo servicio (profesionales)     |
| GET    | `/categories`     | Obtener todas las categorías                |

### Documentación

| Método | Endpoint                        | Descripción                          |
|--------|---------------------------------|--------------------------------------|
| POST   | `/documentation/upload`         | Subir documentación para verificación|
| GET    | `/documentation/user/{userId}`  | Obtener documentos de un usuario     |

---

## Estructura del proyecto

```
EaseAssistant-Backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/          # Configuración de Spring (seguridad, CORS, etc.)
│   │   │   ├── controllers/     # Controladores REST
│   │   │   ├── email/           # Servicio de envío de correos (SendGrid)
│   │   │   ├── entities/        # Entidades JPA
│   │   │   ├── enums/           # Enumeraciones (roles, estados, etc.)
│   │   │   ├── models/          # DTOs y modelos de transferencia
│   │   │   ├── repositories/    # Repositorios Spring Data JPA
│   │   │   ├── security/        # Configuración de seguridad y JWT
│   │   │   └── services/        # Lógica de negocio
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/       # Plantillas de correo
│   └── test/                    # Pruebas unitarias e integración
├── .mvn/wrapper/
├── mvnw
├── mvnw.cmd
├── pom.xml
└── .env.example
```

---

## Tests

```bash
# Ejecutar todas las pruebas
./mvnw test

# Ejecutar una clase específica
./mvnw test -Dtest=NombreDeLaClaseTest
```

Las pruebas se encuentran en `src/test/java/com/example/demo/`.

---

## Autor

**Jesús A. Gómez Rodríguez** · [@Gomrodjes](https://github.com/Gomrodjes)

---

## Licencia

Distribuido bajo la licencia [MIT](LICENSE).
