package com.developer.manuelquinteros.clinicadentalx.model;

import java.util.List;

public class Doctors {

    private String idDoctor;
    private String nombre;
    private String descripcion;
    private String rating;
    private int nb_episode;
    private String especialidad;
    private String estado;
    private String image_url;
    private List<String> mAvailabilityTimes;
    private String horarios;


    public String getIdDoctor() {
        return idDoctor;
    }

    public void setIdDoctor(String idDoctor) {
        this.idDoctor = idDoctor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRating() {
        return rating;
    }

    public int getNb_episode() {
        return nb_episode;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getEstado() {
        return estado;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setNb_episode(int nb_episode) {
        this.nb_episode = nb_episode;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<String> getmAvailabilityTimes() {

        return mAvailabilityTimes;
    }

    public void setmAvailabilityTimes(List<String> mAvailabilityTimes) {
        this.mAvailabilityTimes = mAvailabilityTimes;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }
}
