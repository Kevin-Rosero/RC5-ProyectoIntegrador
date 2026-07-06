package com.refugio.ui;

import com.refugio.modelo.SolicitudAdopcion;
import com.refugio.repositorios.SolicitudAdopcionRepository;
import com.refugio.repositorios.MascotaRepository;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "solicitudes", layout = MainLayout.class)
@PageTitle("Solicitudes de Adopción - Floof")
public class ListaSolicitudesView extends VerticalLayout implements BeforeEnterObserver {
    
    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final MascotaRepository mascotaRepository;

    @Autowired
    public ListaSolicitudesView(SolicitudAdopcionRepository solicitudAdopcionRepository, MascotaRepository mascotaRepository) {
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        this.mascotaRepository = mascotaRepository;
        
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        addClassNames(LumoUtility.Padding.MEDIUM);

        H2 titulo = new H2("Solicitudes de Adopción");
        titulo.addClassNames(LumoUtility.Margin.NONE);

        add(titulo);
        cargarSolicitudes();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!UsuarioSesion.esAdmin()) {
            event.rerouteTo(LoginView.class);
        }
    }

    private void cargarSolicitudes() {
        try {
            List<SolicitudAdopcion> solicitudes = solicitudAdopcionRepository.findByEstadoTramite("PENDIENTE");

            if (solicitudes.isEmpty()) {
                add(new Span("No hay solicitudes pendientes."));
                return;
            }

            Grid<SolicitudAdopcion> grid = new Grid<>(SolicitudAdopcion.class, false);
            grid.setWidthFull();

            grid.addColumn(sol -> sol.getAdoptante() != null ? sol.getAdoptante().getNombre() : "N/A")
                    .setHeader("Adoptante")
                    .setWidth("200px");

            grid.addColumn(sol -> sol.getMascota() != null ? sol.getMascota().getNombre() : "N/A")
                    .setHeader("Mascota")
                    .setWidth("150px");

            grid.addColumn(SolicitudAdopcion::getFechaCreacion)
                    .setHeader("Fecha")
                    .setWidth("150px");

            grid.addColumn(sol -> sol.getEvaluacion() != null ? sol.getEvaluacion().getResultado() : "N/A")
                    .setHeader("Resultado Preliminar")
                    .setWidth("250px");

            grid.addColumn(sol -> sol.getEvaluacion() != null ? String.valueOf(sol.getEvaluacion().getPuntajeTotal()) : "N/A")
                    .setHeader("Puntaje")
                    .setWidth("100px");

            grid.addComponentColumn(this::crearBotonesSolicitud)
                    .setHeader("Acción")
                    .setWidth("200px");

            grid.setItems(solicitudes);
            add(grid);
        } catch (Exception e) {
            Notification notification = Notification.show("Error al cargar solicitudes: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private HorizontalLayout crearBotonesSolicitud(SolicitudAdopcion solicitud) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        Button btnVer = new Button("Ver solicitud", VaadinIcon.INFO.create());
        btnVer.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        btnVer.addClickListener(event -> abrirDetallesSolicitud(solicitud));

        layout.add(btnVer);
        return layout;
    }

    private void abrirDetallesSolicitud(SolicitudAdopcion solicitud) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Detalles de Solicitud");
        dialog.setWidth("900px");
        dialog.setModal(true);

        VerticalLayout contenido = new VerticalLayout();
        contenido.setSpacing(true);
        contenido.setPadding(false);
        contenido.setMaxHeight("80vh");
        contenido.getStyle().set("overflow-y", "auto");

        // Información del adoptante
        if (solicitud.getAdoptante() != null) {
            H3 h3Adoptante = new H3("Adoptante");
            h3Adoptante.addClassNames(LumoUtility.Margin.Top.NONE);
            contenido.add(h3Adoptante);
            contenido.add(crearFilaInfo("Nombre:", solicitud.getAdoptante().getNombre()));
            contenido.add(crearFilaInfo("Correo:", solicitud.getAdoptante().getCorreo()));
            contenido.add(crearFilaInfo("Teléfono:", solicitud.getAdoptante().getTelefono()));
            contenido.add(crearFilaInfo("Cédula:", solicitud.getAdoptante().getCedula()));
            contenido.add(crearFilaInfo("Edad:", solicitud.getAdoptante().getEdad() + " años"));
            contenido.add(crearFilaInfo("Dirección:", solicitud.getAdoptante().getDireccion()));
        }

        // Información de la mascota
        if (solicitud.getMascota() != null) {
            Span separador = new Span();
            separador.getStyle().set("border-top", "1px solid #E0E0E0");
            separador.setWidthFull();
            contenido.add(separador);

            H3 h3Mascota = new H3("Mascota");
            contenido.add(h3Mascota);
            contenido.add(crearFilaInfo("Nombre:", solicitud.getMascota().getNombre()));
            contenido.add(crearFilaInfo("Especie:", solicitud.getMascota().getEspecie()));
            contenido.add(crearFilaInfo("Edad:", solicitud.getMascota().getEdad() + " años"));
            contenido.add(crearFilaInfo("Sexo:", solicitud.getMascota().getSexo()));
            contenido.add(crearFilaInfo("Estado de Salud:", solicitud.getMascota().getEstadoSalud()));
        }

        // Evaluación
        if (solicitud.getEvaluacion() != null) {
            Span separador = new Span();
            separador.getStyle().set("border-top", "1px solid #E0E0E0");
            separador.setWidthFull();
            contenido.add(separador);

            H3 h3Eval = new H3("Evaluación");
            contenido.add(h3Eval);
            
            // Resumen de evaluación
            contenido.add(crearFilaInfo("Estado Preliminar:", solicitud.getEvaluacion().getResultado()));
            contenido.add(crearFilaInfo("Puntaje Total:", String.valueOf(solicitud.getEvaluacion().getPuntajeTotal())));
            
            // Todas las respuestas del cuestionario
            contenido.add(crearFilaInfo("Tipo de Mascota Deseada:", solicitud.getEvaluacion().getTipoMascotaDeseada()));
            contenido.add(crearFilaInfo("Motivación Adopción:", solicitud.getEvaluacion().getMotivoAdopcion()));
            contenido.add(crearFilaInfo("Tipo Vivienda:", solicitud.getEvaluacion().getTipoVivienda()));
            contenido.add(crearFilaInfo("Cantidad de Personas en el Hogar:", solicitud.getEvaluacion().getCantidadPersonasHogar()));
            contenido.add(crearFilaInfo("Acuerdo Familiar:", solicitud.getEvaluacion().getAcuerdoFamiliar()));
            contenido.add(crearFilaInfo("Tiempo Solo (mascota):", solicitud.getEvaluacion().getTiempoSola()));
            contenido.add(crearFilaInfo("Tiempo Dedicado al Cuidado:", solicitud.getEvaluacion().getTiempoDedicado()));
            contenido.add(crearFilaInfo("Experiencia Previa:", solicitud.getEvaluacion().getExperienciaPrevia()));
            contenido.add(crearFilaInfo("Destino de Mascotas Previas:", solicitud.getEvaluacion().getDestinoMascotasPrevias()));
            contenido.add(crearFilaInfo("Recursos Económicos:", solicitud.getEvaluacion().getRecursosEconomicos()));
            contenido.add(crearFilaInfo("Respuesta Enfermedad:", solicitud.getEvaluacion().getRespuestaEnfermedad()));
            contenido.add(crearFilaInfo("Responsable Principal:", solicitud.getEvaluacion().getResponsablePrincipal()));
            contenido.add(crearFilaInfo("Otras Mascotas:", solicitud.getEvaluacion().getOtrasMascotas()));
            contenido.add(crearFilaInfo("Plan Mudanza:", solicitud.getEvaluacion().getPlanMudanza()));
            contenido.add(crearFilaInfo("Dispuesto a Esterilizar:", solicitud.getEvaluacion().getDispuestoEsterilizar()));
            contenido.add(crearFilaInfo("Acepta Visitas de Seguimiento:", solicitud.getEvaluacion().getAceptaVisitas()));
            contenido.add(crearFilaInfo("Conoce Responsabilidades:", solicitud.getEvaluacion().getConoceResponsabilidades()));
        }

        // Botones de decisión
        Span separadorBotones = new Span();
        separadorBotones.getStyle().set("border-top", "1px solid #E0E0E0");
        separadorBotones.setWidthFull();
        contenido.add(separadorBotones);

        HorizontalLayout botones = new HorizontalLayout();
        botones.setWidthFull();
        botones.setSpacing(true);
        botones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button btnAceptar = new Button("Aceptar", VaadinIcon.CHECK.create());
        btnAceptar.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        btnAceptar.addClickListener(event -> {
            validarSolicitud(solicitud, "APROBADA");
            dialog.close();
            cargarSolicitudes();
        });

        Button btnDeclinar = new Button("Declinar", VaadinIcon.CLOSE.create());
        btnDeclinar.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnDeclinar.addClickListener(event -> {
            validarSolicitud(solicitud, "RECHAZADA");
            dialog.close();
            cargarSolicitudes();
        });

        Button btnCancelar = new Button("Cerrar");
        btnCancelar.addClickListener(event -> dialog.close());

        botones.add(btnCancelar, btnDeclinar, btnAceptar);
        contenido.add(botones);

        dialog.add(contenido);
        dialog.open();
    }

    private void validarSolicitud(SolicitudAdopcion solicitud, String estado) {
        try {
            solicitud.setEstadoTramite(estado);
            solicitudAdopcionRepository.save(solicitud);

            // Si la solicitud fue aprobada, actualizar el estado de la mascota a 'ADOPTADA'
            if ("APROBADA".equalsIgnoreCase(estado) && solicitud.getMascota() != null) {
                try {
                    solicitud.getMascota().actualizarEstado("ADOPTADA");
                    mascotaRepository.save(solicitud.getMascota());
                } catch (Exception ignore) {
                    // Si falla actualizar mascota, no impedir el flujo principal
                }
            }

            String mensaje = estado.equals("APROBADA") ? "Solicitud Aceptada" : "Solicitud Rechazada";
            NotificationVariant variante = estado.equals("APROBADA") ? 
                    NotificationVariant.LUMO_SUCCESS : NotificationVariant.LUMO_ERROR;
            
            Notification notification = Notification.show(mensaje);
            notification.addThemeVariants(variante);
        } catch (Exception e) {
            Notification notification = Notification.show("Error al validar solicitud: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private VerticalLayout crearFilaInfo(String etiqueta, String valor) {
        VerticalLayout fila = new VerticalLayout();
        fila.setSpacing(false);
        fila.setPadding(false);
        fila.setMargin(false);

        Span labelSpan = new Span(etiqueta);
        labelSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.BOLD);

        Span valorSpan = new Span(valor);
        valorSpan.addClassNames(LumoUtility.FontSize.MEDIUM);

        fila.add(labelSpan, valorSpan);
        return fila;
    }
}
