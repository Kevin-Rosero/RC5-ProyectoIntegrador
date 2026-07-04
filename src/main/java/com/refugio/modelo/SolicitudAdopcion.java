package com.refugio.modelo;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "solicitudes_adopcion")
public class SolicitudAdopcion {

    // 1. Atributos
    @Id
    private String idSolicitud; // Ej: "SOL-001"
    private Adoptante adoptante; // El cliente que hace la solicitud
    private Mascota mascota; // El perrito o gatito deseado
    private Evaluacion evaluacion; // Las 17 respuestas y el puntaje

    private String estadoTramite; // "PENDIENTE", "APROBADA", "RECHAZADA"
    private String fechaCreacion; // Día en el que se llenó el formulario

    // Constructor vacío requerido por Spring Data MongoDB
    public SolicitudAdopcion() {
    }

    // 2. Constructor
    public SolicitudAdopcion(String idSolicitud, Adoptante adoptante, Mascota mascota, Evaluacion evaluacion) {
        this.idSolicitud = idSolicitud;
        this.adoptante = adoptante;
        this.mascota = mascota;
        this.evaluacion = evaluacion;

        // Valores automáticos por defecto al crear una nueva solicitud
        this.estadoTramite = "PENDIENTE";
        this.fechaCreacion = LocalDate.now().toString(); // Asigna la fecha actual automáticamente (Ej: "2026-06-28")
    }

    // Constructor sobrecargado (Opcional, muy útil para cuando LEAS del archivo txt)
    public SolicitudAdopcion(String idSolicitud, Adoptante adoptante, Mascota mascota, Evaluacion evaluacion, String estadoTramite, String fechaCreacion) {
        this.idSolicitud = idSolicitud;
        this.adoptante = adoptante;
        this.mascota = mascota;
        this.evaluacion = evaluacion;
        this.estadoTramite = estadoTramite;
        this.fechaCreacion = fechaCreacion;
    }

    // 3. Getters y Setters
    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Adoptante getAdoptante() {
        return adoptante;
    }

    public void setAdoptante(Adoptante adoptante) {
        this.adoptante = adoptante;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getEstadoTramite() {
        return estadoTramite;
    }

    public void setEstadoTramite(String estadoTramite) {
        this.estadoTramite = estadoTramite;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}