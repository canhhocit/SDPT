package com.phananh.e_commerce.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Utility class for password operations.
 * Provides static encode and matches methods using BCrypt.
 */
public final class PasswordUtils {

    // Use a static PasswordEncoder so the static helper methods can use it.
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(10);

    private PasswordUtils() {
        // utility class
    }

    public static String encode(String rawPassword) {
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}
