package com.refugio.modelo;

public abstract class Persona {
    private String nombre;
    private String cedula;
    private int edad;
    private String correo;
    private String password;

    // 1. Constructor actualizado para recibir las credenciales
    public Persona(String nombre, String cedula, int edad, String correo, String password) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.edad = edad;
        this.correo = correo;
        this.password = password;
    }

    // Getters y Setters originales
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    // 2. NUEVOS Getters y Setters para correo y password
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract boolean validarDatos();
}