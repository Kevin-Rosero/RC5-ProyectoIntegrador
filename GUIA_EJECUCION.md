# GUÍA PASO A PASO: EJECUTAR EL PROYECTO CON MONGODB

## 🔧 PASO 1: INSTALAR MONGODB

### Opción A: Instalación Directa (Windows)

1. Descarga desde: https://www.mongodb.com/try/download/community
2. Descarga la versión Windows (MSI Installer)
3. Ejecuta el instalador y selecciona "Install MongoDB as a Service"
4. Selecciona "Run the Server as a Windows Service"
5. MongoDB se iniciará automáticamente

### Opción B: Usando Docker (Más fácil)

Si tienes Docker instalado:

```bash
docker run -d -p 27017:27017 --name refugio-mongodb mongo
```

Este comando:
- Descarga e instala MongoDB en un contenedor
- Lo ejecuta en puerto 27017
- Lo mantiene ejecutándose en segundo plano

---

## ✅ PASO 2: VERIFICAR MONGODB ESTÁ EJECUTÁNDOSE

### Windows (Verificar servicio):

```bash
# En PowerShell como Administrador
Get-Service | findstr "MongoDB"
```

Deberías ver: `MongoDB Server    Running`

### O usar MongoDB Compass:

1. Descarga: https://www.mongodb.com/products/compass
2. Abre Compass
3. Intenta conectar a: `mongodb://localhost:27017`
4. Si conecta exitosamente, MongoDB está listo

---

## 📥 PASO 3: DESCARGAR LAS DEPENDENCIAS

```bash
# Navega a la carpeta del proyecto
cd "E:\UDLA\TERCER SEMESTRE\PROGRAMACION II\PROGRESO 3\PROYECTO INTEGRADOR\Codigo\proyectointegrador\proyectointegrador"

# Descarga todas las dependencias Maven
mvn clean install
```

Este paso puede tomar 2-5 minutos la primera vez.

---

## 🚀 PASO 4: EJECUTAR LA APLICACIÓN

### Opción A: Desde la terminal

```bash
mvn spring-boot:run
```

### Opción B: Desde el IDE (IntelliJ IDEA)

1. Abre la carpeta del proyecto en IntelliJ IDEA
2. Espera a que Maven descargue las dependencias
3. Haz clic derecho en `Application.java`
4. Selecciona "Run 'Application.main()'"
5. O presiona Shift + F10

---

## 🌐 PASO 5: ACCEDER A LA APLICACIÓN

1. Abre tu navegador
2. Ve a: `http://localhost:8081`
3. ¡Deberías ver la interfaz de login!

---

## 📊 PASO 6: VERIFICAR QUE LOS DATOS SE GUARDAN EN MONGODB

### Opción A: Usando MongoDB Compass

1. Abre MongoDB Compass
2. Conecta a: `mongodb://localhost:27017`
3. Navega a la base de datos: `refugio_mascotas`
4. Verás las colecciones:
   - `mascotas`
   - `adoptantes`
   - `admins`
   - `solicitudes_adopcion`
   - `evaluaciones`

### Opción B: Usando MongoDB Shell

```bash
# Conectar a MongoDB
mongosh

# Cambiar a la base de datos
use refugio_mascotas

# Ver todas las colecciones
show collections

# Ver documentos de mascotas
db.mascotas.find()

# Contar documentos
db.mascotas.countDocuments()
```

---

## ✨ PASO 7: PROBAR LA APLICACIÓN

### Para Adoptantes:

1. Haz clic en "¿No tienes cuenta?" → "Registrarse"
2. Completa el formulario de registro
3. Inicia sesión con tus credenciales
4. Busca una mascota disponible
5. Completa el formulario de adopción
6. ¡Los datos se guardarán en MongoDB!

### Para Administradores:

1. Si tienes credenciales de admin, inicia sesión
2. Verás un panel con solicitudes pendientes
3. Puedes aprobar o rechazar solicitudes
4. Los cambios se guardarán en MongoDB

---

## 🔧 TROUBLESHOOTING

### Problema: "Cannot connect to MongoDB"

**Solución:**
1. Verifica que MongoDB está ejecutándose: `net start MongoDB`
2. En Docker: `docker start refugio-mongodb`
3. Reinicia MongoDB
4. Reinicia la aplicación

### Problema: "Port 8081 already in use"

**Solución 1: Cambiar puerto**
```properties
# En application.properties
server.port=8082
```

**Solución 2: Matar el proceso**
```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F
```

### Problema: "Cannot find symbol: MascotaRepository"

**Solución:**
```bash
mvn clean install
# Luego recarga el proyecto en el IDE
```

### Problema: "MongoDB Connection Timeout"

**Solución:**
```properties
# En application.properties, aumenta el timeout:
spring.data.mongodb.uri=mongodb://localhost:27017/refugio_mascotas?serverSelectionTimeoutMS=5000
```

---

## 📝 ESTRUCTURA DE CARPETAS CREADAS

```
proyectointegrador/
├── pom.xml (ACTUALIZADO - con dependencias MongoDB)
├── src/main/
│   ├── java/com/refugio/
│   │   ├── modelo/
│   │   │   ├── Mascota.java (ACTUALIZADO)
│   │   │   ├── Persona.java (ACTUALIZADO)
│   │   │   ├── Admin.java (ACTUALIZADO)
│   │   │   ├── Adoptante.java (ACTUALIZADO)
│   │   │   ├── Evaluacion.java (ACTUALIZADO)
│   │   │   └── SolicitudAdopcion.java (ACTUALIZADO)
│   │   ├── repositorios/ (NUEVO)
│   │   │   ├── MascotaRepository.java
│   │   │   ├── AdoptanteRepository.java
│   │   │   ├── AdminRepository.java
│   │   │   └── SolicitudAdopcionRepository.java
│   │   └── servicios/
│   │       ├── MascotaService.java (ACTUALIZADO)
│   │       ├── AuthenticationService.java (ACTUALIZADO)
│   │       └── AdopcionService.java (ACTUALIZADO)
│   └── resources/
│       └── application.properties (ACTUALIZADO)
├── CONFIGURACION_MONGODB.md (NUEVO)
└── RESUMEN_MIGRACION_MONGODB.md (NUEVO)
```

---

## 🎯 PUNTOS CLAVE

✅ **SIN CAMBIOS EN LA UI VAADIN**: La interfaz sigue funcionando igual
✅ **MISMOS MÉTODOS**: Los servicios tienen los mismos nombres y tipos de retorno
✅ **DATOS PERSISTENTES**: Todo se guarda en MongoDB automáticamente
✅ **FÁCIL DE TESTEAR**: Puedes ver todos los datos en MongoDB Compass

---

## 📞 COMANDOS ÚTILES

```bash
# Ver logs en tiempo real
mvn spring-boot:run

# Compilar sin ejecutar
mvn clean install -DskipTests

# Ejecutar solo tests
mvn test

# Limpiar caché
mvn clean

# Ver las dependencias
mvn dependency:tree
```

---

## 🎓 PRÓXIMOS PASOS

1. ✅ Instala MongoDB
2. ✅ Ejecuta `mvn clean install`
3. ✅ Ejecuta `mvn spring-boot:run`
4. ✅ Accede a `http://localhost:8081`
5. ✅ Prueba crear un usuario y adoptar una mascota
6. ✅ Verifica los datos en MongoDB Compass

---

¡Listo! Tu aplicación está configurada para usar MongoDB. 🚀

