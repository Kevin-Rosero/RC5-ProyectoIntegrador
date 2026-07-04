package com.refugio.ui;

import com.refugio.modelo.Adoptante;
import com.refugio.repositorios.AdoptanteRepository;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
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

@Route(value = "registro", layout = MainLayout.class)
@PageTitle("Registro de Adoptante - Floof")
public class RegistroAdoptanteView extends VerticalLayout {

    private final AdoptanteRepository adoptanteRepository;
    
    private final TextField nombre = new TextField("Nombre Completo");
    private final TextField cedula = new TextField("Cédula");
    private final NumberField edad = new NumberField("Edad");
    private final TextField correo = new TextField("Correo Electrónico");
    private final TextField telefono = new TextField("Teléfono");
    private final TextField direccion = new TextField("Dirección");
    private final TextField contrasena = new TextField("Contraseña");

    @Autowired
    public RegistroAdoptanteView(AdoptanteRepository adoptanteRepository) {
        this.adoptanteRepository = adoptanteRepository;
        
        configurarVista();
    }

    private void configurarVista() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        addClassNames(LumoUtility.Padding.MEDIUM);

        HorizontalLayout encabezado = crearEncabezado();
        add(encabezado);

        VerticalLayout formulario = crearFormulario();
        add(formulario);
    }

    private HorizontalLayout crearEncabezado() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setSpacing(true);

        H2 titulo = new H2("Registro de Adoptante");
        titulo.addClassNames(LumoUtility.Margin.NONE);

        header.add(titulo);
        return header;
    }

    private VerticalLayout crearFormulario() {
        VerticalLayout formulario = new VerticalLayout();
        formulario.setMaxWidth("600px");
        formulario.setSpacing(true);

        nombre.setPlaceholder("Ej: Juan Pérez");
        nombre.setWidthFull();

        cedula.setPlaceholder("Ej: 1715234567");
        cedula.setWidthFull();

        edad.setMin(18);
        edad.setMax(120);
        edad.setWidthFull();

        correo.setPlaceholder("Ej: juan@example.com");
        correo.setWidthFull();

        telefono.setPlaceholder("Ej: +593 9 99 999 999");
        telefono.setWidthFull();

        direccion.setPlaceholder("Ej: Calle Principal 123, Apto 4B");
        direccion.setWidthFull();

        contrasena.setPlaceholder("Crea una contraseña segura");
        contrasena.setWidthFull();

        HorizontalLayout acciones = new HorizontalLayout();
        acciones.setWidthFull();
        acciones.setSpacing(true);
        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button btnRegistrar = new Button("Registrar y Continuar", new Icon(VaadinIcon.CHECK));
        btnRegistrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnRegistrar.addClickListener(event -> registrarAdoptante());

        Button btnCancelar = new Button("Cancelar", new Icon(VaadinIcon.ARROW_LEFT));
        btnCancelar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnCancelar.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));

        acciones.add(btnCancelar, btnRegistrar);

        formulario.add(
                nombre,
                cedula,
                edad,
                correo,
                telefono,
                direccion,
                contrasena,
                acciones
        );

        return formulario;
    }

    private void registrarAdoptante() {
        if (!validarCampos()) {
            return;
        }

        try {
            Adoptante nuevoAdoptante = new Adoptante(
                    nombre.getValue(),
                    cedula.getValue(),
                    edad.getValue().intValue(),
                    correo.getValue(),
                    contrasena.getValue(),
                    direccion.getValue(),
                    telefono.getValue()
            );

            adoptanteRepository.save(nuevoAdoptante);

            Notification notif = Notification.show("¡Registro exitoso! Bienvenido a Floof");
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notif.setDuration(3000);

            UsuarioSesion.guardar(nuevoAdoptante);

            getUI().ifPresent(ui -> ui.navigate(CatalogoMascotasView.class));

        } catch (Exception e) {
            Notification notif = Notification.show("Error en el registro: " + e.getMessage());
            notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notif.setDuration(4000);
        }
    }

    private boolean validarCampos() {
        if (nombre.getValue().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio");
            return false;
        }

        if (cedula.getValue().trim().isEmpty()) {
            mostrarError("La cédula es obligatoria");
            return false;
        }

        if (edad.getValue() == null || edad.getValue() < 18) {
            mostrarError("Debes ser mayor de 18 años");
            return false;
        }

        if (correo.getValue().trim().isEmpty() || !correo.getValue().contains("@")) {
            mostrarError("Ingresa un correo válido");
            return false;
        }

        if (telefono.getValue().trim().isEmpty()) {
            mostrarError("El teléfono es obligatorio");
            return false;
        }

        if (direccion.getValue().trim().isEmpty()) {
            mostrarError("La dirección es obligatoria");
            return false;
        }

        if (contrasena.getValue().trim().isEmpty() || contrasena.getValue().length() < 6) {
            mostrarError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        return true;
    }

    private void mostrarError(String mensaje) {
        Notification notif = Notification.show(mensaje);
        notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notif.setDuration(4000);
    }
}
