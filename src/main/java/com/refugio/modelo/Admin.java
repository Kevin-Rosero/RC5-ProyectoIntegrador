package com.refugio.modelo;

public class Admin extends Persona{

    public Admin(String nombre, String cedula, int edad) {
        super(nombre, cedula, edad);
    }
    private String idEmpleado;


    @Override
    public boolean validarDatos() {
        return false;
    }
}
