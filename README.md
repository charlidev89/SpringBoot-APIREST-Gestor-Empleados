# Proyecto de Turnos Rotativos - Java SpringBoot

## Descripción
El siguiente proyecto gestiona Los Turnos Rotativos de Empleados, jornada laboral y Concepto Laboral,
mediante la arquitectura API REST - MVC y la implementación del protocolo HTTP.

## Tecnologías Utilizadas
- Java 17
- Spring Boot 3.3.3
- Spring Data JPA
- H2 Database (Base de datos en memoria , para desarrollo y pruebas)
- Maven: como gestionador de proyecto
- JUnit 5 : utilizado pruebas unitarias
- Mockito : usado para mocking en pruebas
- Lombok: que permite mediante anotaciones la reducción de código.
- Validation API : usado para validaciones de datos

## Estructura del Proyecto
El proyecto sigue una arquitectura de capas estándar MVC:
- `model`: Entidades y mapeo JPA
- `repository`: Interfaces de repositorio para acceso a datos
- `service`: Lógica de negocio y manejo de excepciones
- `controller`: maneja las request y responses de los Endpoints de la API REST
- `dto`: Patron de diseño de objeto de transferencia de datos
- `exception`: Manejo de excepciones personalizadas

## Configuración y Ejecución
1. Clonar  el repositorio  / o abrir el proyecto desde el ide
2. Navega al directorio del proyecto con la terminal
3. Ejecutar `mvn spring-boot:run`
4. La API estará disponible en `http://localhost:8080`

## Pruebas
Para ejecutar las pruebas unitarias:
mvn test

## Colección de Postman
Se incluye una colección de Postman para probar los endpoints de la API en formato JSON
el cual se encuentra en la carpeta principal del proyecto.

Archivo: `ApiRestFull Turnos Rotativos.postman_collection.json`

Para utilizarla:
1. Abrir Postman
2. Haz clic en "Import"
3. Selecciona el archivo JSON mencionado
4. Una vez importada, tendrás acceso a todos los requests predefinidos para probar la API

## Características Principales
- CRUD completo para Empleados
- Gestión de Conceptos Laborales
- Registro y consulta de Jornadas Laborales
- Validaciones de negocio para jornadas y empleados
- Manejo de excepciones personalizado

## Endpoints Principales

### Empleados
- `POST /empleado`: Crea un nuevo empleado
    - Body: JSON con datos del empleado
- `GET /empleado`: Obtiene todos los empleados
- `GET /empleado/{id}`: Obtiene  un empleado  por ID
- `PUT /empleado/{id}`: Actualiza un empleado existente
    - Body: JSON con datos actualizados del empleado
- `DELETE /empleado/{id}`: Elimina un empleado

### Conceptos Laborales
- `GET /concepto-laboral`: Obtiene todos los conceptos laborales
- `GET /concepto-laboral?id={id}`: Obtiene un concepto laboral específico por ID
- `GET /concepto-laboral?nombre={nombre}`: Obtiene conceptos laborales por nombre

### Jornadas Laborales
- `POST /jornada`: Crea una nueva jornada laboral
    - Body: JSON con datos de la jornada
- `GET /jornada`: Obtiene todas las jornadas laborales
- `GET /jornada?fechaDesde={fecha}&fechaHasta={fecha}&nroDocumento={nroDocumento}`: Obtener jornadas laborales con filtros
    - `fechaDesde`: Fecha de inicio del rango (opcional)
    - `fechaHasta`: Fecha de fin del rango (opcional)
    - `nroDocumento`: Número de documento del empleado (opcional)
  
### Consideraciones importantes:
-El siguiente proyecto aplica buenas practicas aplicando el patron de diseño DTO para transferir objetos entre capas , manejamos las excepciones mediante un @controllerAdvice  GlobalExceptionHandler para manejar las excepciones de forma global
-además utilizamos validaciones  en las entidades ,  asi como tambien dejamos que cada capa se encarguie de su responsabilidad.

## Autor:
Carlos Arteaga.
