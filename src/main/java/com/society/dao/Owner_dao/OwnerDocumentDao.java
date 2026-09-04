package com.society.dao.Owner_dao;

import java.util.HashMap;
import java.util.Map;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;
import com.society.config.FirebaseConfig;

public class OwnerDocumentDao {
    
    private final Firestore db;

    private static final String COLLECTION =
            "OwnerDocuments";


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public OwnerDocumentDao() {

        db =
                FirebaseConfig.getFirestore();
    }


    // =========================================================
    // GET OWNER DOCUMENTS
    // =========================================================

    public Map<String, String> getOwnerDocuments(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return new HashMap<>();
            }


            String ownerEmail =
                    email.trim().toLowerCase();


            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(ownerEmail)
                            .get()
                            .get();


            Map<String, String> documents =
                    new HashMap<>();


            if (!document.exists()) {

                return documents;
            }


            documents.put(
                    "email",
                    getString(
                            document,
                            "email"
                    )
            );


            documents.put(
                    "aadhaarUrl",
                    getString(
                            document,
                            "aadhaarUrl"
                    )
            );


            documents.put(
                    "panUrl",
                    getString(
                            document,
                            "panUrl"
                    )
            );


            documents.put(
                    "ownershipProofUrl",
                    getString(
                            document,
                            "ownershipProofUrl"
                    )
            );


            documents.put(
                    "addressProofUrl",
                    getString(
                            document,
                            "addressProofUrl"
                    )
            );


            return documents;

        } catch (Exception e) {

            System.out.println(
                    "OwnerDocumentDao Get Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return new HashMap<>();
        }
    }


    // =========================================================
    // SAVE OWNER DOCUMENTS
    // =========================================================

    public boolean saveOwnerDocuments(
            String email,
            String aadhaarUrl,
            String panUrl,
            String ownershipProofUrl,
            String addressProofUrl) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            String ownerEmail =
                    email.trim().toLowerCase();


            Map<String, Object> data =
                    new HashMap<>();


            // =================================================
            // EMAIL
            // =================================================

            data.put(
                    "email",
                    ownerEmail
            );


            // =================================================
            // AADHAAR
            // =================================================

            if (aadhaarUrl != null &&
                    !aadhaarUrl.trim().isEmpty()) {

                data.put(
                        "aadhaarUrl",
                        aadhaarUrl.trim()
                );
            }


            // =================================================
            // PAN
            // =================================================

            if (panUrl != null &&
                    !panUrl.trim().isEmpty()) {

                data.put(
                        "panUrl",
                        panUrl.trim()
                );
            }


            // =================================================
            // OWNERSHIP PROOF
            // =================================================

            if (ownershipProofUrl != null &&
                    !ownershipProofUrl.trim().isEmpty()) {

                data.put(
                        "ownershipProofUrl",
                        ownershipProofUrl.trim()
                );
            }


            // =================================================
            // ADDRESS PROOF
            // =================================================

            if (addressProofUrl != null &&
                    !addressProofUrl.trim().isEmpty()) {

                data.put(
                        "addressProofUrl",
                        addressProofUrl.trim()
                );
            }


            // =================================================
            // CREATE / UPDATE DOCUMENT
            // =================================================

            db.collection(COLLECTION)
                    .document(ownerEmail)
                    .set(
                            data,
                            SetOptions.merge()
                    )
                    .get();


            System.out.println(
                    "Owner documents saved for: "
                            + ownerEmail
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                    "OwnerDocumentDao Save Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET STRING SAFELY
    // =========================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        String value =
                document.getString(field);

        if (value == null) {
            return "";
        }

        return value;
    }

}
