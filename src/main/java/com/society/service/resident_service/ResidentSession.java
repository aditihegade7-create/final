package com.society.service.resident_service;

public class ResidentSession {

    private static String loggedInEmail;
    private static String loggedInSociety;

    private ResidentSession() {
        // Utility class
    }

    public static void setLoggedInEmail(String email) {

        if (email == null || email.trim().isEmpty()) {
            loggedInEmail = null;
            return;
        }

        loggedInEmail = email.trim().toLowerCase();
    }

    public static String getLoggedInEmail() {

        if (loggedInEmail == null ||
                loggedInEmail.trim().isEmpty()) {

            return null;
        }

        return loggedInEmail;
    }

    public static void setLoggedInSociety(String society) {

        if (society == null || society.trim().isEmpty()) {
            loggedInSociety = null;
            return;
        }

        loggedInSociety = society.trim();
    }

    public static String getLoggedInSociety() {

        if (loggedInSociety == null ||
                loggedInSociety.trim().isEmpty()) {

            return null;
        }

        return loggedInSociety;
    }

    public static boolean isLoggedIn() {

        return loggedInEmail != null &&
                !loggedInEmail.trim().isEmpty();
    }

    public static void clearSession() {

        loggedInEmail = null;
        loggedInSociety = null;
    }
}