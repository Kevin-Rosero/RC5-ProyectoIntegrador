package com.refugio.modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "evaluaciones")
public class Evaluacion {

    @Id
    private String id;

    private String tipoMascotaDeseada;
    private String motivoAdopcion; // Respuesta abierta
    private String tipoVivienda;
    private String cantidadPersonasHogar;
    private String acuerdoFamiliar;
    private String tiempoSola;
    private String tiempoDedicado;
    private String experienciaPrevia;
    private String destinoMascotasPrevias;
    private String recursosEconomicos;
    private String respuestaEnfermedad;
    private String responsablePrincipal;
    private String otrasMascotas;
    private String planMudanza;
    private String dispuestoEsterilizar;
    private String aceptaVisitas;
    private String conoceResponsabilidades;


    private int puntajeTotal;
    private String resultado; // PENDIENTE, APROBADA, RECHAZADA

    // Constructor vacío requerido por Spring Data MongoDB
    public Evaluacion() {
    }

    public Evaluacion(String tipoMascotaDeseada, String motivoAdopcion, String tipoVivienda,
                      String cantidadPersonasHogar, String acuerdoFamiliar, String tiempoSola,
                      String tiempoDedicado, String experienciaPrevia, String destinoMascotasPrevias,
                      String recursosEconomicos, String respuestaEnfermedad, String responsablePrincipal,
                      String otrasMascotas, String planMudanza, String dispuestoEsterilizar,
                      String aceptaVisitas, String conoceResponsabilidades) {

        this.tipoMascotaDeseada = tipoMascotaDeseada;
        this.motivoAdopcion = motivoAdopcion;
        this.tipoVivienda = tipoVivienda;
        this.cantidadPersonasHogar = cantidadPersonasHogar;
        this.acuerdoFamiliar = acuerdoFamiliar;
        this.tiempoSola = tiempoSola;
        this.tiempoDedicado = tiempoDedicado;
        this.experienciaPrevia = experienciaPrevia;
        this.destinoMascotasPrevias = destinoMascotasPrevias;
        this.recursosEconomicos = recursosEconomicos;
        this.respuestaEnfermedad = respuestaEnfermedad;
        this.responsablePrincipal = responsablePrincipal;
        this.otrasMascotas = otrasMascotas;
        this.planMudanza = planMudanza;
        this.dispuestoEsterilizar = dispuestoEsterilizar;
        this.aceptaVisitas = aceptaVisitas;
        this.conoceResponsabilidades = conoceResponsabilidades;

        // Valores por defecto al momento de crear la solicitud
        this.puntajeTotal = 0;
        this.resultado = "PENDIENTE";
    }

    // --- LÓGICA DE NEGOCIO (Motor de evaluación) ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void calcularPuntaje() {
        int puntos = 0;

        // 1. Tipo de vivienda
        if (tipoVivienda.equals("Casa con patio grande")) puntos += 10;
        else if (tipoVivienda.equals("Casa sin patio")) puntos += 8;
        else if (tipoVivienda.equals("Departamento amplio")) puntos += 6;
        else if (tipoVivienda.equals("Departamento pequeño")) puntos += 3;
        else puntos += 2; // Otro

        // 2. Acuerdo familiar
        if (acuerdoFamiliar.equals("Sí")) puntos += 10;
        else if (acuerdoFamiliar.equals("Algunos no están de acuerdo")) puntos += 5;

        // 3. Tiempo sola
        if (tiempoSola.equals("Menos de 2 horas")) puntos += 10;
        else if (tiempoSola.equals("Entre 2 y 5 horas")) puntos += 8;
        else if (tiempoSola.equals("Entre 5 y 8 horas")) puntos += 5;

        // 4. Tiempo dedicado
        if (tiempoDedicado.equals("Más de 3 horas")) puntos += 10;
        else if (tiempoDedicado.equals("Entre 2 y 3 horas")) puntos += 8;
        else if (tiempoDedicado.equals("Entre 1 y 2 horas")) puntos += 5;

        // 5. Experiencia previa
        if (experienciaPrevia.equals("Sí, actualmente tengo mascotas.")) puntos += 5;
        else if (experienciaPrevia.equals("Sí, tuve mascotas anteriormente.")) puntos += 4;
        else puntos += 2; // Primera mascota

        // 6. Capacidad económica
        if (recursosEconomicos.equals("Sí, completamente")) puntos += 15;
        else if (recursosEconomicos.equals("Sí, aunque con algunas limitaciones")) puntos += 10;
        else if (recursosEconomicos.equals("No estoy seguro")) puntos += 5;

        // 7. Atención enfermedad
        if (respuestaEnfermedad.equals("La llevaría inmediatamente al veterinario.")) puntos += 15;
        else if (respuestaEnfermedad.equals("Buscaría atención cuando me sea posible.")) puntos += 10;
        else if (respuestaEnfermedad.equals("Intentaría tratarla en casa.")) puntos += 5;

        // 8. Responsable principal
        if (responsablePrincipal.equals("Yo")) puntos += 10;
        else if (responsablePrincipal.equals("Mi pareja")) puntos += 8;
        else if (responsablePrincipal.equals("Toda la familia")) puntos += 7;
        else if (responsablePrincipal.equals("Otra persona")) puntos += 4;
        else if (responsablePrincipal.equals("Mis hijos")) puntos += 2;

        // 9. Mudanza
        if (planMudanza.equals("Llevaría conmigo a la mascota.")) puntos += 10;
        else if (planMudanza.equals("Buscaría una vivienda que acepte mascotas.")) puntos += 8;
        else if (planMudanza.equals("La dejaría con un familiar.")) puntos += 4;

        // 10. Esterilización
        if (dispuestoEsterilizar.equals("Sí")) puntos += 5;
        else if (dispuestoEsterilizar.equals("Depende del caso")) puntos += 3;

        // 11. Seguimiento
        if (aceptaVisitas.equals("Sí")) puntos += 5;

        // 12. Conocimiento responsabilidad
        if (conoceResponsabilidades.equals("Sí, completamente")) puntos += 5;
        else if (conoceResponsabilidades.equals("Parcialmente")) puntos += 3;
        else if (conoceResponsabilidades.equals("Muy poco")) puntos += 1;

        this.puntajeTotal = puntos;
    }

    public void generarSugerenciaSistema() {
        if (this.puntajeTotal >= 90) {
            this.resultado = "Sugerencia: Apto para adopción";
        } else if (this.puntajeTotal >= 70) {
            this.resultado = "Sugerencia: Apto con revisión";
        } else if (this.puntajeTotal >= 50) {
            this.resultado = "Sugerencia: Requiere entrevista";
        } else {
            this.resultado = "Sugerencia: No apto";
        }
    }

    public String getTipoMascotaDeseada() {
        return tipoMascotaDeseada;
    }

    public void setTipoMascotaDeseada(String tipoMascotaDeseada) {
        this.tipoMascotaDeseada = tipoMascotaDeseada;
    }

    public String getMotivoAdopcion() {
        return motivoAdopcion;
    }

    public void setMotivoAdopcion(String motivoAdopcion) {
        this.motivoAdopcion = motivoAdopcion;
    }

    public String getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(String tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }

    public String getCantidadPersonasHogar() {
        return cantidadPersonasHogar;
    }

    public void setCantidadPersonasHogar(String cantidadPersonasHogar) {
        this.cantidadPersonasHogar = cantidadPersonasHogar;
    }

    public String getAcuerdoFamiliar() {
        return acuerdoFamiliar;
    }

    public void setAcuerdoFamiliar(String acuerdoFamiliar) {
        this.acuerdoFamiliar = acuerdoFamiliar;
    }

    public String getTiempoSola() {
        return tiempoSola;
    }

    public void setTiempoSola(String tiempoSola) {
        this.tiempoSola = tiempoSola;
    }

    public String getTiempoDedicado() {
        return tiempoDedicado;
    }

    public void setTiempoDedicado(String tiempoDedicado) {
        this.tiempoDedicado = tiempoDedicado;
    }

    public String getExperienciaPrevia() {
        return experienciaPrevia;
    }

    public void setExperienciaPrevia(String experienciaPrevia) {
        this.experienciaPrevia = experienciaPrevia;
    }

    public String getDestinoMascotasPrevias() {
        return destinoMascotasPrevias;
    }

    public void setDestinoMascotasPrevias(String destinoMascotasPrevias) {
        this.destinoMascotasPrevias = destinoMascotasPrevias;
    }

    public String getRecursosEconomicos() {
        return recursosEconomicos;
    }

    public void setRecursosEconomicos(String recursosEconomicos) {
        this.recursosEconomicos = recursosEconomicos;
    }

    public String getRespuestaEnfermedad() {
        return respuestaEnfermedad;
    }

    public void setRespuestaEnfermedad(String respuestaEnfermedad) {
        this.respuestaEnfermedad = respuestaEnfermedad;
    }

    public String getResponsablePrincipal() {
        return responsablePrincipal;
    }

    public void setResponsablePrincipal(String responsablePrincipal) {
        this.responsablePrincipal = responsablePrincipal;
    }

    public String getOtrasMascotas() {
        return otrasMascotas;
    }

    public void setOtrasMascotas(String otrasMascotas) {
        this.otrasMascotas = otrasMascotas;
    }

    public String getPlanMudanza() {
        return planMudanza;
    }

    public void setPlanMudanza(String planMudanza) {
        this.planMudanza = planMudanza;
    }

    public String getDispuestoEsterilizar() {
        return dispuestoEsterilizar;
    }

    public void setDispuestoEsterilizar(String dispuestoEsterilizar) {
        this.dispuestoEsterilizar = dispuestoEsterilizar;
    }

    public String getAceptaVisitas() {
        return aceptaVisitas;
    }

    public void setAceptaVisitas(String aceptaVisitas) {
        this.aceptaVisitas = aceptaVisitas;
    }

    public String getConoceResponsabilidades() {
        return conoceResponsabilidades;
    }

    public void setConoceResponsabilidades(String conoceResponsabilidades) {
        this.conoceResponsabilidades = conoceResponsabilidades;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}