# Configuración de MongoDB para el Proyecto de Refugio de Mascotas

## Cambios Realizados

### 1. Dependencias Maven (pom.xml)
Se han agregado las siguientes dependencias:
- `spring-boot-starter-data-jpa`: Para soporte de JPA
- `spring-boot-starter-data-mongodb`: Driver MongoDB y Spring Data MongoDB

### 2. Configuración de Aplicación (application.properties)
Se han agregado las siguientes propiedades:
```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/refugio_mascotas
spring.jpa.hibernate.ddl-auto=update
```

**Nota sobre la configuración:**
- `spring.data.mongodb.uri`: Conecta a MongoDB en localhost:27017. Cambia esto según tu configuración.
- Formato alternativo con usuario/contraseña: `mongodb://usuario:contraseña@localhost:27017/refugio_mascotas`
- `spring.jpa.hibernate.ddl-auto=update`: Crea automáticamente las tablas si no existen.

### 3. Entidades de Modelo Actualizadas
Las siguientes clases han sido convertidas a entidades de MongoDB:
- **Mascota**: Documento en la colección "mascotas"
- **Adoptante**: Documento en la colección "adoptantes" (extiende Persona)
- **Admin**: Documento en la colección "admins" (extiende Persona)
- **Evaluacion**: Documento en la colección "evaluaciones"
- **SolicitudAdopcion**: Documento en la colección "solicitudes_adopcion"
- **Persona**: Clase base con anotaciones MongoDB (colección "personas")

Cada entidad tiene:
- `@Document(collection = "nombre_coleccion")` para definir la colección
- `@Id` para marcar el campo ID
- Campos generados automáticamente por MongoDB

### 4. Repositorios Creados
Se han creado las siguientes interfaces de repositorio:

#### MascotaRepository
Métodos disponibles:
- `Optional<Mascota> findByNombre(String nombre)`
- `List<Mascota> findByEstado(String estado)`
- `List<Mascota> findByEspecie(String especie)`
- `List<Mascota> findByEdadLessThanEqual(int edad)`

#### AdoptanteRepository
Métodos disponibles:
- `Optional<Adoptante> findByCorreo(String correo)`
- `Optional<Adoptante> findByCedula(String cedula)`

#### AdminRepository
Métodos disponibles:
- `Optional<Admin> findByCorreo(String correo)`
- `Optional<Admin> findByCedula(String cedula)`

#### SolicitudAdopcionRepository
Métodos disponibles:
- `List<SolicitudAdopcion> findByEstadoTramite(String estadoTramite)`

### 5. Servicios Actualizados
Los servicios ahora usan inyección de dependencias mediante `@Autowired`:

#### MascotaService (Mantenidos todos los métodos)
- `obtenerMascotasDisponibles()`
- `buscarPorNombre(String nombre)`
- `agregarMascota(Mascota mascota)`
- `actualizarMascota(Mascota mascotaActualizada)`
- `eliminarMascota(String nombre)`
- `cambiarDisponibilidad(String nombre, String nuevoEstado)`
- `buscarPorEspecie(String especie)`
- `buscarPorEdadMaxima(int edad)`

#### AuthenticationService (Mantenido)
- `autenticar(String correo, String password)`

#### AdopcionService (Mantenido)
- `registrarNuevaSolicitud(Adoptante adoptante, Mascota mascota, Evaluacion evaluacion)`
- `obtenerSolicitudesPendientes()`
- `emitirVeredictoFinal(String idSolicitud, String decisionAdmin)`

## Prerequisitos para Ejecutar

1. **MongoDB Instalado y Ejecutándose**:
   - Descárgalo desde: https://www.mongodb.com/try/download/community
   - En Windows, puede iniciarse como servicio: `mongod`
   - Por defecto, se ejecuta en `localhost:27017`

2. **Verificar la Conexión**:
   - Puedes usar MongoDB Compass para visualizar y gestionar la base de datos
   - Descárgalo desde: https://www.mongodb.com/products/compass

## Instrucciones de Ejecución

1. **Compilar el Proyecto**:
   ```bash
   mvn clean install
   ```

2. **Ejecutar la Aplicación**:
   ```bash
   mvn spring-boot:run
   ```
   O desde el IDE, ejecutar como Spring Boot Application.

3. **Acceder a la Aplicación**:
   - La aplicación estará disponible en: `http://localhost:8081`

## Migrando Datos Existentes (Opcional)

Si tienes datos en los archivos `.txt`, puedes migrarlos a MongoDB usando un script Java o manualmente:

```java
// Ejemplo: Migrar mascotas desde archivo a MongoDB
List<Mascota> mascotas = leerMascotasDelArchivo(); // Tu lógica actual
for (Mascota m : mascotas) {
    mascotaService.agregarMascota(m);
}
```

## Notas Importantes

1. **Sin Cambios en Vaadin**: Todos los nombres de métodos y tipos de retorno se han mantenido igual, por lo que las vistas Vaadin no necesitan cambios.

2. **Colecciones Automáticas**: MongoDB creará automáticamente las colecciones al guardar el primer documento.

3. **Sin Archivos .txt**: Los servicios ya no usan `GestorArchivosTxt`. Si aún necesitas los archivos, deberás mantener la clase existente.

4. **Índices MongoDB**: Para mejor rendimiento, considera crear índices en MongoDB Compass para los campos de búsqueda frecuente como `nombre`, `correo`, y `estado`.

## Solución de Problemas

### "Cannot find symbol: class MongoRepository"
- Asegúrate de que `spring-boot-starter-data-mongodb` esté en pom.xml
- Ejecuta `mvn clean install`

### "Cannot connect to MongoDB"
- Verifica que MongoDB esté ejecutándose
- Revisa la URL de conexión en application.properties
- Verifica el firewall si conectas a un servidor remoto

### "No property found for type"
- Asegúrate de que los nombres de las propiedades en las clases coincidan exactamente
- Usa camelCase para las propiedades en Java

## Próximos Pasos

1. Instala y configura MongoDB en tu máquina
2. Compila y ejecuta la aplicación
3. Prueba el login y las operaciones CRUD
4. Verifica los datos en MongoDB Compass

¡Tu aplicación ya está lista para usar MongoDB! 🎉

