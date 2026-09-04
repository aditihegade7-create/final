package com.society.dao.Resident_dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.model.Resident_model.ComplaintModel;

import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore firestore;

    // =========================================================
    // COLLECTION NAMES
    // =========================================================

    private static final String COLLECTION =
            "complaints";

    private static final String SUB_COLLECTION =
            "complaints";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ComplaintDAO(Firestore firestore) {

        if (firestore == null) {
            throw new IllegalArgumentException(
                    "Firestore cannot be null."
            );
        }

        this.firestore = firestore;
    }

    // =========================================================
    // SAVE COMPLAINT
    // =========================================================

    public void saveComplaint(
            ComplaintModel complaint)
            throws Exception {

        // =====================================================
        // VALIDATION
        // =====================================================

        if (complaint == null) {

            throw new IllegalArgumentException(
                    "Complaint cannot be null."
            );
        }

        if (complaint.getId() == null
                || complaint.getId().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint ID cannot be empty."
            );
        }

        if (complaint.getEmail() == null
                || complaint.getEmail().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint email cannot be empty."
            );
        }

        String email =
                complaint.getEmail().trim();

        // =====================================================
        // SAVE
        // =====================================================

        firestore
                .collection(COLLECTION)
                .document(email)
                .collection(SUB_COLLECTION)
                .document(complaint.getId())
                .set(complaint)
                .get();

        // =====================================================
        // LOG
        // =====================================================

        System.out.println(
                "Complaint saved successfully."
        );

        System.out.println(
                "Complaint ID: "
                        + complaint.getId()
        );

        System.out.println(
                "Complaint Email: "
                        + complaint.getEmail()
        );
    }

    // =========================================================
    // GET COMPLAINT BY ID
    // =========================================================

    public ComplaintModel getComplaintById(
            String email,
            String id)
            throws Exception {

        // =====================================================
        // VALIDATION
        // =====================================================

        if (email == null
                || email.trim().isEmpty()) {

            return null;
        }

        if (id == null
                || id.trim().isEmpty()) {

            return null;
        }

        String cleanEmail =
                email.trim();

        String cleanId =
                id.trim();

        // =====================================================
        // GET DOCUMENT
        // =====================================================

        DocumentSnapshot document =
                firestore
                        .collection(COLLECTION)
                        .document(cleanEmail)
                        .collection(SUB_COLLECTION)
                        .document(cleanId)
                        .get()
                        .get();

        // =====================================================
        // CHECK EXISTS
        // =====================================================

        if (!document.exists()) {

            return null;
        }

        // =====================================================
        // CONVERT TO MODEL
        // =====================================================

        ComplaintModel complaint =
                document.toObject(
                        ComplaintModel.class
                );

        // =====================================================
        // RESTORE ID IF NECESSARY
        // =====================================================

        if (complaint != null
                && (complaint.getId() == null
                || complaint.getId().trim().isEmpty())) {

            complaint.setId(
                    document.getId()
            );
        }

        return complaint;
    }

    // =========================================================
    // GET ALL COMPLAINTS
    // =========================================================

    public List<ComplaintModel> getAllComplaints()
            throws Exception {

        List<ComplaintModel> complaints =
                new ArrayList<>();

        // =====================================================
        // GET ALL EMAIL DOCUMENTS
        // =====================================================

        QuerySnapshot parentSnapshot =
                firestore
                        .collection(COLLECTION)
                        .get()
                        .get();

        // =====================================================
        // LOOP THROUGH USERS
        // =====================================================

        for (DocumentSnapshot parentDocument :
                parentSnapshot.getDocuments()) {

            // =================================================
            // GET COMPLAINT SUBCOLLECTION
            // =================================================

            QuerySnapshot complaintSnapshot =
                    parentDocument
                            .getReference()
                            .collection(SUB_COLLECTION)
                            .get()
                            .get();

            // =================================================
            // LOOP THROUGH COMPLAINTS
            // =================================================

            for (DocumentSnapshot document :
                    complaintSnapshot.getDocuments()) {

                ComplaintModel complaint =
                        document.toObject(
                                ComplaintModel.class
                        );

                if (complaint != null) {

                    // Make sure ID is available
                    if (complaint.getId() == null
                            || complaint.getId()
                            .trim()
                            .isEmpty()) {

                        complaint.setId(
                                document.getId()
                        );
                    }

                    complaints.add(complaint);
                }
            }
        }

        System.out.println(
                "Total complaints: "
                        + complaints.size()
        );

        return complaints;
    }

    // =========================================================
    // GET COMPLAINTS BY EMAIL
    // =========================================================

    public List<ComplaintModel> getComplaintsByEmail(
            String email)
            throws Exception {

        List<ComplaintModel> complaints =
                new ArrayList<>();

        // =====================================================
        // VALIDATION
        // =====================================================

        if (email == null
                || email.trim().isEmpty()) {

            return complaints;
        }

        String loggedInEmail =
                email.trim();

        // =====================================================
        // FETCH COMPLAINTS
        // =====================================================

        QuerySnapshot snapshot =
                firestore
                        .collection(COLLECTION)
                        .document(loggedInEmail)
                        .collection(SUB_COLLECTION)
                        .get()
                        .get();

        // =====================================================
        // CONVERT DOCUMENTS
        // =====================================================

        for (DocumentSnapshot document :
                snapshot.getDocuments()) {

            ComplaintModel complaint =
                    document.toObject(
                            ComplaintModel.class
                    );

            if (complaint != null) {

                // Make sure ID exists
                if (complaint.getId() == null
                        || complaint.getId()
                        .trim()
                        .isEmpty()) {

                    complaint.setId(
                            document.getId()
                    );
                }

                complaints.add(complaint);
            }
        }

        // =====================================================
        // LOG
        // =====================================================

        System.out.println(
                "Complaints found for: "
                        + loggedInEmail
        );

        System.out.println(
                "Total complaints: "
                        + complaints.size()
        );

        return complaints;
    }

    // =========================================================
    // GET COMPLAINTS BY FLAT
    // =========================================================

    public List<ComplaintModel> getComplaintsByFlat(
            String flatNumber)
            throws Exception {

        List<ComplaintModel> complaints =
                new ArrayList<>();

        // =====================================================
        // VALIDATION
        // =====================================================

        if (flatNumber == null
                || flatNumber.trim().isEmpty()) {

            return complaints;
        }

        String cleanFlatNumber =
                flatNumber.trim();

        // =====================================================
        // GET ALL EMAIL DOCUMENTS
        // =====================================================

        QuerySnapshot parentSnapshot =
                firestore
                        .collection(COLLECTION)
                        .get()
                        .get();

        // =====================================================
        // SEARCH EACH USER'S COMPLAINTS
        // =====================================================

        for (DocumentSnapshot parentDocument :
                parentSnapshot.getDocuments()) {

            QuerySnapshot snapshot =
                    parentDocument
                            .getReference()
                            .collection(SUB_COLLECTION)
                            .whereEqualTo(
                                    "flatNumber",
                                    cleanFlatNumber
                            )
                            .get()
                            .get();

            // =================================================
            // CONVERT DOCUMENTS
            // =================================================

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                ComplaintModel complaint =
                        document.toObject(
                                ComplaintModel.class
                        );

                if (complaint != null) {

                    if (complaint.getId() == null
                            || complaint.getId()
                            .trim()
                            .isEmpty()) {

                        complaint.setId(
                                document.getId()
                        );
                    }

                    complaints.add(complaint);
                }
            }
        }

        System.out.println(
                "Complaints found for flat: "
                        + cleanFlatNumber
        );

        System.out.println(
                "Total complaints: "
                        + complaints.size()
        );

        return complaints;
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    public void updateStatus(
            String email,
            String complaintId,
            String status)
            throws Exception {

        // =====================================================
        // VALIDATION
        // =====================================================

        if (email == null
                || email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email cannot be empty."
            );
        }

        if (complaintId == null
                || complaintId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint ID cannot be empty."
            );
        }

        if (status == null
                || status.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Status cannot be empty."
            );
        }

        // =====================================================
        // UPDATE
        // =====================================================

        firestore
                .collection(COLLECTION)
                .document(email.trim())
                .collection(SUB_COLLECTION)
                .document(complaintId.trim())
                .update(
                        "status",
                        status.trim()
                )
                .get();

        // =====================================================
        // LOG
        // =====================================================

        System.out.println(
                "Complaint status updated successfully."
        );

        System.out.println(
                "Complaint ID: "
                        + complaintId
        );

        System.out.println(
                "New Status: "
                        + status
        );
    }
}