package com.society.dao.Resident_dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.society.config.FirebaseConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class documentdao {

    private final Firestore db;

    public documentdao() {
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // SAVE DOCUMENT
    // =========================================================

    public boolean saveDocument(
            String email,
            String documentKey,
            String documentType,
            String documentName,
            String cloudinaryUrl) {

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "Resident email is empty."
            );

            return false;
        }

        if (cloudinaryUrl == null ||
                cloudinaryUrl.trim().isEmpty()) {

            System.out.println(
                    "Cloudinary URL is empty."
            );

            return false;
        }

        try {

            Map<String, Object> documentData =
                    new HashMap<>();

            documentData.put(
                    "documentType",
                    documentType
            );

            documentData.put(
                    "documentName",
                    documentName
            );

            documentData.put(
                    "cloudinaryUrl",
                    cloudinaryUrl
            );

            documentData.put(
                    "uploadedAt",
                    System.currentTimeMillis()
            );

            DocumentReference residentDocument =
                    db.collection("documents")
                      .document(email);

            // Put document inside resident's email document
            Map<String, Object> updateData =
                    new HashMap<>();

            updateData.put(
                    documentKey,
                    documentData
            );

            updateData.put(
                    "email",
                    email
            );

            ApiFuture<WriteResult> future =
                    residentDocument.set(
                            updateData,
                            com.google.cloud.firestore.SetOptions.merge()
                    );

            future.get();

            System.out.println(
                    "Document saved successfully."
            );

            System.out.println(
                    "Resident Email: " + email
            );

            System.out.println(
                    "Document Type: " + documentType
            );

            System.out.println(
                    "Cloudinary URL: " + cloudinaryUrl
            );

            return true;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.err.println(
                    "Firestore operation interrupted."
            );

            e.printStackTrace();

            return false;

        } catch (ExecutionException e) {

            System.err.println(
                    "Firestore save failed."
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // FETCH RESIDENT DOCUMENTS
    // =========================================================

    public DocumentSnapshot getResidentDocuments(
            String email) {

        if (email == null ||
                email.trim().isEmpty()) {

            return null;
        }

        try {

            DocumentReference residentDocument =
                    db.collection("documents")
                      .document(email);

            ApiFuture<DocumentSnapshot> future =
                    residentDocument.get();

            return future.get();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            e.printStackTrace();

            return null;

        } catch (ExecutionException e) {

            e.printStackTrace();

            return null;
        }
    }
}