package com.refugio.servicios;

import com.refugio.modelo.Adoptante;
import com.refugio.modelo.Evaluacion;
import com.refugio.modelo.Mascota;
import com.refugio.modelo.SolicitudAdopcion;
import com.refugio.repositorios.MascotaRepository;
import com.refugio.repositorios.SolicitudAdopcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdopcionService {

    @Autowired
    private SolicitudAdopcionRepository solicitudAdopcionRepository;
    
    @Autowired
    private MascotaService mascotaService;

    /**
     * MÉTODO PARA EL CLIENTE (ADOPTANTE)
     * Recibe los datos del formulario de Vaadin, orquesta la evaluación
     * y genera la solicitud en estado PENDIENTE.
     */
    public boolean registrarNuevaSolicitud(Adoptante adoptante, Mascota mascota, Evaluacion evaluacion) {

        // 1. Validar que la mascota siga disponible antes de iniciar el papeleo
        if (!mascota.estaDisponible()) {
            return false; // Error: Alguien más la adoptó mientras llenaba el formulario
        }

        // 2. El "Cerebro" manda a calcular los puntos y la sugerencia
        evaluacion.calcularPuntaje();
        evaluacion.generarSugerenciaSistema();

        // 3. Crear el expediente formal (SolicitudAdopcion)
        // Generamos un ID único basado en los milisegundos actuales (ej.: SOL-171959384)
        String idGenerado = "SOL-" + System.currentTimeMillis();
        SolicitudAdopcion nuevaSolicitud = new SolicitudAdopcion(idGenerado, adoptante, mascota, evaluacion);

        // 4. Guardar en la base de datos MongoDB
        solicitudAdopcionRepository.save(nuevaSolicitud);

        return true; // Solicitud procesada con éxito
    }

    /**
     * MÉTODO PARA EL ADMINISTRADOR (PANEL DE CONTROL)
     * Obtiene solo las solicitudes que requieren revisión humana.
     */
    public List<SolicitudAdopcion> obtenerSolicitudesPendientes() {
        return solicitudAdopcionRepository.findByEstadoTramite("PENDIENTE").stream()
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA EL ADMINISTRADOR (TOMA DE DECISIÓN)
     * Permite al admin sobreescribir la sugerencia del sistema con un veredicto definitivo.
     */
    // Corregimos el parámetro a String idSolicitud
    public boolean emitirVeredictoFinal(String idSolicitud, String decisionAdmin) {
        java.util.Optional<SolicitudAdopcion> solicitudOpt = solicitudAdopcionRepository.findById(idSolicitud);
        
        if (solicitudOpt.isPresent()) {
            SolicitudAdopcion solicitud = solicitudOpt.get();

            // El administrador toma la decisión final (APROBADA o RECHAZADA)
            solicitud.setEstadoTramite(decisionAdmin);

            // Si el administrador aprueba, debemos sacar a la mascota del catálogo
            if (decisionAdmin.equals("APROBADA")) {
                String nombreMascota = solicitud.getMascota().getNombre();
                // Usamos el MascotaService para aplicar la regla de negocio correctamente
                mascotaService.cambiarDisponibilidad(nombreMascota, "ADOPTADA");
            }

            // Guardar los cambios en la base de datos
            solicitudAdopcionRepository.save(solicitud);
            return true;
        }
        return false;
    }
}