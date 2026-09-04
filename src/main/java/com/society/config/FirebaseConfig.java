package com.society.config;

import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class FirebaseConfig {

        private static Firestore firestore;

        // ================= GET FIRESTORE =================

        public static Firestore getFirestore() {

                if (firestore == null) {

                        try {

                                InputStream serviceAccount = FirebaseConfig.class
                                                .getClassLoader()
                                                .getResourceAsStream(
                                                                "java2026.json");

                                if (serviceAccount == null) {

                                        throw new RuntimeException(
                                                        "firebase-service-account.json not found in resources folder.");
                                }

                                firestore = FirestoreOptions.newBuilder()
                                                .setCredentials(
                                                                GoogleCredentials
                                                                                .fromStream(serviceAccount))
                                                .build()
                                                .getService();

                                System.out.println(
                                                "Firebase Firestore Connected Successfully");

                        } catch (Exception e) {

                                e.printStackTrace();

                                throw new RuntimeException(
                                                "Firebase Firestore Connection Failed",
                                                e);
                        }
                }

                return firestore;
        }
}