package com.example.serviciospublicos.modelo;

public class ClEventualidad {

    Integer idUnico;
    String nombre;
    boolean isSelected;
    Integer idTipo;
    Integer valor;

    public ClEventualidad(Integer idUnico, String nombre, boolean isSelected, Integer idTipo, Integer valor) {
        this.idUnico = idUnico;
        this.nombre = nombre;
        this.isSelected = isSelected;
        this.idTipo = idTipo;
        this.valor = valor;
    }

    public Integer getIdUnico() {
        return idUnico;
    }

    public void setIdUnico(Integer idUnico) {
        this.idUnico = idUnico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Integer getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(Integer idTipo) {
        this.idTipo = idTipo;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}




