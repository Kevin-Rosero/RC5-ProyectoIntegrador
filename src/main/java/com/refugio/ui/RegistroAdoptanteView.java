package com.refugio.ui;

import com.refugio.modelo.Adoptante;
import com.refugio.repositorios.AdoptanteRepository;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "registro")
@PageTitle("Registro de Adoptante - Floof")
public class RegistroAdoptanteView extends VerticalLayout {

    private static final int LONGITUD_NUMERICA = 10;

    private final AdoptanteRepository adoptanteRepository;
    
    private final TextField nombre = new TextField("Nombre Completo");
    private final TextField cedula = new TextField("Cédula");
    private final NumberField edad = new NumberField("Edad");
    private final TextField correo = new TextField("Correo Electrónico");
    private final TextField telefono = new TextField("Teléfono");
    private final TextField direccion = new TextField("Dirección");
    private final PasswordField contrasena = new PasswordField("Contraseña");

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
        cedula.setAllowedCharPattern("[0-9]");
        cedula.addValueChangeListener(event -> validarCampoLongitudNumerica(cedula, "La cédula debe tener exactamente 10 dígitos"));
        cedula.setMaxLength(10);
        cedula.setHelperText("Solo 10 dígitos");

        edad.setMin(0);
        edad.setMax(100);
        edad.setStep(1);
        edad.setHelperText("Ingresa edad válida");
        edad.addValueChangeListener(event -> validarEdadEnTiempoReal());
        edad.setWidthFull();

        correo.setPlaceholder("Ej: juan@example.com");
        correo.setWidthFull();
        correo.addValueChangeListener(event -> {
            limpiarEspaciosEnTiempoReal(correo);
            validarCorreoEnTiempoReal();
        });
        correo.addKeyDownListener(Key.SPACE, event -> aplicarTemblorYEliminarEspacios(correo));

        telefono.setPlaceholder("Ej: (+593) 098 123 4567");
        telefono.setWidthFull();
        telefono.setAllowedCharPattern("[0-9]");
        telefono.addValueChangeListener(event -> validarCampoLongitudNumerica(telefono, "El teléfono debe contener exactamente 10 dígitos"));
        telefono.setMaxLength(10);
        telefono.setHelperText("Solo 10 dígitos");

        direccion.setPlaceholder("Ej: Calle Principal 123, Apto 4B");
        direccion.setWidthFull();

        contrasena.setPlaceholder("Crea una contraseña segura");
        contrasena.setWidthFull();
        contrasena.addValueChangeListener(event -> limpiarEspaciosEnTiempoReal(contrasena));
        contrasena.addKeyDownListener(Key.SPACE, event -> aplicarTemblorYEliminarEspacios(contrasena));

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
        resetValidationState();

        if (nombre.getValue().trim().isEmpty()) {
            nombre.setInvalid(true);
            nombre.setErrorMessage("El nombre es obligatorio");
            mostrarError("El nombre es obligatorio");
            return false;
        }

        if (cedula.getValue() == null || !cedula.getValue().matches("\\d{10}")) {
            cedula.setInvalid(true);
            cedula.setErrorMessage("La cédula debe tener exactamente 10 dígitos");
            mostrarError("La cédula debe tener exactamente 10 dígitos");
            return false;
        }

        if (edad.getValue() == null || edad.getValue() < 18) {
            edad.setInvalid(true);
            edad.setErrorMessage("Ingresa edad válida");
            mostrarError("Ingresa edad válida");
            return false;
        }

        if (correo.getValue().trim().isEmpty() || !correo.getValue().contains("@")) {
            correo.setInvalid(true);
            correo.setErrorMessage("Ingresa un correo válido");
            mostrarError("Ingresa un correo válido");
            return false;
        }

        if (telefono.getValue() == null || !telefono.getValue().matches("\\d{10}")) {
            telefono.setInvalid(true);
            telefono.setErrorMessage("El teléfono debe contener exactamente 10 dígitos");
            mostrarError("El teléfono debe contener exactamente 10 dígitos");
            return false;
        }

        if (direccion.getValue().trim().isEmpty()) {
            direccion.setInvalid(true);
            direccion.setErrorMessage("La dirección es obligatoria");
            mostrarError("La dirección es obligatoria");
            return false;
        }

        if (contrasena.getValue().trim().isEmpty() || contrasena.getValue().length() < 6) {
            contrasena.setInvalid(true);
            contrasena.setErrorMessage("La contraseña debe tener al menos 6 caracteres");
            mostrarError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        return true;
    }

    private void resetValidationState() {
        nombre.setInvalid(false);
        cedula.setInvalid(false);
        edad.setInvalid(false);
        correo.setInvalid(false);
        telefono.setInvalid(false);
        direccion.setInvalid(false);
        contrasena.setInvalid(false);

        nombre.setErrorMessage(null);
        cedula.setErrorMessage(null);
        edad.setErrorMessage(null);
        correo.setErrorMessage(null);
        telefono.setErrorMessage(null);
        direccion.setErrorMessage(null);
        contrasena.setErrorMessage(null);
    }

    private void validarCampoLongitudNumerica(TextField campo, String mensajeError) {
        String valor = campo.getValue();
        boolean invalido = valor != null && !valor.isEmpty() && !valor.matches("\\d{" + LONGITUD_NUMERICA + "}");
        campo.setInvalid(invalido);
        campo.setErrorMessage(invalido ? mensajeError : null);
    }

    private void limpiarEspaciosEnTiempoReal(PasswordField campo) {
        String valor = campo.getValue();
        if (valor != null && valor.contains(" ")) {
            campo.setValue(valor.replace(" ", ""));
        }
    }

    private void aplicarTemblorYEliminarEspacios(PasswordField campo) {
        limpiarEspaciosEnTiempoReal(campo);
        aplicarTemblor(campo);
    }

    private void validarEdadEnTiempoReal() {
        Double valor = edad.getValue();
        boolean invalida = valor != null && (valor < 0 || valor > 100 || valor % 1 != 0);
        edad.setInvalid(invalida || (valor == null && edad.isEmpty()));
        edad.setErrorMessage((invalida || (valor == null && edad.isEmpty())) ? "Ingresa edad válida" : null);
    }

    private void validarCorreoEnTiempoReal() {
        String valor = correo.getValue();
        boolean invalido = valor != null && !valor.isEmpty() && (!valor.contains("@") || valor.contains(" "));
        correo.setInvalid(invalido);
        correo.setErrorMessage(invalido ? "Ingresa un correo válido" : null);
    }

    private void limpiarEspaciosEnTiempoReal(TextField campo) {
        String valor = campo.getValue();
        if (valor != null && valor.contains(" ")) {
            campo.setValue(valor.replace(" ", ""));
        }
    }

    private void aplicarTemblorYEliminarEspacios(TextField campo) {
        if (campo.getValue() != null && campo.getValue().contains(" ")) {
            campo.setValue(campo.getValue().replace(" ", ""));
        }
        aplicarTemblor(campo);
    }

    private void aplicarTemblor(TextField campo) {
        campo.getElement().executeJs(
                "const el = this;" +
                "el.style.transition = 'transform 0.05s';" +
                "el.style.transform = 'translateX(-6px)';" +
                "setTimeout(() => { el.style.transform = 'translateX(6px)'; }, 50);" +
                "setTimeout(() => { el.style.transform = 'translateX(-4px)'; }, 100);" +
                "setTimeout(() => { el.style.transform = 'translateX(4px)'; }, 150);" +
                "setTimeout(() => { el.style.transform = 'translateX(0)'; }, 200);"
        );
    }

    private void aplicarTemblor(PasswordField campo) {
        campo.getElement().executeJs(
                "const el = this;" +
                "el.style.transition = 'transform 0.05s';" +
                "el.style.transform = 'translateX(-6px)';" +
                "setTimeout(() => { el.style.transform = 'translateX(6px)'; }, 50);" +
                "setTimeout(() => { el.style.transform = 'translateX(-4px)'; }, 100);" +
                "setTimeout(() => { el.style.transform = 'translateX(4px)'; }, 150);" +
                "setTimeout(() => { el.style.transform = 'translateX(0)'; }, 200);"
        );
    }

    private void mostrarError(String mensaje) {
        Notification notif = Notification.show(mensaje);
        notif.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notif.setDuration(4000);
    }
}
