package com.society.dao.Resident_dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.society.config.FirebaseConfig;

public class EmergencyDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db;

    public EmergencyDao() {
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // SEND EMERGENCY ALERT
    // =========================================================

    public String sendEmergencyAlert(
            String residentEmail,
            String type,
            String location,
            String details) {

        try {

            // =====================================================
            // VALIDATION
            // =====================================================

            if (residentEmail == null ||
                    residentEmail.trim().isEmpty()) {

                throw new Exception(
                        "Resident email not found."
                );
            }

            if (type == null ||
                    type.trim().isEmpty()) {

                throw new Exception(
                        "Emergency type is required."
                );
            }

            if (location == null ||
                    location.trim().isEmpty()) {

                throw new Exception(
                        "Emergency location is required."
                );
            }

            residentEmail = residentEmail.trim();
            type = type.trim();
            location = location.trim();

            details = details == null
                    ? ""
                    : details.trim();

            // =====================================================
            // GET RESIDENT
            // =====================================================

            DocumentSnapshot residentSnapshot =
                    db.collection("Residents")
                      .document(residentEmail)
                      .get()
                      .get();

            if (!residentSnapshot.exists()) {

                throw new Exception(
                        "Resident record not found for: "
                                + residentEmail
                );
            }

            // =====================================================
            // GET SOCIETY
            // =====================================================

            String society =
                    residentSnapshot.getString("society");

            if (society == null ||
                    society.trim().isEmpty()) {

                society =
                        residentSnapshot.getString(
                                "societyName"
                        );
            }

            if (society == null ||
                    society.trim().isEmpty()) {

                throw new Exception(
                        "Society is not assigned to resident: "
                                + residentEmail
                );
            }

            society = society.trim();

            // =====================================================
            // GET SECRETARY
            // =====================================================

            String secretaryEmail =
                    getSecretaryEmail(society);

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                throw new Exception(
                        "Secretary email not found for society: "
                                + society
                );
            }

            secretaryEmail =
                    secretaryEmail.trim();

            // =====================================================
            // GET GUARD
            // =====================================================

            String guardEmail =
                    getGuardEmail(society);

            if (guardEmail == null ||
                    guardEmail.trim().isEmpty()) {

                throw new Exception(
                        "Guard email not found for society: "
                                + society
                );
            }

            guardEmail =
                    guardEmail.trim();

            // =====================================================
            // CURRENT TIME
            // =====================================================

            Date now = new Date();

            SimpleDateFormat timeFormat =
                    new SimpleDateFormat(
                            "dd MMM yyyy, hh:mm a"
                    );

            timeFormat.setTimeZone(
                    TimeZone.getTimeZone(
                            "Asia/Kolkata"
                    )
            );

            String formattedTime =
                    timeFormat.format(now);

            // =====================================================
            // CREATE ALERT DOCUMENT
            // =====================================================

            DocumentReference alertRef =
                    db.collection("emergency_alerts")
                      .document(residentEmail)
                      .collection("alert")
                      .document();

            String emergencyId =
                    alertRef.getId();

            // =====================================================
            // ALERT DATA
            // =====================================================

            Map<String, Object> alertData =
                    new HashMap<>();

            alertData.put(
                    "createdAt",
                    FieldValue.serverTimestamp()
            );

            alertData.put(
                    "details",
                    details
            );

            alertData.put(
                    "email",
                    residentEmail
            );

            alertData.put(
                    "emergencyId",
                    emergencyId
            );

            alertData.put(
                    "location",
                    location
            );

            alertData.put(
                    "sender1",
                    secretaryEmail
            );

            alertData.put(
                    "sender2",
                    guardEmail
            );

            // ONLY SOCIETY
            // societyName is NOT stored
            alertData.put(
                    "society",
                    society
            );

            alertData.put(
                    "status",
                    "ACTIVE"
            );

            alertData.put(
                    "time",
                    formattedTime
            );

            alertData.put(
                    "type",
                    type
            );

            // =====================================================
            // SAVE ALERT
            // =====================================================

            alertRef
                    .set(alertData)
                    .get();

            // =====================================================
            // ROOT DOCUMENT
            // =====================================================

            Map<String, Object> residentAlertData =
                    new HashMap<>();

            residentAlertData.put(
                    "email",
                    residentEmail
            );

            // ONLY SOCIETY
            // societyName is NOT stored
            residentAlertData.put(
                    "society",
                    society
            );

            residentAlertData.put(
                    "updatedAt",
                    FieldValue.serverTimestamp()
            );

            db.collection("emergency_alerts")
              .document(residentEmail)
              .set(residentAlertData)
              .get();

            return emergencyId;

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to send emergency alert: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // =========================================================
    // GET SECRETARY EMAIL
    // =========================================================

    private String getSecretaryEmail(
            String society) {

        try {

            QuerySnapshot snapshot =
                    db.collection("Secretaries")
                      .whereEqualTo(
                              "society",
                              society
                      )
                      .limit(1)
                      .get()
                      .get();

            List<QueryDocumentSnapshot> documents =
                    snapshot.getDocuments();

            if (documents.isEmpty()) {
                return null;
            }

            DocumentSnapshot secretary =
                    documents.get(0);

            String email =
                    secretary.getString("email");

            if (email == null ||
                    email.trim().isEmpty()) {

                email =
                        secretary.getId();
            }

            return email;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET GUARD EMAIL
    // =========================================================

    private String getGuardEmail(
            String society) {

        try {

            QuerySnapshot snapshot =
                    db.collection("Guards")
                      .whereEqualTo(
                              "society",
                              society
                      )
                      .limit(1)
                      .get()
                      .get();

            List<QueryDocumentSnapshot> documents =
                    snapshot.getDocuments();

            if (documents.isEmpty()) {
                return null;
            }

            DocumentSnapshot guard =
                    documents.get(0);

            String email =
                    guard.getString("email");

            if (email == null ||
                    email.trim().isEmpty()) {

                email =
                        guard.getId();
            }

            return email;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET LATEST ACTIVE ALERT
    // =========================================================

    public Map<String, Object> getLatestActiveEmergencyAlert(
            String residentEmail) {

        try {

            if (residentEmail == null ||
                    residentEmail.trim().isEmpty()) {

                return null;
            }

            residentEmail =
                    residentEmail.trim();

            // =====================================================
            // GET ACTIVE ALERTS FOR CURRENT RESIDENT
            // =====================================================

            QuerySnapshot snapshot =
                    db.collection("emergency_alerts")
                      .document(residentEmail)
                      .collection("alert")
                      .whereEqualTo(
                              "status",
                              "ACTIVE"
                      )
                      .get()
                      .get();

            List<QueryDocumentSnapshot> documents =
                    snapshot.getDocuments();

            if (documents.isEmpty()) {

                return null;
            }

            // =====================================================
            // FIND LATEST ALERT
            // =====================================================

            QueryDocumentSnapshot latest =
                    null;

            Date latestCreatedAt =
                    null;

            for (QueryDocumentSnapshot document :
                    documents) {

                Date createdAt =
                        document.getDate("createdAt");

                if (createdAt == null) {
                    continue;
                }

                if (latest == null ||
                        latestCreatedAt == null ||
                        createdAt.after(latestCreatedAt)) {

                    latest = document;

                    latestCreatedAt =
                            createdAt;
                }
            }

            // =====================================================
            // FALLBACK
            // =====================================================

            if (latest == null) {

                latest =
                        documents.get(
                                documents.size() - 1
                        );
            }

            // =====================================================
            // RETURN DATA
            // =====================================================

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "createdAt",
                    latest.getDate("createdAt")
            );

            data.put(
                    "details",
                    getStringValue(
                            latest,
                            "details"
                    )
            );

            data.put(
                    "email",
                    getStringValue(
                            latest,
                            "email"
                    )
            );

            data.put(
                    "emergencyId",
                    getStringValue(
                            latest,
                            "emergencyId"
                    )
            );

            data.put(
                    "location",
                    getStringValue(
                            latest,
                            "location"
                    )
            );

            data.put(
                    "sender1",
                    getStringValue(
                            latest,
                            "sender1"
                    )
            );

            data.put(
                    "sender2",
                    getStringValue(
                            latest,
                            "sender2"
                    )
            );

            data.put(
                    "society",
                    getStringValue(
                            latest,
                            "society"
                    )
            );

            // societyName REMOVED

            data.put(
                    "status",
                    getStringValue(
                            latest,
                            "status"
                    )
            );

            data.put(
                    "time",
                    getStringValue(
                            latest,
                            "time"
                    )
            );

            data.put(
                    "type",
                    getStringValue(
                            latest,
                            "type"
                    )
            );

            return data;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // SAFE STRING GETTER
    // =========================================================

    private String getStringValue(
            DocumentSnapshot document,
            String field) {

        String value =
                document.getString(field);

        if (value == null) {
            return "";
        }

        return value;
    }

    // =========================================================
    // RESOLVE LATEST ACTIVE ALERT
    // =========================================================

    public boolean resolveLatestEmergencyAlert(
            String residentEmail) {

        try {

            if (residentEmail == null ||
                    residentEmail.trim().isEmpty()) {

                return false;
            }

            residentEmail =
                    residentEmail.trim();

            QuerySnapshot snapshot =
                    db.collection("emergency_alerts")
                      .document(residentEmail)
                      .collection("alert")
                      .whereEqualTo(
                              "status",
                              "ACTIVE"
                      )
                      .get()
                      .get();

            List<QueryDocumentSnapshot> documents =
                    snapshot.getDocuments();

            if (documents.isEmpty()) {

                return false;
            }

            QueryDocumentSnapshot latest =
                    null;

            Date latestDate =
                    null;

            for (QueryDocumentSnapshot document :
                    documents) {

                Date createdAt =
                        document.getDate("createdAt");

                if (createdAt == null) {
                    continue;
                }

                if (latest == null ||
                        latestDate == null ||
                        createdAt.after(latestDate)) {

                    latest =
                            document;

                    latestDate =
                            createdAt;
                }
            }

            if (latest == null) {

                latest =
                        documents.get(
                                documents.size() - 1
                        );
            }

            latest.getReference()
                  .update(
                          "status",
                          "RESOLVED"
                  )
                  .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // RESOLVE SPECIFIC ALERT
    // =========================================================

    public boolean resolveEmergencyAlert(
            String residentEmail,
            String emergencyId) {

        try {

            if (residentEmail == null ||
                    residentEmail.trim().isEmpty()) {

                return false;
            }

            if (emergencyId == null ||
                    emergencyId.trim().isEmpty()) {

                return false;
            }

            DocumentReference alertRef =
                    db.collection("emergency_alerts")
                      .document(residentEmail.trim())
                      .collection("alert")
                      .document(emergencyId.trim());

            DocumentSnapshot snapshot =
                    alertRef.get().get();

            if (!snapshot.exists()) {

                return false;
            }

            alertRef
                    .update(
                            "status",
                            "RESOLVED"
                    )
                    .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}