package com.refugio.ui;

import com.refugio.modelo.Adoptante;
import com.refugio.repositorios.AdoptanteRepository;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "adoptantes", layout = MainLayout.class)
@PageTitle("Adoptantes - Floof")
public class ListaAdoptantesView extends VerticalLayout implements BeforeEnterObserver {
    
    private final AdoptanteRepository adoptanteRepository;
    private Grid<Adoptante> grid;

    @Autowired
    public ListaAdoptantesView(AdoptanteRepository adoptanteRepository) {
        this.adoptanteRepository = adoptanteRepository;
        
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        addClassNames(LumoUtility.Padding.MEDIUM);

        H2 titulo = new H2("Gestión de Adoptantes");
        titulo.addClassNames(LumoUtility.Margin.NONE);

        add(titulo);
        cargarAdoptantes();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!UsuarioSesion.esAdmin()) {
            event.rerouteTo(LoginView.class);
        }
    }

    private void cargarAdoptantes() {
        try {
            List<Adoptante> adoptantes = adoptanteRepository.findAll();

            if (adoptantes.isEmpty()) {
                add(new Span("No hay adoptantes registrados aún."));
                return;
            }

            grid = new Grid<>(Adoptante.class, false);
            grid.setWidthFull();

            grid.addColumn(Adoptante::getNombre)
                    .setHeader("Nombre")
                    .setWidth("200px");

            grid.addColumn(Adoptante::getCedula)
                    .setHeader("Cédula")
                    .setWidth("150px");

            grid.addColumn(Adoptante::getCorreo)
                    .setHeader("Correo")
                    .setWidth("250px");

            grid.addColumn(Adoptante::getTelefono)
                    .setHeader("Teléfono")
                    .setWidth("150px");

            grid.addColumn(Adoptante::getDireccion)
                    .setHeader("Dirección")
                    .setWidth("300px");

            grid.addColumn(Adoptante::getEdad)
                    .setHeader("Edad")
                    .setWidth("80px");

            grid.setItems(adoptantes);
            add(grid);
        } catch (Exception e) {
            Notification notification = Notification.show("Error al cargar adoptantes: " + e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
