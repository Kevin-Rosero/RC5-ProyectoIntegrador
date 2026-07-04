package com.refugio.ui;

import com.refugio.modelo.Adoptante;
import com.refugio.modelo.SolicitudAdopcion;
import com.refugio.repositorios.SolicitudAdopcionRepository;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "mis-solicitudes", layout = MainLayout.class)
@PageTitle("Mis Solicitudes - Floof")
public class MisSolicitudesView extends VerticalLayout {
    
    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private Grid<SolicitudAdopcion> grid;

    @Autowired
    public MisSolicitudesView(SolicitudAdopcionRepository solicitudAdopcionRepository) {
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        addClassNames(LumoUtility.Padding.MEDIUM);

        H2 titulo = new H2("Mis Solicitudes de Adopción");
        add(titulo);

        if (UsuarioSesion.esAdoptante()) {
            Adoptante adoptante = (Adoptante) UsuarioSesion.obtener();
            if (adoptante != null) {
                cargarSolicitudes(adoptante);
            }
        } else {
            add(new Span("Debes ser un adoptante para ver tus solicitudes."));
        }
    }

    private void cargarSolicitudes(Adoptante adoptante) {
        try {
            List<SolicitudAdopcion> solicitudes = solicitudAdopcionRepository.findByAdoptanteId(adoptante.getId());

            if (solicitudes.isEmpty()) {
                add(new Span("No tienes solicitudes de adopción aún."));
                return;
            }

            grid = new Grid<>(SolicitudAdopcion.class, false);
            grid.setWidthFull();

            grid.addColumn(sol -> sol.getMascota() != null ? sol.getMascota().getNombre() : "N/A")
                    .setHeader("Mascota")
                    .setWidth("150px");

            grid.addColumn(sol -> sol.getMascota() != null ? sol.getMascota().getEspecie() : "N/A")
                    .setHeader("Tipo")
                    .setWidth("100px");

            grid.addColumn(SolicitudAdopcion::getEstadoTramite)
                    .setHeader("Estado")
                    .setWidth("120px");

            grid.addColumn(sol -> sol.getEvaluacion() != null ? sol.getEvaluacion().getResultado() : "N/A")
                    .setHeader("Evaluación Preliminar")
                    .setWidth("200px");

            grid.addColumn(sol -> sol.getEvaluacion() != null ? String.valueOf(sol.getEvaluacion().getPuntajeTotal()) : "N/A")
                    .setHeader("Puntaje")
                    .setWidth("100px");

            grid.addColumn(SolicitudAdopcion::getFechaCreacion)
                    .setHeader("Fecha Solicitud")
                    .setWidth("150px");

            grid.setItems(solicitudes);
            add(grid);
        } catch (Exception e) {
            Notification notification = Notification.show("Error al cargar solicitudes: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
