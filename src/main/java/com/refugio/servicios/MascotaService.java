package com.refugio.servicios;

import com.refugio.modelo.Mascota;
import com.refugio.repositorios.MascotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MascotaService {
    @Autowired
    private MascotaRepository mascotaRepository;

    // Obtener solo las disponibles
    public List<Mascota> obtenerMascotasDisponibles() {
        return mascotaRepository.findByEstado("DISPONIBLE").stream()
                .filter(Mascota::estaDisponible)
                .collect(Collectors.toList());
    }

    // Buscar mascota por Nombre (Ya que no tenemos ID en el modelo actual)
    public Mascota buscarPorNombre(String nombre) {
        return mascotaRepository.findByNombre(nombre).orElse(null);
    }

    // Agregar una nueva mascota
    public void agregarMascota(Mascota mascota) {
        mascotaRepository.save(mascota);
    }

    // Actualizar una mascota existente
    public boolean actualizarMascota(Mascota mascotaActualizada) {
        Mascota mascota = mascotaRepository.findByNombre(mascotaActualizada.getNombre()).orElse(null);
        
        if (mascota != null) {
            // Actualizar todos los campos
            mascota.setEspecie(mascotaActualizada.getEspecie());
            mascota.setNombre(mascotaActualizada.getNombre());
            mascota.setSexo(mascotaActualizada.getSexo());
            mascota.setEdad(mascotaActualizada.getEdad());
            mascota.setEstadoSalud(mascotaActualizada.getEstadoSalud());
            mascota.actualizarEstado(mascotaActualizada.getEstado());
            mascotaRepository.save(mascota);
            return true;
        }
        return false;
    }

    // Eliminar mascota por Nombre
    public boolean eliminarMascota(String nombre) {
        Mascota mascota = mascotaRepository.findByNombre(nombre).orElse(null);
        
        if (mascota != null) {
            mascotaRepository.delete(mascota);
            return true;
        }
        return false;
    }

    // Cambiar disponibilidad (Ej: cuando es adoptada o entra en tratamiento)
    public boolean cambiarDisponibilidad(String nombre, String nuevoEstado) {
        Mascota mascota = mascotaRepository.findByNombre(nombre).orElse(null);
        
        if (mascota != null) {
            mascota.actualizarEstado(nuevoEstado);
            mascotaRepository.save(mascota);
            return true;
        }
        return false;
    }

    // Buscar por especie
    public List<Mascota> buscarPorEspecie(String especie) {
        return mascotaRepository.findByEspecie(especie);
    }

    // Buscar por edad máxima
    public List<Mascota> buscarPorEdadMaxima(int edad) {
        return mascotaRepository.findByEdadLessThanEqual(edad);
    }
}