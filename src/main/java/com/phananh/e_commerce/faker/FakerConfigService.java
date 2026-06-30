package com.phananh.e_commerce.faker;

import org.springframework.stereotype.Component;

/**
 * Runtime configuration for faker. This bean holds the values used by `DataSeeder`.
 * You can modify these values programmatically (e.g. in code or tests) by injecting
 * this bean and calling the setters.
 */
@Component
public class FakerConfigService {

    private boolean enabled = false;
    private int users = 500;
    private int brands = 10;
    private int categories = 15;
    private int products = 100;
    private int orders = 500;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getUsers() {
        return users;
    }

    public void setUsers(int users) {
        this.users = users;
    }

    public int getBrands() {
        return brands;
    }

    public void setBrands(int brands) {
        this.brands = brands;
    }

    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    public int getProducts() {
        return products;
    }

    public void setProducts(int products) {
        this.products = products;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
}

