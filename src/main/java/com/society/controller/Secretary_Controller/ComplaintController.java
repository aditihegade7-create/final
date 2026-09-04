package com.society.controller.Secretary_Controller;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Complaint;

public class ComplaintController {

    private Firestore firestore;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ComplaintController() {

        firestore = FirebaseConfig.getFirestore();

        System.out.println("======================================");
        System.out.println("COMPLAINT CONTROLLER");
        System.out.println("Firestore initialized");
        System.out.println("======================================");
    }

    // =====================================================
    // GET CURRENT SECRETARY SOCIETY
    // =====================================================
    //
    // Logged-in Secretary Email
    //          ↓
    // Secretaries/{email}
    //          ↓
    // society
    //
    // =====================================================

    private String getCurrentSecretarySociety() {

        try {

            // -------------------------------------------------
            // GET LOGGED-IN SECRETARY EMAIL
            // -------------------------------------------------

            String secretaryEmail =
                    UserDao.getLoggedInEmail();

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                System.err.println(
                        "Logged-in Secretary email is missing."
                );

                return null;
            }

            secretaryEmail =
                    secretaryEmail.trim();

            System.out.println("======================================");
            System.out.println(
                    "GETTING CURRENT SECRETARY SOCIETY"
            );
            System.out.println(
                    "Secretary Email = "
                            + secretaryEmail
            );
            System.out.println("======================================");

            // -------------------------------------------------
            // GET SECRETARY DOCUMENT
            // -------------------------------------------------

            DocumentSnapshot secretaryDocument =
                    firestore
                            .collection("Secretaries")
                            .document(secretaryEmail)
                            .get()
                            .get();

            if (!secretaryDocument.exists()) {

                System.err.println(
                        "Secretary document NOT FOUND."
                );

                System.err.println(
                        "Expected path:"
                );

                System.err.println(
                        "Secretaries/"
                                + secretaryEmail
                );

                return null;
            }

            // -------------------------------------------------
            // GET SOCIETY
            // -------------------------------------------------

            String society =
                    secretaryDocument.getString("society");

            if (society == null ||
                    society.trim().isEmpty()) {

                System.err.println(
                        "Society not found for Secretary."
                );

                return null;
            }

            society =
                    society.trim();

            System.out.println(
                    "Secretary Society = "
                            + society
            );

            return society;

        } catch (Exception e) {

            System.err.println(
                    "ERROR GETTING SECRETARY SOCIETY"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // GET ALL COMPLAINTS
    // =====================================================
    //
    // IMPORTANT:
    //
    // This method returns complaints ONLY from
    // the currently logged-in Secretary's society.
    //
    // Firestore collection group query is used WITHOUT
    // whereEqualTo(), therefore NO FIRESTORE INDEX IS
    // REQUIRED.
    //
    // Data structure:
    //
    // complaints
    //    / residentEmail
    //       / complaints
    //          / complaintId
    //
    // Society filtering is done in Java.
    //
    // =====================================================

    public List<Complaint> getAllComplaints() {

        List<Complaint> complaints =
                new ArrayList<>();

        try {

            System.out.println("======================================");
            System.out.println(
                    "FETCHING COMPLAINTS FOR SECRETARY SOCIETY"
            );
            System.out.println("======================================");

            // -------------------------------------------------
            // GET SECRETARY SOCIETY
            // -------------------------------------------------

            String society =
                    getCurrentSecretarySociety();

            if (society == null ||
                    society.trim().isEmpty()) {

                System.err.println(
                        "Cannot fetch complaints."
                );

                System.err.println(
                        "Secretary society is missing."
                );

                return complaints;
            }

            society =
                    society.trim();

            System.out.println(
                    "Fetching complaints for Society = "
                            + society
            );

            // =================================================
            // FETCH ALL COMPLAINTS
            // =================================================
            //
            // IMPORTANT:
            //
            // DO NOT use:
            //
            // .whereEqualTo("society", society)
            //
            // because that requires a COLLECTION_GROUP
            // index.
            //
            // Instead, fetch all complaint documents and
            // filter society in Java.
            //
            // =================================================

            QuerySnapshot snapshot =
                    firestore
                            .collectionGroup("complaints")
                            .get()
                            .get();

            System.out.println(
                    "TOTAL COMPLAINT DOCUMENTS FOUND = "
                            + snapshot.size()
            );

            // =================================================
            // LOOP THROUGH ALL COMPLAINTS
            // =================================================

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                // -------------------------------------------------
                // GET COMPLAINT SOCIETY
                // -------------------------------------------------

                String complaintSociety =
                        document.getString("society");

                if (complaintSociety == null ||
                        complaintSociety.trim().isEmpty()) {

                    System.out.println(
                            "Skipping complaint because society is missing."
                    );

                    System.out.println(
                            "Complaint ID = "
                                    + document.getId()
                    );

                    continue;
                }

                complaintSociety =
                        complaintSociety.trim();

                // =================================================
                // SOCIETY FILTER
                // =================================================
                //
                // Only complaints belonging to the current
                // Secretary's society will be added.
                //
                // =================================================

                if (!complaintSociety.equalsIgnoreCase(
                        society
                )) {

                    System.out.println(
                            "Skipping complaint from another society."
                    );

                    System.out.println(
                            "Complaint Society = "
                                    + complaintSociety
                    );

                    System.out.println(
                            "Secretary Society = "
                                    + society
                    );

                    continue;
                }

                // -------------------------------------------------
                // GET EMAIL
                // -------------------------------------------------

                String email =
                        document.getString("email");

                // -------------------------------------------------
                // FALLBACK EMAIL FROM PATH
                // -------------------------------------------------

                if (email == null ||
                        email.trim().isEmpty()) {

                    try {

                        DocumentReference emailDocument =
                                document
                                        .getReference()
                                        .getParent()
                                        .getParent();

                        if (emailDocument != null) {

                            email =
                                    emailDocument.getId();
                        }

                    } catch (Exception ex) {

                        System.err.println(
                                "Unable to get email from complaint path."
                        );

                        email = "";
                    }
                }

                email =
                        email == null
                                ? ""
                                : email.trim();

                System.out.println("--------------------------------------");

                System.out.println(
                        "Complaint Document ID = "
                                + document.getId()
                );

                System.out.println(
                        "Resident Email = "
                                + email
                );

                System.out.println(
                        "Complaint Society = "
                                + complaintSociety
                );

                System.out.println(
                        "Secretary Society = "
                                + society
                );

                // =================================================
                // CONVERT DOCUMENT
                // =================================================

                Complaint complaint =
                        convertDocumentToComplaint(
                                document,
                                email
                        );

                if (complaint != null) {

                    complaints.add(
                            complaint
                    );
                }
            }

            System.out.println("======================================");

            System.out.println(
                    "TOTAL SOCIETY COMPLAINTS FETCHED = "
                            + complaints.size()
            );

            System.out.println(
                    "Secretary Society = "
                            + society
            );

            System.out.println("======================================");

            return complaints;

        } catch (Exception e) {

            System.err.println(
                    "ERROR FETCHING SOCIETY COMPLAINTS"
            );

            e.printStackTrace();

            return complaints;
        }
    }

    // =====================================================
    // GET COMPLAINTS BY SOCIETY
    // =====================================================
    //
    // This method also avoids Firestore index requirement.
    //
    // It fetches all complaints and filters society
    // in Java.
    //
    // =====================================================

    public List<Complaint> getComplaintsBySociety(
            String society) {

        List<Complaint> complaints =
                new ArrayList<>();

        try {

            if (society == null ||
                    society.trim().isEmpty()) {

                System.err.println(
                        "Society is empty."
                );

                return complaints;
            }

            society =
                    society.trim();

            System.out.println("======================================");
            System.out.println(
                    "FETCHING COMPLAINTS BY SOCIETY"
            );

            System.out.println(
                    "Requested Society = "
                            + society
            );

            System.out.println("======================================");

            // =================================================
            // FETCH ALL COMPLAINTS
            // =================================================

            QuerySnapshot snapshot =
                    firestore
                            .collectionGroup("complaints")
                            .get()
                            .get();

            System.out.println(
                    "Total complaint documents = "
                            + snapshot.size()
            );

            // =================================================
            // FILTER SOCIETY IN JAVA
            // =================================================

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                String complaintSociety =
                        document.getString("society");

                if (complaintSociety == null ||
                        complaintSociety.trim().isEmpty()) {

                    continue;
                }

                complaintSociety =
                        complaintSociety.trim();

                // -------------------------------------------------
                // SOCIETY MATCH
                // -------------------------------------------------

                if (!complaintSociety.equalsIgnoreCase(
                        society
                )) {

                    continue;
                }

                // -------------------------------------------------
                // GET EMAIL
                // -------------------------------------------------

                String email =
                        document.getString("email");

                // -------------------------------------------------
                // FALLBACK EMAIL
                // -------------------------------------------------

                if (email == null ||
                        email.trim().isEmpty()) {

                    try {

                        DocumentReference emailDocument =
                                document
                                        .getReference()
                                        .getParent()
                                        .getParent();

                        if (emailDocument != null) {

                            email =
                                    emailDocument.getId();
                        }

                    } catch (Exception ex) {

                        email = "";
                    }
                }

                email =
                        email == null
                                ? ""
                                : email.trim();

                Complaint complaint =
                        convertDocumentToComplaint(
                                document,
                                email
                        );

                if (complaint != null) {

                    complaints.add(
                            complaint
                    );
                }
            }

            System.out.println(
                    "Complaints found for society "
                            + society
                            + " = "
                            + complaints.size()
            );

            return complaints;

        } catch (Exception e) {

            System.err.println(
                    "ERROR FETCHING COMPLAINTS BY SOCIETY"
            );

            e.printStackTrace();

            return complaints;
        }
    }

    // =====================================================
    // GET COMPLAINTS BY EMAIL
    // =====================================================
    //
    // This method fetches complaints for one resident.
    //
    // =====================================================

    public List<Complaint> getComplaintsByEmail(
            String email) {

        List<Complaint> complaints =
                new ArrayList<>();

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                System.err.println(
                        "Resident email is empty."
                );

                return complaints;
            }

            email =
                    email.trim();

            System.out.println("======================================");
            System.out.println(
                    "FETCHING COMPLAINTS FOR EMAIL"
            );

            System.out.println(
                    "Resident Email = "
                            + email
            );

            System.out.println("======================================");

            QuerySnapshot snapshot =
                    firestore
                            .collection("complaints")
                            .document(email)
                            .collection("complaints")
                            .get()
                            .get();

            System.out.println(
                    "Complaints found for "
                            + email
                            + " = "
                            + snapshot.size()
            );

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                Complaint complaint =
                        convertDocumentToComplaint(
                                document,
                                email
                        );

                if (complaint != null) {

                    complaints.add(
                            complaint
                    );
                }
            }

            return complaints;

        } catch (Exception e) {

            System.err.println(
                    "ERROR FETCHING COMPLAINTS BY EMAIL"
            );

            e.printStackTrace();

            return complaints;
        }
    }

    // =====================================================
    // GET COMPLAINTS BY STATUS
    // =====================================================
    //
    // IMPORTANT:
    //
    // First gets ONLY current Secretary society
    // complaints.
    //
    // Then applies status filter.
    //
    // =====================================================

    public List<Complaint> getAllComplaintsByStatus(
            String status) {

        List<Complaint> filteredComplaints =
                new ArrayList<>();

        try {

            String requiredStatus =
                    normalizeStatus(status);

            System.out.println("======================================");
            System.out.println(
                    "FILTERING SOCIETY COMPLAINTS"
            );

            System.out.println(
                    "Required Status = "
                            + requiredStatus
            );

            System.out.println("======================================");

            // -------------------------------------------------
            // GET SOCIETY COMPLAINTS
            // -------------------------------------------------

            List<Complaint> societyComplaints =
                    getAllComplaints();

            // =================================================
            // FILTER STATUS
            // =================================================

            for (Complaint complaint :
                    societyComplaints) {

                if (complaint == null) {

                    continue;
                }

                String complaintStatus =
                        normalizeStatus(
                                complaint.getStatus()
                        );

                if (complaintStatus.equalsIgnoreCase(
                        requiredStatus
                )) {

                    filteredComplaints.add(
                            complaint
                    );
                }
            }

            System.out.println(
                    "Filtered society complaints = "
                            + filteredComplaints.size()
            );

            return filteredComplaints;

        } catch (Exception e) {

            System.err.println(
                    "ERROR FILTERING SOCIETY COMPLAINTS"
            );

            e.printStackTrace();

            return filteredComplaints;
        }
    }

    // =====================================================
    // UPDATE COMPLAINT STATUS
    // =====================================================
    //
    // Exact path:
    //
    // complaints
    //    / email
    //       / complaints
    //          / complaintId
    //
    // Security:
    //
    // Secretary Society
    //        ==
    // Complaint Society
    //
    // Only then status will be updated.
    //
    // =====================================================

    public boolean updateComplaintStatus(
            String email,
            String complaintId,
            String newStatus) {

        try {

            // -------------------------------------------------
            // VALIDATE EMAIL
            // -------------------------------------------------

            if (email == null ||
                    email.trim().isEmpty()) {

                System.err.println(
                        "Email missing."
                );

                return false;
            }

            // -------------------------------------------------
            // VALIDATE COMPLAINT ID
            // -------------------------------------------------

            if (complaintId == null ||
                    complaintId.trim().isEmpty()) {

                System.err.println(
                        "Complaint ID missing."
                );

                return false;
            }

            // -------------------------------------------------
            // VALIDATE STATUS
            // -------------------------------------------------

            if (newStatus == null ||
                    newStatus.trim().isEmpty()) {

                System.err.println(
                        "New status missing."
                );

                return false;
            }

            email =
                    email.trim();

            complaintId =
                    complaintId.trim();

            newStatus =
                    normalizeStatus(
                            newStatus
                    );

            System.out.println("======================================");
            System.out.println(
                    "UPDATING COMPLAINT STATUS"
            );

            System.out.println(
                    "Email = "
                            + email
            );

            System.out.println(
                    "Complaint ID = "
                            + complaintId
            );

            System.out.println(
                    "New Status = "
                            + newStatus
            );

            System.out.println("======================================");

            // =================================================
            // GET CURRENT SECRETARY SOCIETY
            // =================================================

            String secretarySociety =
                    getCurrentSecretarySociety();

            if (secretarySociety == null ||
                    secretarySociety.trim().isEmpty()) {

                System.err.println(
                        "Secretary society missing."
                );

                return false;
            }

            secretarySociety =
                    secretarySociety.trim();

            // =================================================
            // COMPLAINT REFERENCE
            // =================================================

            DocumentReference complaintReference =
                    firestore
                            .collection("complaints")
                            .document(email)
                            .collection("complaints")
                            .document(complaintId);

            // =================================================
            // GET COMPLAINT DOCUMENT
            // =================================================

            DocumentSnapshot document =
                    complaintReference
                            .get()
                            .get();

            if (!document.exists()) {

                System.err.println(
                        "Complaint document NOT FOUND."
                );

                System.err.println(
                        "Expected path:"
                );

                System.err.println(
                        "complaints/"
                                + email
                                + "/complaints/"
                                + complaintId
                );

                return false;
            }

            // =================================================
            // SECURITY CHECK
            // =================================================
            //
            // Secretary can update ONLY complaints
            // belonging to their own society.
            //
            // =================================================

            String complaintSociety =
                    document.getString("society");

            if (complaintSociety == null ||
                    complaintSociety.trim().isEmpty()) {

                System.err.println(
                        "Complaint society is missing."
                );

                return false;
            }

            complaintSociety =
                    complaintSociety.trim();

            // -------------------------------------------------
            // COMPARE SOCIETY
            // -------------------------------------------------

            if (!complaintSociety.equalsIgnoreCase(
                    secretarySociety
            )) {

                System.err.println(
                        "ACCESS DENIED."
                );

                System.err.println(
                        "Secretary Society = "
                                + secretarySociety
                );

                System.err.println(
                        "Complaint Society = "
                                + complaintSociety
                );

                return false;
            }

            // =================================================
            // UPDATE STATUS
            // =================================================

            complaintReference
                    .update(
                            "status",
                            newStatus
                    )
                    .get();

            System.out.println(
                    "Complaint status updated successfully."
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "ERROR UPDATING COMPLAINT STATUS"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // CONVERT FIRESTORE DOCUMENT
    // =====================================================

    private Complaint convertDocumentToComplaint(
            DocumentSnapshot document,
            String email) {

        try {

            if (document == null ||
                    !document.exists()) {

                return null;
            }

            Complaint complaint =
                    new Complaint();

            // =================================================
            // ID
            // =================================================

            String id =
                    document.getString("id");

            if (id == null ||
                    id.trim().isEmpty()) {

                id =
                        document.getId();
            }

            complaint.setId(id);

            // =================================================
            // EMAIL
            // =================================================

            String firestoreEmail =
                    document.getString("email");

            if (firestoreEmail == null ||
                    firestoreEmail.trim().isEmpty()) {

                firestoreEmail =
                        email;
            }

            complaint.setEmail(
                    firestoreEmail
            );

            // =================================================
            // TITLE
            // =================================================

            complaint.setTitle(
                    getString(
                            document,
                            "title"
                    )
            );

            // =================================================
            // CATEGORY
            // =================================================

            complaint.setCategory(
                    getString(
                            document,
                            "category"
                    )
            );

            // =================================================
            // DESCRIPTION
            // =================================================

            complaint.setDescription(
                    getString(
                            document,
                            "description"
                    )
            );

            // =================================================
            // FLAT NUMBER
            // =================================================

            complaint.setFlatNumber(
                    getString(
                            document,
                            "flatNumber"
                    )
            );

            // =================================================
            // PREFERRED DATE
            // =================================================

            complaint.setPreferredDate(
                    getString(
                            document,
                            "preferredDate"
                    )
            );

            // =================================================
            // STATUS
            // =================================================

            complaint.setStatus(
                    normalizeStatus(
                            getString(
                                    document,
                                    "status"
                            )
                    )
            );

            // =================================================
            // SOCIETY
            // =================================================

            String society =
                    getString(
                            document,
                            "society"
                    );

            complaint.setSociety(
                    society
            );

            // =================================================
            // DEBUG
            // =================================================

            System.out.println(
                    "Complaint fetched successfully:"
            );

            System.out.println(
                    "Email = "
                            + complaint.getEmail()
            );

            System.out.println(
                    "ID = "
                            + complaint.getId()
            );

            System.out.println(
                    "Title = "
                            + complaint.getTitle()
            );

            System.out.println(
                    "Category = "
                            + complaint.getCategory()
            );

            System.out.println(
                    "Description = "
                            + complaint.getDescription()
            );

            System.out.println(
                    "Flat = "
                            + complaint.getFlatNumber()
            );

            System.out.println(
                    "Preferred Date = "
                            + complaint.getPreferredDate()
            );

            System.out.println(
                    "Status = "
                            + complaint.getStatus()
            );

            System.out.println(
                    "Society = "
                            + complaint.getSociety()
            );

            return complaint;

        } catch (Exception e) {

            System.err.println(
                    "ERROR CONVERTING COMPLAINT"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // GET STRING
    // =====================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        try {

            String value =
                    document.getString(field);

            if (value == null) {

                return "";
            }

            return value;

        } catch (Exception e) {

            return "";
        }
    }

    // =====================================================
    // NORMALIZE STATUS
    // =====================================================

    private String normalizeStatus(
            String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return "IN PROGRESS";
        }

        String value =
                status.trim()
                        .toUpperCase();

        switch (value) {

            case "OPEN":

                return "IN PROGRESS";

            case "IN PROGRESS":

                return "IN PROGRESS";

            case "RESOLVED":

                return "RESOLVED";

            case "CLOSED":

                return "CLOSED";

            default:

                return "IN PROGRESS";
        }
    }
}