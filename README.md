# 🍳 Recetario — Sabor que Inspira

Aplicación web para crear, consultar y administrar recetas de cocina. El proyecto integra autenticación de usuarios y control de acceso por roles, persistencia de información en MySQL y una interfaz web construida con Thymeleaf.

## ✨ Funcionalidades

- Registro e inicio de sesión de usuarios.
- Gestión de acceso mediante roles: **USER, CHEF y ADMIN**.
- Consulta de recetas con título, descripción, ingredientes, pasos, imagen y autor.
- Creación de recetas por usuarios con rol **CHEF** o **ADMIN**.
- Edición y eliminación de recetas desde el panel de administración.
- Carga y visualización de imágenes asociadas a las recetas.
- Paneles diferenciados según el rol del usuario autenticado.
- Protección de rutas y operaciones mediante **Spring Security**.

## 🛠️ Tecnologías

- **Java 21**
- **Spring Boot 3.5.6**
- Spring Web
- Spring Security
- Spring Data JPA
- Thymeleaf
- Thymeleaf Extras Spring Security
- MySQL
- Maven
- HTML / CSS / JavaScript

## 🏗️ Estructura

El backend está organizado por responsabilidades:

- `controller`: manejo de rutas y peticiones web.
- `model`: entidades JPA como `Receta` y `UserEntity`.
- `repository`: acceso a datos mediante Spring Data JPA.
- `service`: lógica relacionada con autenticación y usuarios.
- `config`: configuración de Spring Security.
- `templates`: vistas Thymeleaf.
- `static`: hojas de estilo e imágenes.

## 🚀 Ejecución local

### Requisitos

- JDK 21
- MySQL
- Maven o Maven Wrapper

### Pasos

1. Clona el repositorio:

```bash
git clone https://github.com/Ana030507/Recetas.git
cd Recetas
```

2. Crea una base de datos MySQL para el proyecto.

3. Configura las credenciales y parámetros de conexión en `src/main/resources/application.properties`.

4. Ejecuta la aplicación:

**Windows**
```bash
mvnw.cmd spring-boot:run
```

**Linux / macOS**
```bash
./mvnw spring-boot:run
```

5. Abre la aplicación desde el navegador en la dirección configurada por Spring Boot.

## 👥 Roles

| Rol | Acciones |
|---|---|
| USER | Consultar recetas |
| CHEF | Consultar y crear recetas |
| ADMIN | Consultar, crear, editar y eliminar recetas |

## 📌 Nota

Este proyecto fue desarrollado como proyecto académico para practicar desarrollo web con Java, Spring Boot, persistencia de datos, autenticación y autorización por roles.

## 👩‍💻 Autora

**Ana María Cabrera Silva**  
GitHub: https://github.com/Ana030507
