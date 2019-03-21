package com.example.subidaproductos.Entidades;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Evento {

    private String nombre;
    private String descripcion;
    private GeoPoint ubicacion;
    private String fecha;
    private String desAdicional;
    private String especificaciones;
    private List<String> categoria;
    private List<String> fotos;
    private List<String> locales;

    public Evento() {
    }

    public Evento(String nombre, String descripcion, GeoPoint ubicacion, String fecha, String desAdicional, String especificaciones, List<String> categoria, List<String> fotos, List<String> locales) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.desAdicional = desAdicional;
        this.especificaciones = especificaciones;
        this.categoria = categoria;
        this.fotos = fotos;
        this.locales = locales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(GeoPoint ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDesAdicional() {
        return desAdicional;
    }

    public void setDesAdicional(String desAdicional) {
        this.desAdicional = desAdicional;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public List<String> getCategoria() {
        return categoria;
    }

    public void setCategoria(List<String> categoria) {
        this.categoria = categoria;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public List<String> getLocales() {
        return locales;
    }

    public void setLocales(List<String> locales) {
        this.locales = locales;
    }
}
