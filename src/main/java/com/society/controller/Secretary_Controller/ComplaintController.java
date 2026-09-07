package com.society.controller.Secretary_Controller;

import java.util.ArrayList;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Complaint;

public class ComplaintController {

    private final Firestore firestore;

    private static final String COMPLAINT_COLLECTION = "complaints";
    private static final String RESIDENT_COLLECTION = "Residents";
    private static final String SECRETARY_COLLECTION = "Secretaries";

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public ComplaintController() {
        firestore = FirebaseConfig.getFirestore();
    }

    // ============================================================
    // NORMALIZE EMAIL
    // ============================================================

    private String normalizeEmail(String email) {

        if (email == null) {
            return "";
        }

        return email.trim().toLowerCase();
    }

    // ============================================================
    // NORMALIZE SOCIETY
    // ============================================================

    private String normalizeSociety(String society) {

        if (society == null) {
            return "";
        }

        return society.trim();
    }

    // ============================================================
    // GET CURRENT LOGGED-IN SECRETARY EMAIL
    // ============================================================

    private String getCurrentSecretaryEmail() {

        String email = UserDao.getLoggedInEmail();

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        return normalizeEmail(email);
    }

    // ============================================================
    // GET CURRENT SECRETARY SOCIETY
    // ============================================================

    private String getCurrentSecretarySociety() {

        try {

            String secretaryEmail = getCurrentSecretaryEmail();

            if (secretaryEmail == null || secretaryEmail.isEmpty()) {
                System.out.println("Secretary email not found.");
                return null;
            }

            // First try document ID = logged-in email
            DocumentReference secretaryRef = firestore
                    .collection(SECRETARY_COLLECTION)
                    .document(secretaryEmail);

            DocumentSnapshot secretaryDocument = secretaryRef.get().get();

            if (secretaryDocument.exists()) {

                String society = secretaryDocument.getString("society");

                if (society != null && !society.trim().isEmpty()) {

                    System.out.println(
                            "Current Secretary Email: " + secretaryEmail
                    );

                    System.out.println(
                            "Current Secretary Society: " + society
                    );

                    return normalizeSociety(society);
                }
            }

            /*
             * Fallback:
             * If Secretaries document ID is not email,
             * search by email field.
             */
            QuerySnapshot querySnapshot = firestore
                    .collection(SECRETARY_COLLECTION)
                    .whereEqualTo("email", secretaryEmail)
                    .limit(1)
                    .get()
                    .get();

            if (!querySnapshot.isEmpty()) {

                DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                String society = document.getString("society");

                if (society != null && !society.trim().isEmpty()) {

                    System.out.println(
                            "Current Secretary Email: " + secretaryEmail
                    );

                    System.out.println(
                            "Current Secretary Society: " + society
                    );

                    return normalizeSociety(society);
                }
            }

            System.out.println(
                    "Society not found for Secretary: " + secretaryEmail
            );

        } catch (Exception e) {

            System.out.println(
                    "Error while getting Secretary society: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return null;
    }

    // ============================================================
    // GET RESIDENT SOCIETY USING RESIDENT EMAIL
    // ============================================================

    private String getResidentSociety(String residentEmail) {

        try {

            String normalizedEmail = normalizeEmail(residentEmail);

            if (normalizedEmail.isEmpty()) {
                return null;
            }

            /*
             * ----------------------------------------------------
             * FIRST:
             * Search Residents collection using email field
             * ----------------------------------------------------
             */

            QuerySnapshot querySnapshot = firestore
                    .collection(RESIDENT_COLLECTION)
                    .whereEqualTo("email", normalizedEmail)
                    .limit(1)
                    .get()
                    .get();

            if (!querySnapshot.isEmpty()) {

                DocumentSnapshot residentDocument =
                        querySnapshot.getDocuments().get(0);

                String society =
                        residentDocument.getString("society");

                if (society != null && !society.trim().isEmpty()) {

                    return normalizeSociety(society);
                }
            }

            /*
             * ----------------------------------------------------
             * FALLBACK:
             * Sometimes resident document ID itself is email
             * ----------------------------------------------------
             */

            DocumentSnapshot directDocument = firestore
                    .collection(RESIDENT_COLLECTION)
                    .document(normalizedEmail)
                    .get()
                    .get();

            if (directDocument.exists()) {

                String society =
                        directDocument.getString("society");

                if (society != null && !society.trim().isEmpty()) {

                    return normalizeSociety(society);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error while getting resident society for "
                            + residentEmail
                            + " : "
                            + e.getMessage()
            );
        }

        return null;
    }

    // ============================================================
    // GET ALL COMPLAINTS OF CURRENT SECRETARY'S SOCIETY
    // ============================================================

    public List<Complaint> getAllComplaints() {

        List<Complaint> complaints = new ArrayList<>();

        try {

            // ----------------------------------------------------
            // Get logged-in Secretary's society dynamically
            // ----------------------------------------------------

            String secretarySociety =
                    getCurrentSecretarySociety();

            if (secretarySociety == null
                    || secretarySociety.trim().isEmpty()) {

                System.out.println(
                        "Secretary society not found."
                );

                return complaints;
            }

            secretarySociety =
                    normalizeSociety(secretarySociety);

            System.out.println(
                    "Fetching complaints for society: "
                            + secretarySociety
            );

            // ----------------------------------------------------
            // Get all nested complaint documents
            // ----------------------------------------------------

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collectionGroup(COMPLAINT_COLLECTION)
                            .get();

            QuerySnapshot snapshot = future.get();

            // ----------------------------------------------------
            // Check every complaint
            // ----------------------------------------------------

            for (QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                try {

                    /*
                     * ------------------------------------------------
                     * Get complaint email
                     * ------------------------------------------------
                     *
                     * Example:
                     *
                     * complaints/
                     *    vaishnavi@gmail.com/
                     *       complaints/
                     *          complaintId
                     *
                     * Email can come from document field.
                     */

                    String complaintEmail =
                            document.getString("email");

                    /*
                     * If email field is missing,
                     * get it from parent document ID.
                     */

                    if (complaintEmail == null
                            || complaintEmail.trim().isEmpty()) {

                        DocumentReference complaintReference =
                                document.getReference();

                        DocumentReference emailDocument =
                                complaintReference
                                        .getParent()
                                        .getParent();

                        if (emailDocument != null) {

                            complaintEmail =
                                    emailDocument.getId();
                        }
                    }

                    if (complaintEmail == null
                            || complaintEmail.trim().isEmpty()) {

                        continue;
                    }

                    complaintEmail =
                            normalizeEmail(complaintEmail);

                    // ------------------------------------------------
                    // Get resident society using complaint email
                    // ------------------------------------------------

                    String residentSociety =
                            getResidentSociety(complaintEmail);

                    if (residentSociety == null
                            || residentSociety.trim().isEmpty()) {

                        System.out.println(
                                "Society not found for complaint email: "
                                        + complaintEmail
                        );

                        continue;
                    }

                    residentSociety =
                            normalizeSociety(residentSociety);

                    // ------------------------------------------------
                    // Society comparison
                    // ------------------------------------------------

                    if (!residentSociety.equalsIgnoreCase(
                            secretarySociety)) {

                        System.out.println(
                                "Skipping complaint from another society: "
                                        + complaintEmail
                                        + " | Society: "
                                        + residentSociety
                        );

                        continue;
                    }

                    // ------------------------------------------------
                    // Complaint belongs to Secretary's society
                    // ------------------------------------------------

                    Complaint complaint =
                            convertDocumentToComplaint(
                                    document,
                                    complaintEmail,
                                    residentSociety
                            );

                    complaints.add(complaint);

                } catch (Exception complaintException) {

                    System.out.println(
                            "Error processing complaint document: "
                                    + document.getId()
                    );

                    complaintException.printStackTrace();
                }
            }

            System.out.println(
                    "Total complaints for Secretary society: "
                            + complaints.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error while fetching complaints: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return complaints;
    }

    // ============================================================
    // GET COMPLAINTS BY STATUS
    // ============================================================

    public List<Complaint> getAllComplaintsByStatus(
            String status) {

        List<Complaint> filteredComplaints =
                new ArrayList<>();

        try {

            List<Complaint> allComplaints =
                    getAllComplaints();

            String requiredStatus =
                    normalizeStatus(status);

            for (Complaint complaint : allComplaints) {

                String complaintStatus =
                        normalizeStatus(
                                complaint.getStatus()
                        );

                if (complaintStatus.equalsIgnoreCase(
                        requiredStatus)) {

                    filteredComplaints.add(complaint);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error while filtering complaints by status: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return filteredComplaints;
    }

    // ============================================================
    // GET COMPLAINTS BY EMAIL
    // ============================================================

    public List<Complaint> getComplaintsByEmail(
            String email) {

        List<Complaint> complaints =
                new ArrayList<>();

        try {

            String normalizedEmail =
                    normalizeEmail(email);

            if (normalizedEmail.isEmpty()) {
                return complaints;
            }

            CollectionReference complaintCollection =
                    firestore
                            .collection(COMPLAINT_COLLECTION)
                            .document(normalizedEmail)
                            .collection(COMPLAINT_COLLECTION);

            QuerySnapshot snapshot =
                    complaintCollection.get().get();

            String residentSociety =
                    getResidentSociety(normalizedEmail);

            for (QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                Complaint complaint =
                        convertDocumentToComplaint(
                                document,
                                normalizedEmail,
                                residentSociety
                        );

                complaints.add(complaint);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error while fetching complaints by email: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return complaints;
    }

    // ============================================================
    // UPDATE COMPLAINT STATUS
    // ============================================================

    public boolean updateComplaintStatus(
            String complaintEmail,
            String complaintId,
            String newStatus) {

        try {

            if (complaintEmail == null
                    || complaintEmail.trim().isEmpty()) {

                System.out.println(
                        "Complaint email is missing."
                );

                return false;
            }

            if (complaintId == null
                    || complaintId.trim().isEmpty()) {

                System.out.println(
                        "Complaint ID is missing."
                );

                return false;
            }

            String secretarySociety =
                    getCurrentSecretarySociety();

            if (secretarySociety == null
                    || secretarySociety.trim().isEmpty()) {

                System.out.println(
                        "Secretary society not found."
                );

                return false;
            }

            String normalizedComplaintEmail =
                    normalizeEmail(complaintEmail);

            // ----------------------------------------------------
            // Get complaint
            // ----------------------------------------------------

            DocumentReference complaintReference =
                    firestore
                            .collection(COMPLAINT_COLLECTION)
                            .document(normalizedComplaintEmail)
                            .collection(COMPLAINT_COLLECTION)
                            .document(complaintId);

            DocumentSnapshot complaintDocument =
                    complaintReference.get().get();

            if (!complaintDocument.exists()) {

                System.out.println(
                        "Complaint not found: "
                                + complaintId
                );

                return false;
            }

            // ----------------------------------------------------
            // Find resident's society from email
            // ----------------------------------------------------

            String residentSociety =
                    getResidentSociety(
                            normalizedComplaintEmail
                    );

            if (residentSociety == null
                    || residentSociety.trim().isEmpty()) {

                System.out.println(
                        "Resident society not found."
                );

                return false;
            }

            // ----------------------------------------------------
            // Security check
            // ----------------------------------------------------

            if (!residentSociety.equalsIgnoreCase(
                    secretarySociety)) {

                System.out.println(
                        "Unauthorized complaint status update."
                );

                System.out.println(
                        "Secretary Society: "
                                + secretarySociety
                );

                System.out.println(
                        "Complaint Society: "
                                + residentSociety
                );

                return false;
            }

            // ----------------------------------------------------
            // Update status
            // ----------------------------------------------------

            String normalizedStatus =
                    normalizeStatus(newStatus);

            complaintReference
                    .update("status", normalizedStatus)
                    .get();

            System.out.println(
                    "Complaint status updated successfully."
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while updating complaint status: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }

    // ============================================================
    // CONVERT FIRESTORE DOCUMENT TO COMPLAINT MODEL
    // ============================================================

    private Complaint convertDocumentToComplaint(
            DocumentSnapshot document,
            String email,
            String society) {

        String id =
                document.getString("id");

        if (id == null || id.trim().isEmpty()) {
            id = document.getId();
        }

        String complaintEmail =
                document.getString("email");

        if (complaintEmail == null
                || complaintEmail.trim().isEmpty()) {

            complaintEmail = email;
        }

        String title =
                document.getString("title");

        String category =
                document.getString("category");

        String description =
                document.getString("description");

        String flatNumber =
                document.getString("flatNumber");

        String preferredDate =
                document.getString("preferredDate");

        String status =
                document.getString("status");

        String imageFileName =
                document.getString("imageFileName");

        Complaint complaint =
                new Complaint();

        complaint.setId(id);
        complaint.setEmail(
                normalizeEmail(complaintEmail)
        );

        complaint.setTitle(
                title != null ? title : ""
        );

        complaint.setCategory(
                category != null ? category : ""
        );

        complaint.setDescription(
                description != null ? description : ""
        );

        complaint.setFlatNumber(
                flatNumber != null ? flatNumber : ""
        );

        complaint.setPreferredDate(
                preferredDate != null ? preferredDate : ""
        );

        complaint.setStatus(
                normalizeStatus(status)
        );

        complaint.setImageFileName(
                imageFileName != null
                        ? imageFileName
                        : ""
        );

        complaint.setSociety(
                society != null
                        ? society
                        : ""
        );

        if (document.contains("createdAt")) {

            complaint.setCreatedAt(
                    document.getTimestamp("createdAt")
            );
        }

        return complaint;
    }

    // ============================================================
    // NORMALIZE STATUS
    // ============================================================

    private String normalizeStatus(String status) {

        if (status == null
                || status.trim().isEmpty()) {

            return "IN PROGRESS";
        }

        String normalized =
                status.trim()
                        .toUpperCase()
                        .replace("_", " ");

        switch (normalized) {

            case "OPEN":
                return "IN PROGRESS";

            case "IN PROGRESS":
                return "IN PROGRESS";

            case "RESOLVED":
                return "RESOLVED";

            case "CLOSED":
                return "CLOSED";

            default:
                return normalized;
        }
    }

    // ============================================================
    // OPTIONAL: GET COMPLAINTS BY SOCIETY
    // ============================================================

    public List<Complaint> getComplaintsBySociety(
            String society) {

        List<Complaint> complaints =
                new ArrayList<>();

        try {

            if (society == null
                    || society.trim().isEmpty()) {

                return complaints;
            }

            String requiredSociety =
                    normalizeSociety(society);

            List<Complaint> allComplaints =
                    getAllComplaints();

            for (Complaint complaint : allComplaints) {

                if (complaint.getSociety() != null
                        && complaint.getSociety()
                        .equalsIgnoreCase(requiredSociety)) {

                    complaints.add(complaint);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error while getting complaints by society: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return complaints;
    }
}