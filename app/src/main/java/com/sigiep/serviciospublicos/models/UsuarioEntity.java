package com.sigiep.serviciospublicos.models;

public class UsuarioEntity {
    int id_usuario;
    String nombre;
    String documento;
    String usuario;
    String contrasena;

    public UsuarioEntity(int id_usuario, String usuario, String contrasena, String nombre, String documento) {
        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.documento = documento;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
