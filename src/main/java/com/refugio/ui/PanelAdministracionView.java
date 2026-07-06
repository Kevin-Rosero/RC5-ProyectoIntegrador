package com.refugio.ui;

import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("admin-panel")
@PageTitle("Administración")
public class PanelAdministracionView extends VerticalLayout implements BeforeEnterObserver {

    public PanelAdministracionView() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);

        getStyle()
                .set("background-color", "#f6f4fb")
                .set("min-height", "100vh");

        add(new H2("Panel de administración"));
        add(new Paragraph("Gestiona adoptantes, mascotas y valida solicitudes de adopción."));
        add(crearSeccionFunciones());
    }

    private VerticalLayout crearSeccionFunciones() {
        VerticalLayout seccion = new VerticalLayout();
        seccion.setSpacing(true);
        seccion.setPadding(true);
        seccion.getStyle()
                .set("background-color", "#ffffff")
                .set("border", "1px solid #d8d2ea")
                .set("border-radius", "12px");

        H3 titulo = new H3("Funciones de Administración");
        titulo.addClassNames(LumoUtility.Margin.Top.NONE);
        seccion.add(titulo);

        HorizontalLayout fila = new HorizontalLayout();
        fila.setSpacing(true);
        fila.setWidthFull();
        fila.getStyle().set("flex-wrap", "wrap");

        Button btnAdoptantes = new Button("Gestionar Adoptantes", VaadinIcon.USERS.create());
        btnAdoptantes.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAdoptantes.setWidth("220px");
        btnAdoptantes.getStyle()
                .set("background-color", "#342d57")
                .set("color", "#ffffff")
                .set("border-radius", "10px");
        btnAdoptantes.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("adoptantes")));

        Button btnSolicitudes = new Button("Validar Solicitudes", VaadinIcon.ENVELOPE_OPEN.create());
        btnSolicitudes.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_TERTIARY);
        btnSolicitudes.setWidth("220px");
        btnSolicitudes.getStyle()
                .set("background-color", "#4f4a79")
                .set("color", "#ffffff")
                .set("border-radius", "10px");
        btnSolicitudes.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("solicitudes")));

        Button btnMascotas = new Button("Catálogo de Mascotas", VaadinIcon.SEARCH.create());
        btnMascotas.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnMascotas.setWidth("220px");
        btnMascotas.getStyle()
                .set("color", "#342d57")
                .set("border", "1px solid #342d57")
                .set("border-radius", "10px");
        btnMascotas.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("mascotas")));

        fila.add(btnAdoptantes, btnSolicitudes, btnMascotas);
        seccion.add(fila);

        return seccion;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!UsuarioSesion.esAdmin()) {
            event.rerouteTo(LoginView.class);
        }
    }
}
