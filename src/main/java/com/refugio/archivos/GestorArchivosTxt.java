package com.refugio.archivos;

import com.refugio.modelo.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GestorArchivosTxt {


    private static final String ARCHIVO_MASCOTAS = "mascotas.txt";
    private static final String ARCHIVO_ADOPTANTES = "adoptantes.txt";
    private static final String ARCHIVO_ADMINS = "administradores.txt";
    private static final String ARCHIVO_SOLICITUDES = "solicitudes.txt";
    // Separador de datos para que el programa no se rompa si el usuario usa ',' o ';' en sus datos
    private static final String SEPARADOR = ";";


    //          MÉTODOS PARA MASCOTAS


    public void guardarMascota(Mascota mascota) {
        try (FileWriter fw = new FileWriter(ARCHIVO_MASCOTAS, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String linea = mascota.getEspecie() + SEPARADOR +
                    mascota.getNombre() + SEPARADOR +
                    mascota.getSexo() + SEPARADOR +
                    mascota.getEdad() + SEPARADOR +
                    mascota.getEstadoSalud() + SEPARADOR +
                    mascota.getEstado();

            pw.println(linea);
        } catch (IOException e) {
            System.out.println("Error al guardar mascota: " + e.getMessage());
        }
    }

    public List<Mascota> leerMascotas() {
        List<Mascota> mascotas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_MASCOTAS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(SEPARADOR);
                if (datos.length == 6) {
                    Mascota m = new Mascota(datos[0], datos[1], datos[2], Integer.parseInt(datos[3]), datos[4]);
                    m.actualizarEstado(datos[5]); // Sobrescribe el estado "DISPONIBLE" por defecto si es necesario
                    mascotas.add(m);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de mascotas no encontrado. Se creará uno nuevo.");
        } catch (IOException e) {
            System.out.println("Error al leer mascotas: " + e.getMessage());
        }
        return mascotas;
    }

    public void sobrescribirMascotas(List<Mascota> listaActualizada) {
        // Al NO poner 'true' en el FileWriter, se borra el contenido anterior y se reescribe
        try (FileWriter fw = new FileWriter(ARCHIVO_MASCOTAS, false);
             PrintWriter pw = new PrintWriter(fw)) {

            for (Mascota mascota : listaActualizada) {
                String linea = mascota.getEspecie() + SEPARADOR +
                        mascota.getNombre() + SEPARADOR +
                        mascota.getSexo() + SEPARADOR +
                        mascota.getEdad() + SEPARADOR +
                        mascota.getEstadoSalud() + SEPARADOR +
                        mascota.getEstado();
                pw.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al sobrescribir el archivo de mascotas: " + e.getMessage());
        }
    }


    //          MÉTODOS PARA ADOPTANTES


    public void guardarAdoptante(Adoptante adoptante) {
        try (FileWriter fw = new FileWriter(ARCHIVO_ADOPTANTES, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String linea = adoptante.getNombre() + SEPARADOR +
                    adoptante.getCedula() + SEPARADOR +
                    adoptante.getEdad() + SEPARADOR +
                    adoptante.getCorreo() + SEPARADOR +
                    adoptante.getPassword() + SEPARADOR +
                    adoptante.getDireccion() + SEPARADOR +
                    adoptante.getTelefono();

            pw.println(linea);
        } catch (IOException e) {
            System.out.println("Error al guardar adoptante: " + e.getMessage());
        }
    }

    public List<Adoptante> leerAdoptantes() {
        List<Adoptante> adoptantes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_ADOPTANTES))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(SEPARADOR);
                if (datos.length == 7) {
                    Adoptante a = new Adoptante(datos[0], datos[1], Integer.parseInt(datos[2]),
                            datos[3], datos[4], datos[5], datos[6]);
                    adoptantes.add(a);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de adoptantes no encontrado. Se creará uno nuevo.");
        } catch (IOException e) {
            System.out.println("Error al leer adoptantes: " + e.getMessage());
        }
        return adoptantes;
    }


    //          MÉTODOS PARA SOLICITUDES


    public void guardarSolicitud(SolicitudAdopcion solicitud) {
        try (FileWriter fw = new FileWriter(ARCHIVO_SOLICITUDES, true);
             PrintWriter pw = new PrintWriter(fw)) {

            // Extraemos las respuestas de la evaluación para guardarlas
            Evaluacion ev = solicitud.getEvaluacion();

            // PROTECCIÓN: Si el usuario escribió un ";" en la respuesta abierta, lo cambiamos por "," para no dañar el archivo
            String motivoSeguro = ev.getMotivoAdopcion() != null ? ev.getMotivoAdopcion().replace(";", ",") : "Sin motivo";

            // Armamos la línea. Nota que del adoptante solo guardamos la Cédula y de la mascota solo el Nombre.
            String linea = solicitud.getIdSolicitud() + SEPARADOR +
                    solicitud.getAdoptante().getCedula() + SEPARADOR +
                    solicitud.getMascota().getNombre() + SEPARADOR +
                    ev.getTipoMascotaDeseada() + SEPARADOR +
                    motivoSeguro + SEPARADOR +
                    ev.getTipoVivienda() + SEPARADOR +
                    ev.getCantidadPersonasHogar() + SEPARADOR +
                    ev.getAcuerdoFamiliar() + SEPARADOR +
                    ev.getTiempoSola() + SEPARADOR +
                    ev.getTiempoDedicado() + SEPARADOR +
                    ev.getExperienciaPrevia() + SEPARADOR +
                    ev.getDestinoMascotasPrevias() + SEPARADOR +
                    ev.getRecursosEconomicos() + SEPARADOR +
                    ev.getRespuestaEnfermedad() + SEPARADOR +
                    ev.getResponsablePrincipal() + SEPARADOR +
                    ev.getOtrasMascotas() + SEPARADOR +
                    ev.getPlanMudanza() + SEPARADOR +
                    ev.getDispuestoEsterilizar() + SEPARADOR +
                    ev.getAceptaVisitas() + SEPARADOR +
                    ev.getConoceResponsabilidades() + SEPARADOR +
                    ev.getPuntajeTotal() + SEPARADOR +
                    ev.getResultado() + SEPARADOR +
                    solicitud.getEstadoTramite() + SEPARADOR +
                    solicitud.getFechaCreacion();

            pw.println(linea);
        } catch (IOException e) {
            System.out.println("Error al guardar la solicitud: " + e.getMessage());
        }
    }

    public List<SolicitudAdopcion> leerSolicitudes() {
        List<SolicitudAdopcion> solicitudes = new ArrayList<>();

        // Cargamos a todos los adoptantes y mascotas a la memoria para poder reconstruir la solicitud
        List<Adoptante> todosLosAdoptantes = leerAdoptantes();
        List<Mascota> todasLasMascotas = leerMascotas();

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_SOLICITUDES))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(SEPARADOR);

                // Si la línea tiene los 24 pedazos de información que guardamos
                if (datos.length == 24) {

                    // 1. Buscamos el objeto Adoptante que coincida con la cédula guardada
                    String cedulaGuardada = datos[1];
                    Adoptante adoptanteEncontrado = todosLosAdoptantes.stream()
                            .filter(a -> a.getCedula().equals(cedulaGuardada))
                            .findFirst().orElse(null);

                    // 2. Buscamos el objeto Mascota que coincida con el nombre guardado
                    String nombreMascotaGuardada = datos[2];
                    Mascota mascotaEncontrada = todasLasMascotas.stream()
                            .filter(m -> m.getNombre().equals(nombreMascotaGuardada))
                            .findFirst().orElse(null);

                    // 3. Reconstruimos la Evaluación
                    Evaluacion ev = new Evaluacion(datos[3], datos[4], datos[5], datos[6], datos[7],
                            datos[8], datos[9], datos[10], datos[11], datos[12],
                            datos[13], datos[14], datos[15], datos[16], datos[17],
                            datos[18], datos[19]);
                    ev.setPuntajeTotal(Integer.parseInt(datos[20]));
                    ev.setResultado(datos[21]);

                    // 4. Armamos la Solicitud final
                    if (adoptanteEncontrado != null && mascotaEncontrada != null) {
                        SolicitudAdopcion sol = new SolicitudAdopcion(datos[0], adoptanteEncontrado, mascotaEncontrada, ev, datos[22], datos[23]);
                        solicitudes.add(sol);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de solicitudes no encontrado. Se creará uno nuevo al enviar la primera.");
        } catch (IOException e) {
            System.out.println("Error al leer las solicitudes: " + e.getMessage());
        }
        return solicitudes;
    }

    public void sobrescribirSolicitudes(List<SolicitudAdopcion> solicitudes) {
        // Al NO usar 'true' en FileWriter, limpiamos el archivo y lo reescribimos con los estados actualizados
        try (FileWriter fw = new FileWriter(ARCHIVO_SOLICITUDES, false);
             PrintWriter pw = new PrintWriter(fw)) {

            for (SolicitudAdopcion solicitud : solicitudes) {
                Evaluacion ev = solicitud.getEvaluacion();
                String motivoSeguro = ev.getMotivoAdopcion() != null ? ev.getMotivoAdopcion().replace(";", ",") : "Sin motivo";

                String linea = solicitud.getIdSolicitud() + SEPARADOR +
                        solicitud.getAdoptante().getCedula() + SEPARADOR +
                        solicitud.getMascota().getNombre() + SEPARADOR +
                        ev.getTipoMascotaDeseada() + SEPARADOR +
                        motivoSeguro + SEPARADOR +
                        ev.getTipoVivienda() + SEPARADOR +
                        ev.getCantidadPersonasHogar() + SEPARADOR +
                        ev.getAcuerdoFamiliar() + SEPARADOR +
                        ev.getTiempoSola() + SEPARADOR +
                        ev.getTiempoDedicado() + SEPARADOR +
                        ev.getExperienciaPrevia() + SEPARADOR +
                        ev.getDestinoMascotasPrevias() + SEPARADOR +
                        ev.getRecursosEconomicos() + SEPARADOR +
                        ev.getRespuestaEnfermedad() + SEPARADOR +
                        ev.getResponsablePrincipal() + SEPARADOR +
                        ev.getOtrasMascotas() + SEPARADOR +
                        ev.getPlanMudanza() + SEPARADOR +
                        ev.getDispuestoEsterilizar() + SEPARADOR +
                        ev.getAceptaVisitas() + SEPARADOR +
                        ev.getConoceResponsabilidades() + SEPARADOR +
                        ev.getPuntajeTotal() + SEPARADOR +
                        ev.getResultado() + SEPARADOR +
                        solicitud.getEstadoTramite() + SEPARADOR +
                        solicitud.getFechaCreacion();

                pw.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al sobrescribir solicitudes: " + e.getMessage());
        }
    }


    //          MÉTODOS PARA ADMINISTRADORES


    public void guardarAdmin(Admin admin) {
        try (FileWriter fw = new FileWriter(ARCHIVO_ADMINS, true);
             PrintWriter pw = new PrintWriter(fw)) {

            String linea = admin.getNombre() + SEPARADOR +
                    admin.getCedula() + SEPARADOR +
                    admin.getEdad() + SEPARADOR +
                    admin.getCorreo() + SEPARADOR +
                    admin.getPassword() + SEPARADOR +
                    admin.getIdEmpleado() + SEPARADOR +
                    admin.getCargo() + SEPARADOR +
                    admin.isPermisosTotales();

            pw.println(linea);
        } catch (IOException e) {
            System.out.println("Error al guardar administrador: " + e.getMessage());
        }
    }

    public List<Admin> leerAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO_ADMINS))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(SEPARADOR);
                if (datos.length == 8) {
                    Admin a = new Admin(datos[0], datos[1], Integer.parseInt(datos[2]),
                            datos[3], datos[4], datos[5], datos[6],
                            Boolean.parseBoolean(datos[7]));
                    admins.add(a);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo de administradores no encontrado. Se creará uno nuevo.");
        } catch (IOException e) {
            System.out.println("Error al leer administradores: " + e.getMessage());
        }
        return admins;
    }
}