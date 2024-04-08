package com.example.rendeles;

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

    // Getterek és Setterek
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Map<String, Object>> getMenuItems() {
        return menuItems;
    }

    // Setter a menuItems-hez
    public void setMenuItems(List<Map<String, Object>> menuItems) {
        this.menuItems = menuItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
