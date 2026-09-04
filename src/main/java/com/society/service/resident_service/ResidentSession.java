package com.society.service.resident_service;

public class ResidentSession {

    private static String loggedInEmail;

    private ResidentSession() {
        // Utility class
    }

    // =====================================================
    // SET LOGGED-IN RESIDENT EMAIL
    // =====================================================
    public static void setLoggedInEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            loggedInEmail = null;
            return;
        }

        loggedInEmail = email.trim().toLowerCase();
    }

    // =====================================================
    // GET LOGGED-IN RESIDENT EMAIL
    // =====================================================
    public static String getLoggedInEmail() {

        if (loggedInEmail == null ||
                loggedInEmail.trim().isEmpty()) {

            return null;
        }

        return loggedInEmail;
    }

    // =====================================================
    // CLEAR SESSION
    // =====================================================
    public static void clearSession() {
        loggedInEmail = null;
    }
}