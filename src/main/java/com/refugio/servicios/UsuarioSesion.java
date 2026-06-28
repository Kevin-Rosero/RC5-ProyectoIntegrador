package com.refugio.servicios;

import com.refugio.modelo.Admin;
import com.refugio.modelo.Adoptante;
import com.refugio.modelo.Persona;
import com.vaadin.flow.server.VaadinSession;

public final class UsuarioSesion {

    private static final String CLAVE_USUARIO = UsuarioSesion.class.getName() + ".usuario";

    private UsuarioSesion() {
    }

    public static void guardar(Persona persona) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(CLAVE_USUARIO, persona);
        }
    }

    public static Persona obtener() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session == null) {
            return null;
        }
        return (Persona) session.getAttribute(CLAVE_USUARIO);
    }

    public static boolean esAdmin() {
        return obtener() instanceof Admin;
    }

    public static boolean esAdoptante() {
        return obtener() instanceof Adoptante;
    }

    public static void limpiar() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(CLAVE_USUARIO, null);
        }
    }
}
