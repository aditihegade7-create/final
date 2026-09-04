package com.society.service.resident_service;

public class UserSession {

    // =========================================================
    // CURRENTLY LOGGED-IN USER EMAIL
    // =========================================================

    private static String loggedInEmail;

    // =========================================================
    // SET EMAIL
    // =========================================================

    public static void setLoggedInEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            loggedInEmail = null;
            return;
        }

        loggedInEmail = email.trim();

        System.out.println(
                "UserSession Email Set = "
                        + loggedInEmail
        );
    }

    // =========================================================
    // GET EMAIL
    // =========================================================

    public static String getLoggedInEmail() {

        System.out.println(
                "UserSession Email Get = "
                        + loggedInEmail
        );

        return loggedInEmail;
    }

    // =========================================================
    // CLEAR SESSION
    // =========================================================

    public static void clear() {

        loggedInEmail = null;

        System.out.println(
                "UserSession cleared."
        );
    }
}