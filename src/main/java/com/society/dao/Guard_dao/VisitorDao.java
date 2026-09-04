package com.society.dao.Guard_dao;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.SetOptions;

import com.society.config.FirebaseConfig;
import com.society.model.Guard_model.VisitorModel;

public class VisitorDao {

    private final Firestore db;

    private static final String COLLECTION = "visitors";

    private static final String VISITOR_RECORDS = "visitor_records";


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VisitorDao() {

        db = FirebaseConfig.getFirestore();
    }


    // =====================================================
    // GET ALL VISITORS
    //
    // Reads:
    //
    // visitors/{email}/visitor_records/{visitorId}
    //
    // and old:
    //
    // visitors/{visitorId}
    //
    // =====================================================

    public List<VisitorModel> getAllVisitors() {

        List<VisitorModel> visitors =
                new ArrayList<>();

        try {

            if (db == null) {

                System.out.println(
                        "ERROR: Firestore is null."
                );

                return visitors;
            }


            // =================================================
            // NEW NESTED STRUCTURE
            //
            // visitors/{email}/visitor_records/{visitorId}
            // =================================================

            QuerySnapshot nestedSnapshot =
                    db.collectionGroup(
                            VISITOR_RECORDS
                    )
                    .get()
                    .get();


            for (QueryDocumentSnapshot document :
                    nestedSnapshot.getDocuments()) {

                VisitorModel visitor =
                        mapVisitorDocument(document);


                if (visitor == null) {
                    continue;
                }


                if (!containsVisitorId(
                        visitors,
                        visitor.getVisitorId())) {

                    visitors.add(visitor);
                }
            }


            // =================================================
            // OLD ROOT-LEVEL STRUCTURE
            //
            // visitors/{visitorId}
            // =================================================

            QuerySnapshot legacySnapshot =
                    db.collection(COLLECTION)
                            .get()
                            .get();


            for (QueryDocumentSnapshot document :
                    legacySnapshot.getDocuments()) {

                /*
                 * Parent document such as:
                 *
                 * visitors/vaishnavi@gmail.com
                 *
                 * is not a visitor record.
                 */

                if (document.contains("email")
                        && document.getData().size() <= 1) {

                    continue;
                }


                VisitorModel visitor =
                        mapVisitorDocument(document);


                if (visitor == null) {
                    continue;
                }


                if (!containsVisitorId(
                        visitors,
                        visitor.getVisitorId())) {

                    visitors.add(visitor);
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch visitors."
            );

            e.printStackTrace();
        }


        return visitors;
    }


    // =====================================================
    // MAP FIRESTORE DOCUMENT TO VISITORMODEL
    //
    // Supports:
    //
    // APPROVED VISITOR:
    // visitorName
    // phoneNumber
    // flatNumber
    // qrToken
    // gate
    // visitDate
    // visitTime
    // vehicleNumber
    // used
    //
    // GUARD LOG:
    // name
    // phone
    // flat
    // qrPass
    // etc.
    // =====================================================

    private VisitorModel mapVisitorDocument(
            QueryDocumentSnapshot document) {

        try {

            if (document == null) {
                return null;
            }


            VisitorModel visitor =
                    new VisitorModel();

            // society is stored directly in the visitor record.
            visitor.setSociety(
                    document.getString("society")
            );

            // societyName is stored directly in Firestore for
            // society-based Secretary filtering. If VisitorModel
            // contains a societyName property, it can also be
            // mapped here.


            // =================================================
            // GET IDs
            // =================================================

            String documentId =
                    document.getId();

            String firestoreId =
                    document.getString("id");

            String firestoreVisitorId =
                    document.getString("visitorId");


            /*
             * Priority:
             *
             * 1. Firestore "id"
             * 2. Firestore "visitorId"
             * 3. Firestore document ID
             */

            if (firestoreId != null
                    && !firestoreId.trim().isEmpty()) {

                visitor.setVisitorId(
                        firestoreId.trim()
                );

            } else if (firestoreVisitorId != null
                    && !firestoreVisitorId.trim().isEmpty()) {

                visitor.setVisitorId(
                        firestoreVisitorId.trim()
                );

            } else {

                visitor.setVisitorId(
                        documentId
                );
            }


            // =================================================
            // DETERMINE DOCUMENT TYPE
            // =================================================

            boolean isApprovedVisitor =
                    document.contains("visitorName")
                    || document.contains("phoneNumber")
                    || document.contains("flatNumber")
                    || document.contains("qrToken");


            // =================================================
            // APPROVED VISITOR
            // =================================================

            if (isApprovedVisitor) {

                // visitorName -> name
                visitor.setName(
                        document.getString(
                                "visitorName"
                        )
                );


                // phoneNumber -> phone
                visitor.setPhone(
                        document.getString(
                                "phoneNumber"
                        )
                );


                // flatNumber -> flat
                visitor.setFlat(
                        document.getString(
                                "flatNumber"
                        )
                );


                // purpose
                visitor.setPurpose(
                        document.getString(
                                "purpose"
                        )
                );


                // qrToken -> qrPass
                visitor.setQrPass(
                        document.getString(
                                "qrToken"
                        )
                );


                // status
                visitor.setStatus(
                        document.getString(
                                "status"
                        )
                );


                // gate
                visitor.setGate(
                        document.getString(
                                "gate"
                        )
                );


                // visitDate
                visitor.setVisitDate(
                        document.getString(
                                "visitDate"
                        )
                );


                // visitTime
                visitor.setVisitTime(
                        document.getString(
                                "visitTime"
                        )
                );


                // vehicleNumber
                visitor.setVehicleNumber(
                        document.getString(
                                "vehicleNumber"
                        )
                );


                // used
                visitor.setUsed(
                        document.getBoolean(
                                "used"
                        )
                );


                // createdAt
                Timestamp timestamp =
                        document.getTimestamp(
                                "createdAt"
                        );


                if (timestamp != null) {

                    visitor.setCreatedAt(
                            timestamp.toString()
                    );
                }


                // Entry time after admission
                visitor.setEntryTime(
                        document.getString(
                                "entryTime"
                        )
                );


                // Exit time after checkout
                visitor.setExitTime(
                        document.getString(
                                "exitTime"
                        )
                );


                // Optional remarks
                visitor.setRemarks(
                        document.getString(
                                "remarks"
                        )
                );


                // Guard ID after admission
                visitor.setGuardId(
                        document.getString(
                                "guardId"
                        )
                );


                // Guard email after admission
                visitor.setGuardEmail(
                        document.getString(
                                "guardEmail"
                        )
                );
            }


            // =================================================
            // GUARD / LEGACY VISITOR
            // =================================================

            else {

                visitor.setName(
                        document.getString(
                                "name"
                        )
                );


                visitor.setPhone(
                        document.getString(
                                "phone"
                        )
                );


                visitor.setFlat(
                        document.getString(
                                "flat"
                        )
                );


                visitor.setPurpose(
                        document.getString(
                                "purpose"
                        )
                );


                visitor.setRemarks(
                        document.getString(
                                "remarks"
                        )
                );


                visitor.setEntryTime(
                        document.getString(
                                "entryTime"
                        )
                );


                visitor.setExitTime(
                        document.getString(
                                "exitTime"
                        )
                );


                visitor.setQrPass(
                        document.getString(
                                "qrPass"
                        )
                );


                visitor.setStatus(
                        document.getString(
                                "status"
                        )
                );


                visitor.setGuardId(
                        document.getString(
                                "guardId"
                        )
                );


                visitor.setEmail(
                        document.getString(
                                "email"
                        )
                );


                visitor.setGuardEmail(
                        document.getString(
                                "guardEmail"
                        )
                );


                visitor.setGate(
                        document.getString(
                                "gate"
                        )
                );


                visitor.setVisitDate(
                        document.getString(
                                "visitDate"
                        )
                );


                visitor.setVisitTime(
                        document.getString(
                                "visitTime"
                        )
                );


                visitor.setVehicleNumber(
                        document.getString(
                                "vehicleNumber"
                        )
                );


                visitor.setUsed(
                        document.getBoolean(
                                "used"
                        )
                );


                Timestamp timestamp =
                        document.getTimestamp(
                                "createdAt"
                        );


                if (timestamp != null) {

                    visitor.setCreatedAt(
                            timestamp.toString()
                    );
                }
            }


            // =================================================
            // RECOVER CREATOR EMAIL
            //
            // visitors/{email}/visitor_records/{id}
            // =================================================

            if (visitor.getEmail() == null
                    || visitor.getEmail()
                            .trim()
                            .isEmpty()) {

                DocumentReference parent =
                        document.getReference()
                                .getParent()
                                .getParent();


                if (parent != null) {

                    visitor.setEmail(
                            parent.getId()
                    );
                }
            }


            return visitor;


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to map visitor document: "
                            + document.getId()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =====================================================
    // ADD NEW VISITOR
    //
    // Saves:
    //
    // visitors/{email}/visitor_records/{visitorId}
    //
    // =====================================================

    public boolean addVisitor(
            VisitorModel visitor) {

        if (visitor == null) {

            System.out.println(
                    "ERROR: Visitor is null."
            );

            return false;
        }

        if (visitor.getSociety() == null
                || visitor.getSociety().trim().isEmpty()) {

            System.out.println(
                    "ERROR: Society name is missing."
            );

            return false;
        }

        return addVisitor(
                visitor,
                visitor.getSociety()
        );
    }

    // =====================================================
    // ADD NEW VISITOR WITH SOCIETY
    //
    // Saves:
    //
    // visitors/{email}/visitor_records/{visitorId}
    //
    // and stores societyName in BOTH:
    // 1. the parent email document
    // 2. the individual visitor record
    //
    // =====================================================

    public boolean addVisitor(
            VisitorModel visitor,
            String society) {

        try {

            if (db == null) {

                System.out.println(
                        "ERROR: Firestore is null."
                );

                return false;
            }


            if (visitor == null) {

                System.out.println(
                        "ERROR: Visitor is null."
                );

                return false;
            }

            // =================================================
            // VALIDATE SOCIETY NAME
            // =================================================

            if (society == null
                    || society.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Society name is missing."
                );

                return false;
            }

            String normalizedSociety =
                    society.trim();


            String email =
                    visitor.getEmail();


            if (email == null
                    || email.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Visitor creator email is missing."
                );

                return false;
            }


            email =
                    email.trim()
                            .toLowerCase();


            visitor.setEmail(email);


            // =================================================
            // PARENT DOCUMENT
            // =================================================

            DocumentReference emailDocument =
                    db.collection(COLLECTION)
                            .document(email);


            // =================================================
            // CREATE / UPDATE EMAIL DOCUMENT
            // =================================================

            Map<String, Object> emailData =
                    new HashMap<>();


            emailData.put(
                    "email",
                    email
            );

            emailData.put(
                    "society",
                    normalizedSociety
            );


            emailDocument
                    .set(
                            emailData,
                            SetOptions.merge()
                    )
                    .get();


            // =================================================
            // CREATE VISITOR DOCUMENT
            // =================================================

            DocumentReference visitorDocument =
                    emailDocument
                            .collection(
                                    VISITOR_RECORDS
                            )
                            .document();


            String visitorId =
                    visitorDocument.getId();


            visitor.setVisitorId(
                    visitorId
            );


            Map<String, Object> visitorData =
                    new HashMap<>();

            visitorData.put(
                    "visitorId",
                    visitorId
            );

            visitorData.put(
                    "society",
                    normalizedSociety
            );

            visitorData.put(
                    "email",
                    email
            );

            visitorData.put(
                    "name",
                    visitor.getName()
            );

            visitorData.put(
                    "phone",
                    visitor.getPhone()
            );

            visitorData.put(
                    "flat",
                    visitor.getFlat()
            );

            visitorData.put(
                    "purpose",
                    visitor.getPurpose()
            );

            visitorData.put(
                    "remarks",
                    visitor.getRemarks() == null
                            ? ""
                            : visitor.getRemarks()
            );

            visitorData.put(
                    "entryTime",
                    visitor.getEntryTime()
            );

            visitorData.put(
                    "exitTime",
                    visitor.getExitTime()
            );

            visitorData.put(
                    "qrPass",
                    visitor.getQrPass()
            );

            visitorData.put(
                    "status",
                    visitor.getStatus()
            );

            visitorData.put(
                    "guardId",
                    visitor.getGuardId()
            );

            visitorData.put(
                    "guardEmail",
                    visitor.getGuardEmail()
            );

            visitorData.put(
                    "gate",
                    visitor.getGate()
            );

            visitorData.put(
                    "visitDate",
                    visitor.getVisitDate()
            );

            visitorData.put(
                    "visitTime",
                    visitor.getVisitTime()
            );

            visitorData.put(
                    "vehicleNumber",
                    visitor.getVehicleNumber()
            );

            visitorData.put(
                    "used",
                    visitor.getUsed()
            );

            visitorData.put(
                    "createdAt",
                    visitor.getCreatedAt()
            );

            visitorDocument
                    .set(visitorData)
                    .get();


            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "VISITOR SAVED TO FIRESTORE"
            );

            System.out.println(
                    "Creator Email: "
                            + email
            );

            System.out.println(
                    "Society Name: "
                            + normalizedSociety
            );

            System.out.println(
                    "Visitor ID: "
                            + visitorId
            );

            System.out.println(
                    "Guard Email: "
                            + visitor.getGuardEmail()
            );

            System.out.println(
                    "======================================"
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to save visitor."
            );

            e.printStackTrace();

            return false;
        }
    }


    // =====================================================
    // CHECK OUT VISITOR
    //
    // Searches nested records first.
    // =====================================================

    // =====================================================
// CHECK OUT VISITOR
//
// Firestore structure:
//
// visitors/{residentEmail}/visitor_records/{documentId}
//
// This method DOES NOT use:
//
// whereEqualTo("id", visitorId)
// whereEqualTo("visitorId", visitorId)
//
// Therefore no collection-group index is required.
// =====================================================

public boolean checkOutVisitor(
        String visitorId) {

    try {

        if (db == null) {

            System.out.println(
                    "ERROR: Firestore is null."
            );

            return false;
        }


        // =================================================
        // VALIDATE VISITOR ID
        // =================================================

        if (visitorId == null
                || visitorId.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Visitor ID is missing."
            );

            return false;
        }


        String normalizedVisitorId =
                visitorId.trim();


        // =================================================
        // GENERATE EXIT TIME
        // =================================================

        String exitTime =
                LocalTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "hh:mm a"
                                )
                        );


        System.out.println(
                "======================================"
        );

        System.out.println(
                "TRYING TO CHECK OUT VISITOR"
        );

        System.out.println(
                "Visitor ID: "
                        + normalizedVisitorId
        );


        // =================================================
        // SEARCH ALL NESTED visitor_records
        //
        // NO whereEqualTo()
        // NO INDEX REQUIRED
        // =================================================

        QuerySnapshot snapshot =
                db.collectionGroup(
                        VISITOR_RECORDS
                )
                .get()
                .get();


        System.out.println(
                "visitor_records found: "
                        + snapshot.size()
        );


        // =================================================
        // SEARCH LOCALLY
        // =================================================

        for (QueryDocumentSnapshot document :
                snapshot.getDocuments()) {


            // =================================================
            // DOCUMENT ID
            // =================================================

            String documentId =
                    document.getId();


            // =================================================
            // FIRESTORE "id" FIELD
            // =================================================

            String firestoreId =
                    document.getString("id");


            // =================================================
            // FIRESTORE "visitorId" FIELD
            // =================================================

            String firestoreVisitorId =
                    document.getString("visitorId");


            boolean matches = false;


            // =================================================
            // MATCH 1
            //
            // Firestore id == visitorId
            // =================================================

            if (firestoreId != null
                    && firestoreId.trim()
                            .equals(normalizedVisitorId)) {

                matches = true;
            }


            // =================================================
            // MATCH 2
            //
            // Firestore visitorId == visitorId
            // =================================================

            if (!matches
                    && firestoreVisitorId != null
                    && firestoreVisitorId.trim()
                            .equals(normalizedVisitorId)) {

                matches = true;
            }


            // =================================================
            // MATCH 3
            //
            // Firestore document ID == visitorId
            // =================================================

            if (!matches
                    && documentId.equals(
                            normalizedVisitorId)) {

                matches = true;
            }


            // =================================================
            // VISITOR FOUND
            // =================================================

            if (matches) {

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "VISITOR FOUND"
                );

                System.out.println(
                        "Document ID: "
                                + documentId
                );

                System.out.println(
                        "Document Path: "
                                + document.getReference()
                                        .getPath()
                );

                System.out.println(
                        "Firestore id: "
                                + firestoreId
                );

                System.out.println(
                        "Firestore visitorId: "
                                + firestoreVisitorId
                );


                // =================================================
                // CHECK CURRENT STATUS
                // =================================================

                String currentStatus =
                        document.getString("status");


                System.out.println(
                        "Current Status: "
                                + currentStatus
                );


                if ("Checked Out".equalsIgnoreCase(
                        currentStatus)) {

                    System.out.println(
                            "Visitor has already checked out."
                    );

                    return false;
                }


                // =================================================
                // UPDATE ACTUAL FIRESTORE DOCUMENT
                //
                // document.getReference() points directly to:
                //
                // visitors/{email}/visitor_records/{documentId}
                //
                // =================================================

                System.out.println(
                        "Updating visitor..."
                );


                document.getReference()
                        .update(
                                "status",
                                "Checked Out",

                                "exitTime",
                                exitTime,

                                "used",
                                true
                        )
                        .get();


                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "CHECKOUT SUCCESSFUL"
                );

                System.out.println(
                        "Visitor ID: "
                                + normalizedVisitorId
                );

                System.out.println(
                        "Exit Time: "
                                + exitTime
                );

                System.out.println(
                        "Document Path: "
                                + document.getReference()
                                        .getPath()
                );

                System.out.println(
                        "======================================"
                );


                return true;
            }
        }


        // =================================================
        // LEGACY ROOT-LEVEL STRUCTURE
        //
        // visitors/{visitorId}
        // =================================================

        System.out.println(
                "Visitor not found in nested records."
        );

        System.out.println(
                "Checking legacy visitors collection..."
        );


        DocumentReference legacyDocument =
                db.collection(COLLECTION)
                        .document(
                                normalizedVisitorId
                        );


        if (legacyDocument.get().get().exists()) {

            legacyDocument
                    .update(
                            "status",
                            "Checked Out",

                            "exitTime",
                            exitTime,

                            "used",
                            true
                    )
                    .get();


            System.out.println(
                    "Legacy visitor checked out successfully: "
                            + normalizedVisitorId
            );


            return true;
        }


        // =================================================
        // VISITOR NOT FOUND
        // =================================================

        System.out.println(
                "======================================"
        );

        System.out.println(
                "VISITOR NOT FOUND"
        );

        System.out.println(
                "Visitor ID searched: "
                        + normalizedVisitorId
        );

        System.out.println(
                "======================================"
        );


        return false;


    } catch (Exception e) {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "CHECKOUT FAILED"
        );

        System.out.println(
                "Visitor ID: "
                        + visitorId
        );

        System.out.println(
                "Exception Type: "
                        + e.getClass().getName()
        );

        System.out.println(
                "Exception Message: "
                        + e.getMessage()
        );

        e.printStackTrace();

        System.out.println(
                "======================================"
        );


        return false;
    }
}


    // =====================================================
    // GET VISITORS BY GUARD EMAIL
    // =====================================================

    public List<VisitorModel> getVisitorsByGuardEmail(
            String guardEmail) {

        List<VisitorModel> visitors =
                new ArrayList<>();


        try {

            if (db == null) {

                System.out.println(
                        "ERROR: Firestore is null."
                );

                return visitors;
            }


            if (guardEmail == null
                    || guardEmail.trim().isEmpty()) {

                return visitors;
            }


            String normalizedGuardEmail =
                    guardEmail.trim()
                            .toLowerCase();


            // =================================================
            // NESTED STRUCTURE
            // =================================================

            QuerySnapshot nestedSnapshot =
                    db.collectionGroup(
                            VISITOR_RECORDS
                    )
                    .get()
                    .get();


            for (QueryDocumentSnapshot document :
                    nestedSnapshot.getDocuments()) {

                VisitorModel visitor =
                        mapVisitorDocument(document);


                if (visitor == null) {
                    continue;
                }


                String visitorGuardEmail =
                        visitor.getGuardEmail() == null
                                ? ""
                                : visitor.getGuardEmail()
                                        .trim()
                                        .toLowerCase();


                /*
                 * A visitor is included when this guard
                 * actually handled/admitted the visitor.
                 */

                if (normalizedGuardEmail.equals(
                        visitorGuardEmail)) {

                    if (!containsVisitorId(
                            visitors,
                            visitor.getVisitorId())) {

                        visitors.add(visitor);
                    }
                }
            }


            // =================================================
            // LEGACY ROOT STRUCTURE
            // =================================================

            QuerySnapshot legacySnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "guardEmail",
                                    normalizedGuardEmail
                            )
                            .get()
                            .get();


            for (QueryDocumentSnapshot document :
                    legacySnapshot.getDocuments()) {

                VisitorModel visitor =
                        mapVisitorDocument(document);


                if (visitor == null) {
                    continue;
                }


                if (!containsVisitorId(
                        visitors,
                        visitor.getVisitorId())) {

                    visitors.add(visitor);
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch visitors by guard email."
            );

            e.printStackTrace();
        }


        return visitors;
    }


    // =====================================================
    // ADMIT EXPECTED VISITOR
    // =====================================================
// ADMIT EXPECTED VISITOR
//
// Firestore structure:
//
// visitors/{residentEmail}/visitor_records/{documentId}
//
// Example:
//
// visitors/
//     vaishnavi@gmail.com/
//         visitor_records/
//             44fabc24-1e77-4a63-9c5b-416a1e13b17c
//
// This method DOES NOT use:
//
// whereEqualTo("id", visitorId)
//
// because that requires a collection-group index.
//
// Instead, it reads the collection group and compares
// the IDs locally.
// =====================================================

public boolean admitVisitor(
        String visitorId,
        String guardEmail) {

    try {

        if (db == null) {

            System.out.println(
                    "ERROR: Firestore is null."
            );

            return false;
        }


        // =================================================
        // VALIDATE VISITOR ID
        // =================================================

        if (visitorId == null
                || visitorId.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Visitor ID is missing."
            );

            return false;
        }


        // =================================================
        // VALIDATE GUARD EMAIL
        // =================================================

        if (guardEmail == null
                || guardEmail.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Guard email is missing."
            );

            return false;
        }


        String normalizedVisitorId =
                visitorId.trim();


        String normalizedGuardEmail =
                guardEmail.trim()
                        .toLowerCase();


        String entryTime =
                LocalTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "hh:mm a"
                                )
                        );


        System.out.println(
                "======================================"
        );

        System.out.println(
                "TRYING TO ADMIT VISITOR"
        );

        System.out.println(
                "Visitor ID: "
                        + normalizedVisitorId
        );

        System.out.println(
                "Guard Email: "
                        + normalizedGuardEmail
        );


        // =================================================
        // GET ALL visitor_records
        //
        // IMPORTANT:
        //
        // There is NO whereEqualTo() here.
        //
        // Therefore no collection-group index is required.
        // =================================================

        QuerySnapshot snapshot =
                db.collectionGroup(
                        VISITOR_RECORDS
                )
                .get()
                .get();


        System.out.println(
                "visitor_records found: "
                        + snapshot.size()
        );


        // =================================================
        // SEARCH LOCALLY
        // =================================================

        for (QueryDocumentSnapshot document :
                snapshot.getDocuments()) {


            // =================================================
            // DOCUMENT ID
            // =================================================

            String documentId =
                    document.getId();


            // =================================================
            // FIRESTORE "id" FIELD
            // =================================================

            String firestoreId =
                    document.getString("id");


            // =================================================
            // FIRESTORE "visitorId" FIELD
            // =================================================

            String firestoreVisitorId =
                    document.getString(
                            "visitorId"
                    );


            boolean matches = false;


            // =================================================
            // MATCH 1
            //
            // id == visitorId
            // =================================================

            if (firestoreId != null
                    && firestoreId
                            .trim()
                            .equals(
                                    normalizedVisitorId
                            )) {

                matches = true;
            }


            // =================================================
            // MATCH 2
            //
            // visitorId == visitorId
            // =================================================

            if (!matches
                    && firestoreVisitorId != null
                    && firestoreVisitorId
                            .trim()
                            .equals(
                                    normalizedVisitorId
                            )) {

                matches = true;
            }


            // =================================================
            // MATCH 3
            //
            // Firestore document ID == visitorId
            // =================================================

            if (!matches
                    && documentId
                            .equals(
                                    normalizedVisitorId
                            )) {

                matches = true;
            }


            // =================================================
            // VISITOR FOUND
            // =================================================

            if (matches) {

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "VISITOR FOUND"
                );

                System.out.println(
                        "Document ID: "
                                + documentId
                );

                System.out.println(
                        "Document Path: "
                                + document
                                        .getReference()
                                        .getPath()
                );

                System.out.println(
                        "Firestore id: "
                                + firestoreId
                );

                System.out.println(
                        "Firestore visitorId: "
                                + firestoreVisitorId
                );

                System.out.println(
                        "Current Status: "
                                + document
                                        .getString("status")
                );


                // =================================================
                // CHECK CURRENT STATUS
                // =================================================

                String currentStatus =
                        document.getString(
                                "status"
                        );


                if ("Inside".equalsIgnoreCase(
                        currentStatus)) {

                    System.out.println(
                            "Visitor is already inside."
                    );

                    return false;
                }


                if ("Checked Out".equalsIgnoreCase(
                        currentStatus)) {

                    System.out.println(
                            "Visitor has already checked out."
                    );

                    return false;
                }


                // =================================================
                // UPDATE THE ACTUAL DOCUMENT
                //
                // We use document.getReference()
                // so Firestore updates the exact nested record.
                // =================================================

                System.out.println(
                        "Updating visitor..."
                );


                document.getReference()
                        .update(
                                "status",
                                "Inside",

                                "entryTime",
                                entryTime,

                                "guardEmail",
                                normalizedGuardEmail
                        )
                        .get();


                System.out.println(
                        "UPDATE SUCCESSFUL"
                );

                System.out.println(
                        "Visitor admitted successfully."
                );

                System.out.println(
                        "======================================"
                );


                return true;
            }
        }


        // =================================================
        // VISITOR NOT FOUND
        // =================================================

        System.out.println(
                "======================================"
        );

        System.out.println(
                "VISITOR NOT FOUND"
        );

        System.out.println(
                "Visitor ID searched: "
                        + normalizedVisitorId
        );

        System.out.println(
                "======================================"
        );


        return false;


    } catch (Exception e) {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "ADMISSION FAILED"
        );

        System.out.println(
                "Visitor ID: "
                        + visitorId
        );

        System.out.println(
                "Guard Email: "
                        + guardEmail
        );

        System.out.println(
                "Exception Type: "
                        + e.getClass().getName()
        );

        System.out.println(
                "Exception Message: "
                        + e.getMessage()
        );

        e.printStackTrace();

        System.out.println(
                "======================================"
        );


        return false;
    }
}


// =====================================================
// LEGACY ADMISSION METHOD
//
// Kept for compatibility with older controller code.
//
// =====================================================

public boolean admitVisitor(
        String visitorId) {

    try {

        if (db == null) {

            System.out.println(
                    "ERROR: Firestore is null."
            );

            return false;
        }


        if (visitorId == null
                || visitorId.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Visitor ID is missing."
            );

            return false;
        }


        String normalizedVisitorId =
                visitorId.trim();


        String entryTime =
                LocalTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "hh:mm a"
                                )
                        );


        // =================================================
        // GET ALL visitor_records
        //
        // No whereEqualTo()
        // No index required.
        // =================================================

        QuerySnapshot snapshot =
                db.collectionGroup(
                        VISITOR_RECORDS
                )
                .get()
                .get();


        // =================================================
        // SEARCH LOCALLY
        // =================================================

        for (QueryDocumentSnapshot document :
                snapshot.getDocuments()) {

            String documentId =
                    document.getId();


            String firestoreId =
                    document.getString("id");


            String firestoreVisitorId =
                    document.getString(
                            "visitorId"
                    );


            boolean matches = false;


            if (firestoreId != null
                    && firestoreId
                            .trim()
                            .equals(
                                    normalizedVisitorId
                            )) {

                matches = true;
            }


            if (!matches
                    && firestoreVisitorId != null
                    && firestoreVisitorId
                            .trim()
                            .equals(
                                    normalizedVisitorId
                            )) {

                matches = true;
            }


            if (!matches
                    && documentId.equals(
                            normalizedVisitorId
                    )) {

                matches = true;
            }


            if (matches) {

                document.getReference()
                        .update(
                                "status",
                                "Inside",

                                "entryTime",
                                entryTime
                        )
                        .get();


                System.out.println(
                        "Visitor admitted successfully: "
                                + normalizedVisitorId
                );


                return true;
            }
        }


        System.out.println(
                "ERROR: Visitor document not found: "
                        + normalizedVisitorId
        );


        return false;


    } catch (Exception e) {

        System.out.println(
                "ERROR: Unable to admit visitor."
        );

        e.printStackTrace();

        return false;
    }
}

    // =====================================================
    // CHECK DUPLICATE VISITOR
    // =====================================================

    private boolean containsVisitorId(
            List<VisitorModel> visitors,
            String visitorId) {

        if (visitorId == null) {
            return false;
        }


        for (VisitorModel existing :
                visitors) {

            if (existing == null) {
                continue;
            }


            if (visitorId.equals(
                    existing.getVisitorId())) {

                return true;
            }
        }


        return false;
    }
}