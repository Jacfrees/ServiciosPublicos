package com.example.serviciospublicos.modelo;

public class clRuta {
    String identificador;
    String codigo_ruta;
    String direccion;

    public clRuta() {
    }

    public clRuta(String identificador, String codigo_ruta, String direccion) {
        this.identificador = identificador;
        this.codigo_ruta = codigo_ruta;
        this.direccion = direccion;
    }



    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getCodigo_ruta() {
        return codigo_ruta;
    }

    public void setCodigo_ruta(String codigo_ruta) {
        this.codigo_ruta = codigo_ruta;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}


