package com.phananh.e_commerce.usermanagement.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.seed.admin")
public record AdminSeedProperties(String username,
                                  String password,
                                  String email,
                                  String fullName,
                                  String phoneNumber,
                                  String address) {

    public AdminSeedProperties {
        username = defaultString(username, "admin");
        password = defaultString(password, "admin");
        email = defaultString(email, "admin@ecommerce.local");
        fullName = defaultString(fullName, "System Administrator");
        phoneNumber = defaultString(phoneNumber, "0000000000");
        address = defaultString(address, "System");
    }

    private static String defaultString(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }
}


