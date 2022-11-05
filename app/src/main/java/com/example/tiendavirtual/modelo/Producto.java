package com.example.tiendavirtual.modelo;

public class Producto {
    String name, description, price;

    public Producto() {
    }

    public Producto(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Producto(String name, String description, String price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
