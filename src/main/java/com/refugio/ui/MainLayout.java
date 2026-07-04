package com.refugio.ui;

import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.setPadding(true);
        header.setSpacing(true);

        // Determinar color del header según rol
        if (UsuarioSesion.esAdmin()) {
            header.getStyle().set("background-color", "#460bc9");
        } else {
            header.addClassNames(LumoUtility.Background.PRIMARY);
        }
        header.addClassNames(LumoUtility.Padding.MEDIUM);

        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        H1 titulo = new H1("Floof - Panel de Administración");
        titulo.getStyle().set("margin", "0");
        titulo.getStyle().set("color", "#FFFFFF");
        titulo.addClassNames(LumoUtility.FontSize.LARGE);

        HorizontalLayout espaciador = new HorizontalLayout();
        espaciador.setFlexGrow(1);

        Button btnCerrarSesion = new Button("Cerrar Sesión", new Icon(VaadinIcon.SIGN_OUT));
        btnCerrarSesion.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        btnCerrarSesion.getStyle().set("color", "#FFFFFF");
        btnCerrarSesion.addClickListener(event -> cerrarSesion());

        header.add(toggle, titulo, espaciador, btnCerrarSesion);
        addToNavbar(header);
    }

    private void createDrawer() {
        VerticalLayout drawerLayout = new VerticalLayout();
        drawerLayout.setPadding(false);
        drawerLayout.setSpacing(false);
        drawerLayout.setWidthFull();

        // Encabezado del drawer
        HorizontalLayout drawerHeader = new HorizontalLayout();
        drawerHeader.setWidthFull();
        drawerHeader.setPadding(true);
        drawerHeader.addClassNames(
                LumoUtility.Background.PRIMARY_10,
                LumoUtility.BorderRadius.MEDIUM
        );

        Span titleDrawer = new Span("FUNCIONES");
        titleDrawer.addClassNames(
                LumoUtility.FontWeight.BOLD,
                LumoUtility.FontSize.SMALL
        );

        drawerHeader.add(titleDrawer);
        drawerLayout.add(drawerHeader);

        // Menú de navegación
        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.setPadding(false);
        menuLayout.setSpacing(false);
        menuLayout.setWidthFull();

        if (UsuarioSesion.esAdmin()) {
            // Menú para ADMIN
            menuLayout.add(crearNavLink("Catálogo de Mascotas", VaadinIcon.SEARCH, CatalogoMascotasView.class));
            menuLayout.add(crearNavLink("Adoptantes", VaadinIcon.USERS, ListaAdoptantesView.class));
            menuLayout.add(crearNavLink("Solicitudes Generales", VaadinIcon.ENVELOPE_OPEN, ListaSolicitudesView.class));
        } else {
            // Menú para ADOPTANTE
            menuLayout.add(crearNavLink("Mascotas Disponibles", VaadinIcon.SEARCH, CatalogoMascotasView.class));
            menuLayout.add(crearNavLink("Mi Perfil", VaadinIcon.USER, PerfilAdoptanteView.class));
            menuLayout.add(crearNavLink("Mis Solicitudes", VaadinIcon.FILE_TEXT, MisSolicitudesView.class));
        }

        drawerLayout.add(menuLayout);
        drawerLayout.setFlexGrow(1);
        addToDrawer(drawerLayout);
    }

    private HorizontalLayout crearNavLink(String label, VaadinIcon icon, Class<? extends Component> view) {
        HorizontalLayout navItem = new HorizontalLayout();
        navItem.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        navItem.setWidthFull();
        navItem.setPadding(true);
        navItem.setSpacing(true);
        navItem.getStyle().set("cursor", "pointer");
        navItem.getStyle().set("border-radius", "8px");
        navItem.getStyle().set("transition", "background-color 0.2s ease");

        // Efecto hover
        navItem.getElement().addEventListener("mouseenter", e ->
                navItem.getStyle().set("background-color", "#f0f0f0")
        );
        navItem.getElement().addEventListener("mouseleave", e ->
                navItem.getStyle().set("background-color", "transparent")
        );

        Icon navIcon = new Icon(icon);
        navIcon.getStyle().set("width", "20px");
        navIcon.getStyle().set("height", "20px");

        RouterLink link = new RouterLink(label, view);
        link.getStyle().set("text-decoration", "none");
        link.getStyle().set("color", "inherit");
        link.getStyle().set("flex-grow", "1");

        navItem.add(navIcon, link);
        return navItem;
    }

    private void cerrarSesion() {
        UsuarioSesion.limpiar();
        getUI().ifPresent(ui -> ui.navigate(LoginView.class));
    }
}
