Event Venue API
API REST desarrollada con Java 21 + Spring Boot para la gestión de eventos y lugares (venues). El proyecto implementa operaciones CRUD básicas, validaciones de negocio, manejo global de excepciones, documentación OpenAPI/Swagger, carga de datos iniciales y pruebas unitarias con Mockito.

Nombre del proyecto: event-Venue
Versión: 0.0.1-SNAPSHOT
Versión de Java: 21
Framework: Spring Boot 4.1.1
Tipo de aplicación: API REST
Almacenamiento actual: memoria (listas Java), sin base de datos persistente

📋 Índice
Descripción

Objetivo

Tecnologías

Arquitectura

Estructura del proyecto

Modelo de datos

Endpoints

Eventos

Lugares

Validaciones de negocio

Manejo de excepciones

Datos iniciales

Swagger / OpenAPI

Pruebas

Cómo ejecutar

Ejemplos con JSON

Flujo interno de una petición

Detalles importantes de implementación

Limitaciones actuales

Mejoras recomendadas

Resumen

🎯 Descripción
Event Venue es una API REST orientada a administrar dos recursos principales:

Events: eventos que tienen un identificador, nombre, fecha/hora y descripción.

Venues: lugares donde pueden organizarse actividades, con nombre, dirección y capacidad.

La aplicación está organizada siguiendo una separación sencilla por capas:

Cliente HTTP
│
▼
Controller
│
▼
Service
│
▼
DAO
│
▼
Memoria (List)
Además, las excepciones generadas por las capas de negocio son centralizadas mediante un @RestControllerAdvice.

🧰 Tecnologías
Tecnología	Uso
Java 21	Lenguaje principal
Spring Boot 4.1.1	Framework de la aplicación
Spring Web MVC	Creación de endpoints REST
Lombok	Generación de getters, setters y constructores
SpringDoc OpenAPI	Documentación de la API
Maven	Gestión de dependencias y construcción
JUnit 5	Pruebas unitarias
Mockito	Mocking de dependencias en pruebas
Dependencias principales definidas en pom.xml:

spring-boot-starter-webmvc

lombok

spring-boot-starter-webmvc-test

springdoc-openapi-starter-webmvc-ui

🏗️ Arquitectura
El proyecto utiliza una arquitectura por capas.

1. Model
   Contiene las clases que representan los datos de la aplicación:

Event

Venue

Estas clases son simples POJOs y utilizan Lombok para reducir código repetitivo.

2. Controller
   Expone los endpoints HTTP:

EventController

VenueController

Los controladores reciben las peticiones, delegan la lógica al servicio y construyen las respuestas HTTP.

3. Service
   Contiene las reglas de negocio:

EventService

VenueService

Aquí se realizan validaciones como:

campos obligatorios;

fechas válidas;

nombres duplicados;

existencia de identificadores;

restricciones antes de eliminar o actualizar.

4. DAO
   La capa DAO administra actualmente los datos en memoria:

EventDao

VenueDao

Se utilizan ArrayList para almacenar los objetos y AtomicLong para generar identificadores.

5. Exception
   Centraliza las excepciones específicas de la aplicación:

EventNotFoundException
FechaNotFoundException
InvaliDataException
NotDuplicateException
NotNullFechaException
NotNullValueException
VenueNotFoundException
GlobalExceptionHandler
6. Config
   Incluye:

OpenApiConfig: configuración de OpenAPI.

DataSeedersConfig: carga de información inicial.

📁 Estructura del proyecto
```text
event-Venue/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── README.md
├── .gitignore
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/events/event_Venue/
│   │   │       ├── EventVenueApplication.java
│   │   │       │
│   │   │       ├── config/
│   │   │       │   ├── DataSeedersConfig.java
│   │   │       │   └── OpenApiConfig.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   ├── EventController.java
│   │   │       │   └── VenueController.java
│   │   │       │
│   │   │       ├── dao/
│   │   │       │   ├── EventDao.java
│   │   │       │   └── VenueDao.java
│   │   │       │
│   │   │       ├── exception/
│   │   │       │   ├── EventNotFoundException.java
│   │   │       │   ├── FechaNotFoundException.java
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   ├── InvaliDataException.java
│   │   │       │   ├── NotDuplicateException.java
│   │   │       │   ├── NotNullFechaException.java
│   │   │       │   ├── NotNullValueException.java
│   │   │       │   └── VenueNotFoundException.java
│   │   │       │
│   │   │       ├── model/
│   │   │       │   ├── Event.java
│   │   │       │   └── Venue.java
│   │   │       │
│   │   │       └── service/
│   │   │           ├── EventService.java
│   │   │           └── VenueService.java
│   │   │
│   │   └── resources/
│   │       └── application.yaml
│   │
│   └── test/
│       └── java/
│           └── com/events/event_Venue/
│               ├── EventVenueApplicationTests.java
│               └── service/
│                   ├── EventServiceTest.java
│                   └── VenueServiceTest.java
│
└── target/
└── archivos generados por Maven
```
target/ es una carpeta generada por Maven. No es necesario conservarla en el control de versiones.
```text
🧩 Modelo de datos
Event
Representa un evento.

Campo	Tipo	Descripción
id	Long	Identificador único
nombre	String	Nombre del evento
fecha	LocalDateTime	Fecha y hora programadas
descripcion	String	Descripción del evento
Ejemplo:

{
"id": 1,
"nombre": "Conferencia spring boot",
"fecha": "2026-10-15T09:00:00",
"descripcion": "Evento tecnico sobre desarrollo de backend"
}
Venue
Representa un lugar.

Campo	Tipo	Descripción
id	Long	Identificador único
nombre	String	Nombre del lugar
direccion	String	Dirección o ubicación registrada
capacidad	Long	Capacidad máxima del lugar
Ejemplo:

{
"id": 1,
"nombre": "Teatro amira de la rosa",
"direccion": "Barranquilla",
"capacidad": 1000
}
🌐 Endpoints
Eventos
Base URL:

/api/events
Método	Endpoint	Función
POST	/api/events	Crear evento
GET	/api/events	Listar eventos
GET	/api/events/buscar?fecha=...	Buscar evento por fecha/hora
PUT	/api/events	Actualizar evento
DELETE	/api/events/{id}	Eliminar evento
Lugares
Base URL:

/api/venues
Método	Endpoint	Función
POST	/api/venues	Crear lugar
GET	/api/venues	Listar lugares
PUT	/api/venues	Actualizar lugar
DELETE	/api/venues/{id}	Eliminar lugar
📅 Eventos
Crear evento
Request
POST /api/events
Content-Type: application/json
Body:

{
"nombre": "Conferencia de Java",
"fecha": "2026-12-10T10:30:00",
"descripcion": "Conferencia sobre desarrollo de aplicaciones Java"
}
También es posible enviar un id, aunque si no se proporciona o es menor/igual a cero, el DAO genera uno automáticamente.

Respuesta exitosa
Código:

201 Created
Ejemplo:

{
"id": 4,
"nombre": "Conferencia de Java",
"fecha": "2026-12-10T10:30:00",
"descripcion": "Conferencia sobre desarrollo de aplicaciones Java"
}
Listar eventos
GET /api/events
Respuesta:

[
{
"id": 1,
"nombre": "Conferencia spring boot",
"fecha": "2026-10-15T09:00:00",
"descripcion": "Evento tecnico sobre desarrollo de backend"
},
{
"id": 2,
"nombre": "Meetup de java",
"fecha": "2026-10-15T18:30:00",
"descripcion": "Networking para desarrolladores"
}
]
Código:

200 OK
Buscar evento por fecha
GET /api/events/buscar?fecha=2026-10-15T09:00:00
El controlador recibe la fecha como LocalDateTime.

Si existe un evento con exactamente esa fecha y hora, devuelve el evento.

Código exitoso:

200 OK
Si no existe:

400 Bad Request
con el mensaje de la excepción correspondiente.

La búsqueda utiliza coincidencia exacta de fecha y hora. Por ejemplo, 09:00:00 y 09:00:01 son valores diferentes.

Actualizar evento
PUT /api/events
Content-Type: application/json
Ejemplo:

{
"id": 1,
"nombre": "Conferencia Spring Boot 2026",
"fecha": "2026-10-20T10:00:00",
"descripcion": "Conferencia actualizada sobre desarrollo backend"
}
Respuesta:

200 OK
Evento actualizado exitosamente
Eliminar evento
DELETE /api/events/1
Si existe:

200 OK
Evento eliminado exitosamente
Si no existe, se genera EventNotFoundException.

📍 Lugares
Crear lugar
POST /api/venues
Content-Type: application/json
Body:

{
"nombre": "Centro de Convenciones",
"direccion": "Barranquilla",
"capacidad": 2500
}
Respuesta:

201 Created
Ejemplo:

{
"id": 4,
"nombre": "Centro de Convenciones",
"direccion": "Barranquilla",
"capacidad": 2500
}
Listar lugares
GET /api/venues
Ejemplo de respuesta:

[
{
"id": 1,
"nombre": "Teatro amira de la rosa",
"direccion": "Barranquilla",
"capacidad": 1000
},
{
"id": 2,
"nombre": "Centro de eventos puerta de oro",
"direccion": "Barranquilla",
"capacidad": 200
},
{
"id": 3,
"nombre": "Coliseo elias chegwin",
"direccion": "Barranquilla",
"capacidad": 4000
}
]
Actualizar lugar
PUT /api/venues
Content-Type: application/json
Ejemplo:

{
"id": 1,
"nombre": "Teatro actualizado",
"direccion": "Barranquilla",
"capacidad": 1500
}
Respuesta:

200 OK
El cuerpo contiene el objeto actualizado.

Eliminar lugar
DELETE /api/venues/1
Respuesta:

200 OK
Lugar eliminado exitosamente...
✅ Validaciones de negocio
Eventos
Al crear un evento, EventService valida:

1. ID duplicado
   Si se proporciona un ID positivo y ya existe, se lanza:

EventNotFoundException
2. Nombre obligatorio
   El nombre no puede ser:
```
null

vacío

solamente espacios

Excepción:

NotNullValueException
3. Fecha obligatoria y futura
   La fecha:

no puede ser null;

no puede estar en el pasado.

Excepción:

NotNullFechaException
4. No duplicar nombre + fecha
   No se permite registrar dos eventos con el mismo nombre exactamente en la misma fecha/hora.

Excepción:

NotDuplicateException
5. Descripción obligatoria
   La descripción no puede estar vacía.

Excepción:

NotNullValueException
Lugares
Al crear un lugar se valida:

1. ID
   No debe existir otro registro con el mismo ID positivo.

2. Nombre obligatorio
   No puede estar vacío.

3. Nombre único
   No puede existir otro lugar con el mismo nombre.

4. Dirección obligatoria
   No puede estar vacía.

5. Capacidad
   Debe existir y ser mayor que 0.

🚨 Manejo de excepciones
El proyecto utiliza GlobalExceptionHandler, implementado con:

@RestControllerAdvice
Este componente captura las excepciones de negocio y responde con:

400 Bad Request
El mensaje de la excepción se devuelve directamente como cuerpo de la respuesta.

Ejemplo:

Ya existe un evento con ese nombre programado para esa hora.
Las excepciones manejadas son:

EventNotFoundException
FechaNotFoundException
NotDuplicateException
NotNullFechaException
InvaliDataException
VenueNotFoundException
NotNullValueException
Ventaja
Esto evita repetir bloques de manejo de excepciones en cada controlador.

El flujo queda:
```text
Controller
│
▼
Service
│
├── operación válida ──► respuesta normal
│
└── excepción ─────────► GlobalExceptionHandler
│
▼
400 Bad Request
🌱 Datos iniciales
DataSeedersConfig utiliza CommandLineRunner para insertar datos automáticamente cuando inicia la aplicación.
```
Venues iniciales
ID	Nombre	Dirección	Capacidad
1	Teatro amira de la rosa	Barranquilla	1000
2	Centro de eventos puerta de oro	Barranquilla	200
3	Coliseo elias chegwin	Barranquilla	4000
Eventos iniciales
ID	Nombre	Fecha	Descripción
1	Conferencia spring boot	2026-10-15 09:00	Evento técnico sobre desarrollo de backend
2	Meetup de java	2026-10-15 18:30	Networking para desarrolladores
3	Hackathon backend	2026-11-15 08:00	Competencia de programación de 24 horas
Como los datos se almacenan en memoria, se vuelven a cargar al iniciar una nueva ejecución de la aplicación.

📚 Swagger / OpenAPI
El proyecto incluye:

springdoc-openapi-starter-webmvc-ui
y una configuración personalizada llamada OpenApiConfig.

La API se identifica como:

Eventify API
Versión:

1.0
Descripción:

API para la gestion de eventos y lugares
Con SpringDoc, la interfaz Swagger UI normalmente se encuentra en:

http://localhost:8080/swagger-ui/index.html
La especificación OpenAPI suele estar disponible en:

http://localhost:8080/v3/api-docs
Estas rutas dependen de que la aplicación esté ejecutándose en el puerto por defecto y de la configuración de SpringDoc.

🧪 Pruebas
El proyecto contiene pruebas unitarias para los servicios:

EventServiceTest
VenueServiceTest
Se utiliza:

JUnit 5

Mockito

@Mock

@InjectMocks

assertThrows

verify

when

EventServiceTest
Se prueban escenarios como:

creación válida;

ID cero o menor;

ID duplicado;

nombre vacío;

fecha pasada;

nombre + fecha duplicados;

descripción vacía;

búsqueda sin resultados;

eliminación exitosa;

eliminación de un ID inexistente.

VenueServiceTest
Se prueban escenarios como:

creación válida;

nombre vacío;

nombre duplicado;

dirección vacía;

capacidad inválida;

actualización válida;

actualización de un lugar inexistente;

nombre duplicado durante actualización;

eliminación exitosa;

eliminación de un ID inexistente.

Ejecutar pruebas
Con Maven Wrapper:

./mvnw test
En Windows:

.\mvnw.cmd test
Con Maven instalado:

mvn test
▶️ Cómo ejecutar el proyecto
Requisitos
Se recomienda tener:

Java 21

Maven 3.9+ o utilizar Maven Wrapper

IDE compatible con Spring Boot, como IntelliJ IDEA, Eclipse o VS Code

Comprobar Java:

java -version
La versión principal esperada es:

21
Opción 1: Maven Wrapper
Linux / macOS
Desde la carpeta del proyecto:

./mvnw spring-boot:run
Windows
.\mvnw.cmd spring-boot:run
Opción 2: Maven instalado
mvn spring-boot:run
Opción 3: ejecutar el JAR
Construir:

./mvnw clean package
Después:

java -jar target/event-Venue-0.0.1-SNAPSHOT.jar
En Windows:

.\mvnw.cmd clean package
🔎 Comprobación rápida
Una vez iniciada la aplicación, se puede comprobar que funciona consultando:

GET http://localhost:8080/api/events
y:

GET http://localhost:8080/api/venues
Si la aplicación está recién iniciada, deberían aparecer los datos cargados por DataSeedersConfig.
```text
🧪 Ejemplos completos para probar la API
Crear evento
curl -X POST http://localhost:8080/api/events \
-H "Content-Type: application/json" \
-d '{
"nombre": "Taller de Spring Boot",
"fecha": "2026-12-20T14:00:00",
"descripcion": "Taller práctico de desarrollo backend"
}'
Listar eventos
curl http://localhost:8080/api/events
Buscar evento
curl "http://localhost:8080/api/events/buscar?fecha=2026-10-15T09:00:00"
Actualizar evento
curl -X PUT http://localhost:8080/api/events \
-H "Content-Type: application/json" \
-d '{
"id": 1,
"nombre": "Conferencia Spring Boot actualizada",
"fecha": "2026-12-20T16:00:00",
"descripcion": "Nueva descripción del evento"
}'
Eliminar evento
curl -X DELETE http://localhost:8080/api/events/1
Crear venue
curl -X POST http://localhost:8080/api/venues \
-H "Content-Type: application/json" \
-d '{
"nombre": "Auditorio Principal",
"direccion": "Barranquilla",
"capacidad": 1200
}'
Listar venues
curl http://localhost:8080/api/venues
Actualizar venue
curl -X PUT http://localhost:8080/api/venues \
-H "Content-Type: application/json" \
-d '{
"id": 1,
"nombre": "Auditorio Principal Actualizado",
"direccion": "Barranquilla",
"capacidad": 1500
}'
Eliminar venue
curl -X DELETE http://localhost:8080/api/venues/1
```
🔄 Flujo interno de una petición
Por ejemplo, para crear un evento:
```text
POST /api/events
│
▼
EventController.create()
│
▼
EventService.create()
│
├── valida ID
├── valida nombre
├── valida fecha
├── valida duplicados
└── valida descripción
│
▼
EventDao.guardar()
│
├── genera ID si es necesario
└── agrega Event a la lista
│
▼
HTTP 201 Created
Si alguna validación falla:

EventService
│
▼
RuntimeException
│
▼
GlobalExceptionHandler
│
▼
HTTP 400 Bad Request
```
💾 Persistencia de datos
Actualmente no se utiliza una base de datos.

Los DAO mantienen los datos en estructuras de memoria:

private final List<Event> events = new ArrayList<>();
y:

private final List<Venue> venues = new ArrayList<>();
Los IDs se generan con:

AtomicLong
Esto significa que:

los datos existen mientras la aplicación está ejecutándose;

al detener la aplicación, los cambios realizados se pierden;

al iniciar nuevamente, se cargan los datos de DataSeedersConfig;

el proyecto es adecuado para aprendizaje, prototipos y ejercicios, pero necesita persistencia real para un entorno productivo.

⚠️ Detalles importantes de implementación
1. Los datos no tienen relación Event ↔ Venue
   Aunque el nombre del proyecto sugiere una gestión de eventos y lugares, actualmente Event no contiene un campo que lo relacione con Venue.

Por ejemplo, no existe:

private Long venueId;
ni una relación JPA entre ambas entidades.

Por tanto, actualmente la API administra eventos y lugares como recursos independientes.

2. La búsqueda por fecha es exacta
   EventDao.buscarFecha() utiliza:

e.getFecha().equals(fecha)
Esto significa que se busca una coincidencia exacta de LocalDateTime.

No existe actualmente:

búsqueda por día;

búsqueda por rango;

búsqueda por mes;

filtros por nombre.

3. La API no tiene autenticación
   No se observa una implementación de:

Spring Security;

JWT;

OAuth2;

usuarios;

roles;

permisos.

Por lo tanto, los endpoints están diseñados como una API sin autenticación.

4. No hay DTOs
   Los controladores reciben directamente:

@RequestBody Event event
y:

@RequestBody Venue venue
En una arquitectura más robusta se podrían utilizar DTOs para separar el contrato HTTP de los modelos internos.

5. No se utilizan validaciones Bean Validation
   Las reglas están implementadas manualmente dentro de los servicios.

Por ejemplo:

if (event.getNombre() == null || event.getNombre().isBlank())
Una evolución natural sería utilizar:

@NotBlank
@NotNull
@Positive
@Future
junto con @Valid.

🛠️ Mejoras recomendadas
Estas mejoras no forman parte de la implementación actual, pero serían pasos naturales para evolucionar el proyecto.

Prioridad funcional
Base de datos
Migrar de listas en memoria a una base de datos utilizando:

Spring Data JPA;

PostgreSQL o MySQL;

entidades @Entity;

repositorios JpaRepository.

Esto permitiría conservar los datos después de reiniciar la aplicación.

Relación Event-Venue
Agregar una relación entre eventos y lugares.

Una posible estructura sería:

Venue 1 ───────── N Event
Es decir, un lugar podría tener varios eventos.

DTOs
Crear, por ejemplo:

EventRequest
EventResponse
VenueRequest
VenueResponse
para controlar mejor los datos de entrada y salida.

Bean Validation
Agregar validaciones declarativas:

@NotBlank
private String nombre;

@NotNull
@Future
private LocalDateTime fecha;
y:

@NotNull
@Positive
private Long capacidad;
Calidad de API
Respuestas de error estructuradas
Actualmente el error puede devolverse como texto plano.

Una API más completa podría devolver:

{
"timestamp": "2026-09-16T19:00:00",
"status": 400,
"error": "Bad Request",
"message": "El nombre no puede estar vacío",
"path": "/api/events"
}
Códigos HTTP más específicos
Sería posible diferenciar:

400 Bad Request → datos inválidos;

404 Not Found → recurso inexistente;

409 Conflict → duplicados;

201 Created → creación exitosa;

200 OK → operación exitosa.

Logging
Agregar SLF4J/Logback para registrar:

creación de eventos;

modificaciones;

eliminaciones;

errores;

información de ejecución.

CORS
Si la API será consumida desde un frontend separado, podría requerirse configuración CORS.

🧹 Limpieza del repositorio
La carpeta target/ contiene archivos generados durante la compilación.

Para un repositorio Git limpio, normalmente se recomienda ignorarla:

target/
También conviene evitar subir archivos específicos del IDE, por ejemplo:

.idea/
El proyecto ya incluye .gitignore, por lo que se recomienda revisar que contenga estas carpetas.

🧠 Resumen de responsabilidades
Componente	Responsabilidad
Event	Modelo de evento
Venue	Modelo de lugar
EventController	Endpoints HTTP de eventos
VenueController	Endpoints HTTP de lugares
EventService	Reglas de negocio de eventos
VenueService	Reglas de negocio de lugares
EventDao	Gestión en memoria de eventos
VenueDao	Gestión en memoria de lugares
GlobalExceptionHandler	Manejo centralizado de errores
DataSeedersConfig	Datos iniciales
OpenApiConfig	Información de OpenAPI
EventServiceTest	Pruebas del servicio de eventos
VenueServiceTest	Pruebas del servicio de lugares
EventVenueApplication	Punto de entrada de Spring Boot
📌 Estado actual del proyecto
Implementado
API REST de eventos

API REST de lugares

Crear eventos

Listar eventos

Buscar eventos por fecha/hora

Actualizar eventos

Eliminar eventos

Crear lugares

Listar lugares

Actualizar lugares

Eliminar lugares

Validaciones de negocio

Excepciones personalizadas

Manejo global de excepciones

Generación automática de IDs

Datos iniciales

Documentación OpenAPI

Pruebas unitarias de servicios

No implementado actualmente
Base de datos

Persistencia permanente

Autenticación

Autorización

Relación entre evento y lugar

DTOs

Bean Validation

Paginación

Filtros avanzados

Documentación detallada por endpoint

Docker

CI/CD

🚀 Conclusión
Event Venue es una API REST construida con Spring Boot que demuestra una estructura clara de aplicación backend separando controladores, servicios, DAO, modelos, configuración y manejo de excepciones.

El proyecto permite administrar eventos y lugares mediante operaciones CRUD y aplica reglas básicas de negocio para evitar datos inválidos y duplicados. También incorpora datos iniciales, pruebas unitarias con Mockito y soporte para documentación OpenAPI.

La implementación actual utiliza almacenamiento en memoria, por lo que constituye una base sencilla y clara para aprender o desarrollar un prototipo. Para convertirla en una solución más completa, los siguientes pasos naturales serían incorporar una base de datos, relacionar eventos con lugares, utilizar DTOs y Bean Validation, mejorar el formato de errores y agregar seguridad.

👨‍💻 Comandos rápidos
# Ejecutar
./mvnw spring-boot:run

# Ejecutar pruebas
./mvnw test

# Compilar
./mvnw clean package

# Ejecutar el JAR
java -jar target/event-Venue-0.0.1-SNAPSHOT.jar
Endpoints principales
GET    /api/events
POST   /api/events
GET    /api/events/buscar?fecha=YYYY-MM-DDTHH:MM:SS
PUT    /api/events
DELETE /api/events/{id}

GET    /api/venues
POST   /api/venues
PUT    /api/venues
DELETE /api/venues/{id}
Documentación generada a partir de la estructura y código fuente incluidos en el proyecto event-Venue.

