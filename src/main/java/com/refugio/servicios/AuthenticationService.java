package com.refugio.servicios;

import com.refugio.archivos.GestorArchivosTxt;
import com.refugio.modelo.Admin;
import com.refugio.modelo.Adoptante;
import com.refugio.modelo.Persona;

import java.util.List;

public class AuthenticationService {

    private GestorArchivosTxt gestor = new GestorArchivosTxt();

    /**
     * Valida las credenciales de un usuario.
     * Retorna un objeto 'Persona' (que puede ser Admin o Adoptante) si el login es exitoso.
     * Retorna 'null' si las credenciales son incorrectas.
     */
    public Persona autenticar(String correo, String password) {

        // Buscar en el archivo de Administradores
        List<Admin> administradores = gestor.leerAdmins();
        for (Admin admin : administradores) {
            // Comparamos el correo (ignorando mayúsculas/minúsculas) y el password (exacto)
            if (admin.getCorreo().equalsIgnoreCase(correo) && admin.getPassword().equals(password)) {
                return admin; // Encontramos al admin, lo devolvemos y terminamos la búsqueda.
            }
        }

        // Si no era administrador, buscar en el archivo de Adoptantes
        List<Adoptante> adoptantes = gestor.leerAdoptantes();
        for (Adoptante adoptante : adoptantes) {
            if (adoptante.getCorreo().equalsIgnoreCase(correo) && adoptante.getPassword().equals(password)) {
                return adoptante; // Encontramos al cliente, lo devolvemos y terminamos.
            }
        }

        // Si terminó de buscar en ambos archivos y no encontró coincidencias
        return null;
    }
}