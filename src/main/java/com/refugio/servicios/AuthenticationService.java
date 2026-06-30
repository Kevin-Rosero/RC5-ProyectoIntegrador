package com.refugio.servicios;

import com.refugio.modelo.Admin;
import com.refugio.modelo.Adoptante;
import com.refugio.modelo.Persona;
import com.refugio.repositorios.AdminRepository;
import com.refugio.repositorios.AdoptanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private AdoptanteRepository adoptanteRepository;

    /**
     * Valida las credenciales de un usuario.
     * Retorna un objeto 'Persona' (que puede ser Admin o Adoptante) si el login es exitoso.
     * Retorna 'null' si las credenciales son incorrectas.
     */
    public Persona autenticar(String correo, String password) {

        // Buscar en la colección de Administradores
        Optional<Admin> adminOpt = adminRepository.findByCorreo(correo);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (admin.getPassword().equals(password)) {
                return admin; // Encontramos al admin, lo devolvemos y terminamos la búsqueda.
            }
        }

        // Si no era administrador, buscar en la colección de Adoptantes
        Optional<Adoptante> adoptanteOpt = adoptanteRepository.findByCorreo(correo);
        if (adoptanteOpt.isPresent()) {
            Adoptante adoptante = adoptanteOpt.get();
            if (adoptante.getPassword().equals(password)) {
                return adoptante; // Encontramos al adoptante, lo devolvemos y terminamos.
            }
        }

        // Si terminó de buscar en ambas colecciones y no encontró coincidencias
        return null;
    }
}