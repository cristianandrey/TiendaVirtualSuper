package com.example.tiendavirtual.modelo;

public class Producto {
    String nombre, descripcion, producto_price, photo;

    public Producto() {
    }

    public Producto(String nombre, String descripcion, String producto_price, String photo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.producto_price = producto_price;
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getProducto_price() {
        return producto_price;
    }

    public void setProducto_price(String producto_price) {
        this.producto_price = producto_price;
    }
}
