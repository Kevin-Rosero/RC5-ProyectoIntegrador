package com.refugio.modelo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "adoptantes")
public class Adoptante extends Persona {

    // 1. Atributos específicos del adoptante siempre al inicio
    private String direccion;
    private String telefono;

    public Adoptante(String nombre, String cedula, int edad, String correo, String password, String direccion, String telefono) {
        super(nombre, cedula, edad, correo, password);


        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    @Override
    public boolean validarDatos() {
        // Validamos usando los Getters heredados de Persona
        boolean cedulaValida = (this.getCedula() != null && !this.getCedula().trim().isEmpty());
        boolean edadValida = (this.getEdad() >= 18); // Obligatorio ser mayor de edad para adoptar

        // Validamos los atributos propios de Adoptante
        boolean direccionValida = (this.direccion != null && !this.direccion.trim().isEmpty());
        boolean telefonoValido = (this.telefono != null && !this.telefono.trim().isEmpty());

        // Retorna true solo si todo está correcto
        return cedulaValida && edadValida && direccionValida && telefonoValido;
    }

    /* En esta clase iran los metodos relacionados con el adoptante, para que
     * se creen objetos Adoptante (adoptantes), y se guarden en forma de archivos posteriormente*/
}