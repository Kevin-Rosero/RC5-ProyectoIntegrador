# Proyecto Refugio - Arquitectura y Flujo de Ejecución

## 1. Resumen del Flujo de Ejecución del Proyecto

Este es el ciclo de vida de la aplicación desde que se ejecuta hasta que se completan las tareas principales.

El programa se va a desarrollar mediante la "arquitectura basada en roles".

*   **Arranque y Acceso (Login Único):**
    *   La aplicación web se inicia a través de Vaadin. El usuario llega a una única pantalla de inicio de sesión.
    *   El sistema verifica las credenciales ingresadas y enruta al usuario dependiendo de su rol: Cliente (Adoptante) o Administrador.

*   **Modo Cliente (Adoptante):**
    *   **Catálogo:** El cliente ve una galería visual con las mascotas cuyo estado sea `DISPONIBLE`.
    *   **Formulario Inteligente:** Si le interesa una mascota, el cliente inicia una solicitud. Aquí llena el cuestionario de evaluación (tipo de vivienda, tiempo disponible, etc.) mediante opciones desplegables. Si elige "Otro", escribe en el campo de texto abierto.
    *   **Persistencia:** Al enviar el formulario, el sistema calcula automáticamente el puntaje de la `Evaluacion`. La `SolicitudAdopcion` se guarda en un archivo `.txt` con estado `PENDIENTE`, y la sesión del cliente puede cerrarse.

*   **Modo Administrador (Gestor del Refugio):**
    *   **Dashboard:** Tras iniciar sesión con su contraseña, accede a un panel de control avanzado.
    *   **Toma de Decisiones:** El administrador abre las solicitudes `PENDIENTES`. Revisa rápidamente el puntaje calculado por el sistema y lee los campos de texto abiertos para tomar la decisión final.
    *   **Resolución:** Cambia el estado de la solicitud a `APROBADA` o `RECHAZADA`. Si es aprobada, el estado de la `Mascota` pasa a `ADOPTADA` y se genera un registro en el `HistorialAdopcion`.
    *   **Gestión y Reportes:** Puede agregar nuevos animales al sistema y descargar archivos `.txt` consolidados con los reportes del mes.

---

## 2. Esquema de Paquetes en Vaadin (Arquitectura Limpia)

El paquete de **servicios** es el motor interno. Este es el esquema definitivo que debes replicar en tu IDE:

```text
com.refugio  (Paquete Raíz)
│
├── Application.java                  <-- Arranca el servidor de Spring Boot/Vaadin. ¡No se toca!
│
├── modelo/                           <-- Los datos puros.
│   ├── Persona.java                  
│   ├── Adoptante.java                
│   ├── Mascota.java                  
│   ├── Evaluacion.java               
│   └── SolicitudAdopcion.java        
│
├── servicios/                        <-- El "Cerebro" del sistema (Reglas de negocio).
│   ├── AutenticacionService.java     <-- Decide si el login es de Admin o Cliente.
│   ├── AdopcionService.java          <-- Calcula el puntaje y valida si es apto.
│   └── MascotaService.java           <-- Filtra y actualiza el inventario.
│
├── archivos/                         <-- La "Base de Datos".
│   └── GestorArchivosTxt.java        <-- Única clase encargada de leer/escribir los .txt.
│
└── ui/                               <-- La Interfaz Gráfica (Vaadin).
    ├── LoginView.java                
    ├── CatalogoClienteView.java      
    └── DashboardAdminView.java