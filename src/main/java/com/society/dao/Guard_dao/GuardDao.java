package com.society.dao.Guard_dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.model.Guard_model.GuardModel;

public class GuardDao {

    private final Firestore db;

    public GuardDao() {
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // GET GUARD BY EMAIL
    // =========================================================

    public GuardModel getGuard(String email) {

        try {

            if (db == null ||
                    email == null ||
                    email.trim().isEmpty()) {

                return null;
            }

            String normalizedEmail =
                    email.trim().toLowerCase();

            // =====================================================
            // 1. TRY LEGACY ROOT-LEVEL STRUCTURE
            //
            // Guards/{email}
            // =====================================================

            DocumentSnapshot document =
                    db.collection("Guards")
                            .document(normalizedEmail)
                            .get()
                            .get();

            if (document.exists()) {

                GuardModel guard =
                        createGuardFromDocument(
                                document,
                                null
                        );

                if (guard != null) {
                    return guard;
                }
            }

            // =====================================================
            // 2. TRY SOCIETY-WISE STRUCTURE
            //
            // societies/{society}/Guards/{email}
            //
            // Email is used as the document ID.
            // =====================================================

            QuerySnapshot societies =
                    db.collection("societies")
                            .get()
                            .get();

            for (DocumentSnapshot societyDocument :
                    societies.getDocuments()) {

                if (!societyDocument.exists()) {
                    continue;
                }

                String societyName =
                        societyDocument.getId();

                DocumentSnapshot guardDocument =
                        db.collection("societies")
                                .document(societyName)
                                .collection("Guards")
                                .document(normalizedEmail)
                                .get()
                                .get();

                if (!guardDocument.exists()) {
                    continue;
                }

                GuardModel guard =
                        createGuardFromDocument(
                                guardDocument,
                                societyName
                        );

                if (guard != null) {
                    return guard;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to get guard by email."
            );

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // CREATE GUARD MODEL FROM FIRESTORE DOCUMENT
    // =========================================================

    private GuardModel createGuardFromDocument(
            DocumentSnapshot document,
            String societyFromPath) {

        try {

            if (document == null ||
                    !document.exists()) {

                return null;
            }

            GuardModel guard =
                    new GuardModel();

            // =====================================================
            // BASIC INFORMATION
            // =====================================================

            guard.setGuardId(
                    document.getString("guardId")
            );

            guard.setName(
                    document.getString("name")
            );

            guard.setPhone(
                    document.getString("phone")
            );

            // =====================================================
            // EMAIL
            // =====================================================

            String storedEmail =
                    document.getString("email");

            if (storedEmail == null ||
                    storedEmail.trim().isEmpty()) {

                guard.setEmail(
                        document.getId()
                );

            } else {

                guard.setEmail(
                        storedEmail.trim()
                );
            }

            // =====================================================
            // DUTY INFORMATION
            // =====================================================

            guard.setGate(
                    document.getString("gate")
            );

            guard.setShift(
                    document.getString("shift")
            );

            guard.setJoiningDate(
                    document.getString("joiningDate")
            );

            guard.setStatus(
                    document.getString("status")
            );

            // =====================================================
            // PROFILE IMAGE
            // =====================================================

            guard.setProfileImageUrl(
                    document.getString(
                            "profileImageUrl"
                    )
            );

            // =====================================================
            // SOCIETY
            //
            // First use stored "society".
            // Then "societyName".
            // Finally use the society path.
            // =====================================================

            String society =
                    document.getString("society");

            if (society == null ||
                    society.trim().isEmpty()) {

                society =
                        document.getString(
                                "societyName"
                        );
            }

            if ((society == null ||
                    society.trim().isEmpty())
                    && societyFromPath != null) {

                society =
                        societyFromPath;
            }

            guard.setSociety(
                    society == null
                            ? ""
                            : society.trim()
            );

            return guard;

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to create GuardModel."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // UPDATE GUARD
    // =========================================================

    public boolean updateGuard(
            String email,
            String name,
            String phone,
            String guardId,
            String gate,
            String shift,
            String joiningDate,
            String status) {

        try {

            if (db == null ||
                    email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            String normalizedEmail =
                    email.trim().toLowerCase();

            // =====================================================
            // 1. TRY LEGACY ROOT-LEVEL STRUCTURE
            //
            // Guards/{email}
            // =====================================================

            DocumentSnapshot rootDocument =
                    db.collection("Guards")
                            .document(normalizedEmail)
                            .get()
                            .get();

            if (rootDocument.exists()) {

                db.collection("Guards")
                        .document(normalizedEmail)
                        .update(
                                "name", name,
                                "phone", phone,
                                "guardId", guardId,
                                "gate", gate,
                                "shift", shift,
                                "joiningDate", joiningDate,
                                "status", status
                        )
                        .get();

                return true;
            }

            // =====================================================
            // 2. TRY SOCIETY-WISE STRUCTURE
            //
            // societies/{society}/Guards/{email}
            // =====================================================

            QuerySnapshot societies =
                    db.collection("societies")
                            .get()
                            .get();

            for (DocumentSnapshot societyDocument :
                    societies.getDocuments()) {

                if (!societyDocument.exists()) {
                    continue;
                }

                String societyName =
                        societyDocument.getId();

                DocumentSnapshot guardDocument =
                        db.collection("societies")
                                .document(societyName)
                                .collection("Guards")
                                .document(normalizedEmail)
                                .get()
                                .get();

                if (!guardDocument.exists()) {
                    continue;
                }

                db.collection("societies")
                        .document(societyName)
                        .collection("Guards")
                        .document(normalizedEmail)
                        .update(
                                "name", name,
                                "phone", phone,
                                "guardId", guardId,
                                "gate", gate,
                                "shift", shift,
                                "joiningDate", joiningDate,
                                "status", status
                        )
                        .get();

                return true;
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to update guard."
            );

            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // UPDATE PROFILE IMAGE
    // =========================================================

    public boolean updateProfileImage(
            String email,
            String profileImageUrl) {

        try {

            if (db == null ||
                    email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            if (profileImageUrl == null ||
                    profileImageUrl.trim().isEmpty()) {

                return false;
            }

            String normalizedEmail =
                    email.trim().toLowerCase();

            // =====================================================
            // 1. TRY LEGACY ROOT-LEVEL STRUCTURE
            //
            // Guards/{email}
            // =====================================================

            DocumentSnapshot rootDocument =
                    db.collection("Guards")
                            .document(normalizedEmail)
                            .get()
                            .get();

            if (rootDocument.exists()) {

                db.collection("Guards")
                        .document(normalizedEmail)
                        .update(
                                "profileImageUrl",
                                profileImageUrl.trim()
                        )
                        .get();

                return true;
            }

            // =====================================================
            // 2. TRY SOCIETY-WISE STRUCTURE
            //
            // societies/{society}/Guards/{email}
            // =====================================================

            QuerySnapshot societies =
                    db.collection("societies")
                            .get()
                            .get();

            for (DocumentSnapshot societyDocument :
                    societies.getDocuments()) {

                if (!societyDocument.exists()) {
                    continue;
                }

                String societyName =
                        societyDocument.getId();

                DocumentSnapshot guardDocument =
                        db.collection("societies")
                                .document(societyName)
                                .collection("Guards")
                                .document(normalizedEmail)
                                .get()
                                .get();

                if (!guardDocument.exists()) {
                    continue;
                }

                db.collection("societies")
                        .document(societyName)
                        .collection("Guards")
                        .document(normalizedEmail)
                        .update(
                                "profileImageUrl",
                                profileImageUrl.trim()
                        )
                        .get();

                return true;
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to update guard profile image."
            );

            e.printStackTrace();
        }

        return false;
    }
}