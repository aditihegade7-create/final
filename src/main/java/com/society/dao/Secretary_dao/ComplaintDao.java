package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.Complaint;

public class ComplaintDao {

    // =====================================================
    // FIRESTORE
    // =====================================================

    private final Firestore firestore;

    private static final String COMPLAINT_COLLECTION =
            "complaints";

    private static final String RESIDENT_COLLECTION =
            "Residents";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ComplaintDao() {

        firestore = FirebaseConfig.getFirestore();

        System.out.println("====================================");
        System.out.println("ComplaintDao initialized");
        System.out.println("====================================");
    }

    // =====================================================
    // NORMALIZE EMAIL
    // =====================================================

    private String normalizeEmail(String email) {

        if (email == null) {
            return "";
        }

        return email.trim().toLowerCase();
    }

    // =====================================================
    // GET COMPLAINT COLLECTION BY EMAIL
    //
    // complaints
    //      └── email
    //           └── complaints
    // =====================================================

    private CollectionReference getComplaintCollection(
            String email) {

        email = normalizeEmail(email);

        if (email.isEmpty()) {
            return null;
        }

        return firestore
                .collection(COMPLAINT_COLLECTION)
                .document(email)
                .collection("complaints");
    }

    // =====================================================
    // CHECK RESIDENT EXISTS
    // =====================================================
    //
    // Checks:
    //
    // Residents collection
    //       ↓
    // email field == given email
    //
    // This does NOT assume that email is the document ID.
    //
    // =====================================================

    public boolean residentExists(String email) {

        try {

            String normalizedEmail =
                    normalizeEmail(email);

            if (normalizedEmail.isEmpty()) {

                System.out.println(
                        "Resident check failed: email empty."
                );

                return false;
            }

            System.out.println("====================================");
            System.out.println("CHECKING RESIDENT");
            System.out.println("Email = " + normalizedEmail);
            System.out.println("Collection = " + RESIDENT_COLLECTION);
            System.out.println("====================================");

            QuerySnapshot snapshot =
                    firestore
                            .collection(RESIDENT_COLLECTION)
                            .whereEqualTo(
                                    "email",
                                    normalizedEmail
                            )
                            .limit(1)
                            .get()
                            .get();

            if (!snapshot.isEmpty()) {

                System.out.println(
                        "RESIDENT FOUND: "
                                + normalizedEmail
                );

                return true;
            }

            /*
             * Extra check:
             *
             * If your Residents documents use EMAIL
             * itself as document ID, this also supports it.
             */

            DocumentSnapshot directDocument =
                    firestore
                            .collection(RESIDENT_COLLECTION)
                            .document(normalizedEmail)
                            .get()
                            .get();

            if (directDocument.exists()) {

                System.out.println(
                        "RESIDENT FOUND BY DOCUMENT ID: "
                                + normalizedEmail
                );

                return true;
            }

            System.out.println(
                    "RESIDENT NOT FOUND: "
                            + normalizedEmail
            );

            return false;

        } catch (Exception e) {

            System.out.println(
                    "ERROR checking resident."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // ADD / SAVE COMPLAINT
    // =====================================================

    public boolean addComplaint(
            String email,
            Complaint complaint) {

        try {

            // =================================================
            // EMAIL
            // =================================================

            String normalizedEmail =
                    normalizeEmail(email);

            if (normalizedEmail.isEmpty()) {

                System.out.println(
                        "Complaint save failed: email empty."
                );

                return false;
            }

            // =================================================
            // COMPLAINT
            // =================================================

            if (complaint == null) {

                System.out.println(
                        "Complaint save failed: complaint null."
                );

                return false;
            }

            // =================================================
            // CHECK RESIDENT FIRST
            // =================================================

            System.out.println(
                    "Checking whether resident belongs to society..."
            );

            boolean residentFound =
                    residentExists(normalizedEmail);

            if (!residentFound) {

                System.out.println(
                        "Complaint NOT saved."
                );

                System.out.println(
                        "Resident does not exist in society."
                );

                System.out.println(
                        "Email = "
                                + normalizedEmail
                );

                return false;
            }

            // =================================================
            // SET EMAIL
            // =================================================

            complaint.setEmail(
                    normalizedEmail
            );

            // =================================================
            // STATUS
            // =================================================

            String status =
                    complaint.getStatus();

            if (status == null ||
                    status.trim().isEmpty()) {

                status = "IN PROGRESS";

            } else {

                status =
                        normalizeStatus(status);
            }

            complaint.setStatus(status);

            // =================================================
            // CREATE DOCUMENT
            // =================================================

            CollectionReference collection =
                    getComplaintCollection(
                            normalizedEmail
                    );

            if (collection == null) {
                return false;
            }

            DocumentReference document;

            String complaintId =
                    complaint.getId();

            if (complaintId == null ||
                    complaintId.trim().isEmpty()) {

                document =
                        collection.document();

                complaintId =
                        document.getId();

                complaint.setId(
                        complaintId
                );

            } else {

                complaintId =
                        complaintId.trim();

                document =
                        collection.document(
                                complaintId
                        );
            }

            // =================================================
            // FIRESTORE DATA
            // =================================================

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "id",
                    complaintId
            );

            data.put(
                    "email",
                    normalizedEmail
            );

            data.put(
                    "title",
                    complaint.getTitle()
            );

            data.put(
                    "category",
                    complaint.getCategory()
            );

            data.put(
                    "description",
                    complaint.getDescription()
            );

            data.put(
                    "flatNumber",
                    complaint.getFlatNumber()
            );

            data.put(
                    "preferredDate",
                    complaint.getPreferredDate()
            );

            data.put(
                    "status",
                    status
            );

            data.put(
                    "imageFileName",
                    complaint.getImageFileName()
            );

            data.put(
                    "createdAt",
                    FieldValue.serverTimestamp()
            );

            // =================================================
            // SAVE
            // =================================================

            document
                    .set(data)
                    .get();

            System.out.println("====================================");
            System.out.println("COMPLAINT SAVED SUCCESSFULLY");
            System.out.println("Email = " + normalizedEmail);
            System.out.println("Complaint ID = " + complaintId);
            System.out.println("Status = " + status);
            System.out.println("====================================");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "ERROR saving complaint."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // FETCH COMPLAINTS BY EMAIL
    // =====================================================

    public List<Complaint> getComplaintsByEmail(
            String email) {

        List<Complaint> complaints =
                new ArrayList<>();

        try {

            String normalizedEmail =
                    normalizeEmail(email);

            if (normalizedEmail.isEmpty()) {

                System.out.println(
                        "Fetch failed: email empty."
                );

                return complaints;
            }

            System.out.println("====================================");
            System.out.println("FETCHING COMPLAINTS");
            System.out.println(
                    "Email = " + normalizedEmail
            );
            System.out.println("====================================");

            CollectionReference collection =
                    getComplaintCollection(
                            normalizedEmail
                    );

            if (collection == null) {
                return complaints;
            }

            QuerySnapshot snapshot =
                    collection
                            .get()
                            .get();

            for (QueryDocumentSnapshot document :
                    snapshot.getDocuments()) {

                Complaint complaint =
                        convertDocumentToComplaint(
                                document
                        );

                if (complaint != null) {

                    /*
                     * Always use requested email.
                     * This guarantees email based fetching.
                     */

                    complaint.setEmail(
                            normalizedEmail
                    );

                    complaints.add(
                            complaint
                    );
                }
            }

            System.out.println(
                    "FETCH COMPLETE"
            );

            System.out.println(
                    "Email = "
                            + normalizedEmail
            );

            System.out.println(
                    "Total = "
                            + complaints.size()
            );

            return complaints;

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching complaints by email."
            );

            e.printStackTrace();

            return complaints;
        }
    }

    // =====================================================
    // FETCH BY EMAIL + STATUS
    // =====================================================

    public List<Complaint>
    getComplaintsByEmailAndStatus(
            String email,
            String status) {

        List<Complaint> complaints =
                new ArrayList<>();

        try {

            String normalizedEmail =
                    normalizeEmail(email);

            String normalizedStatus =
                    normalizeStatus(status);

            if (normalizedEmail.isEmpty()) {
                return complaints;
            }

            System.out.println("====================================");
            System.out.println(
                    "FETCH BY EMAIL + STATUS"
            );
            System.out.println(
                    "Email = " + normalizedEmail
            );
            System.out.println(
                    "Status = " + normalizedStatus
            );
            System.out.println("====================================");

            /*
             * FIRST FETCH USING EMAIL.
             *
             * This is intentional.
             * We don't directly depend on Firestore
             * compound indexes.
             */

            List<Complaint> all =
                    getComplaintsByEmail(
                            normalizedEmail
                    );

            // =================================================
            // FILTER STATUS
            // =================================================

            for (Complaint complaint : all) {

                if (complaint == null) {
                    continue;
                }

                String complaintStatus =
                        normalizeStatus(
                                complaint.getStatus()
                        );

                if (complaintStatus.equals(
                        normalizedStatus)) {

                    complaints.add(
                            complaint
                    );
                }
            }

            System.out.println(
                    "Filtered complaints = "
                            + complaints.size()
            );

            return complaints;

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching complaints by email/status."
            );

            e.printStackTrace();

            return complaints;
        }
    }

    // =====================================================
    // GET ALL COMPLAINTS
    // =====================================================

    public List<Complaint> getAllComplaints() {

        List<Complaint> allComplaints =
                new ArrayList<>();

        try {

            Iterable<DocumentReference>
                    emailDocuments =
                    firestore
                            .collection(
                                    COMPLAINT_COLLECTION
                            )
                            .listDocuments();

            for (DocumentReference emailDocument :
                    emailDocuments) {

                String email =
                        normalizeEmail(
                                emailDocument.getId()
                        );

                if (email.isEmpty()) {
                    continue;
                }

                CollectionReference collection =
                        emailDocument
                                .collection("complaints");

                QuerySnapshot snapshot =
                        collection
                                .get()
                                .get();

                for (QueryDocumentSnapshot document :
                        snapshot.getDocuments()) {

                    Complaint complaint =
                            convertDocumentToComplaint(
                                    document
                            );

                    if (complaint != null) {

                        complaint.setEmail(email);

                        allComplaints.add(
                                complaint
                        );
                    }
                }
            }

            System.out.println(
                    "Total ALL complaints = "
                            + allComplaints.size()
            );

            return allComplaints;

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching all complaints."
            );

            e.printStackTrace();

            return allComplaints;
        }
    }

    // =====================================================
    // GET ALL COMPLAINTS BY STATUS
    // =====================================================

    public List<Complaint>
    getAllComplaintsByStatus(
            String status) {

        List<Complaint> result =
                new ArrayList<>();

        String requiredStatus =
                normalizeStatus(status);

        List<Complaint> all =
                getAllComplaints();

        for (Complaint complaint : all) {

            if (complaint == null) {
                continue;
            }

            String complaintStatus =
                    normalizeStatus(
                            complaint.getStatus()
                    );

            if (complaintStatus.equals(
                    requiredStatus)) {

                result.add(
                        complaint
                );
            }
        }

        return result;
    }

    // =====================================================
    // UPDATE STATUS
    // =====================================================

    public boolean updateComplaintStatus(
            String email,
            String complaintId,
            String newStatus) {

        try {

            String normalizedEmail =
                    normalizeEmail(email);

            if (normalizedEmail.isEmpty()) {

                System.out.println(
                        "Update failed: email empty."
                );

                return false;
            }

            if (complaintId == null ||
                    complaintId.trim().isEmpty()) {

                System.out.println(
                        "Update failed: complaint ID empty."
                );

                return false;
            }

            String normalizedId =
                    complaintId.trim();

            String normalizedStatus =
                    normalizeStatus(newStatus);

            // =================================================
            // RESIDENT CHECK
            // =================================================

            if (!residentExists(
                    normalizedEmail)) {

                System.out.println(
                        "Update failed: resident not found."
                );

                return false;
            }

            // =================================================
            // DOCUMENT PATH
            // =================================================

            DocumentReference document =
                    firestore
                            .collection(
                                    COMPLAINT_COLLECTION
                            )
                            .document(
                                    normalizedEmail
                            )
                            .collection(
                                    "complaints"
                            )
                            .document(
                                    normalizedId
                            );

            // =================================================
            // CHECK DOCUMENT
            // =================================================

            DocumentSnapshot snapshot =
                    document
                            .get()
                            .get();

            if (!snapshot.exists()) {

                System.out.println(
                        "Complaint not found."
                );

                System.out.println(
                        "Email = "
                                + normalizedEmail
                );

                System.out.println(
                        "ID = "
                                + normalizedId
                );

                return false;
            }

            // =================================================
            // UPDATE
            // =================================================

            Map<String, Object> update =
                    new HashMap<>();

            update.put(
                    "status",
                    normalizedStatus
            );

            update.put(
                    "email",
                    normalizedEmail
            );

            update.put(
                    "id",
                    normalizedId
            );

            document
                    .update(update)
                    .get();

            System.out.println("====================================");
            System.out.println(
                    "COMPLAINT STATUS UPDATED"
            );
            System.out.println(
                    "Email = " + normalizedEmail
            );
            System.out.println(
                    "Complaint ID = " + normalizedId
            );
            System.out.println(
                    "Status = " + normalizedStatus
            );
            System.out.println("====================================");

            return true;

        } catch (Exception e) {

            System.out.println(
                    "ERROR updating complaint."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET COMPLAINT BY EMAIL + ID
    // =====================================================

    public Complaint getComplaintByEmailAndId(
            String email,
            String complaintId) {

        try {

            String normalizedEmail =
                    normalizeEmail(email);

            if (normalizedEmail.isEmpty() ||
                    complaintId == null ||
                    complaintId.trim().isEmpty()) {

                return null;
            }

            DocumentSnapshot document =
                    firestore
                            .collection(
                                    COMPLAINT_COLLECTION
                            )
                            .document(
                                    normalizedEmail
                            )
                            .collection(
                                    "complaints"
                            )
                            .document(
                                    complaintId.trim()
                            )
                            .get()
                            .get();

            if (!document.exists()) {
                return null;
            }

            Complaint complaint =
                    convertDocumentToComplaint(
                            document
                    );

            if (complaint != null) {

                complaint.setEmail(
                        normalizedEmail
                );
            }

            return complaint;

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching complaint by email/id."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // CONVERT DOCUMENT
    // =====================================================

    private Complaint convertDocumentToComplaint(
            DocumentSnapshot document) {

        try {

            Complaint complaint =
                    new Complaint();

            // ID

            String id =
                    document.getString("id");

            if (id == null ||
                    id.trim().isEmpty()) {

                id =
                        document.getId();
            }

            complaint.setId(id);

            // EMAIL

            complaint.setEmail(
                    document.getString("email")
            );

            // TITLE

            complaint.setTitle(
                    document.getString("title")
            );

            // CATEGORY

            complaint.setCategory(
                    document.getString("category")
            );

            // DESCRIPTION

            complaint.setDescription(
                    document.getString("description")
            );

            // FLAT

            complaint.setFlatNumber(
                    document.getString("flatNumber")
            );

            // DATE

            complaint.setPreferredDate(
                    document.getString("preferredDate")
            );

            // STATUS

            complaint.setStatus(
                    normalizeStatus(
                            document.getString("status")
                    )
            );

            // IMAGE

            complaint.setImageFileName(
                    document.getString(
                            "imageFileName"
                    )
            );

            return complaint;

        } catch (Exception e) {

            System.out.println(
                    "ERROR converting complaint document."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // STATUS NORMALIZER
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

        if (value.equals("OPEN")) {

            return "IN PROGRESS";
        }

        if (value.equals("IN PROGRESS")) {

            return "IN PROGRESS";
        }

        if (value.equals("RESOLVED")) {

            return "RESOLVED";
        }

        if (value.equals("CLOSED")) {

            return "CLOSED";
        }

        return "IN PROGRESS";
    }

    // =====================================================
    // IN PROGRESS
    // =====================================================

    public List<Complaint>
    getInProgressComplaints() {

        return getAllComplaintsByStatus(
                "IN PROGRESS"
        );
    }

    // =====================================================
    // RESOLVED
    // =====================================================

    public List<Complaint>
    getResolvedComplaints() {

        return getAllComplaintsByStatus(
                "RESOLVED"
        );
    }

    // =====================================================
    // CLOSED
    // =====================================================

    public List<Complaint>
    getClosedComplaints() {

        return getAllComplaintsByStatus(
                "CLOSED"
        );
    }
}