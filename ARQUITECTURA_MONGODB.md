# 🏗️ ARQUITECTURA DEL PROYECTO - MONGODB INTEGRATION

## Diagrama de Flujo General

```
┌─────────────────────────────────────────────────────────────┐
│                    APLICACIÓN VAADIN                         │
│                   (Frontend - HTML/JS)                       │
└────────────────┬────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────┐
│              SPRING BOOT APPLICATION                         │
│              (http://localhost:8081)                         │
└────────────────┬────────────────────────────────────────────┘
                 │
        ┌────────┴────────┬──────────────┬──────────────┐
        ▼                 ▼              ▼              ▼
   ┌─────────┐      ┌──────────┐  ┌──────────┐  ┌──────────┐
   │Mascota  │      │Adoption  │  │Auth      │  │Usuarios  │
   │Service  │      │Service   │  │Service   │  │UI        │
   └────┬────┘      └────┬─────┘  └────┬─────┘  └──────────┘
        │                 │             │
        ▼                 ▼             ▼
   ┌─────────────────────────────────────────┐
   │         SPRING DATA REPOSITORIES        │
   │  (Inyección mediante @Autowired)        │
   │                                         │
   │  • MascotaRepository                    │
   │  • AdoptanteRepository                  │
   │  • AdminRepository                      │
   │  • SolicitudAdopcionRepository          │
   └────────────────┬────────────────────────┘
                    │
                    ▼
        ┌───────────────────────────┐
        │   MONGODB DATABASE        │
        │  (localhost:27017)        │
        │                           │
        │ refugio_mascotas/         │
        │  ├─ mascotas              │
        │  ├─ adoptantes            │
        │  ├─ admins                │
        │  ├─ evaluaciones          │
        │  └─ solicitudes_adopcion  │
        └───────────────────────────┘
```

---

## Estructura de Clases (Modelo)

```
┌─────────────────────────────────────────────────────────┐
│ DOCUMENTO MONGODB: Persona (colección: personas)        │
├─────────────────────────────────────────────────────────┤
│ - id: String (@Id)                                      │
│ - nombre: String                                        │
│ - cedula: String                                        │
│ - edad: int                                             │
│ - correo: String                                        │
│ - password: String                                      │
│ - validarDatos(): abstract                              │
└──────────────────┬──────────────────────────────────────┘
                   │
        ┌──────────┴──────────┐
        ▼                     ▼
   ┌─────────────┐      ┌──────────────┐
   │ ADOPTANTE   │      │ ADMIN        │
   ├─────────────┤      ├──────────────┤
   │ direccion   │      │ idEmpleado   │
   │ telefono    │      │ cargo        │
   └─────────────┘      │ permisos     │
   (colección:          └──────────────┘
    adoptantes)         (colección: admins)
```

```
┌────────────────────────────────────────┐
│ DOCUMENTO MONGODB: Mascota             │
├────────────────────────────────────────┤
│ - id: String (@Id)                     │
│ - especie: String                      │
│ - nombre: String                       │
│ - sexo: String                         │
│ - edad: int                            │
│ - estadoSalud: String                  │
│ - estado: String (DISPONIBLE/ADOPTADA) │
│ - estaDisponible(): boolean            │
│ - actualizarEstado(String): void       │
└────────────────────────────────────────┘
(colección: mascotas)

┌──────────────────────────────────────────┐
│ DOCUMENTO MONGODB: Evaluacion            │
├──────────────────────────────────────────┤
│ - id: String (@Id)                       │
│ - tipoMascotaDeseada: String             │
│ - motivoAdopcion: String                 │
│ - tipoVivienda: String                   │
│ - ... (16 campos más)                    │
│ - puntajeTotal: int                      │
│ - resultado: String                      │
│ - calcularPuntaje(): void                │
│ - generarSugerenciaSistema(): void       │
└──────────────────────────────────────────┘
(colección: evaluaciones)

┌──────────────────────────────────────────┐
│ DOCUMENTO MONGODB: SolicitudAdopcion     │
├──────────────────────────────────────────┤
│ - idSolicitud: String (@Id)              │
│ - adoptante: Adoptante                   │
│ - mascota: Mascota                       │
│ - evaluacion: Evaluacion                 │
│ - estadoTramite: String                  │
│ - fechaCreacion: String                  │
└──────────────────────────────────────────┘
(colección: solicitudes_adopcion)
```

---

## Capas de la Aplicación

```
PRESENTACIÓN (Vaadin)
     │
     ├─ MascotasView
     ├─ AdopcionView
     ├─ AdminView
     └─ LoginView
           │
           ▼
SERVICIOS (Spring Services)
     │
     ├─ @Service MascotaService
     │    ├─ obtenerMascotasDisponibles()
     │    ├─ buscarPorNombre()
     │    ├─ agregarMascota()
     │    ├─ actualizarMascota()
     │    ├─ eliminarMascota()
     │    ├─ cambiarDisponibilidad()
     │    ├─ buscarPorEspecie()
     │    └─ buscarPorEdadMaxima()
     │
     ├─ @Service AuthenticationService
     │    └─ autenticar(correo, password)
     │
     └─ @Service AdopcionService
          ├─ registrarNuevaSolicitud()
          ├─ obtenerSolicitudesPendientes()
          └─ emitirVeredictoFinal()
           │
           ▼
REPOSITORIOS (Spring Data MongoDB)
     │
     ├─ MascotaRepository
     │    extends MongoRepository<Mascota, String>
     │
     ├─ AdoptanteRepository
     │    extends MongoRepository<Adoptante, String>
     │
     ├─ AdminRepository
     │    extends MongoRepository<Admin, String>
     │
     └─ SolicitudAdopcionRepository
          extends MongoRepository<SolicitudAdopcion, String>
           │
           ▼
PERSISTENCIA (MongoDB)
     │
     └─ Base de Datos: refugio_mascotas
         ├─ Colección: mascotas
         ├─ Colección: adoptantes
         ├─ Colección: admins
         ├─ Colección: evaluaciones
         └─ Colección: solicitudes_adopcion
```

---

## Flujo de Datos: Crear Mascota

```
┌────────────────┐
│ Usuario Vaadin │ (Rellena formulario)
└────────┬───────┘
         │
         ▼
┌────────────────────────┐
│ MascotasView.java      │ (Vista Vaadin)
│ agregarMascota()       │
└────────┬───────────────┘
         │
         ▼
┌────────────────────────┐
│ MascotaService.java    │ (@Service)
│ agregarMascota()       │ (@Autowired)
└────────┬───────────────┘
         │
         ▼
┌────────────────────────┐
│ MascotaRepository      │ (extends MongoRepository)
│ save(mascota)          │
└────────┬───────────────┘
         │
         ▼
┌────────────────────────┐
│ MONGODB DATABASE       │
│ INSERT INTO mascotas   │
│ { _id, especie, ...}   │
└────────────────────────┘
```

---

## Flujo de Datos: Buscar Mascota por Nombre

```
┌────────────────┐
│ Usuario        │ (Busca "Firulais")
└────────┬───────┘
         │
         ▼
┌────────────────────────┐
│ MascotasView.java      │
│ buscarMascota(nombre)  │
└────────┬───────────────┘
         │
         ▼
┌────────────────────────┐
│ MascotaService.java    │ (@Service)
│ buscarPorNombre()      │ (@Autowired)
└────────┬───────────────┘
         │
         ▼
┌────────────────────────┐
│ MascotaRepository      │
│ findByNombre(nombre)   │
└────────┬───────────────┘
         │
         ▼
┌────────────────────────┐
│ MONGODB DATABASE       │
│ QUERY: {nombre: ...}   │
└────────┬───────────────┘
         │
         ▼
┌────────────────────────┐
│ Result: Optional       │
│ <Mascota>              │
│ Retorna al Servicio    │
└────────────────────────┘
```

---

## Flujo de Datos: Autenticación

```
┌──────────────────┐
│ Usuario          │ (Ingresa correo/password)
└────────┬─────────┘
         │
         ▼
┌──────────────────────────┐
│ LoginView.java           │
│ attemptLogin()           │
└────────┬─────────────────┘
         │
         ▼
┌──────────────────────────┐
│ AuthenticationService    │ (@Service)
│ autenticar(correo, pwd)  │ (@Autowired)
└────────┬─────────────────┘
         │
    ┌────┴────┐
    │          │
    ▼          ▼
┌─────────┐  ┌──────────┐
│AdminRepo│  │AdoptRepo │
│findBy   │  │findBy    │
│Correo() │  │Correo()  │
└────┬────┘  └────┬─────┘
     │            │
     ▼            ▼
┌─────────────────────────┐
│ MONGODB DATABASE        │
│ Query: {correo: ...}    │
└────────┬────────────────┘
         │
         ▼
┌──────────────────────┐
│ Persona (Admin u     │
│ Adoptante)           │ ← Retorna
└──────────────────────┘
```

---

## Inyección de Dependencias (@Autowired)

```java
// MascotaService.java
@Service
public class MascotaService {
    @Autowired                          // ← Inyección automática
    private MascotaRepository mascotaRepository;
    
    public void agregarMascota(Mascota m) {
        mascotaRepository.save(m);      // ← Usa el repositorio inyectado
    }
}

// AuthenticationService.java
@Service
public class AuthenticationService {
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private AdoptanteRepository adoptanteRepository;
    
    public Persona autenticar(String correo, String password) {
        Optional<Admin> admin = adminRepository.findByCorreo(correo);
        // ... usa los repositorios inyectados
    }
}
```

---

## Métodos de MongoRepository (Heredados Automáticamente)

```java
// Todos estos métodos están disponibles sin implementación
mascotaRepository.save(mascota);           // INSERT/UPDATE
mascotaRepository.saveAll(mascotas);       // Batch INSERT
mascotaRepository.findById(id);            // Buscar por ID
mascotaRepository.findAll();               // Obtener todos
mascotaRepository.count();                 // Contar documentos
mascotaRepository.deleteById(id);          // Eliminar por ID
mascotaRepository.delete(mascota);         // Eliminar documento
mascotaRepository.exists(id);              // Verificar existencia

// Métodos personalizados (definidos en la interfaz)
mascotaRepository.findByNombre(nombre);           // Query personalizado
mascotaRepository.findByEstado(estado);          // Query personalizado
mascotaRepository.findByEspecie(especie);        // Query personalizado
mascotaRepository.findByEdadLessThanEqual(edad); // Query con operador
```

---

## Transformación Antes vs Después

### ANTES (Archivos .txt)
```
Mascota.java → GestorArchivosTxt → mascotas.txt → Lectura/Escritura Manual
```

### DESPUÉS (MongoDB)
```
Mascota.java → (@Document) → MascotaRepository → (@Autowired) → MongoDB
```

---

## Características de MongoDB en Este Proyecto

| Característica | Implementación |
|---|---|
| **Colecciones** | Automáticas (@Document) |
| **Documentos** | BSON (JSON binario) |
| **IDs** | ObjectId automático (@Id) |
| **Índices** | Por defecto en _id |
| **Queries** | Spring Data Query Methods |
| **Transacciones** | Soportadas en MongoDB 4.0+ |
| **Replicación** | Soportada (no necesaria para desarrollo) |

---

## Ventajas de Esta Arquitectura

✅ **Separación de capas**: Modelo → Repositorio → Servicio → Presentación
✅ **Inyección de dependencias**: Spring maneja automáticamente las instancias
✅ **Sin código repetitivo**: MongoRepository proporciona CRUD automático
✅ **Fácil de testear**: Se pueden mockear los repositorios
✅ **Escalable**: Fácil agregar nuevos repositorios/servicios
✅ **Mantenible**: Código limpio y organizado
✅ **Compatible con Vaadin**: Sin cambios necesarios en la UI

---

**Fecha**: 2026-06-30
**Versión**: 1.0
**MongoDB**: 4.0+
**Spring Boot**: 3.2.0+

