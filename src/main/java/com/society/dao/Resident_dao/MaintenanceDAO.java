package com.society.dao.Resident_dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resident-side Maintenance DAO.
 *
 * Firebase structure used by this DAO:
 *
 * Maintenance
 *   ├── <secretaryEmail>
 *   │     ├── email: <secretaryEmail>
 *   │     ├── society: <society>
 *   │     └── records
 *   │           └── <maintenanceDocumentId>
 *   │                 ├── addedBySecretaryEmail
 *   │                 ├── amount
 *   │                 ├── date
 *   │                 ├── maintenanceId
 *   │                 ├── month
 *   │                 ├── society
 *   │                 └── status
 *   └── ...
 */
public class MaintenanceDAO {

    private final Firestore db;

    public MaintenanceDAO() {
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // FETCH MAINTENANCE FOR THE LOGGED-IN RESIDENT
    // =========================================================
    //
    // residentEmail is accepted so the method is called with the
    // logged-in resident identity. The actual maintenance ownership
    // is by SOCIETY, because the secretary creates one maintenance
    // record for all residents of that society.
    //
    // Parent document = secretary email
    // Record filter  = resident society
    //
    // =========================================================
    public List<Map<String, Object>> getMaintenanceBySociety(
            String residentEmail,
            String residentSociety) {

        List<Map<String, Object>> result = new ArrayList<>();

        if (residentEmail == null || residentEmail.trim().isEmpty()) {
            return result;
        }

        if (residentSociety == null || residentSociety.trim().isEmpty()) {
            return result;
        }

        String targetSociety = residentSociety.trim();

        try {
            CollectionReference maintenanceCollection =
                    db.collection("Maintenance");

            ApiFuture<QuerySnapshot> secretaryFuture =
                    maintenanceCollection.get();

            QuerySnapshot secretarySnapshot =
                    secretaryFuture.get();

            for (DocumentSnapshot secretaryDocument :
                    secretarySnapshot.getDocuments()) {

                String secretaryEmail = secretaryDocument.getId();

                // -------------------------------------------------
                // First check the society stored on the secretary
                // document, exactly as in the Firebase structure.
                // -------------------------------------------------
                String parentSociety =
                        stringValue(
                                secretaryDocument,
                                "society",
                                ""
                        );

                if (parentSociety.isEmpty()
                        || !parentSociety.equalsIgnoreCase(targetSociety)) {
                    continue;
                }

                // -------------------------------------------------
                // Read records subcollection.
                // -------------------------------------------------
                CollectionReference records =
                        secretaryDocument
                                .getReference()
                                .collection("records");

                ApiFuture<QuerySnapshot> recordsFuture =
                        records.get();

                QuerySnapshot recordsSnapshot =
                        recordsFuture.get();

                for (DocumentSnapshot record :
                        recordsSnapshot.getDocuments()) {

                    String recordSociety =
                            stringValue(
                                    record,
                                    "society",
                                    parentSociety
                            );

                    // -------------------------------------------------
                    // Second society check. This protects against a
                    // wrongly stored record under a secretary document.
                    // -------------------------------------------------
                    if (!recordSociety.isEmpty()
                            && !recordSociety.equalsIgnoreCase(targetSociety)) {
                        continue;
                    }

                    String maintenanceId =
                            stringValue(
                                    record,
                                    "maintenanceId",
                                    record.getId()
                            );

                    if (maintenanceId.isEmpty()) {
                        maintenanceId = record.getId();
                    }

                    String amount =
                            stringValue(record, "amount", "");

                    String date =
                            stringValue(record, "date", "");

                    String month =
                            stringValue(record, "month", "");

                    String status =
                            stringValue(record, "status", "Pending");

                    String addedBySecretaryEmail =
                            stringValue(
                                    record,
                                    "addedBySecretaryEmail",
                                    secretaryEmail
                            );

                    Map<String, Object> data =
                            new HashMap<>();

                    // Firestore document id. This is the exact ID
                    // required to update records/{maintenanceId}.
                    data.put("billId", record.getId());

                    data.put(
                            "maintenanceId",
                            maintenanceId
                    );

                    data.put("amount", amount);
                    data.put("date", date);
                    data.put("month", month);
                    data.put("status", status);
                    data.put("society", recordSociety.isEmpty()
                            ? parentSociety
                            : recordSociety);
                    data.put(
                            "addedBySecretaryEmail",
                            addedBySecretaryEmail
                    );

                    // Keep the parent secretary email available to the
                    // UI/debugging layer, even though payment below can
                    // also locate the document by billId.
                    data.put(
                            "secretaryEmail",
                            secretaryEmail
                    );

                    result.add(data);
                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =========================================================
    // UPDATE MAINTENANCE STATUS BY FIRESTORE DOCUMENT ID
    // =========================================================
    //
    // Searches:
    // Maintenance/<secretaryEmail>/records/<billId>
    //
    // This is needed because the resident email is NOT the parent
    // document ID in the maintenance collection.
    // =========================================================
    public boolean updateMaintenanceStatusByBillId(
            String billId,
            String newStatus) {

        if (billId == null || billId.trim().isEmpty()) {
            return false;
        }

        if (newStatus == null || newStatus.trim().isEmpty()) {
            return false;
        }

        String targetBillId = billId.trim();
        String targetStatus = newStatus.trim();

        try {
            CollectionReference maintenanceCollection =
                    db.collection("Maintenance");

            QuerySnapshot secretarySnapshot =
                    maintenanceCollection.get().get();

            for (DocumentSnapshot secretaryDocument :
                    secretarySnapshot.getDocuments()) {

                DocumentReference recordRef =
                        secretaryDocument
                                .getReference()
                                .collection("records")
                                .document(targetBillId);

                DocumentSnapshot record =
                        recordRef.get().get();

                if (record.exists()) {

                    recordRef.update(
                            "status",
                            targetStatus
                    ).get();

                    System.out.println(
                            "Maintenance status updated successfully."
                    );
                    System.out.println(
                            "Secretary Email : " + secretaryDocument.getId()
                    );
                    System.out.println(
                            "Maintenance ID  : " + targetBillId
                    );
                    System.out.println(
                            "New Status      : " + targetStatus
                    );

                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // BACKWARD-COMPATIBLE OLD METHOD
    // =========================================================
    //
    // Kept so other old resident code does not break at compile time.
    // New Mybills code should use getMaintenanceBySociety().
    // =========================================================
    public List<Map<String, Object>> getMaintenanceByEmail(
            String email) {

        List<Map<String, Object>> result = new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            return result;
        }

        try {
            DocumentSnapshot document =
                    db.collection("Maintenance")
                            .document(email.trim())
                            .get()
                            .get();

            if (!document.exists()) {
                return result;
            }

            String parentSociety =
                    stringValue(document, "society", "");

            QuerySnapshot records =
                    document.getReference()
                            .collection("records")
                            .get()
                            .get();

            for (DocumentSnapshot record : records.getDocuments()) {

                Map<String, Object> data =
                        new HashMap<>();

                data.put("billId", record.getId());
                data.put(
                        "maintenanceId",
                        stringValue(
                                record,
                                "maintenanceId",
                                record.getId()
                        )
                );
                data.put(
                        "amount",
                        stringValue(record, "amount", "")
                );
                data.put(
                        "date",
                        stringValue(record, "date", "")
                );
                data.put(
                        "month",
                        stringValue(record, "month", "")
                );
                data.put(
                        "society",
                        stringValue(
                                record,
                                "society",
                                parentSociety
                        )
                );
                data.put(
                        "status",
                        stringValue(record, "status", "Pending")
                );
                data.put(
                        "addedBySecretaryEmail",
                        stringValue(
                                record,
                                "addedBySecretaryEmail",
                                email.trim()
                        )
                );
                data.put("secretaryEmail", email.trim());

                result.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // =========================================================
    // BACKWARD-COMPATIBLE OLD UPDATE METHOD
    // =========================================================
    //
    // Old code used parentEmail + month. Keep it for compatibility,
    // but the new Mybills implementation does NOT use it.
    // =========================================================
    public boolean updateMaintenanceStatus(
            String parentEmail,
            String month,
            String newStatus) {

        if (parentEmail == null || parentEmail.trim().isEmpty()) {
            return false;
        }

        if (month == null || month.trim().isEmpty()) {
            return false;
        }

        try {
            CollectionReference records =
                    db.collection("Maintenance")
                            .document(parentEmail.trim())
                            .collection("records");

            QuerySnapshot snapshot =
                    records.get().get();

            for (DocumentSnapshot record : snapshot.getDocuments()) {

                String recordMonth =
                        stringValue(record, "month", "");

                if (recordMonth.equalsIgnoreCase(month.trim())
                        || record.getId().equals(month.trim())) {

                    record.getReference()
                            .update("status", newStatus)
                            .get();

                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================================================
    // SAFE FIRESTORE STRING READER
    // =========================================================
    private String stringValue(
            DocumentSnapshot document,
            String field,
            String defaultValue) {

        Object value = document.get(field);

        if (value == null) {
            return defaultValue;
        }

        return String.valueOf(value).trim();
    }
}
