package com.society.service.resident_service;

import java.awt.Desktop;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.json.JSONObject;

public class RazorpayService {

    // =====================================================
    // RAZORPAY KEYS
    // =====================================================

    private static final String KEY_ID =
            "rzp_test_TWfWEBbT8ZzPHa";

    private static final String KEY_SECRET =
            "BbCK4ly09CXmMie1ynM6wc5g";

    // =====================================================
    // RAZORPAY API
    // =====================================================

    private static final String RAZORPAY_URL =
            "https://api.razorpay.com/v1/payment_links";

    // =====================================================
    // CREATE PAYMENT LINK
    // =====================================================

    public String createPaymentLink(
            double amount,
            String description) throws Exception {

        // =================================================
        // VALIDATION
        // =================================================

        if (amount <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than 0."
            );
        }

        if (description == null ||
                description.trim().isEmpty()) {

            description =
                    "Society360 Amenity Booking";
        }

        // =================================================
        // RUPEES -> PAISE
        // =================================================

        long amountInPaise =
                Math.round(amount * 100);

        // =================================================
        // REQUEST BODY
        // =================================================

        JSONObject requestBody =
                new JSONObject();

        requestBody.put(
                "amount",
                amountInPaise
        );

        requestBody.put(
                "currency",
                "INR"
        );

        requestBody.put(
                "description",
                description
        );

        // =================================================
        // NOTES
        // =================================================

        JSONObject notes =
                new JSONObject();

        notes.put(
                "project",
                "Society360"
        );

        notes.put(
                "description",
                description
        );

        notes.put(
                "amount",
                String.valueOf(amount)
        );

        requestBody.put(
                "notes",
                notes
        );

        // =================================================
        // AUTHENTICATION
        // =================================================

        String credentials =
                KEY_ID + ":" + KEY_SECRET;

        String encodedCredentials =
                Base64.getEncoder()
                        .encodeToString(
                                credentials.getBytes(
                                        StandardCharsets.UTF_8
                                )
                        );

        // =================================================
        // HTTP REQUEST
        // =================================================

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        RAZORPAY_URL
                                )
                        )
                        .header(
                                "Authorization",
                                "Basic " +
                                encodedCredentials
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(
                                                requestBody.toString()
                                        )
                        )
                        .build();

        // =================================================
        // HTTP CLIENT
        // =================================================

        HttpClient client =
                HttpClient.newHttpClient();

        // =================================================
        // SEND REQUEST
        // =================================================

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers
                                .ofString()
                );

        // =================================================
        // DEBUG
        // =================================================

        System.out.println(
                "Razorpay Response Code = "
                        + response.statusCode()
        );

        System.out.println(
                "Razorpay Response = "
                        + response.body()
        );

        // =================================================
        // CHECK RESPONSE
        // =================================================

        if (response.statusCode() < 200 ||
                response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Razorpay Error: "
                            + response.body()
            );
        }

        // =================================================
        // RESPONSE JSON
        // =================================================

        JSONObject responseJson =
                new JSONObject(
                        response.body()
                );

        // =================================================
        // PAYMENT LINK
        // =================================================

        if (!responseJson.has(
                "short_url"
        )) {

            throw new RuntimeException(
                    "Razorpay did not return payment URL."
            );
        }

        return responseJson.getString(
                "short_url"
        );
    }

    // =====================================================
    // OPEN PAYMENT PAGE
    // =====================================================

    public void openPaymentPage(
            String paymentUrl) throws Exception {

        if (paymentUrl == null ||
                paymentUrl.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Payment URL is empty."
            );
        }

        if (!Desktop.isDesktopSupported()) {

            throw new RuntimeException(
                    "Desktop browser is not supported."
            );
        }

        Desktop.getDesktop().browse(
                new URI(paymentUrl)
        );
    }
}