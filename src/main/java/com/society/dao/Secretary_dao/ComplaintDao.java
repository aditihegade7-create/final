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

    private static final String SECRETARY_COLLECTION =
            "Secretaries";

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
    // NORMALIZE SOCIETY
    // =====================================================

    private String normalizeSociety(String society) {

        if (society == null) {
            return "";
        }

        return society.trim();
    }

    // =====================================================
    // GET COMPLAINT COLLECTION BY EMAIL
    // =====================================================

    private CollectionReference getComplaintCollection(
            String email) {

        String normalizedEmail =
                normalizeEmail(email);

        if (normalizedEmail.isEmpty()) {
            return null;
        }

        return firestore
                .collection(COMPLAINT_COLLECTION)
                .document(normalizedEmail)
                .collection("complaints");
    }

    // =====================================================
    // GET RESIDENT SOCIETY BY EMAIL
    // =====================================================
    //
    // Residents collection:
    //
    // Residents
    //    └── document
    //          ├── email
    //          └── society
    //
    // Email hardcoded नाही.
    //
    // =====================================================

    public String getResidentSociety(String email) {

        try {

            String normalizedEmail =
                    normalizeEmail(email);

            if (normalizedEmail.isEmpty()) {

                System.out.println(
                        "Resident society check failed: email empty."
                );

                return null;
            }

            System.out.println("====================================");
            System.out.println("GETTING RESIDENT SOCIETY");
            System.out.println(
                    "Resident Email = "
                            + normalizedEmail
            );
            System.out.println("====================================");

            // -------------------------------------------------
            // FIRST: email FIELD
            // -------------------------------------------------

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

                DocumentSnapshot document =
                        snapshot.getDocuments().get(0);

                String society =
                        document.getString("society");

                if (society != null &&
                        !society.trim().isEmpty()) {

                    System.out.println(
                            "Resident Society = "
                                    + society
                    );

                    return society.trim();
                }
            }

            // -------------------------------------------------
            // SECOND: EMAIL AS DOCUMENT ID
            // -------------------------------------------------

            DocumentSnapshot directDocument =
                    firestore
                            .collection(RESIDENT_COLLECTION)
                            .document(normalizedEmail)
                            .get()
                            .get();

            if (directDocument.exists()) {

                String society =
                        directDocument.getString("society");

                if (society != null &&
                        !society.trim().isEmpty()) {

                    System.out.println(
                            "Resident Society by Document ID = "
                                    + society
                    );

                    return society.trim();
                }
            }

            System.out.println(
                    "Resident society NOT FOUND for "
                            + normalizedEmail
            );

            return null;

        } catch (Exception e) {

            System.err.println(
                    "ERROR GETTING RESIDENT SOCIETY"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // CHECK RESIDENT EXISTS
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

                return true;
            }

            DocumentSnapshot directDocument =
                    firestore
                            .collection(RESIDENT_COLLECTION)
                            .document(normalizedEmail)
                            .get()
                            .get();

            return directDocument.exists();

        } catch (Exception e) {

            System.err.println(
                    "ERROR CHECKING RESIDENT"
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

            String normalizedEmail =
                    normalizeEmail(email);

            if (normalizedEmail.isEmpty()) {

                System.out.println(
                        "Complaint save failed: email empty."
                );

                return false;
            }

            if (complaint == null) {

                System.out.println(
                        "Complaint save failed: complaint null."
                );

                return false;
            }

            // -------------------------------------------------
            // RESIDENT CHECK
            // -------------------------------------------------

            if (!residentExists(
                    normalizedEmail)) {

                System.out.println(
                        "Complaint NOT saved."
                );

                System.out.println(
                        "Resident does not exist."
                );

                return false;
            }

            // -------------------------------------------------
            // GET RESIDENT SOCIETY
            // -------------------------------------------------

            String residentSociety =
                    getResidentSociety(
                            normalizedEmail
                    );

            if (residentSociety == null ||
                    residentSociety.trim().isEmpty()) {

                System.out.println(
                        "Complaint NOT saved."
                );

                System.out.println(
                        "Resident society not found."
                );

                return false;
            }

            residentSociety =
                    normalizeSociety(
                            residentSociety
                    );

            // -------------------------------------------------
            // SET EMAIL
            // -------------------------------------------------

            complaint.setEmail(
                    normalizedEmail
            );

            // -------------------------------------------------
            // SET SOCIETY
            // -------------------------------------------------

            complaint.setSociety(
                    residentSociety
            );

            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            String status =
                    complaint.getStatus();

            if (status == null ||
                    status.trim().isEmpty()) {

                status = "IN PROGRESS";

            } else {

                status =
                        normalizeStatus(
                                status
                        );
            }

            complaint.setStatus(status);

            // -------------------------------------------------
            // GET COLLECTION
            // -------------------------------------------------

            CollectionReference collection =
                    getComplaintCollection(
                            normalizedEmail
                    );

            if (collection == null) {
                return false;
            }

            // -------------------------------------------------
            // CREATE DOCUMENT
            // -------------------------------------------------

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

            // -------------------------------------------------
            // FIRESTORE DATA
            // -------------------------------------------------

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
                    "society",
                    residentSociety
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

            // -------------------------------------------------
            // SAVE
            // -------------------------------------------------

            document
                    .set(data)
                    .get();

            System.out.println("====================================");
            System.out.println(
                    "COMPLAINT SAVED SUCCESSFULLY"
            );
            System.out.println(
                    "Email = "
                            + normalizedEmail
            );
            System.out.println(
                    "Society = "
                            + residentSociety
            );
            System.out.println(
                    "Complaint ID = "
                            + complaintId
            );
            System.out.println(
                    "Status = "
                            + status
            );
            System.out.println("====================================");

            return true;

        } catch (Exception e) {

            System.err.println(
                    "ERROR SAVING COMPLAINT"
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

            System.out.println("====================================");
            System.out.println(
                    "FETCHING COMPLAINTS BY EMAIL"
            );
            System.out.println(
                    "Email = "
                            + normalizedEmail
            );
            System.out.println(
                    "Total = "
                            + snapshot.size()
            );
            System.out.println("====================================");

            for (QueryDocumentSnapshot document :
                    snapshot.getDocuments()) {

                Complaint complaint =
                        convertDocumentToComplaint(
                                document
                        );

                if (complaint != null) {

                    complaint.setEmail(
                            normalizedEmail
                    );

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
    // FETCH COMPLAINTS BY EMAIL + SOCIETY
    // =====================================================
    //
    // This is the important method for Secretary.
    //
    // Email:
    //     dynamically received
    //
    // Society:
    //     dynamically received from Secretary
    //
    // =====================================================

    public List<Complaint> getComplaintsByEmailAndSociety(
            String email,
            String society) {

        List<Complaint> complaints =
                new ArrayList<>();

        try {

            String normalizedEmail =
                    normalizeEmail(email);

            String normalizedSociety =
                    normalizeSociety(society);

            if (normalizedEmail.isEmpty()) {

                System.out.println(
                        "Fetch failed: email empty."
                );

                return complaints;
            }

            if (normalizedSociety.isEmpty()) {

                System.out.println(
                        "Fetch failed: society empty."
                );

                return complaints;
            }

            System.out.println("====================================");
            System.out.println(
                    "FETCH COMPLAINTS BY EMAIL + SOCIETY"
            );
            System.out.println(
                    "Email = "
                            + normalizedEmail
            );
            System.out.println(
                    "Society = "
                            + normalizedSociety
            );
            System.out.println("====================================");

            // -------------------------------------------------
            // FETCH USING EMAIL
            // -------------------------------------------------

            List<Complaint> emailComplaints =
                    getComplaintsByEmail(
                            normalizedEmail
                    );

            // -------------------------------------------------
            // SOCIETY FILTER
            // -------------------------------------------------

            for (Complaint complaint :
                    emailComplaints) {

                if (complaint == null) {
                    continue;
                }

                String complaintSociety =
                        normalizeSociety(
                                complaint.getSociety()
                        );

                if (complaintSociety.isEmpty()) {

                    continue;
                }

                if (complaintSociety.equalsIgnoreCase(
                        normalizedSociety
                )) {

                    complaints.add(
                            complaint
                    );
                }
            }

            System.out.println(
                    "Matching complaints = "
                            + complaints.size()
            );

            return complaints;

        } catch (Exception e) {

            System.err.println(
                    "ERROR FETCHING EMAIL + SOCIETY COMPLAINTS"
            );

            e.printStackTrace();

            return complaints;
        }
    }

    // =====================================================
    // FETCH BY EMAIL + SOCIETY + STATUS
    // =====================================================

    public List<Complaint>
    getComplaintsByEmailSocietyAndStatus(
            String email,
            String society,
            String status) {

        List<Complaint> result =
                new ArrayList<>();

        String requiredStatus =
                normalizeStatus(status);

        List<Complaint> complaints =
                getComplaintsByEmailAndSociety(
                        email,
                        society
                );

        for (Complaint complaint :
                complaints) {

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

                result.add(
                        complaint
                );
            }
        }

        return result;
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

            return allComplaints;

        } catch (Exception e) {

            System.err.println(
                    "ERROR FETCHING ALL COMPLAINTS"
            );

            e.printStackTrace();

            return allComplaints;
        }
    }

    // =====================================================
    // GET ALL COMPLAINTS BY SOCIETY
    // =====================================================

    public List<Complaint> getAllComplaintsBySociety(
            String society) {

        List<Complaint> result =
                new ArrayList<>();

        String normalizedSociety =
                normalizeSociety(society);

        if (normalizedSociety.isEmpty()) {
            return result;
        }

        List<Complaint> all =
                getAllComplaints();

        for (Complaint complaint :
                all) {

            if (complaint == null) {
                continue;
            }

            String complaintSociety =
                    normalizeSociety(
                            complaint.getSociety()
                    );

            if (complaintSociety.equalsIgnoreCase(
                    normalizedSociety
            )) {

                result.add(
                        complaint
                );
            }
        }

        return result;
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

        for (Complaint complaint :
                all) {

            if (complaint == null) {
                continue;
            }

            String complaintStatus =
                    normalizeStatus(
                            complaint.getStatus()
                    );

            if (complaintStatus.equals(
                    requiredStatus
            )) {

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

                return false;
            }

            if (complaintId == null ||
                    complaintId.trim().isEmpty()) {

                return false;
            }

            String normalizedId =
                    complaintId.trim();

            String normalizedStatus =
                    normalizeStatus(
                            newStatus
                    );

            // -------------------------------------------------
            // DOCUMENT
            // -------------------------------------------------

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

            DocumentSnapshot snapshot =
                    document
                            .get()
                            .get();

            if (!snapshot.exists()) {

                System.out.println(
                        "Complaint not found."
                );

                return false;
            }

            // -------------------------------------------------
            // UPDATE
            // -------------------------------------------------

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

            System.out.println(
                    "Complaint status updated successfully."
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "ERROR UPDATING COMPLAINT"
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

            System.err.println(
                    "ERROR FETCHING COMPLAINT BY EMAIL/ID"
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

            if (document == null ||
                    !document.exists()) {

                return null;
            }

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

            // SOCIETY
            complaint.setSociety(
                    document.getString("society")
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

            System.err.println(
                    "ERROR CONVERTING COMPLAINT"
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