package com.example.serviciospublicos.modelo;

public class ItemLista {
    public String texto;
    public int orden;

    public ItemLista(String texto, int orden) {
        this.texto = texto;
        this.orden = orden;
    }

    @Override
    public String toString() {
        return texto;
    }
}
