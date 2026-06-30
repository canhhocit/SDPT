package com.phananh.e_commerce.core.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new IllegalStateException("No authenticated user found");
        }

        String currentUser = authentication.getName();
        if (currentUser == null || currentUser.isBlank()) {
            throw new IllegalStateException("Authenticated principal name is empty");
        }

        return currentUser;
    }
}
