package com.refugio.ui;

import com.refugio.modelo.Admin;
import com.refugio.modelo.Adoptante;
import com.refugio.modelo.Persona;
import com.refugio.servicios.AuthenticationService;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Acceso")
public class LoginView extends VerticalLayout {

    private final AuthenticationService authenticationService = new AuthenticationService();
    private final EmailField correo = new EmailField("Correo");
    private final PasswordField password = new PasswordField("Contraseña");

    public LoginView() {
        setWidthFull();
        getStyle().set("min-height", "100vh");
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);

        VerticalLayout card = new VerticalLayout();
        card.setWidth("420px");
        card.setPadding(true);
        card.setSpacing(true);
        card.getStyle()
                .set("background-color", "var(--color-blanco-puro)")
                .set("border-radius", "18px")
                .set("box-shadow", "0 8px 24px rgba(0, 0, 0, 0.08)");

        correo.setWidthFull();
        correo.setLabel("Correo");
        password.setWidthFull();
        password.setLabel("Contraseña");

        Button entrar = new Button("Entrar", event -> autenticar());
        entrar.addThemeVariants(com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout acciones = new HorizontalLayout(entrar);
        acciones.setWidthFull();
        acciones.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        acciones.setAlignItems(FlexComponent.Alignment.CENTER);

        card.add(new H2("Iniciar sesión"), new Paragraph("Accede como administrador o adoptante."), correo, password, acciones);
        add(card);
    }

    private void autenticar() {
        Persona persona = authenticationService.autenticar(correo.getValue(), password.getValue());
        if (persona == null) {
            Notification notification = Notification.show("Credenciales inválidas");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        UsuarioSesion.guardar(persona);

        if (persona instanceof Admin) {
            getUI().ifPresent(ui -> ui.navigate(PanelAdministracionView.class));
            return;
        }

        if (persona instanceof Adoptante) {
            getUI().ifPresent(ui -> ui.navigate(CatalogoClienteView.class));
        }
    }
}
