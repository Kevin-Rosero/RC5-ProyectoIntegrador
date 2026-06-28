package com.refugio.ui;

import com.refugio.archivos.GestorArchivosTxt;
import com.refugio.modelo.Mascota;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.refugio.servicios.UsuarioSesion;

import java.util.List;

@Route("catalogo-cliente")
@PageTitle("Catálogo de mascotas")
public class CatalogoClienteView extends VerticalLayout implements BeforeEnterObserver {

    private final GestorArchivosTxt gestorArchivosTxt = new GestorArchivosTxt();

    public CatalogoClienteView() {
        addClassName("catalogo-cliente-view");
        setSizeFull();
        setPadding(false);
        setSpacing(true);
        setMargin(false);
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH);

        H2 titulo = new H2("Catálogo de mascotas");
        Span subtitulo = new Span("Explora las mascotas registradas y revisa su estado actual.");

        Div grid = new Div();
        grid.addClassName("catalogo-cliente-grid");

        List<Mascota> mascotas = gestorArchivosTxt.leerMascotas();
        if (mascotas.isEmpty()) {
            Div vacio = new Div(new Text("No hay mascotas registradas para mostrar."));
            vacio.getStyle()
                    .set("background-color", "#FFFFFF")
                    .set("border-radius", "18px")
                    .set("padding", "1rem")
                    .set("box-shadow", "0 6px 18px rgba(0, 0, 0, 0.08)");
            grid.add(vacio);
        } else {
            mascotas.forEach(mascota -> grid.add(new TarjetaMascota(mascota)));
        }

        add(titulo, subtitulo, grid);
        expand(grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!UsuarioSesion.esAdoptante()) {
            event.rerouteTo(LoginView.class);
        }
    }
}
