# ⚡ INICIO RÁPIDO EN 60 SEGUNDOS

## 🎯 3 PASOS PARA EMPEZAR

### Paso 1: Instalar MongoDB (30 segundos)

**Opción A - Descarga e instala:**
```bash
# Descarga desde:
https://www.mongodb.com/try/download/community

# En Windows, selecciona "Install MongoDB as a Service"
# Se iniciará automáticamente
```

**Opción B - Con Docker (Más rápido):**
```bash
docker run -d -p 27017:27017 --name refugio-mongodb mongo
```

### Paso 2: Compilar Proyecto (20 segundos)

```bash
cd "E:\UDLA\TERCER SEMESTRE\PROGRAMACION II\PROGRESO 3\PROYECTO INTEGRADOR\Codigo\proyectointegrador\proyectointegrador"
mvn clean install
```

### Paso 3: Ejecutar Aplicación (10 segundos)

```bash
mvn spring-boot:run
```

---

## 🌐 Acceder a la aplicación

Una vez ejecutada, abre tu navegador:

```
http://localhost:8081
```

¡Listo! Tu app usa MongoDB 🚀

---

## ✅ Verificar que funciona

1. Abre MongoDB Compass: https://www.mongodb.com/products/compass
2. Conecta a: `mongodb://localhost:27017`
3. Navega a: `refugio_mascotas`
4. Verás las colecciones: `mascotas`, `adoptantes`, etc.

---

## 📚 ¿Necesitas más información?

Lee los documentos en este orden:

1. **REFERENCIA_RAPIDA.md** - Cheat sheet
2. **GUIA_EJECUCION.md** - Instrucciones detalladas
3. **LISTA_VERIFICACION.md** - Checklist
4. **ARQUITECTURA_MONGODB.md** - Entiende el proyecto

---

## ⚠️ IMPORTANTE

✅ MongoDB debe estar ejecutándose ANTES de iniciar la app
✅ Vaadin UI no cambia - sigue funcionando igual
✅ Todos los datos se guardan automáticamente en MongoDB

---

## 🆘 Si algo falla

```bash
# Problema: Cannot connect to MongoDB
# Solución: mongod

# Problema: Port already in use
# Solución: Cambia puerto en application.properties

# Problema: Cannot find MongoRepository
# Solución: mvn clean install
```

---

¡Eso es todo! Tienes 60 segundos para empezar. 🎉

