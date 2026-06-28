package com.refugio.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import com.refugio.servicios.UsuarioSesion;

@Route("formulario-solicitud")
public class FormularioSolicitudView extends VerticalLayout implements BeforeEnterObserver {

    public FormularioSolicitudView() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);

        add(new H3("Acciones del formulario"));
        add(crearLayoutAccionesFormulario());
        add(crearLayoutAccionesAdministracion());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!UsuarioSesion.esAdmin()) {
            event.rerouteTo(LoginView.class);
        }
    }

    public Button crearBotonGuardar() {
        Button boton = new Button("Guardar");
        boton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        boton.addClassName("boton-accion--texto-legible");
        return boton;
    }

    public Button crearBotonBuscar() {
        Button boton = new Button("Buscar");
        boton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        boton.addClassName("boton-accion--texto-legible");
        return boton;
    }

    public Button crearBotonEditar() {
        Button boton = new Button("Editar");
        boton.addClassName("boton-accion--editar");
        boton.addClassName("boton-accion--texto-legible");
        boton.getStyle()
                .set("background-color", "#FB8C00")
                .set("color", "#FFFFFF")
                .set("border", "none");
        return boton;
    }

    public Button crearBotonEliminar() {
        Button boton = new Button("Eliminar");
        boton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        boton.addClassName("boton-accion--texto-legible");
        return boton;
    }

    public Button crearBotonCancelar() {
        Button boton = new Button("Cancelar");
        boton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        boton.addClassName("boton-accion--texto-legible");
        return boton;
    }

    public HorizontalLayout crearLayoutAccionesFormulario() {
        HorizontalLayout acciones = new HorizontalLayout(crearBotonGuardar(), crearBotonCancelar());
        acciones.setWidthFull();
        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        acciones.setAlignItems(FlexComponent.Alignment.CENTER);
        acciones.setSpacing(true);
        return acciones;
    }

    public HorizontalLayout crearLayoutAccionesAdministracion() {
        HorizontalLayout acciones = new HorizontalLayout(
                crearBotonBuscar(),
                crearBotonEditar(),
                crearBotonEliminar()
        );
        acciones.setWidthFull();
        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        acciones.setAlignItems(FlexComponent.Alignment.CENTER);
        acciones.setSpacing(true);
        return acciones;
    }
}
