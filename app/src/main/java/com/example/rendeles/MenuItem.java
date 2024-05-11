package com.example.rendeles;

/**
 * Represents an item in a menu, including its name, description, price, and quantity.
 */
public class MenuItem {
    private String name;
    private String description;
    private double price;
    private int quantity; // Tracks quantity, useful for order processing

    /**
     * Default constructor necessary for Firestore's automatic data mapping.
     */
    public MenuItem() {
        // Default constructor is needed for Firestore to deserialize data.
    }

    /**
     * Constructs a new MenuItem with the specified name, description, and price.
     * @param name The name of the menu item.
     * @param description The description of the menu item.
     * @param price The price of the menu item.
     */
    public MenuItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /**
     * Gets the name of the menu item.
     * @return The name of the menu item.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the menu item.
     * @param name The new name of the menu item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the menu item.
     * @return The description of the menu item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the menu item.
     * @param description The new description of the menu item.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the price of the menu item.
     * @return The price of the menu item.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the menu item.
     * @param price The new price of the menu item.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the current quantity of the menu item.
     * @return The current quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the menu item.
     * @param quantity The new quantity of the menu item.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
