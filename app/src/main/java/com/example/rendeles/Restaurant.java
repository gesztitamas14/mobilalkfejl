package com.example.rendeles;

import com.google.firebase.firestore.PropertyName;

import java.util.List;
import java.util.Map;

public class Restaurant {
    private String id;
    private String name;
    private String address;
    private List<Map<String, Object>> menuItems;

    public Restaurant() {
    }

    // Konstruktor
    public Restaurant(String id, String name, String address, List<Map<String, Object>> menuItems) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.menuItems = menuItems;
    }

    // Getters and Setters
    @PropertyName("id")
    public String getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("address")
    public String getAddress() {
        return address;
    }

    @PropertyName("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @PropertyName("MenuItems")
    public List<Map<String, Object>> getMenuItems() {
        return menuItems;
    }

    @PropertyName("MenuItems")
    public void setMenuItems(List<Map<String, Object>> menuItems) {
        this.menuItems = menuItems;
    }
}
