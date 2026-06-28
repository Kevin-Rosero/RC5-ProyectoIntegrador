package com.refugio.ui;

import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("admin-panel")
@PageTitle("Administración")
public class PanelAdministracionView extends VerticalLayout implements BeforeEnterObserver {

    public PanelAdministracionView() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);

        add(new H2("Panel de administración"));
        add(new Paragraph("Vista protegida para usuarios administradores."));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!UsuarioSesion.esAdmin()) {
            event.rerouteTo(LoginView.class);
        }
    }
}
