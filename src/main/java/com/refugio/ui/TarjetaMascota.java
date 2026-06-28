package com.refugio.ui;

import com.refugio.modelo.Mascota;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.text.Normalizer;
import java.util.Locale;

public class TarjetaMascota extends Div {

    public TarjetaMascota(Mascota mascota) {
        addClassName("mascota-card");

        H3 nombre = new H3(mascota.getNombre());
        nombre.addClassName("mascota-card__titulo");

        Span badgeEstado = new Span(formatearEstado(mascota.getEstado()));
        badgeEstado.addClassNames("mascota-card__badge", claseBadgeEstado(mascota.getEstado()));

        Div cabecera = new Div(nombre, badgeEstado);
        cabecera.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("align-items", "start")
                .set("gap", "0.75rem");

        VerticalLayout detalles = new VerticalLayout(
                crearMeta("Especie", mascota.getEspecie()),
                crearMeta("Sexo", mascota.getSexo()),
                crearMeta("Edad", mascota.getEdad() + " años"),
                crearMeta("Salud", mascota.getEstadoSalud())
        );
        detalles.setPadding(false);
        detalles.setSpacing(false);
        detalles.setMargin(false);
        detalles.setWidthFull();
        detalles.addClassName("mascota-card__meta");

        add(cabecera, detalles);
    }

    private Component crearMeta(String etiqueta, String valor) {
        Span texto = new Span(etiqueta + ": " + valor);
        texto.getStyle().set("display", "block");
        return texto;
    }

    private String formatearEstado(String estado) {
        String normalizado = normalizar(estado);
        return switch (normalizado) {
            case "disponible" -> "Disponible";
            case "en proceso de adopcion" -> "En proceso de adopción";
            case "adoptada" -> "Adoptada";
            case "no disponible" -> "No disponible";
            default -> estado != null && !estado.isBlank() ? estado.trim() : "Sin estado";
        };
    }

    private String claseBadgeEstado(String estado) {
        String normalizado = normalizar(estado);
        return switch (normalizado) {
            case "disponible" -> "mascota-card__badge--disponible";
            case "en proceso de adopcion" -> "mascota-card__badge--en-proceso-de-adopcion";
            case "adoptada" -> "mascota-card__badge--adoptada";
            case "no disponible" -> "mascota-card__badge--no-disponible";
            default -> "mascota-card__badge--no-disponible";
        };
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }

        String sinAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinAcentos.trim().toLowerCase(Locale.ROOT);
    }
}
