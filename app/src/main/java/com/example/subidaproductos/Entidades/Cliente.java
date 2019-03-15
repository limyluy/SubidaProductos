package com.example.subidaproductos.Entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cliente implements Serializable {

    private String nombre;
    private String direccion;
    private String telefono;
    private boolean atrazado;
    private List<String> locales;

    public Cliente() {
    }

    public Cliente(String nombre, String direccion, String telefono, boolean atrazado, List<String> locales) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.atrazado = atrazado;
        this.locales = locales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public boolean isAtrazado() {
        return atrazado;
    }

    public void setAtrazado(boolean atrazado) {
        this.atrazado = atrazado;
    }

    public List<String> getLocales() {
        return locales;
    }

    public void setLocales(List<String> locales) {
        this.locales = locales;
    }
}
