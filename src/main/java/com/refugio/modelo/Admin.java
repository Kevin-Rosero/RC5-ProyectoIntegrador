package com.refugio.modelo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admins")
public class Admin extends Persona {


    private String idEmpleado;
    private String cargo;
    private boolean permisosTotales;


    public Admin(String nombre, String cedula, int edad, String correo, String password, String idEmpleado, String cargo, boolean permisosTotales) {

        super(nombre, cedula, edad, correo, password);
        this.idEmpleado = idEmpleado;
        this.cargo = cargo;
        this.permisosTotales = permisosTotales;
    }


    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public boolean isPermisosTotales() {
        return permisosTotales;
    }

    public void setPermisosTotales(boolean permisosTotales) {
        this.permisosTotales = permisosTotales;
    }


    @Override
    public boolean validarDatos() {
        // Verifica que el ID no sea nulo ni esté vacío
        boolean idValido = (this.idEmpleado != null && !this.idEmpleado.trim().isEmpty());

        // Verifica que la cédula exista
        boolean cedulaValida = (this.getCedula() != null && !this.getCedula().trim().isEmpty());

        // Verifica que sea mayor de edad (asumiendo que 'edad' y 'cedula' están como 'protected' en Persona)
        boolean edadValida = (this.getEdad() >= 18);

        // Retorna true solo si cumple todas las condiciones
        return idValido && cedulaValida && edadValida;
    }
}