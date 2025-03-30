package com.iths.mianshop.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static String getCurrentUsername() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        return principal.split("\\|")[0];
    }

    public static String getCurrentUserType() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        String[] parts = principal.split("\\|");
        return parts.length > 1 ? parts[1] : "UNKNOWN";
    }
}
