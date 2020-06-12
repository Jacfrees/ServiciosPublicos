package com.sigiep.serviciospublicos.models;

public class CompaniaEntity {
    
    private Integer id_compania;
    private String nombre;
    private String nit;
    private String correo;
    private String direccion;
    private String telefono;
    private String slogan;
    private String ruta_logo;
    private String ciudad;
    private String codigo_ean;
    private String nuir;

    public CompaniaEntity(Integer id_compania, String nombre, String nit, String correo, String direccion, String telefono, String slogan, String ruta_logo, String ciudad, String codigo_ean, String nuir) {
        this.id_compania = id_compania;
        this.nombre = nombre;
        this.nit = nit;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.slogan = slogan;
        this.ruta_logo = ruta_logo;
        this.ciudad = ciudad;
        this.codigo_ean = codigo_ean;
        this.nuir = nuir;
    }

    public Integer getId_compania() {
        return id_compania;
    }

    public void setId_compania(Integer id_compania) {
        this.id_compania = id_compania;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getRuta_logo() {
        return ruta_logo;
    }

    public void setRuta_logo(String ruta_logo) {
        this.ruta_logo = ruta_logo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigo_ean() {
        return codigo_ean;
    }

    public void setCodigo_ean(String codigo_ean) {
        this.codigo_ean = codigo_ean;
    }

    public String getNuir() {
        return nuir;
    }

    public void setNuir(String nuir) {
        this.nuir = nuir;
    }
}
