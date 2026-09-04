package com.society.dao.Secretary_dao;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.society.model.Secretary_model.SecretaryModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SecretaryDao {

    // =========================================================
    // FIRESTORE COLLECTION
    // =========================================================

    private static final String COLLECTION_NAME = "secretaries";


    // =========================================================
    // GET FIRESTORE
    // =========================================================

    private Firestore getFirestore() {

        return FirestoreClient.getFirestore();
    }


    // =========================================================
    // GET SECRETARY BY EMAIL
    // =========================================================

    public SecretaryModel getSecretary(String email) {

        if (email == null || email.trim().isEmpty()) {

            System.out.println("Secretary email is empty.");
            return null;
        }

        email = email.trim();

        try {

            Firestore db = getFirestore();

            /*
             * IMPORTANT:
             *
             * This assumes your Firestore document contains
             * an "email" field.
             *
             * Example:
             *
             * secretaries
             *    documentId
             *       email: "aditi@gmail.com"
             */

            var query = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .get()
                    .get(10, TimeUnit.SECONDS);

            if (query.isEmpty()) {

                System.out.println(
                        "Secretary not found for email: " + email
                );

                return null;
            }

            DocumentSnapshot document =
                    query.getDocuments().get(0);

            return document.toObject(SecretaryModel.class);

        } catch (Exception e) {

            System.out.println(
                    "Error fetching secretary: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // SAVE SECRETARY
    // =========================================================

    public boolean saveSecretary(SecretaryModel secretary) {

        if (secretary == null) {

            System.out.println(
                    "Secretary object is null."
            );

            return false;
        }

        if (secretary.getEmail() == null ||
                secretary.getEmail().trim().isEmpty()) {

            System.out.println(
                    "Secretary email is required."
            );

            return false;
        }

        try {

            Firestore db = getFirestore();

            String email =
                    secretary.getEmail().trim();

            /*
             * Search existing secretary using email.
             */

            var query = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .get()
                    .get(10, TimeUnit.SECONDS);

            if (!query.isEmpty()) {

                System.out.println(
                        "Secretary already exists: "
                                + email
                );

                return false;
            }


            /*
             * Add new secretary.
             */

            DocumentReference document =
                    db.collection(COLLECTION_NAME)
                            .document();

            document.set(secretary)
                    .get(
                            10,
                            TimeUnit.SECONDS
                    );

            System.out.println(
                    "Secretary saved successfully: "
                            + email
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error saving secretary: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // UPDATE SECRETARY
    // =========================================================

    public boolean updateSecretary(
            String email,
            String name,
            String phone,
            String dateOfBirth,
            String gender,
            String aadhaarNumber,
            String societyName) {

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "Secretary email is empty."
            );

            return false;
        }

        email = email.trim();

        try {

            Firestore db = getFirestore();


            // =================================================
            // FIND SECRETARY
            // =================================================

            var query = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .get()
                    .get(10, TimeUnit.SECONDS);


            if (query.isEmpty()) {

                System.out.println(
                        "Secretary not found: "
                                + email
                );

                return false;
            }


            // =================================================
            // GET DOCUMENT
            // =================================================

            DocumentSnapshot document =
                    query.getDocuments().get(0);


            DocumentReference documentReference =
                    document.getReference();


            // =================================================
            // UPDATE DATA
            // =================================================

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "name",
                    name == null ? "" : name.trim()
            );

            updates.put(
                    "phone",
                    phone == null ? "" : phone.trim()
            );

            updates.put(
                    "dateOfBirth",
                    dateOfBirth == null
                            ? ""
                            : dateOfBirth.trim()
            );

            updates.put(
                    "gender",
                    gender == null
                            ? ""
                            : gender.trim()
            );

            updates.put(
                    "aadhaarNumber",
                    aadhaarNumber == null
                            ? ""
                            : aadhaarNumber.trim()
            );

            updates.put(
                    "societyName",
                    societyName == null
                            ? ""
                            : societyName.trim()
            );


            // =================================================
            // FIRESTORE UPDATE
            // =================================================

            documentReference
                    .update(updates)
                    .get(
                            10,
                            TimeUnit.SECONDS
                    );


            System.out.println(
                    "Secretary updated successfully: "
                            + email
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error updating secretary: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // UPDATE PROFILE IMAGE
    // =========================================================
    //
    // THIS IS THE IMPORTANT METHOD
    //
    // SecretaryProfile.java calls:
    //
    // secretaryController.updateProfileImage(
    //      secretaryEmail,
    //      imageUrl
    // );
    //
    // Controller calls:
    //
    // secretaryDao.updateProfileImage(
    //      email,
    //      profileImageUrl
    // );
    //
    // =========================================================

    public boolean updateProfileImage(
            String email,
            String profileImageUrl) {

        // =====================================================
        // VALIDATE EMAIL
        // =====================================================

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "Secretary email is empty."
            );

            return false;
        }

        email = email.trim();


        // =====================================================
        // VALIDATE IMAGE URL
        // =====================================================

        if (profileImageUrl == null ||
                profileImageUrl.trim().isEmpty()) {

            System.out.println(
                    "Profile image URL is empty."
            );

            return false;
        }

        profileImageUrl =
                profileImageUrl.trim();


        try {

            Firestore db = getFirestore();


            // =================================================
            // FIND SECRETARY USING EMAIL
            // =================================================

            var query = db.collection(COLLECTION_NAME)
                    .whereEqualTo("email", email)
                    .get()
                    .get(10, TimeUnit.SECONDS);


            // =================================================
            // SECRETARY NOT FOUND
            // =================================================

            if (query.isEmpty()) {

                System.out.println(
                        "Secretary not found for profile image update: "
                                + email
                );

                return false;
            }


            // =================================================
            // GET SECRETARY DOCUMENT
            // =================================================

            DocumentSnapshot document =
                    query.getDocuments().get(0);


            DocumentReference documentReference =
                    document.getReference();


            // =================================================
            // UPDATE PROFILE IMAGE URL
            // =================================================

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "profileImageUrl",
                    profileImageUrl
            );


            // =================================================
            // SAVE TO FIRESTORE
            // =================================================

            WriteResult result =
                    documentReference
                            .update(updates)
                            .get(
                                    10,
                                    TimeUnit.SECONDS
                            );


            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "PROFILE IMAGE UPDATED"
            );

            System.out.println(
                    "Email: " + email
            );

            System.out.println(
                    "Image URL: " + profileImageUrl
            );

            System.out.println(
                    "Update Time: " + result.getUpdateTime()
            );

            System.out.println(
                    "========================================"
            );


            return true;

        } catch (Exception e) {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "PROFILE IMAGE UPDATE FAILED"
            );

            System.out.println(
                    "Email: " + email
            );

            System.out.println(
                    "Error: " + e.getMessage()
            );

            System.out.println(
                    "========================================"
            );

            e.printStackTrace();

            return false;
        }
    }
}