# 📋 REFERENCIA RÁPIDA - MONGODB SETUP

## 🚀 INICIO RÁPIDO (3 PASOS)

```bash
# 1. Iniciar MongoDB
mongod

# 2. Compilar e instalar dependencias
mvn clean install

# 3. Ejecutar aplicación
mvn spring-boot:run
```

Listo! Accede a `http://localhost:8081`

---

## 📝 ARCHIVOS MODIFICADOS/CREADOS

| Archivo | Estado | Cambio |
|---------|--------|--------|
| `pom.xml` | ✏️ MODIFICADO | +2 dependencias MongoDB |
| `application.properties` | ✏️ MODIFICADO | +2 propiedades MongoDB |
| `Mascota.java` | ✏️ MODIFICADO | +@Document, @Id, getId(), setId() |
| `Persona.java` | ✏️ MODIFICADO | +@Document, @Id, getId(), setId() |
| `Adoptante.java` | ✏️ MODIFICADO | +@Document |
| `Admin.java` | ✏️ MODIFICADO | +@Document |
| `Evaluacion.java` | ✏️ MODIFICADO | +@Document, @Id, getId(), setId() |
| `SolicitudAdopcion.java` | ✏️ MODIFICADO | +@Document, @Id en idSolicitud |
| `MascotaRepository.java` | ✨ NUEVO | Interfaz para CRUD de mascotas |
| `AdoptanteRepository.java` | ✨ NUEVO | Interfaz para CRUD de adoptantes |
| `AdminRepository.java` | ✨ NUEVO | Interfaz para CRUD de admins |
| `SolicitudAdopcionRepository.java` | ✨ NUEVO | Interfaz para CRUD de solicitudes |
| `MascotaService.java` | ✏️ MODIFICADO | Ahora usa @Autowired MascotaRepository |
| `AuthenticationService.java` | ✏️ MODIFICADO | Ahora usa @Autowired Repositorios |
| `AdopcionService.java` | ✏️ MODIFICADO | Ahora usa @Autowired Repositorios |

---

## 🔗 MAPEO DE SERVICIOS A REPOSITORIOS

```java
// MascotaService.java
@Autowired
private MascotaRepository mascotaRepository;  // ← Nuevo

// AuthenticationService.java
@Autowired
private AdminRepository adminRepository;      // ← Nuevo
@Autowired
private AdoptanteRepository adoptanteRepository;  // ← Nuevo

// AdopcionService.java
@Autowired
private SolicitudAdopcionRepository solicitudAdopcionRepository;  // ← Nuevo
```

---

## 🔍 MÉTODOS DE BÚSQUEDA DISPONIBLES

### MascotaRepository
```java
mascotaRepository.findByNombre(String)          // Optional<Mascota>
mascotaRepository.findByEstado(String)          // List<Mascota>
mascotaRepository.findByEspecie(String)         // List<Mascota>
mascotaRepository.findByEdadLessThanEqual(int)  // List<Mascota>
```

### AdoptanteRepository
```java
adoptanteRepository.findByCorreo(String)   // Optional<Adoptante>
adoptanteRepository.findByCedula(String)   // Optional<Adoptante>
```

### AdminRepository
```java
adminRepository.findByCorreo(String)       // Optional<Admin>
adminRepository.findByCedula(String)       // Optional<Admin>
```

### SolicitudAdopcionRepository
```java
solicitudAdopcionRepository.findByEstadoTramite(String)  // List<SolicitudAdopcion>
```

---

## ⚙️ CONFIGURACIÓN MONGODB

### application.properties
```properties
# Configuración básica (desarrollo local)
spring.data.mongodb.uri=mongodb://localhost:27017/refugio_mascotas

# Con usuario y contraseña (producción)
spring.data.mongodb.uri=mongodb://usuario:contraseña@host:27017/refugio_mascotas

# Crear colecciones automáticamente
spring.jpa.hibernate.ddl-auto=update
```

---

## 📊 COLECCIONES EN MONGODB

```
refugio_mascotas/
├── mascotas           (Documentos de mascotas)
├── adoptantes         (Documentos de adoptantes)
├── admins             (Documentos de administradores)
├── evaluaciones       (Documentos de evaluaciones)
└── solicitudes_adopcion (Documentos de solicitudes)
```

---

## 🧪 VERIFICAR EN MONGODB COMPASS

```
1. Conectar a: mongodb://localhost:27017
2. Base de datos: refugio_mascotas
3. Colecciones: mascotas, adoptantes, admins, solicitudes_adopcion
4. Ver documentos con formato JSON
```

---

## ⚠️ TROUBLESHOOTING RÁPIDO

| Problema | Solución |
|----------|----------|
| Port 27017 in use | `mongod` ya ejecutándose |
| Cannot find MongoRepository | `mvn clean install` |
| Application won't start | Verifica `application.properties` |
| No data in MongoDB | Verifica conexión en Compass |
| "Connection refused" | MongoDB no está ejecutándose |

---

## 💡 IMPORTANTE

✅ **Los nombres de métodos en los servicios NO CAMBIARON**
✅ **Los tipos de retorno de los métodos NO CAMBIARON**  
✅ **Vaadin UI no necesita cambios**
✅ **Todos los datos ahora se guardan en MongoDB automáticamente**

---

## 📌 CHECKLIST ANTES DE EJECUTAR

- [ ] MongoDB instalado: `mongod --version`
- [ ] MongoDB ejecutándose: `Get-Service MongoDB`
- [ ] Proyecto compilado: `mvn clean install`
- [ ] Dependencias descargadas: `~/.m2/repository`
- [ ] Terminal en carpeta del proyecto
- [ ] Puertos libres: 8081 (app), 27017 (MongoDB)

---

## 🎯 COMANDOS ÚTILES

```bash
# Iniciar MongoDB
mongod

# Verificar servicio (Windows)
Get-Service MongoDB

# Compilar proyecto
mvn clean install

# Ejecutar aplicación
mvn spring-boot:run

# Ejecutar en puerto diferente
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8082"

# Ver logs en tiempo real
mvn spring-boot:run | findstr "INFO\|ERROR"

# Conectar a MongoDB shell
mongosh

# Ver bases de datos en MongoDB
mongosh --eval "show databases"

# Ver colecciones en refugio_mascotas
mongosh --eval "use refugio_mascotas; show collections"

# Contar documentos
mongosh --eval "use refugio_mascotas; db.mascotas.countDocuments()"
```

---

## 🌐 URL DE REFERENCIA

| Recurso | URL |
|---------|-----|
| Aplicación | `http://localhost:8081` |
| MongoDB Compass | `https://www.mongodb.com/products/compass` |
| Spring Data MongoDB | `https://spring.io/projects/spring-data-mongodb` |
| Maven Central | `https://mvnrepository.com` |

---

**Última actualización**: 2026-06-30
**Versión MongoDB**: Community Edition 6.0+
**Java Version**: 21+
**Spring Boot**: 3.2.0+

