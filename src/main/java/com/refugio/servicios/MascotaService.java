package com.refugio.servicios;

import com.refugio.archivos.GestorArchivosTxt;
import com.refugio.modelo.Mascota;
import java.util.List;
import java.util.stream.Collectors;

public class MascotaService {
    private GestorArchivosTxt gestor = new GestorArchivosTxt();

    // Obtener solo las disponibles
    public List<Mascota> obtenerMascotasDisponibles() {
        return gestor.leerMascotas().stream()
                .filter(Mascota::estaDisponible)
                .collect(Collectors.toList());
    }

    // Buscar mascota por Nombre (Ya que no tenemos ID en el modelo actual)
    public Mascota buscarPorNombre(String nombre) {
        return gestor.leerMascotas()
                .stream()
                .filter(m -> m.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    // Agregar una nueva mascota
    public void agregarMascota(Mascota mascota) {
        gestor.guardarMascota(mascota);
    }

    // Actualizar una mascota existente
    public boolean actualizarMascota(Mascota mascotaActualizada) {
        List<Mascota> mascotas = gestor.leerMascotas();

        for (int i = 0; i < mascotas.size(); i++) {
            if (mascotas.get(i).getNombre().equalsIgnoreCase(mascotaActualizada.getNombre())) {
                mascotas.set(i, mascotaActualizada);
                // Aquí usamos un NUEVO método del gestor para sobrescribir todo el archivo
                gestor.sobrescribirMascotas(mascotas);
                return true;
            }
        }
        return false;
    }

    // Eliminar mascota por Nombre
    public boolean eliminarMascota(String nombre) {
        List<Mascota> mascotas = gestor.leerMascotas();

        // removeIf devuelve true si encontró y eliminó a la mascota
        boolean eliminada = mascotas.removeIf(m -> m.getNombre().equalsIgnoreCase(nombre));

        if (eliminada) {
            gestor.sobrescribirMascotas(mascotas);
        }
        return eliminada;
    }

    // Cambiar disponibilidad (Ej: cuando es adoptada o entra en tratamiento)
    public boolean cambiarDisponibilidad(String nombre, String nuevoEstado) {
        List<Mascota> mascotas = gestor.leerMascotas();

        for (Mascota m : mascotas) {
            if (m.getNombre().equalsIgnoreCase(nombre)) {
                // Usamos el método oficial del modelo
                m.actualizarEstado(nuevoEstado);
                gestor.sobrescribirMascotas(mascotas);
                return true;
            }
        }
        return false;
    }

    // Buscar por especie
    public List<Mascota> buscarPorEspecie(String especie) {
        return gestor.leerMascotas()
                .stream()
                .filter(m -> m.getEspecie().equalsIgnoreCase(especie))
                .collect(Collectors.toList());
    }

    // Buscar por edad máxima
    public List<Mascota> buscarPorEdadMaxima(int edad) {
        return gestor.leerMascotas()
                .stream()
                .filter(m -> m.getEdad() <= edad)
                .collect(Collectors.toList());
    }
}