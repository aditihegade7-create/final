package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.SosAlert;

/**
 * =========================================================
 * SOS DAO
 * =========================================================
 *
 * FIRESTORE STRUCTURE
 *
 * emergency_alerts
 *      |
 *      |--- email-document-1
 *      |       |
 *      |       |--- email
 *      |       |--- society
 *      |       |--- updatedAt
 *      |       |
 *      |       |--- alerts
 *      |               |
 *      |               |--- alertId
 *      |                       |--- email
 *      |                       |--- sender1
 *      |                       |--- sender2
 *      |                       |--- society
 *      |                       |--- location
 *      |                       |--- type
 *      |                       |--- time
 *      |                       |--- status
 *      |                       |--- details
 *      |                       |--- createdAt
 *
 * =========================================================
 */
public class SosDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db;

    // =========================================================
    // COLLECTION
    // =========================================================

    private static final String COLLECTION =
            "emergency_alerts";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SosDao() {

        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // GET ALL SOS ALERTS FOR SPECIFIC SOCIETY
    // =========================================================

    public List<SosAlert> getAlertsBySociety(
            String society) {

        List<SosAlert> alerts =
                new ArrayList<>();

        try {

            String requiredSociety =
                    safeString(society);

            if (requiredSociety.isEmpty()) {

                System.out.println(
                        "SosDao: Society is empty."
                );

                return alerts;
            }

            // =================================================
            // GET ALL EMAIL DOCUMENTS
            // =================================================

            CollectionReference emergencyCollection =
                    db.collection(COLLECTION);

            ApiFuture<QuerySnapshot> parentFuture =
                    emergencyCollection.get();

            QuerySnapshot parentSnapshot =
                    parentFuture.get();

            System.out.println(
                    "SosDao: Total email documents = "
                    + parentSnapshot.size()
            );

            // =================================================
            // LOOP THROUGH EVERY EMAIL DOCUMENT
            // =================================================

            for (QueryDocumentSnapshot emailDocument
                    : parentSnapshot.getDocuments()) {

                // =================================================
                // PARENT EMAIL
                // =================================================

                String parentEmail =
                        safeString(
                                emailDocument.getString(
                                        "email"
                                )
                        );

                // If email field does not exist,
                // use document ID
                if (parentEmail.isEmpty()) {

                    parentEmail =
                            safeString(
                                    emailDocument.getId()
                            );
                }

                // =================================================
                // PARENT SOCIETY
                // =================================================

                String parentSociety =
                        safeString(
                                emailDocument.getString(
                                        "society"
                                )
                        );

                System.out.println(
                        "Checking email = "
                        + parentEmail
                        + " | society = "
                        + parentSociety
                );

                // =================================================
                // STRICT SOCIETY FILTER
                // =================================================

                if (parentSociety.isEmpty()) {

                    System.out.println(
                            "Skipped because parent society is empty: "
                            + parentEmail
                    );

                    continue;
                }

                if (!parentSociety.equalsIgnoreCase(
                        requiredSociety)) {

                    System.out.println(
                            "Skipped different society: "
                            + parentEmail
                    );

                    continue;
                }

                // =================================================
                // GET ALERTS SUBCOLLECTION
                // =================================================

                CollectionReference alertsCollection =
                        emailDocument
                                .getReference()
                                .collection("alerts");

                ApiFuture<QuerySnapshot> alertsFuture =
                        alertsCollection.get();

                QuerySnapshot alertsSnapshot =
                        alertsFuture.get();

                System.out.println(
                        "Alerts found for "
                        + parentEmail
                        + " = "
                        + alertsSnapshot.size()
                );

                // =================================================
                // LOOP THROUGH ALL ALERTS
                // =================================================

                for (QueryDocumentSnapshot alertDocument
                        : alertsSnapshot.getDocuments()) {

                    SosAlert alert =
                            convertDocumentToAlert(
                                    alertDocument,
                                    parentEmail,
                                    parentSociety
                            );

                    if (alert == null) {

                        continue;
                    }

                    // =================================================
                    // FINAL SOCIETY CHECK
                    // =================================================

                    String alertSociety =
                            safeString(
                                    alert.getSociety()
                            );

                    if (!alertSociety.isEmpty()
                            && !alertSociety.equalsIgnoreCase(
                                    requiredSociety)) {

                        System.out.println(
                                "Skipped child alert from different society: "
                                + alertDocument.getId()
                        );

                        continue;
                    }

                    // =================================================
                    // ADD ALERT
                    // =================================================

                    alerts.add(alert);

                    System.out.println(
                            "FETCHED SOS -> "
                            + "parentEmail=" + parentEmail
                            + " | alertEmail=" + alert.getEmail()
                            + " | type=" + alert.getType()
                            + " | status=" + alert.getStatus()
                    );
                }
            }

            // =================================================
            // SORT LATEST FIRST
            // =================================================

            alerts.sort(
                    Comparator.comparing(
                            SosAlert::getCreatedAt,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

            // =================================================
            // FINAL LOG
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "SosDao: TOTAL FETCHED = "
                    + alerts.size()
            );

            System.out.println(
                    "SosDao: SOCIETY = "
                    + requiredSociety
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "SosDao: Error fetching SOS alerts."
            );

            e.printStackTrace();
        }

        return alerts;
    }

    // =========================================================
    // GET ACTIVE ALERTS
    // =========================================================

    public List<SosAlert> getActiveAlertsBySociety(
            String society) {

        List<SosAlert> allAlerts =
                getAlertsBySociety(society);

        List<SosAlert> activeAlerts =
                new ArrayList<>();

        for (SosAlert alert : allAlerts) {

            if (alert.getStatus() != null
                    && alert.getStatus()
                            .equalsIgnoreCase("ACTIVE")) {

                activeAlerts.add(alert);
            }
        }

        return activeAlerts;
    }

    // =========================================================
    // GET RESOLVED ALERTS
    // =========================================================

    public List<SosAlert> getResolvedAlertsBySociety(
            String society) {

        List<SosAlert> allAlerts =
                getAlertsBySociety(society);

        List<SosAlert> resolvedAlerts =
                new ArrayList<>();

        for (SosAlert alert : allAlerts) {

            if (alert.getStatus() != null
                    && alert.getStatus()
                            .equalsIgnoreCase("RESOLVED")) {

                resolvedAlerts.add(alert);
            }
        }

        return resolvedAlerts;
    }

    // =========================================================
    // GET ALERTS FOR ONE EMAIL
    // =========================================================
    //
    // Email is used ONLY internally.
    // It will NOT be shown in UI.
    //
    // =========================================================

    public List<SosAlert> getAlertsByEmailAndSociety(
            String email,
            String society) {

        List<SosAlert> alerts =
                new ArrayList<>();

        try {

            String requiredEmail =
                    safeString(email);

            String requiredSociety =
                    safeString(society);

            if (requiredEmail.isEmpty()
                    || requiredSociety.isEmpty()) {

                return alerts;
            }

            // =================================================
            // SEARCH ALL PARENT DOCUMENTS
            // =================================================

            QuerySnapshot parentSnapshot =
                    db.collection(COLLECTION)
                            .get()
                            .get();

            for (QueryDocumentSnapshot emailDocument
                    : parentSnapshot.getDocuments()) {

                String parentEmail =
                        safeString(
                                emailDocument.getString(
                                        "email"
                                )
                        );

                if (parentEmail.isEmpty()) {

                    parentEmail =
                            safeString(
                                    emailDocument.getId()
                            );
                }

                // =================================================
                // EMAIL MATCH
                // =================================================

                if (!parentEmail.equalsIgnoreCase(
                        requiredEmail)) {

                    continue;
                }

                // =================================================
                // SOCIETY MATCH
                // =================================================

                String parentSociety =
                        safeString(
                                emailDocument.getString(
                                        "society"
                                )
                        );

                if (!parentSociety.equalsIgnoreCase(
                        requiredSociety)) {

                    continue;
                }

                // =================================================
                // GET ALERTS
                // =================================================

                QuerySnapshot alertsSnapshot =
                        emailDocument
                                .getReference()
                                .collection("alerts")
                                .get()
                                .get();

                for (QueryDocumentSnapshot alertDocument
                        : alertsSnapshot.getDocuments()) {

                    SosAlert alert =
                            convertDocumentToAlert(
                                    alertDocument,
                                    parentEmail,
                                    parentSociety
                            );

                    if (alert == null) {
                        continue;
                    }

                    if (!alert.getSociety().isEmpty()
                            && !alert.getSociety()
                                    .equalsIgnoreCase(
                                            requiredSociety
                                    )) {

                        continue;
                    }

                    alerts.add(alert);
                }

                // Email document found.
                break;
            }

            alerts.sort(
                    Comparator.comparing(
                            SosAlert::getCreatedAt,
                            Comparator.nullsLast(
                                    Comparator.reverseOrder()
                            )
                    )
            );

        } catch (Exception e) {

            System.out.println(
                    "SosDao: Error fetching alerts by email."
            );

            e.printStackTrace();
        }

        return alerts;
    }

    // =========================================================
    // CONVERT FIRESTORE DOCUMENT TO MODEL
    // =========================================================

    private SosAlert convertDocumentToAlert(
            DocumentSnapshot document,
            String parentEmail,
            String parentSociety) {

        try {

            SosAlert alert =
                    new SosAlert();

            // =================================================
            // ALERT ID
            // =================================================

            alert.setAlertId(
                    document.getId()
            );

            // =================================================
            // EMAIL
            // =================================================
            //
            // First priority:
            // child alert email
            //
            // If missing:
            // parent email
            //
            // =================================================

            String email =
                    safeString(
                            document.getString("email")
                    );

            if (email.isEmpty()) {

                email = parentEmail;
            }

            alert.setEmail(email);

            // =================================================
            // SOCIETY
            // =================================================

            String alertSociety =
                    safeString(
                            document.getString("society")
                    );

            if (alertSociety.isEmpty()) {

                alertSociety =
                        parentSociety;
            }

            alert.setSociety(
                    alertSociety
            );

            // =================================================
            // LOCATION
            // =================================================

            alert.setLocation(
                    safeString(
                            document.getString("location")
                    )
            );

            // =================================================
            // TYPE
            // =================================================

            alert.setType(
                    safeString(
                            document.getString("type")
                    )
            );

            // =================================================
            // TIME
            // =================================================

            alert.setTime(
                    safeString(
                            document.getString("time")
                    )
            );

            // =================================================
            // STATUS
            // =================================================

            alert.setStatus(
                    safeString(
                            document.getString("status")
                    )
            );

            // =================================================
            // DETAILS
            // =================================================

            alert.setDetails(
                    safeString(
                            document.getString("details")
                    )
            );

            // =================================================
            // CREATED AT
            // =================================================

            Object createdAt =
                    document.get("createdAt");

            Date createdDate = null;

            if (createdAt instanceof Timestamp) {

                Timestamp timestamp =
                        (Timestamp) createdAt;

                createdDate =
                        timestamp.toDate();

            } else if (createdAt instanceof Date) {

                createdDate =
                        (Date) createdAt;
            }

            alert.setCreatedAt(
                    createdDate
            );

            return alert;

        } catch (Exception e) {

            System.out.println(
                    "SosDao: Error converting alert = "
                    + document.getId()
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safeString(
            String value) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }
}