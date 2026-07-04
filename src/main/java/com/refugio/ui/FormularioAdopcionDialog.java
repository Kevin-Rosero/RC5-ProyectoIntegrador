package com.refugio.ui;

import com.refugio.modelo.Adoptante;
import com.refugio.modelo.Evaluacion;
import com.refugio.modelo.Mascota;
import com.refugio.modelo.Persona;
import com.refugio.modelo.SolicitudAdopcion;
import com.refugio.repositorios.SolicitudAdopcionRepository;
import com.refugio.servicios.UsuarioSesion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

public class FormularioAdopcionDialog extends Dialog {

    private static final List<String> TIPOS_MASCOTA = List.of(
            "Perro", "Gato", "Conejo", "Cuy", "Hamster", "Perico", "Canario"
    );
    private static final List<String> TIPOS_VIVIENDA = List.of(
            "Casa con patio grande",
            "Casa sin patio",
            "Departamento amplio",
            "Departamento pequeño",
            "Otro"
    );
    private static final List<String> TIEMPO_SOLO = List.of(
            "Menos de 2 horas",
            "Entre 2 y 5 horas",
            "Entre 5 y 8 horas",
            "Más de 8 horas"
    );
    private static final List<String> TIEMPO_DEDICADO = List.of(
            "Más de 3 horas",
            "Entre 2 y 3 horas",
            "Entre 1 y 2 horas",
            "Menos de 1 hora"
    );
    private static final List<String> CANTIDAD_HOGAR = List.of(
            "1 persona",
            "2 personas",
            "3 a 4 personas",
            "5 o más personas"
    );
    private static final List<String> RESPONSABLES = List.of(
            "Yo",
            "Mi pareja",
            "Toda la familia",
            "Mis hijos",
            "Otra persona"
    );

    private final Mascota mascota;
    private final SolicitudAdopcionRepository solicitudAdopcionRepository;

    private ComboBox<String> tipoMascotaDeseada;
    private TextArea motivoAdopcion;
    private ComboBox<String> tipoVivienda;
    private ComboBox<String> cantidadPersonasHogar;
    private RadioButtonGroup<String> acuerdoFamiliar;
    private ComboBox<String> tiempoSola;
    private ComboBox<String> tiempoDedicado;
    private RadioButtonGroup<String> experienciaPrevia;
    private TextArea destinoMascotasPrevias;
    private RadioButtonGroup<String> recursosEconomicos;
    private RadioButtonGroup<String> respuestaEnfermedad;
    private ComboBox<String> responsablePrincipal;
    private RadioButtonGroup<String> otrasMascotas;
    private RadioButtonGroup<String> planMudanza;
    private RadioButtonGroup<String> dispuestoEsterilizar;
    private RadioButtonGroup<String> aceptaVisitas;
    private RadioButtonGroup<String> conoceResponsabilidades;

    private int puntajeEvaluacion;

    public FormularioAdopcionDialog(Mascota mascota, SolicitudAdopcionRepository solicitudAdopcionRepository) {
        this.mascota = mascota;
        this.solicitudAdopcionRepository = solicitudAdopcionRepository;
        configurarDialog();
    }

    private void configurarDialog() {
        setHeaderTitle("Solicitud de Adopción");
        setWidth("920px");
        setModal(true);

        VerticalLayout contenido = new VerticalLayout();
        contenido.setSpacing(true);
        contenido.setPadding(false);
        contenido.setWidthFull();

        contenido.add(crearInfoMascota());

        Span separador = new Span();
        separador.getStyle().set("border-top", "1px solid #E0E0E0");
        separador.setWidthFull();
        contenido.add(separador);

        contenido.add(crearFormulario());
        contenido.add(crearBotones());

        add(contenido);
    }

    private HorizontalLayout crearInfoMascota() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setWidthFull();
        layout.setSpacing(true);
        layout.setPadding(true);
        layout.addClassNames(LumoUtility.Background.PRIMARY_10, LumoUtility.BorderRadius.MEDIUM);

        Span emoji = new Span(obtenerEmoji(mascota.getEspecie()));
        emoji.addClassNames(LumoUtility.FontSize.XLARGE);

        VerticalLayout info = new VerticalLayout();
        info.setSpacing(false);
        info.setPadding(false);
        info.setMargin(false);

        Span nombre = new Span(mascota.getNombre());
        nombre.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD);

        Span detalles = new Span(mascota.getEspecie() + " • " + mascota.getEdad() + " años");
        detalles.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        info.add(nombre, detalles);
        layout.add(emoji, info);
        return layout;
    }

    private FormLayout crearFormulario() {
        FormLayout form = new FormLayout();
        form.setWidthFull();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("900px", 2)
        );

        tipoMascotaDeseada = new ComboBox<>("Tipo de mascota deseada");
        tipoMascotaDeseada.setItems(TIPOS_MASCOTA);
        tipoMascotaDeseada.setRequiredIndicatorVisible(true);
        tipoMascotaDeseada.setWidthFull();
        String valorInicial = normalizarValorInicial(mascota.getEspecie());
        if (valorInicial != null) {
            tipoMascotaDeseada.setValue(valorInicial);
        }

        tipoVivienda = new ComboBox<>("Tipo de vivienda");
        tipoVivienda.setItems(TIPOS_VIVIENDA);
        tipoVivienda.setRequiredIndicatorVisible(true);
        tipoVivienda.setWidthFull();

        cantidadPersonasHogar = new ComboBox<>("Cantidad de personas en el hogar");
        cantidadPersonasHogar.setItems(CANTIDAD_HOGAR);
        cantidadPersonasHogar.setRequiredIndicatorVisible(true);
        cantidadPersonasHogar.setWidthFull();

        acuerdoFamiliar = crearGrupoOpciones(
                "¿Tu familia está de acuerdo con la adopción?",
                "Sí", "Parcialmente", "No"
        );

        tiempoSola = new ComboBox<>("¿Cuánto tiempo estaría sola la mascota al día?");
        tiempoSola.setItems(TIEMPO_SOLO);
        tiempoSola.setRequiredIndicatorVisible(true);
        tiempoSola.setWidthFull();

        tiempoDedicado = new ComboBox<>("¿Cuánto tiempo dedicarás al cuidado de la mascota?");
        tiempoDedicado.setItems(TIEMPO_DEDICADO);
        tiempoDedicado.setRequiredIndicatorVisible(true);
        tiempoDedicado.setWidthFull();

        experienciaPrevia = crearGrupoOpciones(
                "¿Has tenido mascotas anteriormente?",
                "Sí, actualmente tengo mascotas",
                "Sí, tuve mascotas anteriormente",
                "No"
        );

        destinoMascotasPrevias = new TextArea("Si has tenido mascotas antes, cuéntanos qué pasó con ellas");
        destinoMascotasPrevias.setWidthFull();
        destinoMascotasPrevias.setHeight("100px");

        recursosEconomicos = crearGrupoOpciones(
                "¿Cuentas con recursos económicos para su manutención?",
                "Sí, completamente",
                "Sí, aunque con algunas limitaciones",
                "No"
        );

        respuestaEnfermedad = crearGrupoOpciones(
                "¿Qué harías si la mascota enferma?",
                "La llevaría inmediatamente al veterinario.",
                "Buscaría atención cuando me sea posible.",
                "Intentaría tratarla en casa."
        );

        responsablePrincipal = new ComboBox<>("¿Quién será el responsable principal?");
        responsablePrincipal.setItems(RESPONSABLES);
        responsablePrincipal.setRequiredIndicatorVisible(true);
        responsablePrincipal.setWidthFull();

        otrasMascotas = crearGrupoOpciones(
                "¿Tienes otras mascotas en casa?",
                "Sí",
                "No"
        );

        planMudanza = crearGrupoOpciones(
                "Si te mudas, ¿qué harías con la mascota?",
                "Llevaría conmigo a la mascota.",
                "Buscaría una vivienda que acepte mascotas.",
                "La dejaría con un familiar."
        );

        dispuestoEsterilizar = crearGrupoOpciones(
                "¿Estarías dispuesto a esterilizarla?",
                "Sí",
                "Depende del caso",
                "No"
        );

        aceptaVisitas = crearGrupoOpciones(
                "¿Aceptas visitas de seguimiento?",
                "Sí",
                "No"
        );

        conoceResponsabilidades = crearGrupoOpciones(
                "¿Conoces las responsabilidades de adoptar?",
                "Sí, completamente",
                "Parcialmente",
                "Muy poco"
        );

        motivoAdopcion = new TextArea("¿Por qué deseas adoptar a " + mascota.getNombre() + "?");
        motivoAdopcion.setWidthFull();
        motivoAdopcion.setHeight("120px");
        motivoAdopcion.setRequiredIndicatorVisible(true);

        form.add(
                tipoMascotaDeseada,
                tipoVivienda,
                cantidadPersonasHogar,
                acuerdoFamiliar,
                tiempoSola,
                tiempoDedicado,
                experienciaPrevia,
                destinoMascotasPrevias,
                recursosEconomicos,
                respuestaEnfermedad,
                responsablePrincipal,
                otrasMascotas,
                planMudanza,
                dispuestoEsterilizar,
                aceptaVisitas,
                conoceResponsabilidades,
                motivoAdopcion
        );

        form.setColspan(acuerdoFamiliar, 2);
        form.setColspan(experienciaPrevia, 2);
        form.setColspan(destinoMascotasPrevias, 2);
        form.setColspan(recursosEconomicos, 2);
        form.setColspan(respuestaEnfermedad, 2);
        form.setColspan(otrasMascotas, 2);
        form.setColspan(planMudanza, 2);
        form.setColspan(dispuestoEsterilizar, 2);
        form.setColspan(aceptaVisitas, 2);
        form.setColspan(conoceResponsabilidades, 2);
        form.setColspan(motivoAdopcion, 2);

        return form;
    }

    private RadioButtonGroup<String> crearGrupoOpciones(String etiqueta, String... opciones) {
        RadioButtonGroup<String> grupo = new RadioButtonGroup<>(etiqueta);
        grupo.setItems(opciones);
        grupo.setRequiredIndicatorVisible(true);
        grupo.setWidthFull();
        return grupo;
    }

    private HorizontalLayout crearBotones() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setSpacing(true);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button btnEnviar = new Button("Enviar Solicitud");
        btnEnviar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnEnviar.addClickListener(event -> {
            if (!validarFormulario()) {
                return;
            }

            String estadoPreliminar = evaluarCandidato();
            if (guardarSolicitud(estadoPreliminar)) {
                mostrarNotificacion("Solicitud enviada. Estado preliminar: " + estadoPreliminar, NotificationVariant.LUMO_SUCCESS);
                close();
            }
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.addClickListener(event -> close());

        layout.add(btnCancelar, btnEnviar);
        return layout;
    }

    private boolean validarFormulario() {
        if (tipoMascotaDeseada.isEmpty()
                || tipoVivienda.isEmpty()
                || cantidadPersonasHogar.isEmpty()
                || acuerdoFamiliar.isEmpty()
                || tiempoSola.isEmpty()
                || tiempoDedicado.isEmpty()
                || experienciaPrevia.isEmpty()
                || recursosEconomicos.isEmpty()
                || respuestaEnfermedad.isEmpty()
                || responsablePrincipal.isEmpty()
                || otrasMascotas.isEmpty()
                || planMudanza.isEmpty()
                || dispuestoEsterilizar.isEmpty()
                || aceptaVisitas.isEmpty()
                || conoceResponsabilidades.isEmpty()
                || motivoAdopcion.isEmpty()) {
            mostrarNotificacion("Completa todos los campos obligatorios antes de enviar la solicitud.", NotificationVariant.LUMO_ERROR);
            return false;
        }
        return true;
    }

    private String evaluarCandidato() {
        int puntaje = 0;

        if (mascota.getEspecie() != null && mascota.getEspecie().equalsIgnoreCase(valor(tipoMascotaDeseada))) {
            puntaje += 15;
        } else {
            puntaje -= 10;
        }

        String vivienda = valor(tipoVivienda);
        if ("Casa con patio grande".equals(vivienda)) puntaje += 15;
        else if ("Casa sin patio".equals(vivienda)) puntaje += 10;
        else if ("Departamento amplio".equals(vivienda)) puntaje += 7;
        else if ("Departamento pequeño".equals(vivienda)) puntaje += 2;
        else puntaje += 1;

        String personas = valor(cantidadPersonasHogar);
        if ("1 persona".equals(personas) || "2 personas".equals(personas)) puntaje += 5;
        else if ("3 a 4 personas".equals(personas)) puntaje += 8;
        else if ("5 o más personas".equals(personas)) puntaje += 10;

        String acuerdo = valor(acuerdoFamiliar);
        if ("Sí".equals(acuerdo)) puntaje += 15;
        else if ("Parcialmente".equals(acuerdo)) puntaje += 4;
        else if ("No".equals(acuerdo)) puntaje -= 30;

        String tiempoSolaValor = valor(tiempoSola);
        if ("Menos de 2 horas".equals(tiempoSolaValor)) puntaje += 12;
        else if ("Entre 2 y 5 horas".equals(tiempoSolaValor)) puntaje += 8;
        else if ("Entre 5 y 8 horas".equals(tiempoSolaValor)) puntaje += 2;
        else if ("Más de 8 horas".equals(tiempoSolaValor)) puntaje -= 20;

        String tiempoDedicadoValor = valor(tiempoDedicado);
        if ("Más de 3 horas".equals(tiempoDedicadoValor)) puntaje += 15;
        else if ("Entre 2 y 3 horas".equals(tiempoDedicadoValor)) puntaje += 10;
        else if ("Entre 1 y 2 horas".equals(tiempoDedicadoValor)) puntaje += 4;
        else if ("Menos de 1 hora".equals(tiempoDedicadoValor)) puntaje -= 20;

        String experiencia = valor(experienciaPrevia);
        if ("Sí, actualmente tengo mascotas".equals(experiencia)) puntaje += 10;
        else if ("Sí, tuve mascotas anteriormente".equals(experiencia)) puntaje += 8;
        else if ("No".equals(experiencia)) puntaje += 2;

        String recursos = valor(recursosEconomicos);
        if ("Sí, completamente".equals(recursos)) puntaje += 15;
        else if ("Sí, aunque con algunas limitaciones".equals(recursos)) puntaje += 8;
        else if ("No".equals(recursos)) puntaje -= 30;

        String enfermedad = valor(respuestaEnfermedad);
        if ("La llevaría inmediatamente al veterinario.".equals(enfermedad)) puntaje += 15;
        else if ("Buscaría atención cuando me sea posible.".equals(enfermedad)) puntaje += 8;
        else if ("Intentaría tratarla en casa.".equals(enfermedad)) puntaje -= 20;

        String responsable = valor(responsablePrincipal);
        if ("Yo".equals(responsable)) puntaje += 10;
        else if ("Mi pareja".equals(responsable)) puntaje += 8;
        else if ("Toda la familia".equals(responsable)) puntaje += 7;
        else if ("Mis hijos".equals(responsable)) puntaje += 3;
        else if ("Otra persona".equals(responsable)) puntaje += 2;

        String otrasMascotasValor = valor(otrasMascotas);
        if ("No".equals(otrasMascotasValor)) puntaje += 8;
        else if ("Sí".equals(otrasMascotasValor)) puntaje += 3;

        String mudanza = valor(planMudanza);
        if ("Llevaría conmigo a la mascota.".equals(mudanza)) puntaje += 12;
        else if ("Buscaría una vivienda que acepte mascotas.".equals(mudanza)) puntaje += 10;
        else if ("La dejaría con un familiar.".equals(mudanza)) puntaje -= 10;

        String esterilizar = valor(dispuestoEsterilizar);
        if ("Sí".equals(esterilizar)) puntaje += 8;
        else if ("Depende del caso".equals(esterilizar)) puntaje += 3;
        else if ("No".equals(esterilizar)) puntaje -= 10;

        String visitas = valor(aceptaVisitas);
        if ("Sí".equals(visitas)) puntaje += 8;
        else if ("No".equals(visitas)) puntaje -= 15;

        String responsabilidades = valor(conoceResponsabilidades);
        if ("Sí, completamente".equals(responsabilidades)) puntaje += 10;
        else if ("Parcialmente".equals(responsabilidades)) puntaje += 5;
        else if ("Muy poco".equals(responsabilidades)) puntaje -= 10;

        puntaje += puntajeMotivacion(valor(motivoAdopcion));
        puntaje += puntajeMotivacion(valor(destinoMascotasPrevias));

        puntajeEvaluacion = puntaje;
        return puntaje >= 70 ? "APTO" : "NO APTO";
    }

    private int puntajeMotivacion(String texto) {
        if (texto == null || texto.isBlank()) {
            return 0;
        }
        int longitud = texto.trim().length();
        if (longitud >= 120) {
            return 10;
        }
        if (longitud >= 60) {
            return 6;
        }
        if (longitud >= 25) {
            return 3;
        }
        return -5;
    }

    private boolean guardarSolicitud(String estadoPreliminar) {
        Adoptante adoptante = obtenerAdoptanteSesion();
        if (adoptante == null) {
            mostrarNotificacion("No hay un adoptante válido en la sesión.", NotificationVariant.LUMO_ERROR);
            return false;
        }

        try {
            Evaluacion evaluacion = new Evaluacion(
                    valor(tipoMascotaDeseada),
                    valor(motivoAdopcion),
                    valor(tipoVivienda),
                    valor(cantidadPersonasHogar),
                    valor(acuerdoFamiliar),
                    valor(tiempoSola),
                    valor(tiempoDedicado),
                    valor(experienciaPrevia),
                    valor(destinoMascotasPrevias),
                    valor(recursosEconomicos),
                    valor(respuestaEnfermedad),
                    valor(responsablePrincipal),
                    valor(otrasMascotas),
                    valor(planMudanza),
                    valor(dispuestoEsterilizar),
                    valor(aceptaVisitas),
                    valor(conoceResponsabilidades)
            );
            evaluacion.setPuntajeTotal(puntajeEvaluacion);
            evaluacion.setResultado("Estado preliminar: " + estadoPreliminar + " | Puntaje: " + puntajeEvaluacion);

            SolicitudAdopcion solicitud = new SolicitudAdopcion(
                    "SOL-" + System.currentTimeMillis(),
                    adoptante,
                    mascota,
                    evaluacion
            );
            solicitudAdopcionRepository.save(solicitud);
            return true;
        } catch (Exception e) {
            mostrarNotificacion("No se pudo guardar la solicitud: " + e.getMessage(), NotificationVariant.LUMO_ERROR);
            return false;
        }
    }

    private Adoptante obtenerAdoptanteSesion() {
        Persona personaActual = UsuarioSesion.obtener();
        if (personaActual instanceof Adoptante) {
            return (Adoptante) personaActual;
        }
        return null;
    }

    private String valor(ComboBox<String> campo) {
        return campo.getValue() == null ? "" : campo.getValue();
    }

    private String valor(RadioButtonGroup<String> campo) {
        return campo.getValue() == null ? "" : campo.getValue();
    }

    private String valor(TextArea campo) {
        return campo.getValue() == null ? "" : campo.getValue();
    }

    private String obtenerEmoji(String tipo) {
        if (tipo == null) {
            return "🐾";
        }

        switch (tipo.toLowerCase()) {
            case "perro":
                return "🐶";
            case "gato":
                return "🐱";
            case "conejo":
                return "🐰";
            case "cuy":
                return "🐹";
            case "hamster":
                return "🐹";
            case "perico":
                return "🦜";
            case "canario":
                return "🐤";
            default:
                return "🐾";
        }
    }

    private String normalizarValorInicial(String tipoMascota) {
        if (tipoMascota == null || tipoMascota.isBlank()) {
            return null;
        }
        for (String opcion : TIPOS_MASCOTA) {
            if (opcion.equalsIgnoreCase(tipoMascota.trim())) {
                return opcion;
            }
        }
        return null;
    }

    private void mostrarNotificacion(String mensaje, NotificationVariant variante) {
        Notification notification = Notification.show(mensaje);
        notification.addThemeVariants(variante);
        notification.setDuration(4000);
    }
}
