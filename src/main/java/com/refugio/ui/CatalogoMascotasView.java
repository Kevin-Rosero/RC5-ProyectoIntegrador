package com.refugio.ui;

import com.refugio.modelo.Mascota;
import com.refugio.repositorios.MascotaRepository;
import com.refugio.repositorios.SolicitudAdopcionRepository;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

@Route(value = "mascotas", layout = MainLayout.class)
@PageTitle("Catálogo de Mascotas - Floof")
public class CatalogoMascotasView extends VerticalLayout {

    private final MascotaRepository mascotaRepository;
    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private FlexLayout galeriaMascotas;
    private static final List<String> TIPOS_ANIMALES = Arrays.asList(
            "Perro", "Gato", "Conejo", "Cuy", "Hamster", "Perico", "Canario"
    );

    @Autowired
    public CatalogoMascotasView(MascotaRepository mascotaRepository,
                                SolicitudAdopcionRepository solicitudAdopcionRepository) {
        this.mascotaRepository = mascotaRepository;
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        
        configurarVista();
        cargarMascotas();
    }

    private void configurarVista() {
        setWidthFull();
        setPadding(true);
        setSpacing(true);
        addClassNames(LumoUtility.Padding.MEDIUM);

        // Encabezado con título y botón
        HorizontalLayout encabezado = crearEncabezado();
        add(encabezado);

        // Galería de mascotas en tarjetas
        galeriaMascotas = crearGaleria();
        add(galeriaMascotas);
    }

    private HorizontalLayout crearEncabezado() {
        HorizontalLayout encabezado = new HorizontalLayout();
        encabezado.setWidthFull();
        encabezado.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        encabezado.setSpacing(true);

        H2 titulo = new H2("Catálogo de Mascotas");
        titulo.addClassNames(LumoUtility.Margin.NONE);

        HorizontalLayout espaciador = new HorizontalLayout();
        espaciador.setFlexGrow(1);

        // Botón "Añadir Mascota" - solo visible para Admin
        if (UsuarioSesion.esAdmin()) {
            Button btnAnadir = new Button("Añadir Mascota", new Icon(VaadinIcon.PLUS));
            btnAnadir.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btnAnadir.addClickListener(event -> abrirDialogoCrearMascota());
            encabezado.add(titulo, espaciador, btnAnadir);
        } else {
            encabezado.add(titulo, espaciador);
        }

        return encabezado;
    }

    private FlexLayout crearGaleria() {
        FlexLayout galeria = new FlexLayout();
        galeria.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        galeria.setWidthFull();
        galeria.getStyle().set("gap", "1.5rem");
        galeria.addClassNames(LumoUtility.Padding.MEDIUM);
        return galeria;
    }

    private void cargarMascotas() {
        try {
            galeriaMascotas.removeAll();
            List<Mascota> mascotas = mascotaRepository.findAll();

            if (mascotas.isEmpty()) {
                mostrarNotificacion("No hay mascotas disponibles en el catálogo", NotificationVariant.LUMO_WARNING);
            } else {
                for (Mascota mascota : mascotas) {
                    galeriaMascotas.add(crearTarjetaMascota(mascota));
                }
            }
        } catch (Exception e) {
            mostrarNotificacion("Error al cargar las mascotas: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    private VerticalLayout crearTarjetaMascota(Mascota mascota) {
        VerticalLayout tarjeta = new VerticalLayout();
        tarjeta.setWidth("280px");
        tarjeta.setHeight("380px");
        tarjeta.setPadding(true);
        tarjeta.setSpacing(false);
        tarjeta.addClassNames(
                LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Padding.LARGE,
                LumoUtility.BoxShadow.SMALL
        );
        tarjeta.getStyle().set("background-color", "#FFFFFF");
        tarjeta.getStyle().set("border", "1px solid #E0E0E0");
        tarjeta.getStyle().set("transition", "box-shadow 0.3s ease, transform 0.3s ease");
        tarjeta.getStyle().set("cursor", "pointer");

        // Interacción hover
        tarjeta.getElement().addEventListener("mouseenter", e -> {
            tarjeta.getStyle().set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.15)");
            tarjeta.getStyle().set("transform", "translateY(-4px)");
        });
        tarjeta.getElement().addEventListener("mouseleave", e -> {
            tarjeta.getStyle().set("box-shadow", "0 2px 4px rgba(0, 0, 0, 0.08)");
            tarjeta.getStyle().set("transform", "translateY(0)");
        });

        // Nombre con emoji
        HorizontalLayout headerTarjeta = new HorizontalLayout();
        headerTarjeta.setWidthFull();
        headerTarjeta.setSpacing(true);
        headerTarjeta.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        String emoji = obtenerEmoji(mascota.getEspecie());
        Span nombre = new Span(emoji + " " + mascota.getNombre());
        nombre.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);
        headerTarjeta.add(nombre);

        tarjeta.add(headerTarjeta);

        // Separador visual
        Span separador = new Span();
        separador.getStyle().set("border-top", "1px solid #E0E0E0");
        separador.setWidthFull();
        tarjeta.add(separador);

        // Información de la mascota
        tarjeta.add(crearFilaInfo("Tipo:", mascota.getEspecie()));
        tarjeta.add(crearFilaInfo("Edad:", mascota.getEdad() + " años"));
        tarjeta.add(crearFilaInfo("Sexo:", mascota.getSexo()));
        tarjeta.add(crearFilaInfo("Salud:", mascota.getEstadoSalud()));
        tarjeta.add(crearFilaInfo("Estado:", mascota.getEstado()));

        // Espaciador flexible
        VerticalLayout espaciador = new VerticalLayout();
        espaciador.setFlexGrow(1);
        tarjeta.add(espaciador);

        // Botón de acción dinámico según rol
        Button btnAccion = crearBotonAccion(mascota);
        btnAccion.setWidthFull();
        tarjeta.add(btnAccion);

        return tarjeta;
    }

    private Button crearBotonAccion(Mascota mascota) {
        Button btn = new Button();
        
        if (UsuarioSesion.esAdmin()) {
            btn.setText("Editar Mascota");
            btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            btn.addClickListener(event -> mostrarNotificacion("Abriendo editor...", NotificationVariant.LUMO_PRIMARY));
        } else {
            btn.setText("Adoptar");
            btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            btn.addClickListener(event -> abrirFormularioAdopcion(mascota));
        }
        
        return btn;
    }

    private void abrirFormularioAdopcion(Mascota mascota) {
        new FormularioAdopcionDialog(mascota, solicitudAdopcionRepository).open();
    }

    private VerticalLayout crearFilaInfo(String etiqueta, String valor) {
        VerticalLayout fila = new VerticalLayout();
        fila.setSpacing(false);
        fila.setPadding(false);
        fila.setMargin(false);

        Span labelSpan = new Span(etiqueta);
        labelSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        Span valorSpan = new Span(valor);
        valorSpan.addClassNames(LumoUtility.FontSize.MEDIUM, LumoUtility.FontWeight.SEMIBOLD);

        fila.add(labelSpan, valorSpan);
        return fila;
    }

    private String obtenerEmoji(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "perro" -> "🐶";
            case "gato" -> "🐱";
            case "conejo" -> "🐰";
            case "cuy" -> "🐹";
            case "hamster" -> "🐹";
            case "perico" -> "🦜";
            case "canario" -> "🐤";
            default -> "🐾";
        };
    }

    private void abrirDialogoCrearMascota() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Crear Nueva Mascota");
        dialog.setWidth("500px");
        dialog.setModal(true);

        VerticalLayout contenido = new VerticalLayout();
        contenido.setSpacing(true);
        contenido.setPadding(false);

        // Campos del formulario
        TextField campoNombre = new TextField("Nombre de la Mascota");
        campoNombre.setWidthFull();
        campoNombre.setRequiredIndicatorVisible(true);

        ComboBox<String> comboTipo = new ComboBox<>("Tipo de Animal");
        comboTipo.setItems(TIPOS_ANIMALES);
        comboTipo.setWidthFull();
        comboTipo.setRequiredIndicatorVisible(true);

        IntegerField campoEdad = new IntegerField("Edad (años)");
        campoEdad.setWidthFull();
        campoEdad.setValue(1);
        campoEdad.setMin(0);
        campoEdad.setRequiredIndicatorVisible(true);

        ComboBox<String> comboSexo = new ComboBox<>("Sexo");
        comboSexo.setItems("Macho", "Hembra");
        comboSexo.setWidthFull();
        comboSexo.setRequiredIndicatorVisible(true);

        TextField campoSalud = new TextField("Estado de Salud");
        campoSalud.setWidthFull();
        campoSalud.setRequiredIndicatorVisible(true);

        contenido.add(campoNombre, comboTipo, campoEdad, comboSexo, campoSalud);

        // Botones
        Button btnGuardar = new Button("Guardar");
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnGuardar.addClickListener(event -> {
            if (validarFormulario(campoNombre, comboTipo, campoEdad, comboSexo, campoSalud)) {
                guardarMascota(
                        campoNombre.getValue(),
                        comboTipo.getValue(),
                        campoEdad.getValue(),
                        comboSexo.getValue(),
                        campoSalud.getValue()
                );
                dialog.close();
                cargarMascotas();
            }
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.addClickListener(event -> dialog.close());

        HorizontalLayout botones = new HorizontalLayout(btnGuardar, btnCancelar);
        botones.setWidthFull();
        botones.setSpacing(true);

        dialog.add(contenido);
        dialog.getFooter().add(botones);
        dialog.open();
    }

    private boolean validarFormulario(TextField nombre, ComboBox<String> tipo, IntegerField edad, 
                                      ComboBox<String> sexo, TextField salud) {
        if (nombre.isEmpty()) {
            mostrarNotificacion("El nombre es requerido", NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (tipo.isEmpty()) {
            mostrarNotificacion("Debe seleccionar un tipo de animal", NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (edad.getValue() == null || edad.getValue() < 0) {
            mostrarNotificacion("La edad debe ser un número válido", NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (sexo.isEmpty()) {
            mostrarNotificacion("Debe seleccionar un sexo", NotificationVariant.LUMO_ERROR);
            return false;
        }
        if (salud.isEmpty()) {
            mostrarNotificacion("El estado de salud es requerido", NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    private void guardarMascota(String nombre, String tipo, int edad, String sexo, String salud) {
        try {
            Mascota mascota = new Mascota(tipo, nombre, sexo, edad, salud);
            mascotaRepository.save(mascota);
            mostrarNotificacion("¡Mascota creada exitosamente!", NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            mostrarNotificacion("Error al guardar la mascota: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
        }
    }

    private void mostrarNotificacion(String mensaje, NotificationVariant variante) {
        Notification notification = Notification.show(mensaje);
        notification.addThemeVariants(variante);
        notification.setDuration(4000);
    }
}
