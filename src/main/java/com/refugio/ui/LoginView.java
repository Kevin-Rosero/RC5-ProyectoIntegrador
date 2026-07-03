package com.refugio.ui;

import com.refugio.modelo.Admin;
import com.refugio.modelo.Adoptante;
import com.refugio.modelo.Persona;
import com.refugio.repositorios.AdoptanteRepository;
import com.refugio.servicios.AuthenticationService;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PageTitle("Floof - Iniciar Sesión")
public class LoginView extends VerticalLayout {

    private final AuthenticationService authenticationService;
    private final AdoptanteRepository adoptanteRepository;
    private final TextField usuario = new TextField();
    private final PasswordField contrasena = new PasswordField();

    @Autowired
    public LoginView(AuthenticationService authenticationService, AdoptanteRepository adoptanteRepository) {
        this.authenticationService = authenticationService;
        this.adoptanteRepository = adoptanteRepository;
        
        setWidthFull();
        setHeightFull();
        getStyle()
                .set("min-height", "100vh")
                .set("background-color", "#f5f5f5")
                .set("margin", "0")
                .set("padding", "0");
        
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setPadding(false);
        setSpacing(false);

        add(crearContenedorPrincipal());
    }

    private HorizontalLayout crearContenedorPrincipal() {
        HorizontalLayout principal = new HorizontalLayout();
        principal.setWidthFull();
        principal.setHeight("100vh");
        principal.setSpacing(false);
        principal.setPadding(false);

        // Sección izquierda - Logo y descripción
        VerticalLayout seccionIzquierda = crearSeccionIzquierda();
        seccionIzquierda.setWidth("50%");
        seccionIzquierda.getStyle()
                .set("background-color", "#f9f9f9")
                .set("padding", "40px");

        // Sección derecha - Formulario
        VerticalLayout seccionDerecha = crearSeccionDerecha();
        seccionDerecha.setWidth("50%");
        seccionDerecha.getStyle()
                .set("background-color", "#ffffff")
                .set("padding", "40px")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("justify-content", "center");

        principal.add(seccionIzquierda, seccionDerecha);
        return principal;
    }

    private VerticalLayout crearSeccionIzquierda() {
        VerticalLayout layout = new VerticalLayout();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setSpacing(true);
        layout.setPadding(true);

        H1 titulo = new H1("Floof");
        titulo.getStyle().set("font-size", "48px").set("color", "#5a4a42");
        
        Paragraph slogan = new Paragraph("Adopta amor, cambia una vida.");
        slogan.getStyle()
                .set("font-size", "16px")
                .set("color", "#666666")
                .set("margin-top", "0");

        Paragraph descripcion = new Paragraph("Conectamos animales en situación de calle\ncon familias responsables y amorosas.");
        descripcion.getStyle()
                .set("font-size", "14px")
                .set("color", "#888888")
                .set("text-align", "center");

        layout.add(titulo, slogan, descripcion);
        return layout;
    }

    private VerticalLayout crearSeccionDerecha() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(false);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        H2 encabezado = new H2("Iniciar sesión");
        encabezado.getStyle()
                .set("margin-top", "0")
                .set("color", "#333333");

        // Campo Usuario
        usuario.setPlaceholder("Ingresa tu usuario o correo");
        usuario.setLabel("Usuario");
        usuario.setWidthFull();
        usuario.getStyle().set("margin-bottom", "16px");

        // Campo Contraseña
        contrasena.setPlaceholder("Ingresa tu contraseña");
        contrasena.setLabel("Contraseña");
        contrasena.setWidthFull();
        contrasena.getStyle().set("margin-bottom", "24px");

        // Botón Ingresar
        Button btnIngresar = new Button("Ingresar", event -> validarCredenciales());
        btnIngresar.setWidthFull();
        btnIngresar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnIngresar.getStyle()
                .set("background-color", "#6b8e3d")
                .set("color", "white")
                .set("padding", "12px")
                .set("font-size", "16px")
                .set("border-radius", "4px");

        // Link olvido contraseña
        Paragraph olvidaste = new Paragraph("¿Olvidaste tu contraseña?");
        olvidaste.getStyle()
                .set("font-size", "12px")
                .set("color", "#0066cc")
                .set("text-align", "center")
                .set("margin-top", "16px")
                .set("cursor", "pointer");

        layout.add(encabezado, usuario, contrasena, btnIngresar, olvidaste);
        return layout;
    }

    private void validarCredenciales() {
        String usuarioIngresado = usuario.getValue().trim();
        String contrasenaIngresada = contrasena.getValue().trim();

        // Validación básica
        if (usuarioIngresado.isEmpty() || contrasenaIngresada.isEmpty()) {
            mostrarError("Por favor completa todos los campos");
            return;
        }

        // FLUJO 1: Validar Administrador (credenciales quemadas)
        if (esAdministradorValido(usuarioIngresado, contrasenaIngresada)) {
            Notification notif = Notification.show("Bienvenido Administrador");
            notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notif.setDuration(2000);
            
            // Guardar sesión de admin
            Admin adminSesion = new Admin("Admin", "", 0, usuarioIngresado, contrasenaIngresada, "ADMIN_001", "Administrador", true);
            UsuarioSesion.guardar(adminSesion);
            
            // Navegar a catálogo de mascotas
            getUI().ifPresent(ui -> ui.navigate(CatalogoMascotasView.class));
            return;
        }

        // FLUJO 2: Buscar Adoptante en MongoDB
        try {
            var adoptanteOpt = adoptanteRepository.findByCorreo(usuarioIngresado);
            
            if (adoptanteOpt.isPresent()) {
                // Adoptante existente
                Adoptante adoptante = adoptanteOpt.get();
                
                // Validar contraseña
                if (adoptante.getPassword().equals(contrasenaIngresada)) {
                    Notification notif = Notification.show("¡Bienvenido " + adoptante.getNombre() + "!");
                    notif.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notif.setDuration(2000);
                    
                    UsuarioSesion.guardar(adoptante);
                    
                    // Navegar a vista del adoptante (por ahora usa el catálogo)
                    getUI().ifPresent(ui -> ui.navigate(CatalogoMascotasView.class));
                } else {
                    mostrarError("Contraseña incorrecta");
                    contrasena.clear();
                }
            } else {
                // Adoptante no registrado - redirigir a registro
                mostrarNotificacionRegistro();
                limpiarCampos();
                
                // Navegar a vista de registro
                getUI().ifPresent(ui -> ui.navigate(RegistroAdoptanteView.class));
            }
        } catch (Exception e) {
            mostrarError("Error al procesar login: " + e.getMessage());
        }
    }

    private boolean esAdministradorValido(String usuario, String contrasena) {
        // Admin 1 (El por defecto)
        boolean esAdmin1 = usuario.equals("admin") && contrasena.equals("123");

        // Admin 2
        boolean esAdmin2 = usuario.equals("kevinrosero") && contrasena.equals("floof2026");

        // Admin 3
        boolean esAdmin3 = usuario.equals("joseserrano") && contrasena.equals("floof2026");

        // Admin 4
        boolean esAdmin4 = usuario.equals("andresmorales") && contrasena.equals("floof2026");

        // Si coincide con CUALQUIERA de los tres, devuelve true y lo deja pasar
        return esAdmin1 || esAdmin2 || esAdmin3 || esAdmin4;
    }

    private void mostrarError(String mensaje) {
        Notification notification = Notification.show(mensaje);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setDuration(4000);
    }

    private void mostrarNotificacionRegistro() {
        Notification notification = Notification.show("Parece que es tu primer acceso. Completa tu registro para continuar.");
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.setDuration(5000);
    }

    private void limpiarCampos() {
        usuario.clear();
        contrasena.clear();
    }
}
