package com.refugio.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mascotas")
public class Mascota {

    @Id
    private String id;
    private String especie;
    private String nombre;
    private String sexo;
    private int edad;


    private String estadoSalud;
    private String estado;

    // Constructor vacío requerido por Spring Data MongoDB
    public Mascota() {
    }

    public Mascota(String especie, String nombre, String sexo, int edad, String estadoSalud) {
        this.especie = especie;
        this.nombre = nombre;
        this.sexo = sexo;
        this.edad = edad;
        this.estadoSalud = estadoSalud;
        this.estado = "DISPONIBLE";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getEstadoSalud() {
        return estadoSalud;
    }

    public void setEstadoSalud(String estadoSalud) {
        this.estadoSalud = estadoSalud;
    }

    public String getEstado() {
        return estado;
    }

    public void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean estaDisponible() {
        return this.estado != null && this.estado.equalsIgnoreCase("DISPONIBLE");
    }
}