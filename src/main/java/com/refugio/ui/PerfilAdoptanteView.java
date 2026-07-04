package com.refugio.ui;

import com.refugio.modelo.Adoptante;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "perfil", layout = MainLayout.class)
@PageTitle("Mi Perfil - Floof")
public class PerfilAdoptanteView extends VerticalLayout {

    public PerfilAdoptanteView() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        addClassNames(LumoUtility.Padding.MEDIUM);

        H2 titulo = new H2("Mi Perfil");
        add(titulo);

        // Información del adoptante actual
        if (UsuarioSesion.esAdoptante()) {
            Adoptante adoptante = (Adoptante) UsuarioSesion.obtener();
            if (adoptante != null) {
                add(crearSeccionDatos(adoptante));
            }
        } else {
            add(new Span("Debes ser un adoptante para ver tu perfil."));
        }

    }

    private VerticalLayout crearSeccionDatos(Adoptante adoptante) {
        VerticalLayout seccion = new VerticalLayout();
        seccion.setSpacing(false);
        seccion.setPadding(true);
        seccion.addClassNames(
                LumoUtility.Background.BASE,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Border.ALL,
                LumoUtility.Padding.MEDIUM
        );

        H3 subtitulo = new H3("Información Personal");
        subtitulo.addClassNames(LumoUtility.Margin.Top.NONE);
        seccion.add(subtitulo);

        seccion.add(crearFilaDato("Nombre:", adoptante.getNombre()));
        seccion.add(crearFilaDato("Cédula:", adoptante.getCedula()));
        seccion.add(crearFilaDato("Edad:", adoptante.getEdad() + " años"));
        seccion.add(crearFilaDato("Correo:", adoptante.getCorreo()));
        seccion.add(crearFilaDato("Teléfono:", adoptante.getTelefono()));
        seccion.add(crearFilaDato("Dirección:", adoptante.getDireccion()));

        return seccion;
    }

    private VerticalLayout crearFilaDato(String etiqueta, String valor) {
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
