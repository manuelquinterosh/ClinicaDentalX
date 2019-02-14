package com.developer.manuelquinteros.clinicadentalx.model;

public class Schedule {
    String idhora;
    String hora_inicio;
    String hora_final;
    String imageResource;

    public Schedule() {
    }


    public Schedule(String idhora, String hora_inicio, String hora_final, String imageResource) {
        this.idhora = idhora;
        this.hora_inicio = hora_inicio;
        this.hora_final = hora_final;
        this.imageResource = imageResource;
    }


    public String getIdhora() {
        return idhora;
    }

    public void setIdhora(String idhora) {
        this.idhora = idhora;
    }

    public String getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(String hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public String getHora_final() {
        return hora_final;
    }

    public void setHora_final(String hora_final) {
        this.hora_final = hora_final;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
