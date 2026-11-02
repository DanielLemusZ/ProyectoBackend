# ProyectoBackend

Backend para la **Gestión de voluntariado comunitario**: publica actividades, gestiona inscripciones y registra asistencia respetando cupos y fechas.

---

## Tecnologías

| Tecnología              | Versión / detalle                          |
|-------------------------|--------------------------------------------|
| Java                    | 21                                         |
| Spring Boot             | 4.1.1                                      |
| Spring Web MVC          | API REST                                   |
| Spring Data JPA         | Persistencia (Hibernate)                   |
| Jakarta Bean Validation | Validación de los DTOs de petición (`spring-boot-starter-validation`) |
| PostgreSQL              | Base de datos `DaMano_db`                  |
| Lombok                  | 1.18.46 (versión administrada por Spring Boot). Genera getters y constructores. |
| Maven                   | Usar el wrapper incluido: `./mvnw`         |

## Estado del proyecto

Seguimiento de lo implementado y lo pendiente. Actualizar esta tabla con cada avance.

| Componente                                         | Estado        | Detalle                                                              |
|----------------------------------------------------|---------------|----------------------------------------------------------------------|
| Endpoint de estado (`GET /api/estado`)             | Hecho     | Verifica que la API está disponible.                                 |
| Dependencias de persistencia (JPA + PostgreSQL)    | Hecho     | Agregadas en `pom.xml`.                                              |
| Modelo relacional de la base de datos              | Hecho     | Ver [Modelo de base de datos relacional](#modelo-de-base-de-datos-relacional). |
| Entidades JPA con relaciones `@ManyToOne`          | Hecho     | Ver [Implementación de entidades JPA](#implementación-de-entidades-jpa). |
| Conexión a `DaMano_db` en `application.properties` | Hecho     | La contraseña llega por la variable de entorno `DB_PASSWORD`. Ver [Configuración y ejecución](#configuración-y-ejecución). |
| Repositorios (`JpaRepository`)                     | Hecho     | Ver [Repositorios Spring Data JPA](#repositorios-spring-data-jpa).   |
| Query methods                                      | Hecho     | Las seis entidades. Ver [Repositorios Spring Data JPA](#repositorios-spring-data-jpa). |
| Contrato de la API REST                            | Hecho     | Ver [Contrato de la API REST](#contrato-de-la-api-rest).             |
| DTOs de petición y respuesta                       | Hecho     | Ver [DTOs de petición y respuesta](#dtos-de-petición-y-respuesta).   |
| Lombok en entidades, `Response` e inyección de dependencias | Hecho | Entidades sin setters, `Response` inmutables y `@RequiredArgsConstructor` en servicios y controladores. Ver [Convenciones del código](#convenciones-del-código). |
| Servicios con reglas de negocio (RN-01 a RN-06)    | Hecho | `OrganizacionServicio`, `CoordinadorServicio`, `VoluntarioServicio`, `ActividadServicio`, `InscripcionServicio` y `AsistenciaServicio`. Ver [Servicios y controladores](#servicios-y-controladores). |
| Controladores del contrato HTTP                    | Hecho | `OrganizacionController` (endpoints 2 a 10), `CoordinadorController` (endpoints 11 a 17), `VoluntarioController` (endpoints 18 a 25), `ActividadController` (endpoints 26 a 33), `InscripcionController` (endpoints 34 a 38) y `AsistenciaController` (endpoints 39 a 42). Ver [resumen de endpoints](#resumen-de-endpoints). |
| Manejo de errores (400, 404, 409)                  | Pendiente | Los códigos ya se responden; falta el formato común del cuerpo de error. Ver [Códigos de estado HTTP](#códigos-de-estado-http). |

## Estructura del proyecto

```
src/main/java/co/edu/autonoma/reservasapi/
├── ProyectoBackendApplication.java   # Clase principal de Spring Boot
├── controller/
│   ├── EstadoController.java         # GET /api/estado
│   ├── OrganizacionController.java   # /api/organizaciones
│   ├── CoordinadorController.java    # /api/coordinadores
│   ├── VoluntarioController.java     # /api/voluntarios
│   ├── ActividadController.java      # /api/actividades
│   ├── InscripcionController.java    # /api/inscripciones
│   └── AsistenciaController.java     # /api/asistencias
├── dto/
│   ├── OrganizacionRequest.java                # record: POST y PUT de organizaciones
│   ├── CoordinadorRequest.java                 # record: POST de coordinadores
│   ├── CoordinadorActualizacionRequest.java    # record: PUT de coordinadores
│   ├── VoluntarioRequest.java                  # record: POST y PUT de voluntarios
│   ├── ActividadRequest.java                   # record: POST de actividades
│   ├── ActividadActualizacionRequest.java      # record: PUT de actividades
│   ├── ActividadEstadoRequest.java             # record: PATCH del estado de una actividad
│   ├── InscripcionRequest.java                 # record: POST de inscripciones
│   ├── AsistenciaRequest.java                  # record: PATCH de asistencia de una inscripción
│   ├── AsistenciaActualizacionRequest.java     # record: PUT de asistencias
│   ├── EstadoResponse.java                     # clase: respuesta de GET /api/estado
│   ├── OrganizacionResponse.java               # clase: respuesta de organizaciones
│   ├── ParticipacionOrganizacionResponse.java  # clase: resumen de participación de una organización
│   ├── ParticipacionActividadResponse.java     # clase: participación por actividad
│   ├── CoordinadorResponse.java                # clase: respuesta de coordinadores
│   ├── VoluntarioResponse.java                 # clase: respuesta de voluntarios
│   ├── ActividadResponse.java                  # clase: respuesta de actividades, con cupos
│   ├── InscripcionResponse.java                # clase: respuesta de inscripciones
│   └── AsistenciaResponse.java                 # clase: respuesta de asistencias
├── entity/
│   ├── OrganizacionEntity.java       # Entidad JPA → tabla organizacion
│   ├── CoordinadorEntity.java        # Entidad JPA → tabla coordinador
│   ├── VoluntarioEntity.java         # Entidad JPA → tabla voluntario
│   ├── ActividadEntity.java          # Entidad JPA → tabla actividad
│   ├── InscripcionEntity.java        # Entidad JPA → tabla inscripcion
│   ├── AsistenciaEntity.java         # Entidad JPA → tabla asistencia
│   ├── EstadoActividad.java          # Enum: estados de una actividad
│   └── EstadoInscripcion.java        # Enum: estados de una inscripción
├── repository/
│   ├── OrganizacionRepository.java   # JpaRepository<OrganizacionEntity, Integer>
│   ├── CoordinadorRepository.java    # JpaRepository<CoordinadorEntity, Integer>
│   ├── VoluntarioRepository.java     # JpaRepository<VoluntarioEntity, Integer>
│   ├── ActividadRepository.java      # JpaRepository<ActividadEntity, Integer>
│   ├── InscripcionRepository.java    # JpaRepository<InscripcionEntity, Integer>
│   └── AsistenciaRepository.java     # JpaRepository<AsistenciaEntity, Integer>
└── service/
    ├── EstadoServicio.java           # Lógica del endpoint de estado
    ├── OrganizacionServicio.java     # Reglas, mapeo y participación de organizaciones
    ├── CoordinadorServicio.java      # Reglas y mapeo de coordinadores
    ├── VoluntarioServicio.java       # Reglas y mapeo de voluntarios
    ├── ActividadServicio.java        # Reglas, transiciones de estado y mapeo de actividades
    ├── InscripcionServicio.java      # Reglas RN-01, RN-02, RN-03 y RN-06, y mapeo de inscripciones
    └── AsistenciaServicio.java       # Regla RN-04 y mapeo de asistencias
```

| Paquete       | Contenido                                                               |
|---------------|-------------------------------------------------------------------------|
| `entity/`     | Clases `@Entity` y enums del negocio. Toda entidad termina en `Entity`. |
| `repository/` | Interfaces que extienden `JpaRepository`.                               |
| `dto/`        | Entrada y salida de la API: records `Request` y clases `Response`; ninguna clase `@Entity`. |
| `service/`    | Clases `@Service` terminadas en `Servicio`: reglas de negocio y conversión entidad a `Response`. |
| `controller/` | Clases `@RestController`: reciben DTOs, delegan en el servicio y devuelven el código HTTP. |

## Configuración y ejecución

La conexión está en `src/main/resources/application.properties`. La contraseña de PostgreSQL no se escribe en el archivo: se lee de la variable de entorno `DB_PASSWORD`.

| Entorno            | Cómo definir la variable                                                          |
|--------------------|-----------------------------------------------------------------------------------|
| IntelliJ IDEA      | Run → Edit Configurations… → Environment variables: `DB_PASSWORD=tu_clave`         |
| Windows PowerShell | `$env:DB_PASSWORD="tu_clave"` y luego `.\mvnw.cmd spring-boot:run`                 |
| macOS / Linux      | `export DB_PASSWORD=tu_clave` y luego `./mvnw spring-boot:run`                     |

Lombok está declarado como dependencia `<optional>true</optional>` y excluido del jar en el `spring-boot-maven-plugin`, porque solo se usa al compilar. Se ejecuta como procesador de anotaciones, declarado en `annotationProcessorPaths` del `maven-compiler-plugin`. Desde el JDK 23, `javac` no lo ejecuta si solo está en el classpath. En IntelliJ IDEA debe estar activado Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing. Los avisos `sun.misc.Unsafe ... lombok.permit.Permit` que aparecen al compilar con JDK 25 son de Lombok y no impiden la compilación.

Si la consola muestra `password authentication failed for user "postgres"`, la variable no llegó a la aplicación: revisar el nombre y reiniciar la ejecución.

## Endpoints implementados

| Verbo  | Ruta                            | Descripción                                                  | Respuesta de ejemplo                                   |
|--------|---------------------------------|--------------------------------------------------------------|--------------------------------------------------------|
| GET    | `/api/estado`                   | Indica si la API está disponible.                            | `{"servicio": "DaMano-api", "estado": "disponible"}`   |
| GET    | `/api/organizaciones`           | Lista todas las organizaciones.                              | `[{"id": 1, "nombre": "...", "nit": "...", ...}]`      |
| GET    | `/api/organizaciones/{id}`      | Consulta una organización. 404 si no existe.                 | `OrganizacionResponse`                                 |
| GET    | `/api/organizaciones/nit/{nit}` | Busca una organización por NIT. 404 si no existe.            | `OrganizacionResponse`                                 |
| POST   | `/api/organizaciones`           | Registra una organización. 409 si el NIT o el correo ya existen. | `201` con la `OrganizacionResponse` creada              |
| PUT    | `/api/organizaciones/{id}`      | Reemplaza los datos. 404 si no existe; 409 si el NIT o el correo pertenecen a otra. | `OrganizacionResponse`              |
| DELETE | `/api/organizaciones/{id}`      | Elimina la organización. 409 si tiene coordinadores o actividades. | `204` sin cuerpo                                 |
| GET    | `/api/organizaciones/{id}/coordinadores` | Lista los coordinadores de la organización. 404 si no existe. | `List<CoordinadorResponse>`                    |
| GET    | `/api/coordinadores`            | Lista todos los coordinadores.                               | `[{"id": 1, "organizacionId": 1, "organizacionNombre": "...", ...}]` |
| GET    | `/api/coordinadores/{id}`       | Consulta un coordinador. 404 si no existe.                   | `CoordinadorResponse`                                  |
| GET    | `/api/coordinadores/email/{email}` | Busca un coordinador por correo. 404 si no existe.        | `CoordinadorResponse`                                  |
| POST   | `/api/coordinadores`            | Registra un coordinador. 404 si la organización no existe; 409 si el correo ya existe. | `201` con la `CoordinadorResponse` creada |
| PUT    | `/api/coordinadores/{id}`       | Reemplaza los datos personales, sin cambiar la organización. 404 si no existe; 409 si el correo pertenece a otro. | `CoordinadorResponse` |
| DELETE | `/api/coordinadores/{id}`       | Elimina el coordinador. 409 si tiene actividades o asistencias. | `204` sin cuerpo                                    |
| GET    | `/api/voluntarios`              | Lista todos los voluntarios.                                 | `[{"id": 1, "documento": "...", "nombre": "...", ...}]` |
| GET    | `/api/voluntarios/{id}`         | Consulta un voluntario. 404 si no existe.                    | `VoluntarioResponse`                                   |
| GET    | `/api/voluntarios/documento/{documento}` | Busca un voluntario por documento. 404 si no existe. | `VoluntarioResponse`                                   |
| POST   | `/api/voluntarios`              | Registra un voluntario. 409 si el documento o el correo ya existen. | `201` con la `VoluntarioResponse` creada         |
| PUT    | `/api/voluntarios/{id}`         | Reemplaza los datos. 404 si no existe; 409 si el documento o el correo pertenecen a otro. | `VoluntarioResponse`       |
| DELETE | `/api/voluntarios/{id}`         | Elimina el voluntario. 409 si tiene inscripciones en cualquier estado. | `204` sin cuerpo                             |
| GET    | `/api/organizaciones/{id}/actividades` | Lista las actividades de la organización, en cualquier estado. 404 si no existe. | `List<ActividadResponse>`          |
| GET    | `/api/organizaciones/{id}/participacion` | Resume inscripciones, asistencias y horas por actividad, con totales. 404 si no existe. | `ParticipacionOrganizacionResponse` |
| GET    | `/api/coordinadores/{id}/actividades` | Lista las actividades del coordinador, en cualquier estado. 404 si no existe. | `List<ActividadResponse>`               |
| GET    | `/api/actividades?estado={estado}` | Sin filtro lista las abiertas a inscripción; con filtro, las de ese estado. 400 si el estado no es válido. | `List<ActividadResponse>` |
| GET    | `/api/actividades/{id}`         | Consulta una actividad con sus cupos. 404 si no existe.      | `ActividadResponse`                                    |
| POST   | `/api/actividades`              | Crea una actividad `ABIERTA`. 400 si las fechas son incoherentes; 404 si el coordinador no existe. | `201` con la `ActividadResponse` creada |
| PUT    | `/api/actividades/{id}`         | Reemplaza los datos. 404 si no existe; 409 si tiene inscripciones `CONFIRMADA` o está `CANCELADA` o `FINALIZADA`. | `ActividadResponse` |
| PATCH  | `/api/actividades/{id}/estado`  | Cambia el estado. 400 si se pide `CANCELADA`; 404 si no existe; 409 si la transición no está permitida. | `ActividadResponse` |
| DELETE | `/api/actividades/{id}`         | Cancelación lógica de la actividad y sus inscripciones. 404 si no existe; 409 si está `FINALIZADA` o tiene asistencias. | `200` con la `ActividadResponse` |
| GET    | `/api/voluntarios/{id}/inscripciones?estado={estado}` | Lista las inscripciones del voluntario. 400 si el estado no es válido; 404 si no existe. | `List<InscripcionResponse>` |
| GET    | `/api/actividades/{id}/inscripciones?estado={estado}` | Lista las inscripciones de la actividad. 400 si el estado no es válido; 404 si no existe. | `List<InscripcionResponse>` |
| GET    | `/api/inscripciones?estado={estado}` | Lista las inscripciones, con filtro opcional. 400 si el estado no es válido. | `List<InscripcionResponse>`                |
| GET    | `/api/inscripciones/{id}`       | Consulta una inscripción. 404 si no existe.                  | `InscripcionResponse`                                  |
| POST   | `/api/inscripciones`            | Inscribe a un voluntario (RN-06, RN-03, RN-02 y RN-01). 404 si el voluntario o la actividad no existen; 409 si se viola una regla. | `201` con la `InscripcionResponse` creada; `200` si la clave ya existe |
| DELETE | `/api/inscripciones/{id}`       | Cancelación lógica. 404 si no existe; 409 si ya pasó `fechaCierre`. | `200` con la `InscripcionResponse`                |
| PATCH  | `/api/inscripciones/{id}/asistencia` | Registra la asistencia. 400 si las horas no son válidas; 404 si la inscripción o el coordinador no existen; 409 si se viola una regla. | `200` con la `AsistenciaResponse` creada |
| GET    | `/api/voluntarios/{id}/asistencias` | Lista el historial de asistencias del voluntario. 404 si no existe. | `List<AsistenciaResponse>`                     |
| GET    | `/api/actividades/{id}/asistencias` | Lista las asistencias de la actividad. 404 si no existe.  | `List<AsistenciaResponse>`                             |
| GET    | `/api/asistencias`              | Lista todas las asistencias.                                 | `List<AsistenciaResponse>`                             |
| GET    | `/api/asistencias/{id}`         | Consulta una asistencia. 404 si no existe.                   | `AsistenciaResponse`                                   |
| PUT    | `/api/asistencias/{id}`         | Corrige `horas` y `observaciones`. 400 si las horas no son válidas; 404 si no existe. | `AsistenciaResponse`          |
| DELETE | `/api/asistencias/{id}`         | Elimina la asistencia. 404 si no existe.                     | `204` sin cuerpo                                       |

## Contrato de la API REST

Diseño de las peticiones HTTP de la API. Están implementados todos los endpoints: `GET /api/estado`, los endpoints 2 a 10 de organizaciones, los endpoints 11 a 17 de coordinadores, los endpoints 18 a 25 de voluntarios, los endpoints 26 a 33 de actividades, los endpoints 34 a 38 de inscripciones y los endpoints 39 a 42 de asistencias. Los DTOs que reciben y devuelven ya existen (ver [DTOs de petición y respuesta](#dtos-de-petición-y-respuesta)).

### Resumen de endpoints

La columna Estado indica si el endpoint ya está implementado (controlador y servicio). Actualizarla a medida que se implementa cada uno.

| #  | Verbo    | Ruta                                                 | Request                            | Response                              | Éxito | Estado    |
|----|----------|------------------------------------------------------|------------------------------------|---------------------------------------|-------|-----------|
| 1  | `GET`    | `/api/estado`                                        | —                                  | `EstadoResponse`                      | 200   | Hecho     |
| 2  | `GET`    | `/api/organizaciones`                                | —                                  | `List<OrganizacionResponse>`          | 200   | Hecho     |
| 3  | `GET`    | `/api/organizaciones/{id}`                           | —                                  | `OrganizacionResponse`                | 200   | Hecho     |
| 4  | `GET`    | `/api/organizaciones/nit/{nit}`                      | —                                  | `OrganizacionResponse`                | 200   | Hecho     |
| 5  | `POST`   | `/api/organizaciones`                                | `OrganizacionRequest`              | `OrganizacionResponse`                | 201   | Hecho     |
| 6  | `PUT`    | `/api/organizaciones/{id}`                           | `OrganizacionRequest`              | `OrganizacionResponse`                | 200   | Hecho     |
| 7  | `DELETE` | `/api/organizaciones/{id}`                           | —                                  | —                                     | 204   | Hecho     |
| 8  | `GET`    | `/api/organizaciones/{id}/coordinadores`             | —                                  | `List<CoordinadorResponse>`           | 200   | Hecho     |
| 9  | `GET`    | `/api/organizaciones/{id}/actividades`               | —                                  | `List<ActividadResponse>`             | 200   | Hecho     |
| 10 | `GET`    | `/api/organizaciones/{id}/participacion`             | —                                  | `ParticipacionOrganizacionResponse`   | 200   | Hecho     |
| 11 | `GET`    | `/api/coordinadores`                                 | —                                  | `List<CoordinadorResponse>`           | 200   | Hecho     |
| 12 | `GET`    | `/api/coordinadores/{id}`                            | —                                  | `CoordinadorResponse`                 | 200   | Hecho     |
| 13 | `GET`    | `/api/coordinadores/email/{email}`                   | —                                  | `CoordinadorResponse`                 | 200   | Hecho     |
| 14 | `POST`   | `/api/coordinadores`                                 | `CoordinadorRequest`               | `CoordinadorResponse`                 | 201   | Hecho     |
| 15 | `PUT`    | `/api/coordinadores/{id}`                            | `CoordinadorActualizacionRequest`  | `CoordinadorResponse`                 | 200   | Hecho     |
| 16 | `DELETE` | `/api/coordinadores/{id}`                            | —                                  | —                                     | 204   | Hecho     |
| 17 | `GET`    | `/api/coordinadores/{id}/actividades`                | —                                  | `List<ActividadResponse>`             | 200   | Hecho     |
| 18 | `GET`    | `/api/voluntarios`                                   | —                                  | `List<VoluntarioResponse>`            | 200   | Hecho     |
| 19 | `GET`    | `/api/voluntarios/{id}`                              | —                                  | `VoluntarioResponse`                  | 200   | Hecho     |
| 20 | `GET`    | `/api/voluntarios/documento/{documento}`             | —                                  | `VoluntarioResponse`                  | 200   | Hecho     |
| 21 | `POST`   | `/api/voluntarios`                                   | `VoluntarioRequest`                | `VoluntarioResponse`                  | 201   | Hecho     |
| 22 | `PUT`    | `/api/voluntarios/{id}`                              | `VoluntarioRequest`                | `VoluntarioResponse`                  | 200   | Hecho     |
| 23 | `DELETE` | `/api/voluntarios/{id}`                              | —                                  | —                                     | 204   | Hecho     |
| 24 | `GET`    | `/api/voluntarios/{id}/inscripciones?estado={estado}`| —                                  | `List<InscripcionResponse>`           | 200   | Hecho     |
| 25 | `GET`    | `/api/voluntarios/{id}/asistencias`                  | —                                  | `List<AsistenciaResponse>`            | 200   | Hecho     |
| 26 | `GET`    | `/api/actividades?estado={estado}`                   | —                                  | `List<ActividadResponse>`             | 200   | Hecho     |
| 27 | `GET`    | `/api/actividades/{id}`                              | —                                  | `ActividadResponse`                   | 200   | Hecho     |
| 28 | `POST`   | `/api/actividades`                                   | `ActividadRequest`                 | `ActividadResponse`                   | 201   | Hecho     |
| 29 | `PUT`    | `/api/actividades/{id}`                              | `ActividadActualizacionRequest`    | `ActividadResponse`                   | 200   | Hecho     |
| 30 | `PATCH`  | `/api/actividades/{id}/estado`                       | `ActividadEstadoRequest`           | `ActividadResponse`                   | 200   | Hecho     |
| 31 | `DELETE` | `/api/actividades/{id}`                              | —                                  | `ActividadResponse`                   | 200   | Hecho     |
| 32 | `GET`    | `/api/actividades/{id}/inscripciones?estado={estado}`| —                                  | `List<InscripcionResponse>`           | 200   | Hecho     |
| 33 | `GET`    | `/api/actividades/{id}/asistencias`                  | —                                  | `List<AsistenciaResponse>`            | 200   | Hecho     |
| 34 | `GET`    | `/api/inscripciones?estado={estado}`                 | —                                  | `List<InscripcionResponse>`           | 200   | Hecho     |
| 35 | `GET`    | `/api/inscripciones/{id}`                            | —                                  | `InscripcionResponse`                 | 200   | Hecho     |
| 36 | `POST`   | `/api/inscripciones`                                 | `InscripcionRequest`               | `InscripcionResponse`                 | 201 (200 si la clave ya existe) | Hecho     |
| 37 | `PATCH`  | `/api/inscripciones/{id}/asistencia`                 | `AsistenciaRequest`                | `AsistenciaResponse`                  | 200   | Hecho     |
| 38 | `DELETE` | `/api/inscripciones/{id}`                            | —                                  | `InscripcionResponse`                 | 200   | Hecho     |
| 39 | `GET`    | `/api/asistencias`                                   | —                                  | `List<AsistenciaResponse>`            | 200   | Hecho     |
| 40 | `GET`    | `/api/asistencias/{id}`                              | —                                  | `AsistenciaResponse`                  | 200   | Hecho     |
| 41 | `PUT`    | `/api/asistencias/{id}`                              | `AsistenciaActualizacionRequest`   | `AsistenciaResponse`                  | 200   | Hecho     |
| 42 | `DELETE` | `/api/asistencias/{id}`                              | —                                  | —                                     | 204   | Hecho     |

El parámetro `?estado=` es opcional en todas las rutas donde aparece. Si se omite, se devuelven todos los registros, salvo en `GET /api/actividades`, que sin filtro devuelve solo las actividades abiertas a inscripción.

### Códigos de estado HTTP

| Código             | Uso                                                                                                           |
|--------------------|---------------------------------------------------------------------------------------------------------------|
| `200 OK`           | Consulta, actualización, cancelación lógica o registro de asistencia (`PATCH`) exitoso.                       |
| `201 Created`      | Recurso creado. El cuerpo es la `Response` del nuevo recurso, con su `id`.                                    |
| `204 No Content`   | Eliminación física exitosa. Sin cuerpo.                                                                       |
| `400 Bad Request`  | Cuerpo inválido: validación de campos, fechas incoherentes, JSON mal formado o valor de enum desconocido.     |
| `404 Not Found`    | El recurso de la ruta, o un id referenciado en el cuerpo, no existe.                                          |
| `409 Conflict`     | Se viola una regla de negocio (RN-01 a RN-04, RN-06), un valor único, una dependencia que impide eliminar o el momento en que se permite la operación (editar una actividad con inscritos, cancelar una inscripción después del cierre). |

### Contrato HTTP mínimo del enunciado

El enunciado del proyecto exige estas seis operaciones. Todas están incluidas en el [resumen de endpoints](#resumen-de-endpoints).

| Verbo    | Ruta                                  | Responsabilidad según el enunciado                  | Cómo se cumple                                                             | Estado    |
|----------|---------------------------------------|-----------------------------------------------------|----------------------------------------------------------------------------|-----------|
| `GET`    | `/api/actividades`                    | Consulta abiertas.                                  | Sin filtro devuelve las actividades `ABIERTA` con `fechaCierre` futura.    | Hecho     |
| `GET`    | `/api/actividades/{id}/inscripciones` | Consulta del coordinador.                           | Inscripciones de la actividad, con filtro opcional `?estado=`.             | Hecho     |
| `POST`   | `/api/inscripciones`                  | Inscribe sin duplicar y controla cupo.              | Aplica RN-06, RN-03, RN-02 y RN-01, en ese orden.                          | Hecho     |
| `PUT`    | `/api/actividades/{id}`               | Reemplaza actividad antes de recibir inscripciones. | Responde 409 si la actividad ya tiene inscripciones `CONFIRMADA`.          | Hecho     |
| `PATCH`  | `/api/inscripciones/{id}/asistencia`  | Registra asistencia.                                | Crea la asistencia de la inscripción aplicando RN-04 y RN-05.              | Hecho     |
| `DELETE` | `/api/inscripciones/{id}`             | Cancela/elimina inscripción antes del cierre.       | Cancelación lógica; responde 409 si ya pasó `fechaCierre`.                 | Hecho     |

---

## Modelo de base de datos relacional

- **Motor:** PostgreSQL
- **Base de datos:** `DaMano_db`

### Entidades

| Entidad          | Tipo                  | Descripción                                                                                   |
|------------------|-----------------------|-----------------------------------------------------------------------------------------------|
| `organizacion`   | Fuerte                | Organización comunitaria que publica actividades y consulta la participación en ellas.       |
| `coordinador`    | Fuerte                | Usuario que pertenece a una organización, crea actividades y registra asistencia.            |
| `voluntario`     | Fuerte                | Usuario que consulta actividades y se inscribe en ellas.                                     |
| `actividad`      | Fuerte                | Actividad de voluntariado con cupo y fecha de cierre de inscripción.                         |
| `inscripcion`    | Débil (asociativa)    | Resuelve la relación N:M entre `voluntario` y `actividad`. No existe sin ambos.             |
| `asistencia`     | Débil                 | Registro de asistencia y horas de una inscripción confirmada. No existe sin la inscripción.  |

### Atributos

> **PK** = Clave primaria · **FK** = Clave foránea · **UQ** = Único · **NN** = No nulo

#### `organizacion`

| Atributo         | Tipo de dato   | Restricciones        | Descripción                         |
|------------------|----------------|----------------------|-------------------------------------|
| `id`             | `BIGINT`       | **PK**, identidad    | Identificador de la organización.   |
| `nombre`         | `VARCHAR(150)` | NN                   | Nombre de la organización.          |
| `nit`            | `VARCHAR(20)`  | NN, UQ               | Identificación tributaria.          |
| `email`          | `VARCHAR(150)` | NN, UQ               | Correo de contacto.                 |
| `telefono`       | `VARCHAR(20)`  |                      | Teléfono de contacto.               |
| `fecha_creacion` | `TIMESTAMP`    | NN, default `NOW()`  | Fecha de registro.                  |

#### `coordinador`

| Atributo          | Tipo de dato   | Restricciones                        | Descripción                         |
|-------------------|----------------|--------------------------------------|-------------------------------------|
| `id`              | `BIGINT`       | **PK**, identidad                    | Identificador del coordinador.      |
| `organizacion_id` | `BIGINT`       | **FK** → `organizacion(id)`, NN      | Organización a la que pertenece.    |
| `nombre`          | `VARCHAR(100)` | NN                                   | Nombres.                            |
| `apellido`        | `VARCHAR(100)` | NN                                   | Apellidos.                          |
| `email`           | `VARCHAR(150)` | NN, UQ                               | Correo electrónico.                 |
| `telefono`        | `VARCHAR(20)`  |                                      | Teléfono.                           |
| `fecha_creacion`  | `TIMESTAMP`    | NN, default `NOW()`                  | Fecha de registro.                  |

#### `voluntario`

| Atributo         | Tipo de dato   | Restricciones        | Descripción                         |
|------------------|----------------|----------------------|-------------------------------------|
| `id`             | `BIGINT`       | **PK**, identidad    | Identificador del voluntario.       |
| `documento`      | `VARCHAR(20)`  | NN, UQ               | Documento de identidad.             |
| `nombre`         | `VARCHAR(100)` | NN                   | Nombres.                            |
| `apellido`       | `VARCHAR(100)` | NN                   | Apellidos.                          |
| `email`          | `VARCHAR(150)` | NN, UQ               | Correo electrónico.                 |
| `telefono`       | `VARCHAR(20)`  |                      | Teléfono.                           |
| `fecha_creacion` | `TIMESTAMP`    | NN, default `NOW()`  | Fecha de registro.                  |

#### `actividad`

| Atributo              | Tipo de dato   | Restricciones                                                    | Descripción                                         |
|-----------------------|----------------|------------------------------------------------------------------|-----------------------------------------------------|
| `id`                  | `BIGINT`       | **PK**, identidad                                                | Identificador de la actividad.                      |
| `organizacion_id`     | `BIGINT`       | **FK** → `organizacion(id)`, NN                                  | Organización que publica la actividad.              |
| `coordinador_id`      | `BIGINT`       | **FK** → `coordinador(id)`, NN                                   | Coordinador que crea la actividad.                  |
| `titulo`              | `VARCHAR(150)` | NN                                                               | Título.                                             |
| `descripcion`         | `TEXT`         |                                                                  | Descripción.                                        |
| `ubicacion`           | `VARCHAR(200)` | NN                                                               | Lugar donde se realiza.                             |
| `fecha_inicio`        | `TIMESTAMP`    | NN                                                               | Inicio de la actividad.                             |
| `fecha_fin`           | `TIMESTAMP`    | NN, `CHECK (fecha_fin > fecha_inicio)`                           | Fin de la actividad.                                |
| `fecha_cierre`        | `TIMESTAMP`    | NN, `CHECK (fecha_cierre <= fecha_inicio)`                       | Fecha de cierre de inscripciones (RN-03).           |
| `cupo_maximo`         | `INTEGER`      | NN, `CHECK (cupo_maximo > 0)`                                    | Cupo total de voluntarios (RN-01).                  |
| `estado`              | `VARCHAR(20)`  | NN, `CHECK IN ('ABIERTA','CERRADA','CANCELADA','FINALIZADA')`    | Estado de la actividad.                             |
| `fecha_creacion`      | `TIMESTAMP`    | NN, default `NOW()`                                              | Fecha de creación.                                  |
| `fecha_actualizacion` | `TIMESTAMP`    | NN, default `NOW()`                                              | Última modificación (trazabilidad).                 |

#### `inscripcion`

| Atributo             | Tipo de dato   | Restricciones                                       | Descripción                                                   |
|----------------------|----------------|-----------------------------------------------------|---------------------------------------------------------------|
| `id`                 | `BIGINT`       | **PK**, identidad                                   | Identificador de la inscripción.                              |
| `actividad_id`       | `BIGINT`       | **FK** → `actividad(id)`, NN                        | Actividad a la que se inscribe.                               |
| `voluntario_id`      | `BIGINT`       | **FK** → `voluntario(id)`, NN                       | Voluntario inscrito.                                          |
| `clave_idempotencia` | `VARCHAR(100)` | NN, UQ                                              | Clave enviada por el cliente para no duplicar (RN-06).        |
| `estado`             | `VARCHAR(20)`  | NN, `CHECK IN ('CONFIRMADA','CANCELADA')`           | Estado de la inscripción.                                     |
| `fecha_inscripcion`  | `TIMESTAMP`    | NN, default `NOW()`                                 | Fecha en que se inscribió.                                    |
| `fecha_cancelacion`  | `TIMESTAMP`    |                                                     | Fecha de cancelación (cancelación lógica, trazabilidad).      |

#### `asistencia`

| Atributo          | Tipo de dato    | Restricciones                               | Descripción                                           |
|-------------------|-----------------|---------------------------------------------|-------------------------------------------------------|
| `id`              | `BIGINT`        | **PK**, identidad                           | Identificador de la asistencia.                       |
| `inscripcion_id`  | `BIGINT`        | **FK** → `inscripcion(id)`, NN, UQ          | Inscripción confirmada a la que corresponde (RN-04).  |
| `coordinador_id`  | `BIGINT`        | **FK** → `coordinador(id)`, NN              | Coordinador que registra la asistencia.               |
| `horas`           | `NUMERIC(5,2)`  | NN, `CHECK (horas > 0)`                     | Horas de voluntariado realizadas (RN-05).             |
| `observaciones`   | `TEXT`          |                                             | Observaciones del coordinador.                        |
| `fecha_registro`  | `TIMESTAMP`     | NN, default `NOW()`                         | Fecha en que se registró la asistencia.               |

### Claves primarias y foráneas

| Tabla          | Clave primaria | Clave foránea      | Referencia          |
|----------------|----------------|--------------------|---------------------|
| `organizacion` | `id`           | —                  | —                   |
| `coordinador`  | `id`           | `organizacion_id`  | `organizacion(id)`  |
| `voluntario`   | `id`           | —                  | —                   |
| `actividad`    | `id`           | `organizacion_id`  | `organizacion(id)`  |
|                |                | `coordinador_id`   | `coordinador(id)`   |
| `inscripcion`  | `id`           | `actividad_id`     | `actividad(id)`     |
|                |                | `voluntario_id`    | `voluntario(id)`    |
| `asistencia`   | `id`           | `inscripcion_id`   | `inscripcion(id)`   |
|                |                | `coordinador_id`   | `coordinador(id)`   |

### Relaciones

| Relación                              | Cardinalidad | Descripción                                                                                   |
|---------------------------------------|--------------|-----------------------------------------------------------------------------------------------|
| `organizacion` → `coordinador`        | 1 : N        | Una organización tiene muchos coordinadores; un coordinador pertenece a una organización.     |
| `organizacion` → `actividad`          | 1 : N        | Una organización publica muchas actividades; una actividad pertenece a una organización.     |
| `coordinador` → `actividad`           | 1 : N        | Un coordinador crea muchas actividades; una actividad es creada por un coordinador.          |
| `actividad` → `inscripcion`           | 1 : N        | Una actividad recibe muchas inscripciones; una inscripción es de una sola actividad.         |
| `voluntario` → `inscripcion`          | 1 : N        | Un voluntario tiene muchas inscripciones; una inscripción es de un solo voluntario.          |
| `voluntario` ↔ `actividad`            | N : M        | Resuelta mediante la entidad asociativa `inscripcion`.                                       |
| `inscripcion` → `asistencia`          | 1 : 0..1     | Una inscripción tiene como máximo un registro de asistencia.                                 |
| `coordinador` → `asistencia`          | 1 : N        | Un coordinador registra muchas asistencias.                                                  |

### Diagrama entidad-relación

```mermaid
erDiagram
    ORGANIZACION ||--o{ COORDINADOR : "tiene"
    ORGANIZACION ||--o{ ACTIVIDAD : "publica"
    COORDINADOR  ||--o{ ACTIVIDAD : "crea"
    ACTIVIDAD    ||--o{ INSCRIPCION : "recibe"
    VOLUNTARIO   ||--o{ INSCRIPCION : "realiza"
    INSCRIPCION  ||--o| ASISTENCIA : "genera"
    COORDINADOR  ||--o{ ASISTENCIA : "registra"

    ORGANIZACION {
        BIGINT id PK
        VARCHAR nombre
        VARCHAR nit UK
        VARCHAR email UK
        VARCHAR telefono
        TIMESTAMP fecha_creacion
    }
    COORDINADOR {
        BIGINT id PK
        BIGINT organizacion_id FK
        VARCHAR nombre
        VARCHAR apellido
        VARCHAR email UK
        VARCHAR telefono
        TIMESTAMP fecha_creacion
    }
    VOLUNTARIO {
        BIGINT id PK
        VARCHAR documento UK
        VARCHAR nombre
        VARCHAR apellido
        VARCHAR email UK
        VARCHAR telefono
        TIMESTAMP fecha_creacion
    }
    ACTIVIDAD {
        BIGINT id PK
        BIGINT organizacion_id FK
        BIGINT coordinador_id FK
        VARCHAR titulo
        TEXT descripcion
        VARCHAR ubicacion
        TIMESTAMP fecha_inicio
        TIMESTAMP fecha_fin
        TIMESTAMP fecha_cierre
        INTEGER cupo_maximo
        VARCHAR estado
        TIMESTAMP fecha_creacion
        TIMESTAMP fecha_actualizacion
    }
    INSCRIPCION {
        BIGINT id PK
        BIGINT actividad_id FK
        BIGINT voluntario_id FK
        VARCHAR clave_idempotencia UK
        VARCHAR estado
        TIMESTAMP fecha_inscripcion
        TIMESTAMP fecha_cancelacion
    }
    ASISTENCIA {
        BIGINT id PK
        BIGINT inscripcion_id FK "UK"
        BIGINT coordinador_id FK
        NUMERIC horas
        TEXT observaciones
        TIMESTAMP fecha_registro
    }
```

### Reglas de negocio reflejadas en el modelo

| Regla | Cómo se soporta                                                                                                                                                  |
|-------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| RN-01 | `actividad.cupo_maximo` con `CHECK > 0`. El servicio cuenta las inscripciones `CONFIRMADA` antes de insertar, sin bloqueo (ver [Inscripciones](#inscripciones)). |
| RN-02 | Índice único parcial `(actividad_id, voluntario_id) WHERE estado = 'CONFIRMADA'`: solo puede existir una inscripción activa por voluntario y actividad.          |
| RN-03 | `actividad.fecha_cierre` y `actividad.estado`. El servicio rechaza la inscripción si `NOW() > fecha_cierre` o si el cupo está completo (pasa a `CERRADA`). Al consultar actividades, las que siguen `ABIERTA` con `fecha_cierre` vencida pasan a `CERRADA`. |
| RN-04 | `asistencia.inscripcion_id` es FK y único. El servicio valida que la inscripción esté en estado `CONFIRMADA` antes de registrar.                                 |
| RN-05 | `asistencia.horas` con `CHECK (horas > 0)`.                                                                                                                      |
| RN-06 | `inscripcion.clave_idempotencia` única: si se repite la petición con la misma clave, se devuelve la inscripción ya creada en lugar de crear otra.               |

> **Trazabilidad:** las inscripciones no se borran físicamente; `DELETE /api/inscripciones/{id}` cambia el estado a `CANCELADA` y guarda `fecha_cancelacion`. Solo se permite antes de `fecha_cierre` de la actividad.

---

## Implementación de entidades JPA

Cada tabla del modelo relacional tiene una clase Java anotada con `@Entity` en el paquete `co.edu.autonoma.reservasapi.entity`.

### Clase ↔ tabla

| Clase Java              | Tabla          | Relaciones `@ManyToOne`                                  |
|-------------------------|----------------|----------------------------------------------------------|
| `OrganizacionEntity`    | `organizacion` | — (no tiene claves foráneas)                             |
| `CoordinadorEntity`     | `coordinador`  | `organizacion` → `OrganizacionEntity`                    |
| `VoluntarioEntity`      | `voluntario`   | — (no tiene claves foráneas)                             |
| `ActividadEntity`       | `actividad`    | `organizacion` → `OrganizacionEntity`, `coordinador` → `CoordinadorEntity` |
| `InscripcionEntity`     | `inscripcion`  | `actividad` → `ActividadEntity`, `voluntario` → `VoluntarioEntity` |
| `AsistenciaEntity`      | `asistencia`   | `inscripcion` → `InscripcionEntity`, `coordinador` → `CoordinadorEntity` |

Las columnas se escriben en `snake_case` en la base de datos (`fecha_creacion`) y en `camelCase` en Java (`fechaCreacion`), mapeadas con `@Column(name = "...")`.

### Relaciones `@ManyToOne`

Las relaciones se definen **solo en el lado "muchos"** (la entidad que tiene la clave foránea). No hay `@OneToMany` en el lado "uno". Todas siguen el mismo patrón:

```java
@ManyToOne(fetch = FetchType.LAZY, optional = false)
@JoinColumn(name = "coordinador_id", nullable = false)
private CoordinadorEntity coordinador;
```

| Elemento                     | Para qué sirve                                                                                                   |
|------------------------------|------------------------------------------------------------------------------------------------------------------|
| `@ManyToOne`                 | Muchas filas de esta entidad apuntan a una sola fila de la otra (ej. muchas actividades → un coordinador).     |
| `fetch = FetchType.LAZY`     | La entidad relacionada se consulta solo cuando se llama a su getter. Evita consultas innecesarias (por defecto `@ManyToOne` es `EAGER`). |
| `optional = false`           | Hibernate valida en Java que la relación no sea `null` antes de guardar.                                         |
| `@JoinColumn(name = "...")`  | Nombre de la columna que guarda la clave foránea en la tabla.                                                    |
| `nullable = false`           | La columna se crea como `NOT NULL` en la base de datos.                                                          |

**Caso especial — `AsistenciaEntity.inscripcion`:** la relación inscripción–asistencia es 1 : 0..1. Se implementa con `@ManyToOne` y `@JoinColumn(..., unique = true)`, lo que garantiza una sola asistencia por inscripción.

> **Cuidado con `LAZY`:** la relación se consulta en la base de datos solo cuando se accede a ella, y para eso la sesión de Hibernate debe seguir abierta (ver [Mapeo entidad a respuesta](#mapeo-entidad-a-respuesta)). Aun así, los controladores devuelven DTOs de respuesta y no entidades: el JSON no depende de la estructura de las tablas ni expone los proxies que Hibernate usa para las relaciones `LAZY`.

### Enums de estado

Los estados se guardan como texto (`@Enumerated(EnumType.STRING)`), así la base de datos almacena `'ABIERTA'` y no un número.

| Enum                | Valores                                           | Valor inicial  | Usado en             |
|---------------------|---------------------------------------------------|----------------|----------------------|
| `EstadoActividad`   | `ABIERTA`, `CERRADA`, `CANCELADA`, `FINALIZADA`   | `ABIERTA`      | `ActividadEntity`    |
| `EstadoInscripcion` | `CONFIRMADA`, `CANCELADA`                         | `CONFIRMADA`   | `InscripcionEntity`  |

### Fechas automáticas

Las fechas de auditoría las asigna la propia entidad mediante callbacks de JPA; **no deben enviarse desde el cliente** y ningún constructor ni método las recibe.

| Callback       | Entidades                                         | Qué asigna                                                      |
|----------------|---------------------------------------------------|-----------------------------------------------------------------|
| `@PrePersist`  | Todas                                             | `fechaCreacion` / `fechaInscripcion` / `fechaRegistro` al insertar. |
| `@PreUpdate`   | `ActividadEntity`                                 | `fechaActualizacion` en cada modificación.                     |

`fechaCancelacion` no es automática: la asigna `cancelar()` de `InscripcionEntity` al cancelar la inscripción.

### Convenciones del código

- Las clases de entidad terminan en `Entity`.
- Las entidades solo llevan `@Getter` y `@NoArgsConstructor(access = AccessLevel.PROTECTED)` de Lombok. No tienen setters.
- El constructor vacío protegido es el que usa JPA para cargar filas. El código crea entidades con un constructor público que recibe los datos de su `Request`, y las modifica con métodos de la propia entidad (ver la tabla siguiente).
- Las entidades no usan `@Data`: su `equals`, `hashCode` y `toString` recorren todos los campos, incluidas las relaciones `LAZY`, lo que dispara consultas extra o `LazyInitializationException`.
- Llaves de apertura en línea nueva, igual que el resto del código.
- Identificadores `Integer` con `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
- Montos decimales con `BigDecimal` (`horas` → `NUMERIC(5,2)`) y fechas con `LocalDateTime`.

| Entidad              | Constructor público                                                                                  | Métodos que la modifican |
|----------------------|------------------------------------------------------------------------------------------------------|--------------------------|
| `OrganizacionEntity` | `(nombre, nit, email, telefono)`                                                                     | `actualizar(nombre, nit, email, telefono)` para el `PUT` |
| `CoordinadorEntity`  | `(organizacion, nombre, apellido, email, telefono)`                                                  | `actualizar(nombre, apellido, email, telefono)` para el `PUT`; la organización no cambia |
| `VoluntarioEntity`   | `(documento, nombre, apellido, email, telefono)`                                                     | `actualizar(documento, nombre, apellido, email, telefono)` para el `PUT` |
| `ActividadEntity`    | `(coordinador, titulo, descripcion, ubicacion, fechaInicio, fechaFin, fechaCierre, cupoMaximo)`. La organización se toma del coordinador y el estado inicial es `ABIERTA`. | `actualizar(titulo, descripcion, ubicacion, fechaInicio, fechaFin, fechaCierre, cupoMaximo)` para el `PUT`; `cambiarEstado(estado)` para las transiciones |
| `InscripcionEntity`  | `(actividad, voluntario, claveIdempotencia)`. El estado inicial es `CONFIRMADA`.                     | `cancelar()` para la cancelación lógica |
| `AsistenciaEntity`   | `(inscripcion, coordinador, horas, observaciones)`                                                   | `actualizar(horas, observaciones)` para el `PUT`; la inscripción y el coordinador no cambian |

### Restricciones que las entidades no cubren

Las anotaciones JPA reflejan claves, `NOT NULL`, `UNIQUE` y longitudes, pero **no** estas restricciones del modelo. Deben crearse en la base de datos o validarse en los servicios:

| Restricción                                                         | Regla  |
|---------------------------------------------------------------------|--------|
| `CHECK (cupo_maximo > 0)`                                           | RN-01  |
| Índice único parcial `(actividad_id, voluntario_id) WHERE estado = 'CONFIRMADA'` | RN-02  |
| `CHECK (fecha_fin > fecha_inicio)` y `CHECK (fecha_cierre <= fecha_inicio)` | RN-03  |
| `CHECK (horas > 0)`                                                 | RN-05  |

---

## Repositorios Spring Data JPA

Cada entidad tiene una interfaz en `co.edu.autonoma.reservasapi.repository` que extiende `JpaRepository<Entidad, Integer>`. El segundo tipo genérico es el tipo del atributo `@Id`. No existe una clase de implementación: Spring Data la crea al arrancar la aplicación.

Todos los repositorios heredan `save`, `findById`, `findAll`, `count`, `existsById` y `deleteById`.

### Query methods

Spring Data genera la consulta a partir del nombre del método, usando los atributos Java de la entidad (no los nombres de columna).

| Repositorio              | Método                                                                     | SQL generado (resumen)                                                           | Uso previsto |
|--------------------------|----------------------------------------------------------------------------|----------------------------------------------------------------------------------|--------------|
| `OrganizacionRepository` | `findByNit(String)`                                                        | `select ... from organizacion where nit = ?`                                     | Buscar por identificación tributaria |
| `OrganizacionRepository` | `existsByNit(String)`, `existsByEmail(String)`                             | `select id from organizacion where nit = ?` (o `email = ?`)                      | `POST`: 409 si el valor único ya existe |
| `OrganizacionRepository` | `existsByNitAndIdNot(String, Integer)`, `existsByEmailAndIdNot(String, Integer)` | `select id from organizacion where nit = ? and id <> ?`                    | `PUT`: 409 si otra organización ya usa el valor |
| `CoordinadorRepository`  | `findByEmail(String)`                                                      | `select ... from coordinador where email = ?`                                    | Buscar por correo |
| `CoordinadorRepository`  | `existsByOrganizacionId(Integer)`                                          | `select id from coordinador where organizacion_id = ?`                           | `DELETE` de organización: 409 si tiene coordinadores |
| `CoordinadorRepository`  | `existsByEmail(String)`                                                    | `select id from coordinador where email = ?`                                     | `POST`: 409 si el correo ya existe |
| `CoordinadorRepository`  | `existsByEmailAndIdNot(String, Integer)`                                   | `select id from coordinador where email = ? and id <> ?`                         | `PUT`: 409 si otro coordinador ya usa el correo |
| `CoordinadorRepository`  | `findByOrganizacionId(Integer)`                                            | `select ... from coordinador where organizacion_id = ?`                          | Coordinadores de una organización |
| `VoluntarioRepository`   | `findByDocumento(String)`                                                  | `select ... from voluntario where documento = ?`                                 | Buscar por documento de identidad |
| `VoluntarioRepository`   | `existsByDocumento(String)`, `existsByEmail(String)`                       | `select id from voluntario where documento = ?` (o `email = ?`)                  | `POST`: 409 si el valor único ya existe |
| `VoluntarioRepository`   | `existsByDocumentoAndIdNot(String, Integer)`, `existsByEmailAndIdNot(String, Integer)` | `select id from voluntario where documento = ? and id <> ?`          | `PUT`: 409 si otro voluntario ya usa el valor |
| `ActividadRepository`    | `findByEstado(EstadoActividad)`                                            | `select ... from actividad where estado = ?`                                     | Listar actividades por estado |
| `ActividadRepository`    | `countByEstado(EstadoActividad)`                                           | `select count(id) from actividad where estado = ?`                               | Contar actividades por estado |
| `ActividadRepository`    | `findByFechaCierreAfter(LocalDateTime)`                                    | `select ... from actividad where fecha_cierre > ?`                               | RN-03: inscripción aún abierta |
| `ActividadRepository`    | `existsByOrganizacionId(Integer)`                                          | `select id from actividad where organizacion_id = ?`                             | `DELETE` de organización: 409 si tiene actividades |
| `ActividadRepository`    | `existsByCoordinadorId(Integer)`                                           | `select id from actividad where coordinador_id = ?`                              | `DELETE` de coordinador: 409 si tiene actividades |
| `ActividadRepository`    | `findByEstadoAndFechaCierreBefore(EstadoActividad, LocalDateTime)`         | `select ... from actividad where estado = ? and fecha_cierre < ?`                | RN-03: actividades `ABIERTA` que deben pasar a `CERRADA` |
| `ActividadRepository`    | `findByOrganizacionId(Integer)`, `findByCoordinadorId(Integer)`            | `select ... from actividad where organizacion_id = ?` (o `coordinador_id = ?`)   | Actividades de una organización o de un coordinador |
| `InscripcionRepository`  | `findByClaveIdempotencia(String)`                                          | `select ... from inscripcion where clave_idempotencia = ?`                       | RN-06: devolver la inscripción existente |
| `InscripcionRepository`  | `countByActividadIdAndEstado(Integer, EstadoInscripcion)`                  | `select count(id) from inscripcion where actividad_id = ? and estado = ?`        | RN-01: cupos ocupados |
| `InscripcionRepository`  | `existsByActividadIdAndVoluntarioIdAndEstado(Integer, Integer, EstadoInscripcion)` | `select id from inscripcion where actividad_id = ? and voluntario_id = ? and estado = ?` | RN-02: inscripción activa duplicada |
| `InscripcionRepository`  | `existsByVoluntarioId(Integer)`                                            | `select id from inscripcion where voluntario_id = ?`                             | `DELETE` de voluntario: 409 si tiene inscripciones |
| `InscripcionRepository`  | `findByActividadIdAndEstado(Integer, EstadoInscripcion)`                   | `select ... from inscripcion where actividad_id = ? and estado = ?`              | `DELETE` de actividad: inscripciones por cancelar |
| `InscripcionRepository`  | `findByEstado(EstadoInscripcion)`                                          | `select ... from inscripcion where estado = ?`                                   | Filtro `?estado=` de `GET /api/inscripciones` |
| `InscripcionRepository`  | `findByActividadId(Integer)`, `findByVoluntarioId(Integer)`                | `select ... from inscripcion where actividad_id = ?` (o `voluntario_id = ?`)     | Inscripciones de una actividad o de un voluntario |
| `InscripcionRepository`  | `findByVoluntarioIdAndEstado(Integer, EstadoInscripcion)`                  | `select ... from inscripcion where voluntario_id = ? and estado = ?`             | Inscripciones de un voluntario con filtro `?estado=` |
| `AsistenciaRepository`   | `existsByInscripcionId(Integer)`                                           | `select id from asistencia where inscripcion_id = ?`                             | RN-04: una asistencia por inscripción |
| `AsistenciaRepository`   | `existsByCoordinadorId(Integer)`                                           | `select id from asistencia where coordinador_id = ?`                             | `DELETE` de coordinador: 409 si tiene asistencias |
| `AsistenciaRepository`   | `existsByInscripcionActividadId(Integer)`                                  | `select id from asistencia join inscripcion ... where actividad_id = ?`          | `DELETE` de actividad: 409 si tiene asistencias |
| `AsistenciaRepository`   | `findByInscripcionActividadId(Integer)`, `findByInscripcionVoluntarioId(Integer)` | `select ... from asistencia join inscripcion ... where actividad_id = ?` (o `voluntario_id = ?`) | Asistencias de una actividad o de un voluntario |

En nombres como `countByActividadIdAndEstado`, `ActividadId` recorre la relación `actividad.id`; Hibernate lo resuelve con la columna `actividad_id`, sin join.

---

## DTOs de petición y respuesta

Los DTOs están en `co.edu.autonoma.reservasapi.dto`. Los controladores reciben y devuelven DTOs, nunca entidades.

| Tipo      | Sufijo     | Implementación                                                                                     |
|-----------|------------|----------------------------------------------------------------------------------------------------|
| Petición  | `Request`  | `record` inmutable. Cada componente lleva sus anotaciones de `jakarta.validation.constraints`.      |
| Respuesta | `Response` | Clase inmutable: campos `private final` con `@Getter` de Lombok y un constructor escrito a mano con todos los campos (no se usa `@AllArgsConstructor` ni `@RequiredArgsConstructor`). No tiene setters ni constructor vacío; Jackson genera el JSON a partir de los getters. Puede tener un método estático `desde(entidad)` que la construye (ver [Mapeo entidad a respuesta](#mapeo-entidad-a-respuesta)). |

### Clases de petición (`Request`)

| Record                            | Endpoint                                         | Campos                                                                                                  |
|-----------------------------------|--------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| `OrganizacionRequest`             | `POST /api/organizaciones`, `PUT /api/organizaciones/{id}` | `nombre`, `nit`, `email`, `telefono`                                                          |
| `CoordinadorRequest`              | `POST /api/coordinadores`                        | `organizacionId`, `nombre`, `apellido`, `email`, `telefono`                                             |
| `CoordinadorActualizacionRequest` | `PUT /api/coordinadores/{id}`                    | Igual que `CoordinadorRequest` sin `organizacionId`: un coordinador no cambia de organización.          |
| `VoluntarioRequest`               | `POST /api/voluntarios`, `PUT /api/voluntarios/{id}` | `documento`, `nombre`, `apellido`, `email`, `telefono`                                              |
| `ActividadRequest`                | `POST /api/actividades`                          | `coordinadorId`, `titulo`, `descripcion`, `ubicacion`, `fechaInicio`, `fechaFin`, `fechaCierre`, `cupoMaximo` |
| `ActividadActualizacionRequest`   | `PUT /api/actividades/{id}`                      | Igual que `ActividadRequest` sin `coordinadorId` y sin exigir fechas futuras. Solo se acepta mientras la actividad no tenga inscripciones `CONFIRMADA`. |
| `ActividadEstadoRequest`          | `PATCH /api/actividades/{id}/estado`             | `estado` (`EstadoActividad`)                                                                            |
| `InscripcionRequest`              | `POST /api/inscripciones`                        | `actividadId`, `voluntarioId`, `claveIdempotencia` (RN-06)                                              |
| `AsistenciaRequest`               | `PATCH /api/inscripciones/{id}/asistencia`       | `coordinadorId`, `horas` (RN-05), `observaciones`. La inscripción va en la ruta.                        |
| `AsistenciaActualizacionRequest`  | `PUT /api/asistencias/{id}`                      | `horas`, `observaciones`                                                                                |

Ningún `Request` incluye el `id` del recurso (va en la ruta) ni los campos de auditoría (`fechaCreacion`, `fechaInscripcion`, etc.), que asigna el servidor.

### Validaciones

| Anotación                                       | Qué valida                                                         | Dónde se usa                                              |
|-------------------------------------------------|--------------------------------------------------------------------|-----------------------------------------------------------|
| `@NotNull`                                      | El campo no puede ser `null`.                                      | Ids referenciados, fechas, `cupoMaximo`, `estado`, `horas` |
| `@NotBlank`                                     | Texto no nulo y con al menos un carácter distinto de espacio.      | Nombres, documentos, correos, títulos, `claveIdempotencia` |
| `@Size(max = n)`                                | Longitud máxima, igual al `VARCHAR(n)` de la columna.              | Todos los campos de texto con límite                      |
| `@Email`                                        | Formato de correo electrónico.                                     | `email`                                                   |
| `@Future`                                       | Fecha posterior al momento actual.                                 | `fechaInicio` y `fechaCierre` de `ActividadRequest`       |
| `@Positive`                                     | Número mayor que cero (RN-01).                                     | `cupoMaximo`                                              |
| `@DecimalMin(value = "0.0", inclusive = false)` | Decimal mayor que cero (RN-05).                                    | `horas`                                                   |
| `@Digits(integer = 3, fraction = 2)`            | Hasta 3 enteros y 2 decimales, como la columna `NUMERIC(5,2)`.     | `horas`                                                   |

Las anotaciones solo se evalúan si el parámetro del controlador lleva `@Valid`. Si alguna falla, Spring lanza `MethodArgumentNotValidException` y la respuesta es `400 Bad Request`:

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
public ActividadResponse crear(@Valid @RequestBody ActividadRequest request)
```

Lo que no se puede expresar con anotaciones de un solo campo lo valida el servicio:

| Validación                                                    | Respuesta |
|---------------------------------------------------------------|-----------|
| `fechaFin > fechaInicio` y `fechaCierre <= fechaInicio` (RN-03) | 400       |
| Existencia de los ids referenciados en el cuerpo              | 404       |
| Valores únicos (`nit`, `email`, `documento`) y reglas RN-01 a RN-04 y RN-06 | 409 |
| Momento de la operación: `PUT` de una actividad con inscripciones `CONFIRMADA`, cancelación de una inscripción después de `fechaCierre` | 409 |

### Clases de respuesta (`Response`)

| Clase                               | Endpoints                                                                  | Campos que no están en la entidad                                                                                  |
|-------------------------------------|----------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|
| `EstadoResponse`                    | `GET /api/estado`                                                          | —                                                                                                                  |
| `OrganizacionResponse`              | `/api/organizaciones`                                                      | —                                                                                                                  |
| `ParticipacionOrganizacionResponse` | `GET /api/organizaciones/{id}/participacion`                               | Totales de actividades, inscripciones `CONFIRMADA`, asistencias y horas; lista de `ParticipacionActividadResponse`. |
| `ParticipacionActividadResponse`    | Dentro de `ParticipacionOrganizacionResponse`                              | `inscripcionesConfirmadas`, `asistencias` y `horas` de cada actividad.                                             |
| `CoordinadorResponse`               | `/api/coordinadores`, `/api/organizaciones/{id}/coordinadores`             | `organizacionId` y `organizacionNombre`, tomados de la relación `organizacion`.                                    |
| `VoluntarioResponse`                | `/api/voluntarios`                                                         | —                                                                                                                  |
| `ActividadResponse`                 | `/api/actividades`, `/api/organizaciones/{id}/actividades`, `/api/coordinadores/{id}/actividades` | `organizacionNombre`, `coordinadorNombre`, `cuposOcupados` (`countByActividadIdAndEstado(id, CONFIRMADA)`) y `cuposDisponibles` (`cupoMaximo - cuposOcupados`). |
| `InscripcionResponse`               | `/api/inscripciones`, `/api/actividades/{id}/inscripciones`, `/api/voluntarios/{id}/inscripciones` | `actividadTitulo` y `voluntarioNombre`.                                                     |
| `AsistenciaResponse`                | `/api/asistencias`, `PATCH /api/inscripciones/{id}/asistencia`, `/api/actividades/{id}/asistencias`, `/api/voluntarios/{id}/asistencias` | `actividadId`, `actividadTitulo`, `voluntarioId` y `voluntarioNombre` (a través de la inscripción), y `coordinadorNombre`. |

- Los campos `...Nombre` de coordinadores y voluntarios se forman con nombre y apellido: `"Laura Gómez"`.
- Los conteos (`cuposOcupados`, `asistencias`, `totalActividades`, etc.) son `long` primitivo: valen `0` cuando no hay registros y nunca llegan como `null`.
- `fechaCancelacion` de `InscripcionResponse` es `null` mientras la inscripción esté `CONFIRMADA`.
- Los enums `EstadoActividad` y `EstadoInscripcion` del paquete `entity` se reutilizan en los DTOs y viajan como texto (`"ABIERTA"`).

### Mapeo entidad a respuesta

La conversión de entidad a `Response` la hace un método estático `desde(entidad)` de cada `Response`, y el servicio lo llama: para listas recorre `findAll()` con un `for-each` y agrega cada `OrganizacionResponse.desde(organizacion)` a un `ArrayList`; para un solo registro llama directamente a `OrganizacionResponse.desde(organizacion)`. Lo tienen `OrganizacionResponse`, `CoordinadorResponse`, `VoluntarioResponse`, `ActividadResponse`, `InscripcionResponse` y `AsistenciaResponse`. `ParticipacionActividadResponse` y `ParticipacionOrganizacionResponse` también lo tienen: la primera recibe los conteos y las horas que calcula el servicio, y la segunda suma los totales a partir de esas filas. `ActividadResponse` recibe además los cupos ocupados (`desde(actividad, cuposOcupados)`), porque los cuenta el servicio y la entidad no tiene sus inscripciones.

El servicio no usa `@Transactional`. Las relaciones `@ManyToOne` son `LAZY` (ver [Relaciones `@ManyToOne`](#relaciones-manytoone)), y leer `actividad.getCoordinador().getNombre()` funciona porque Spring Boot activa por defecto `spring.jpa.open-in-view`: la sesión de Hibernate queda abierta durante toda la petición HTTP. Fuera de una petición, esa misma lectura lanza `LazyInitializationException`.

---

## Servicios y controladores

Cada recurso tiene un `@Service` en `service/` (sufijo `Servicio`) y un `@RestController` en `controller/` (sufijo `Controller`). Ambos reciben sus dependencias por constructor en campos `private final`; el constructor lo genera `@RequiredArgsConstructor` de Lombok.

| Capa        | Responsabilidad                                                                                              |
|-------------|--------------------------------------------------------------------------------------------------------------|
| Controlador | Define la ruta, valida el cuerpo con `@Valid`, delega en el servicio y devuelve el DTO directamente. El código de éxito distinto de 200 se fija con `@ResponseStatus`: `CREATED` (201) en `POST` y `NO_CONTENT` (204) en los `DELETE` físicos. `POST /api/inscripciones` usa `ResponseEntity`, porque responde 201 o 200 según la clave de idempotencia. No usa repositorios ni entidades. |
| Servicio    | Busca las entidades, aplica las reglas del contrato, guarda y convierte el resultado con `Response.desde(...)`. No usa `@Transactional`: cada llamada al repositorio se ejecuta en su propia transacción. |

### Organizaciones

`OrganizacionController` y `OrganizacionServicio` implementan los endpoints 2 a 7 y 10 del [resumen de endpoints](#resumen-de-endpoints).

| Método del servicio        | Endpoint                            | Regla                                                                                   |
|----------------------------|-------------------------------------|-----------------------------------------------------------------------------------------|
| `listarOrganizaciones()`   | `GET /api/organizaciones`           | —                                                                                       |
| `consultarOrganizacion(id)`| `GET /api/organizaciones/{id}`      | 404 si no existe.                                                                       |
| `consultarPorNit(nit)`     | `GET /api/organizaciones/nit/{nit}` | 404 si no existe.                                                                       |
| `crearOrganizacion(...)`   | `POST /api/organizaciones`          | 409 si el `nit` o el `email` ya existen.                                                |
| `actualizarOrganizacion(...)` | `PUT /api/organizaciones/{id}`   | 404 si no existe; 409 si el `nit` o el `email` pertenecen a otra organización.          |
| `eliminarOrganizacion(id)` | `DELETE /api/organizaciones/{id}`   | 404 si no existe; 409 si tiene coordinadores o actividades (eliminación física).        |
| `consultarParticipacion(id)` | `GET /api/organizaciones/{id}/participacion` | 404 si no existe. Incluye todas sus actividades, en cualquier estado.         |

El endpoint 8 (`/coordinadores` de una organización) está en `OrganizacionController` y delega en `CoordinadorServicio` (ver [Coordinadores](#coordinadores)). El endpoint 9 (`/actividades` de una organización) también está en `OrganizacionController` y delega en `ActividadServicio` (ver [Actividades](#actividades)). 

**Participación (endpoint 10):** por cada actividad de la organización cuenta las inscripciones `CONFIRMADA` y las asistencias, y suma las horas con un `for-each`. Los totales se suman a partir de esas filas. Una actividad sin asistencias muestra `0.00` horas, y las `CANCELADA` también aparecen, con `0` inscripciones confirmadas. Como es una consulta de actividades, aplica antes el [cierre por fecha](#actividades).

### Coordinadores

`CoordinadorController` y `CoordinadorServicio` implementan los endpoints 11 a 16 del [resumen de endpoints](#resumen-de-endpoints). `CoordinadorServicio` también atiende el endpoint 8.

| Método del servicio        | Endpoint                                 | Regla                                                                                   |
|----------------------------|------------------------------------------|-----------------------------------------------------------------------------------------|
| `listarCoordinadores()`    | `GET /api/coordinadores`                 | —                                                                                       |
| `listarPorOrganizacion(organizacionId)` | `GET /api/organizaciones/{id}/coordinadores` | 404 si la organización no existe.                                          |
| `consultarCoordinador(id)` | `GET /api/coordinadores/{id}`            | 404 si no existe.                                                                       |
| `consultarPorEmail(email)` | `GET /api/coordinadores/email/{email}`   | 404 si no existe.                                                                       |
| `crearCoordinador(...)`    | `POST /api/coordinadores`                | 404 si la organización no existe; 409 si el `email` ya existe.                          |
| `actualizarCoordinador(...)` | `PUT /api/coordinadores/{id}`          | 404 si no existe; 409 si el `email` pertenece a otro coordinador. No cambia la organización. |
| `eliminarCoordinador(id)`  | `DELETE /api/coordinadores/{id}`         | 404 si no existe; 409 si tiene actividades o asistencias (eliminación física).          |

El endpoint 17 (`/actividades` de un coordinador) está en `CoordinadorController` y delega en `ActividadServicio` (ver [Actividades](#actividades)).

### Voluntarios

`VoluntarioController` y `VoluntarioServicio` implementan los endpoints 18 a 23 del [resumen de endpoints](#resumen-de-endpoints).

| Método del servicio        | Endpoint                                 | Regla                                                                                   |
|----------------------------|------------------------------------------|-----------------------------------------------------------------------------------------|
| `listarVoluntarios()`      | `GET /api/voluntarios`                   | —                                                                                       |
| `consultarVoluntario(id)`  | `GET /api/voluntarios/{id}`              | 404 si no existe.                                                                       |
| `consultarPorDocumento(documento)` | `GET /api/voluntarios/documento/{documento}` | 404 si no existe.                                                               |
| `crearVoluntario(...)`     | `POST /api/voluntarios`                  | 409 si el `documento` o el `email` ya existen.                                          |
| `actualizarVoluntario(...)` | `PUT /api/voluntarios/{id}`             | 404 si no existe; 409 si el `documento` o el `email` pertenecen a otro voluntario.      |
| `eliminarVoluntario(id)`   | `DELETE /api/voluntarios/{id}`           | 404 si no existe; 409 si tiene inscripciones en cualquier estado (eliminación física).  |

El endpoint 24 (`/inscripciones` de un voluntario) está en `VoluntarioController` y delega en `InscripcionServicio` (ver [Inscripciones](#inscripciones)). El endpoint 25 (`/asistencias` de un voluntario) también está en `VoluntarioController` y delega en `AsistenciaServicio` (ver [Asistencias](#asistencias)).

### Actividades

`ActividadController` y `ActividadServicio` implementan los endpoints 26 a 31 del [resumen de endpoints](#resumen-de-endpoints). `ActividadServicio` también atiende los endpoints 9 y 17.

| Método del servicio        | Endpoint                                 | Regla                                                                                   |
|----------------------------|------------------------------------------|-----------------------------------------------------------------------------------------|
| `listarActividades(estado)` | `GET /api/actividades?estado={estado}`  | Sin filtro, solo las `ABIERTA`. 400 si el estado no es válido.                          |
| `listarPorOrganizacion(organizacionId)` | `GET /api/organizaciones/{id}/actividades` | En cualquier estado. 404 si la organización no existe.                      |
| `listarPorCoordinador(coordinadorId)` | `GET /api/coordinadores/{id}/actividades` | En cualquier estado. 404 si el coordinador no existe.                          |
| `consultarActividad(id)`   | `GET /api/actividades/{id}`              | 404 si no existe.                                                                       |
| `crearActividad(...)`      | `POST /api/actividades`                  | 400 si las fechas son incoherentes (RN-03); 404 si el coordinador no existe.            |
| `actualizarActividad(...)` | `PUT /api/actividades/{id}`              | 400 por fechas; 404 si no existe; 409 si está `CANCELADA` o `FINALIZADA` o tiene inscripciones `CONFIRMADA`. |
| `cambiarEstado(...)`       | `PATCH /api/actividades/{id}/estado`     | 400 si se pide `CANCELADA`; 404 si no existe; 409 si la transición no está permitida.   |
| `cancelarActividad(id)`    | `DELETE /api/actividades/{id}`           | 404 si no existe; 409 si está `FINALIZADA` o tiene asistencias. Si ya estaba `CANCELADA`, responde 200 con el estado actual. |

Las validaciones siguen el orden cuerpo (400), existencia (404) y conflicto (409).

**Transiciones por `PATCH`:**

| Estado actual         | Nuevo estado | Condición                                          |
|-----------------------|--------------|----------------------------------------------------|
| `ABIERTA`             | `CERRADA`    | Ninguna: cierre manual de inscripciones.           |
| `CERRADA`             | `ABIERTA`    | `fechaCierre` no ha pasado y hay cupos disponibles. |
| `ABIERTA` o `CERRADA` | `FINALIZADA` | `fechaFin` ya pasó.                                |

Cualquier otra transición responde 409, incluido pedir el estado que la actividad ya tiene. `CANCELADA` y `FINALIZADA` son estados finales.

**Cierre por fecha (RN-03):** al consultar actividades (individual, listas, rutas anidadas y participación de una organización), las `ABIERTA` cuya `fechaCierre` ya pasó se guardan como `CERRADA` antes de responder. En `GET /api/actividades` esto ocurre antes de filtrar, para que el filtro por estado vea el estado real.

**Cancelación (`DELETE`):** es lógica. La actividad pasa a `CANCELADA` y sus inscripciones `CONFIRMADA` pasan a `CANCELADA` con `fechaCancelacion`. Como los servicios no usan `@Transactional`, cada guardado es independiente: se cancelan primero las inscripciones y al final la actividad. Si algo falla a mitad de camino, la actividad sigue sin cancelar y repetir el `DELETE` completa las inscripciones que falten.

El endpoint 32 (`/inscripciones` de una actividad) está en `ActividadController` y delega en `InscripcionServicio` (ver [Inscripciones](#inscripciones)). El endpoint 33 (`/asistencias` de una actividad) también está en `ActividadController` y delega en `AsistenciaServicio` (ver [Asistencias](#asistencias)).

### Inscripciones

`InscripcionController` y `InscripcionServicio` implementan los endpoints 34 a 36 y 38 del [resumen de endpoints](#resumen-de-endpoints). `InscripcionServicio` también atiende los endpoints 24 y 32. El endpoint 37 (`PATCH /api/inscripciones/{id}/asistencia`) está en `InscripcionController` y delega en `AsistenciaServicio` (ver [Asistencias](#asistencias)).

| Método del servicio        | Endpoint                                 | Regla                                                                                   |
|----------------------------|------------------------------------------|-----------------------------------------------------------------------------------------|
| `listarInscripciones(estado)` | `GET /api/inscripciones?estado={estado}` | Sin filtro, todas. 400 si el estado no es válido.                                    |
| `listarPorActividad(actividadId, estado)` | `GET /api/actividades/{id}/inscripciones?estado={estado}` | 400 si el estado no es válido; 404 si la actividad no existe.    |
| `listarPorVoluntario(voluntarioId, estado)` | `GET /api/voluntarios/{id}/inscripciones?estado={estado}` | 400 si el estado no es válido; 404 si el voluntario no existe. |
| `consultarInscripcion(id)` | `GET /api/inscripciones/{id}`            | 404 si no existe.                                                                       |
| `buscarRepetida(...)`      | `POST /api/inscripciones`                | RN-06: devuelve la inscripción con esa clave (200); 409 si la clave se usó con otros datos. |
| `crearInscripcion(...)`    | `POST /api/inscripciones`                | Si la clave es nueva: 404, RN-03, RN-02 y RN-01 (409), y 201 al guardar.                |
| `cancelarInscripcion(id)`  | `DELETE /api/inscripciones/{id}`         | 404 si no existe; 409 si ya pasó `fechaCierre`. Si ya estaba `CANCELADA`, responde 200 con el estado actual. |

**Orden de validación en `POST`:**

1. Cuerpo inválido: 400.
2. RN-06: con la misma clave y los mismos datos responde 200 con la inscripción existente, aunque esté `CANCELADA`; con otros datos responde 409.
3. El voluntario y la actividad deben existir: 404.
4. RN-03: la actividad debe estar `ABIERTA` y la fecha actual no puede superar `fechaCierre`: 409.
5. RN-02: el voluntario no puede tener una inscripción `CONFIRMADA` en la actividad: 409.
6. RN-01: las inscripciones `CONFIRMADA` deben ser menos que `cupoMaximo`: 409.
7. Se guarda la inscripción y responde 201. Si con ella se completa el cupo, la actividad pasa a `CERRADA`.

**Cancelación (`DELETE`):** es lógica y libera el cupo, pero no reabre la actividad: si estaba `CERRADA`, sigue así hasta que se reabra con `PATCH /api/actividades/{id}/estado`. Para volver a inscribirse se usa una clave nueva.

**Limitación con peticiones simultáneas:** los servicios no usan `@Transactional` ni bloqueos, así que las reglas se comprueban y la inscripción se guarda en pasos separados. Con peticiones simultáneas pueden ocurrir estos casos:

| Regla | Qué puede pasar                                                                                         |
|-------|---------------------------------------------------------------------------------------------------------|
| RN-01 | Varias inscripciones pasan la comprobación a la vez y se supera `cupoMaximo`.                           |
| RN-02 | El mismo voluntario queda con varias inscripciones `CONFIRMADA` en la actividad, si no existe en la base el índice único parcial (ver [Restricciones que las entidades no cubren](#restricciones-que-las-entidades-no-cubren)). |
| RN-06 | No se crean filas duplicadas, porque `clave_idempotencia` es única, pero las peticiones repetidas responden 500 en lugar de 200. |

### Asistencias

`AsistenciaController` y `AsistenciaServicio` implementan los endpoints 39 a 42 del [resumen de endpoints](#resumen-de-endpoints). `AsistenciaServicio` también atiende los endpoints 25, 33 y 37. La asistencia se registra con `PATCH /api/inscripciones/{id}/asistencia`; en `/api/asistencias` solo se consulta, corrige o elimina.

| Método del servicio        | Endpoint                                 | Regla                                                                                   |
|----------------------------|------------------------------------------|-----------------------------------------------------------------------------------------|
| `listarAsistencias()`      | `GET /api/asistencias`                   | —                                                                                       |
| `listarPorActividad(actividadId)` | `GET /api/actividades/{id}/asistencias` | 404 si la actividad no existe.                                                     |
| `listarPorVoluntario(voluntarioId)` | `GET /api/voluntarios/{id}/asistencias` | 404 si el voluntario no existe.                                                  |
| `consultarAsistencia(id)`  | `GET /api/asistencias/{id}`              | 404 si no existe.                                                                       |
| `registrarAsistencia(inscripcionId, ...)` | `PATCH /api/inscripciones/{id}/asistencia` | Reglas en el orden indicado abajo. Responde 200 con el registro creado.        |
| `actualizarAsistencia(...)` | `PUT /api/asistencias/{id}`             | 400 si las horas no son válidas; 404 si no existe. La inscripción y el coordinador no cambian. |
| `eliminarAsistencia(id)`   | `DELETE /api/asistencias/{id}`           | 404 si no existe (eliminación física). La inscripción sigue `CONFIRMADA` y se puede registrar de nuevo. |

**Orden de validación en `PATCH`:**

1. Cuerpo inválido, incluida RN-05 (`horas` mayores que cero, con hasta 3 enteros y 2 decimales): 400.
2. La inscripción y el coordinador deben existir: 404.
3. RN-04: la inscripción debe estar `CONFIRMADA` y no puede tener una asistencia previa: 409.
4. El coordinador debe pertenecer a la organización de la actividad, aunque no sea quien la creó: 409.
5. La actividad ya debe haber iniciado: 409.
6. Se guarda la asistencia y responde 200.

El estado de la actividad no se comprueba por separado: se puede registrar en una actividad `CERRADA` o `FINALIZADA`, y una `CANCELADA` no llega a este punto porque sus inscripciones también están canceladas.

**Limitación con peticiones simultáneas:** como en las [inscripciones](#inscripciones), si se registra a la vez la asistencia de una misma inscripción no se crean filas duplicadas, porque `inscripcion_id` es única, pero las peticiones repetidas pueden responder 500 en lugar de 409.

### Errores mientras no exista el manejador global

El servicio lanza `ResponseStatusException` con `404` o `409`, y Spring responde con ese código. Un cuerpo inválido (`@Valid`) o un JSON mal formado responde `400`. El cuerpo de error es el predeterminado de Spring Boot; el formato propio de la API queda para la capa de [manejo de errores](#estado-del-proyecto).
