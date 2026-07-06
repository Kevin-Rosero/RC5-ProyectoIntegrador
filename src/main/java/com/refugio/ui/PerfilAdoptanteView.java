package com.refugio.ui;

import com.refugio.modelo.Adoptante;
import com.refugio.repositorios.AdoptanteRepository;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Objects;

@Route(value = "perfil", layout = MainLayout.class)
@PageTitle("Mi Perfil - Floof")
public class PerfilAdoptanteView extends VerticalLayout {

    private final AdoptanteRepository adoptanteRepository;

    @Autowired
    public PerfilAdoptanteView(AdoptanteRepository adoptanteRepository) {
        this.adoptanteRepository = adoptanteRepository;
        Objects.requireNonNull(this.adoptanteRepository);

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

        // Botón para editar información del adoptante
        Button btnEditar = new Button("Editar", new Icon(VaadinIcon.EDIT));
        btnEditar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnEditar.getElement().getStyle().set("margin-top", "8px");
        btnEditar.addClickListener(event -> abrirDialogoEditar(adoptante));

        HorizontalLayout acciones = new HorizontalLayout(btnEditar);
        acciones.setWidthFull();
        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        seccion.add(acciones);

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

    private void abrirDialogoEditar(Adoptante adoptante) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Editar Perfil");
        dialog.setWidth("560px");

        VerticalLayout contenido = new VerticalLayout();
        contenido.setSpacing(true);

        TextField nombre = new TextField("Nombre Completo");
        nombre.setWidthFull();
        nombre.setValue(adoptante.getNombre() != null ? adoptante.getNombre() : "");

        TextField cedula = new TextField("Cédula");
        cedula.setWidthFull();
        cedula.setValue(adoptante.getCedula() != null ? adoptante.getCedula() : "");

        NumberField edad = new NumberField("Edad");
        edad.setMin(18);
        edad.setWidthFull();
        edad.setValue((double) adoptante.getEdad());

        TextField correo = new TextField("Correo Electrónico");
        correo.setWidthFull();
        correo.setValue(adoptante.getCorreo() != null ? adoptante.getCorreo() : "");

        TextField telefono = new TextField("Teléfono");
        telefono.setWidthFull();
        telefono.setValue(adoptante.getTelefono() != null ? adoptante.getTelefono() : "");

        TextField direccion = new TextField("Dirección");
        direccion.setWidthFull();
        direccion.setValue(adoptante.getDireccion() != null ? adoptante.getDireccion() : "");

        contenido.add(nombre, cedula, edad, correo, telefono, direccion);

        Button btnGuardar = new Button("Guardar", new Icon(VaadinIcon.CHECK));
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnGuardar.addClickListener(event -> {
            // Validaciones simples
            if (nombre.getValue().trim().isEmpty()) {
                Notification.show("El nombre es obligatorio").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (cedula.getValue().trim().isEmpty()) {
                Notification.show("La cédula es obligatoria").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (edad.getValue() == null || edad.getValue() < 18) {
                Notification.show("Debes ser mayor de 18 años").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (correo.getValue().trim().isEmpty() || !correo.getValue().contains("@")) {
                Notification.show("Ingresa un correo válido").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (telefono.getValue().trim().isEmpty()) {
                Notification.show("El teléfono es obligatorio").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (direccion.getValue().trim().isEmpty()) {
                Notification.show("La dirección es obligatoria").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                adoptante.setNombre(nombre.getValue());
                adoptante.setCedula(cedula.getValue());
                adoptante.setEdad(edad.getValue().intValue());
                adoptante.setCorreo(correo.getValue());
                adoptante.setTelefono(telefono.getValue());
                adoptante.setDireccion(direccion.getValue());

                // Usamos el repositorio para guardar los cambios
                adoptanteRepository.save(adoptante);
                UsuarioSesion.guardar(adoptante);

                Notification success = Notification.show("Perfil actualizado correctamente");
                success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                success.setDuration(3000);

                dialog.close();

                // Recargar la vista para reflejar cambios
                getUI().ifPresent(ui -> ui.navigate(PerfilAdoptanteView.class));

            } catch (Exception e) {
                Notification notif = Notification.show("Error al actualizar perfil: " + e.getMessage());
                notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.addClickListener(ev -> dialog.close());

        HorizontalLayout acciones = new HorizontalLayout(btnCancelar, btnGuardar);
        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        dialog.add(contenido);
        dialog.getFooter().add(acciones);
        dialog.open();
    }
}
