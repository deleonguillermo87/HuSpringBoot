# Event Venue - Proyecto Spring Boot por semanas

Este repositorio contiene una secuencia de proyectos académicos para aprender Java + Spring Boot, arquitectura por capas, validación, JPA, DTOs, manejo de errores y pruebas automatizadas.

## Objetivo general

La aplicación gestiona eventos y lugares (venues) con las siguientes capacidades:

- CRUD completo de eventos y lugares
- validación de entrada con Bean Validation
- DTOs de entrada y salida
- mapeo con MapStruct
- manejo centralizado de errores con Problem Detail RFC 7807
- documentación OpenAPI / Swagger
- formularios web con Thymeleaf y retroalimentación visual
- pruebas unitarias e integración ligera con JUnit 5 y Mockito

## Estructura del repositorio

```text
HuSpringBoot/
├── Semana1/
├── Semana2/
├── Semana3/
├── Semana4/
├── Semana5/
├── README.md
└── .gitignore
```

## Evolución por semanas

| Semana | Estado | Enfoque principal |
|---|---|---|
| Semana1 | Base inicial | CRUD y estructura del proyecto |
| Semana2 | Persistencia | JPA, repositorios y PostgreSQL |
| Semana3 | Lógica de negocio | Servicios, validaciones y capas |
| Semana4 | Proyecto consolidado | Pruebas, configuración y ejecución real |
| Semana5 | Modernización | DTOs, MapStruct, validación robusta y Problem Details |

## Tecnologías

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- H2 (solo para pruebas)
- Lombok
- MapStruct
- SpringDoc OpenAPI / Swagger
- Thymeleaf
- Maven
- JUnit 5
- Mockito

## Configuración de la base de datos

La aplicación usa PostgreSQL para ejecución real y H2 en pruebas.

### PostgreSQL local

```properties
spring.application.name=eventify
spring.datasource.url=jdbc:postgresql://localhost:5433/postgres
spring.datasource.username=postgres
spring.datasource.password=eventify123
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=none
```

### Ejecutar PostgreSQL con Docker

```bash
docker run --name eventify-db \
  -e POSTGRES_DB=postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=eventify123 \
  -p 5433:5432 \
  -d postgres:16
```

## Cómo ejecutar una semana

```bash
cd HuSpringBoot/Semana1
mvn spring-boot:run
```

```bash
cd HuSpringBoot/Semana2
mvn spring-boot:run
```

```bash
cd HuSpringBoot/Semana3
mvn spring-boot:run
```

```bash
cd HuSpringBoot/Semana4
mvn spring-boot:run
```

```bash
cd HuSpringBoot/Semana5
mvn spring-boot:run
```

## Verificación y pruebas

Se validó la semana 5 con Maven:

```bash
cd HuSpringBoot/Semana5
mvn test
```

Estado verificado en esta sesión: EXIT_CODE:0.

## Semana 5: entregables clave

La semana 5 incorpora una evolución importante de la arquitectura para cumplir con la historia de usuario solicitada:

- DTOs de entrada y salida para eventos y venues
- mapeo con MapStruct
- validación declarativa con Bean Validation
- validación personalizada para fechas futuras y no pasadas
- manejo centralizado de errores con ProblemDetail RFC 7807
- integración con Thymeleaf para retroalimentación visual en formularios
- control de contratos REST más limpios y consistentes
- pruebas ajustadas para validar el comportamiento esperado

## Swagger / OpenAPI

Cuando la app está levantada, la documentación queda en:

- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/v3/api-docs

## Estructura general de la aplicación

```text
Controller
   ↓
Service
   ↓
Mapper / DTO
   ↓
Repository / DAO
   ↓
Base de datos
```

## Importante sobre los rojos del editor

Los archivos marcados en rojo dentro de VS Code pueden deberse a un problema de indexación del proyecto Java en el editor, no a un fallo real del build. La validación con Maven es la referencia correcta y, en esta sesión, quedó confirmada como exitosa.

## Resumen

El repositorio refleja un camino de aprendizaje real y progresivo: desde CRUD simple hasta una API más estructurada, validada y documentada, con un cierre importante en Semana5 usando DTOs y validaciones robustas.


```bash
cd "c:\Users\guill\Desktop\Hu 1 a 5\HuSpringBoot\Semana4"
mvn -q test
```

Resultado verificado: `✅ TESTS_OK`

## 💡 Recomendación práctica

Para usar la aplicación en su estado más completo, ejecuta la semana 4 y asegúrate de tener PostgreSQL levantado en el puerto 5433 con las credenciales descritas.

## ⚠️ Nota importante

El repositorio no es un único proyecto compilado de una sola vez; cada semana representa la evolución del mismo sistema. Por esa razón, lo correcto es abrir la semana exacta que se desea ejecutar o revisar.

---

Si quieres, puedo dejarte ahora una versión final todavía más pulida con ejemplos de request/response JSON, diagrama de arquitectura y guía de troubleshooting para PostgreSQL y Swagger.
