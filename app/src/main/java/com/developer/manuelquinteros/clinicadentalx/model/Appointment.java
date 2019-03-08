package com.developer.manuelquinteros.clinicadentalx.model;

public class Appointment {


    private String idcitas;
    private String fecha;
    private String hora;
    private String especialidad;
    private String motivo;
    private String Ndoctor;
    private String npersonas;
    private String usuario;
    private String status;

    private String appActiva;
    private String appCumplida;
    private String appCancelada;

    public String getAppCancelada() {
        return appCancelada;
    }

    public void setAppCancelada(String appCancelada) {
        this.appCancelada = appCancelada;
    }

    public String getAppCumplida() {
        return appCumplida;
    }

    public void setAppCumplida(String appCumplida) {
        this.appCumplida = appCumplida;
    }

    public String getAppActiva() {
        return appActiva;
    }

    public void setAppActiva(String appActiva) {
        this.appActiva = appActiva;
    }

    public String getIdcitas() {
        return idcitas;
    }

    public void setIdcitas(String idcitas) {
        this.idcitas = idcitas;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getNdoctor() {
        return Ndoctor;
    }

    public void setNdoctor(String ndoctor) {
        Ndoctor = ndoctor;
    }

    public String getNpersonas() {
        return npersonas;
    }

    public void setNpersonas(String npersonas) {
        this.npersonas = npersonas;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
