package com.refugio.modelo;

public class Adoptante extends Persona{

    public Adoptante(String nombre, String cedula, int edad) {
        super(nombre, cedula, edad);
    }

    @Override
    public boolean validarDatos() {
        return false;
    }
    /* En esta clase iran los metodos realcionados con el adoptante, para que
    * se creen objetos Adoptante (adoptantes), y se guarden en forma de archivos posteriormente*/
}
