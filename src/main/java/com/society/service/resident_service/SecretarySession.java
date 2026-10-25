package com.society.service.resident_service;

public class SecretarySession {

    // =========================================================
    // LOGGED-IN SECRETARY EMAIL
    // =========================================================

    private static String loggedInEmail;

    // =========================================================
    // SET SECRETARY EMAIL
    // =========================================================

    public static void setLoggedInEmail(String email) {

        if (email == null || email.trim().isEmpty()) {

            loggedInEmail = null;

            System.out.println(
                    "SecretarySession Email Set = null"
            );

            return;
        }

        loggedInEmail =
                email.trim().toLowerCase();

        System.out.println(
                "SecretarySession Email Set = "
                        + loggedInEmail
        );
    }

    // =========================================================
    // GET SECRETARY EMAIL
    // =========================================================

    public static String getLoggedInEmail() {

        System.out.println(
                "SecretarySession Email Get = "
                        + loggedInEmail
        );

        return loggedInEmail;
    }

    // =========================================================
    // CLEAR SECRETARY SESSION
    // =========================================================

    public static void clear() {

        loggedInEmail = null;

        System.out.println(
                "SecretarySession cleared."
        );
    }
}