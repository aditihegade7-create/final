package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.DashboardData;

/**
 * ============================================================
 * DashboardDao
 * ============================================================
 *
 * Fetches Secretary Dashboard data according to the
 * currently logged-in Secretary's society.
 *
 * IMPORTANT:
 *
 * Secretaries:
 *     Secretaries/{email}
 *          society
 *
 * Residents:
 *     society OR societyName
 *
 * Owners:
 *     society
 *
 * Guards:
 *     society
 *
 * Complaints:
 *     complaints/{residentEmail}/complaints/{complaintId}
 *
 * Maintenance:
 *     Maintenance/{secretaryEmail}/records/{maintenanceId}
 *
 * SOS:
 *     emergency_alerts/{residentEmail}/alert/{alertId}
 *
 * Events:
 *     Supports common Events/events structures.
 */
public class DashboardDao {

    // ============================================================
    // FIRESTORE
    // ============================================================

    private final Firestore firestore;

    // ============================================================
    // LOGGED-IN SECRETARY
    // ============================================================

    private final String secretaryEmail;

    private String societyName;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public DashboardDao(String secretaryEmail) {

        this.firestore =
                FirebaseConfig.getFirestore();

        this.secretaryEmail =
                secretaryEmail == null
                        ? ""
                        : secretaryEmail
                                .trim()
                                .toLowerCase();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "DashboardDao created"
        );

        System.out.println(
                "Secretary Email : "
                        + this.secretaryEmail
        );

        System.out.println(
                "========================================"
        );

        this.societyName =
                loadSecretarySociety();

        System.out.println(
                "DashboardDao: Secretary Society = "
                        + this.societyName
        );
    }

    // ============================================================
    // GET SECRETARY SOCIETY
    // ============================================================

    private String loadSecretarySociety() {

        try {

            if (secretaryEmail.isEmpty()) {

                System.out.println(
                        "DashboardDao: Secretary email is empty."
                );

                return "";
            }

            DocumentSnapshot secretaryDocument =
                    firestore
                            .collection("Secretaries")
                            .document(secretaryEmail)
                            .get()
                            .get();

            if (!secretaryDocument.exists()) {

                System.out.println(
                        "DashboardDao: Secretary document NOT FOUND."
                );

                System.out.println(
                        "Path: Secretaries/"
                                + secretaryEmail
                );

                return "";
            }

            // ====================================================
            // IMPORTANT
            //
            // Your Firestore field is:
            //
            // society
            //
            // NOT societyName
            // ====================================================

            String society =
                    getString(
                            secretaryDocument,
                            "society"
                    );

            // fallback for old documents
            if (society.isEmpty()) {

                society =
                        getString(
                                secretaryDocument,
                                "societyName"
                        );
            }

            if (society.isEmpty()) {

                System.out.println(
                        "DashboardDao: Society field is empty."
                );

                return "";
            }

            society =
                    society.trim();

            System.out.println(
                    "DashboardDao: Secretary Society = "
                            + society
            );

            return society;

        } catch (Exception e) {

            System.out.println(
                    "DashboardDao: Error loading secretary society."
            );

            e.printStackTrace();

            return "";
        }
    }

    // ============================================================
    // GET SOCIETY
    // ============================================================

    public String getSocietyName() {

        return societyName;
    }

    // ============================================================
    // GET SECRETARY EMAIL
    // ============================================================

    public String getSecretaryEmail() {

        return secretaryEmail;
    }

    // ============================================================
    // GET FULL DASHBOARD DATA
    // ============================================================

    public DashboardData getDashboardData() {

        try {

            System.out.println(
                    "\n========================================"
            );

            System.out.println(
                    "FETCHING COMPLETE SECRETARY DASHBOARD"
            );

            System.out.println(
                    "Secretary Email : "
                            + secretaryEmail
            );

            System.out.println(
                    "Society         : "
                            + societyName
            );

            System.out.println(
                    "========================================"
            );

            if (societyName == null ||
                    societyName.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Secretary society is NULL or empty."
                );

                return new DashboardData(
                        0,
                        0,
                        0,
                        0,
                        0.0,
                        secretaryEmail,
                        ""
                );
            }

            // ====================================================
            // RESIDENTS
            // ====================================================

            int totalResidents =
                    getResidentCount();

            // ====================================================
            // OWNERS
            // ====================================================

            int totalOwners =
                    getOwnerCount();

            // ====================================================
            // GUARDS
            // ====================================================

            int totalGuards =
                    getGuardCount();

            // ====================================================
            // COMPLAINTS
            // ====================================================

            int openComplaints =
                    getOpenComplaints();

            // ====================================================
            // MAINTENANCE
            // ====================================================

            double maintenanceTotal =
                    getMaintenanceTotal();

            // ====================================================
            // MODEL
            // ====================================================

            DashboardData data =
                    new DashboardData(
                            totalResidents,
                            totalOwners,
                            totalGuards,
                            openComplaints,
                            maintenanceTotal,
                            secretaryEmail,
                            societyName
                    );

            // ====================================================
            // DEBUG
            // ====================================================

            System.out.println(
                    "----------------------------------------"
            );

            System.out.println(
                    "COMPLETE DASHBOARD DATA"
            );

            System.out.println(
                    "Residents       : "
                            + totalResidents
            );

            System.out.println(
                    "Owners          : "
                            + totalOwners
            );

            System.out.println(
                    "Guards          : "
                            + totalGuards
            );

            System.out.println(
                    "Open Complaints : "
                            + openComplaints
            );

            System.out.println(
                    "Maintenance     : ₹"
                            + maintenanceTotal
            );

            System.out.println(
                    "Secretary Email : "
                            + secretaryEmail
            );

            System.out.println(
                    "Society         : "
                            + societyName
            );

            System.out.println(
                    "----------------------------------------"
            );

            return data;

        } catch (Exception e) {

            System.out.println(
                    "DashboardDao Error:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // ============================================================
    // RESIDENT COUNT
    // ============================================================

    public int getResidentCount() {

        return getSocietyUserCount(
                "Residents"
        );
    }

    // ============================================================
    // OWNER COUNT
    // ============================================================

    public int getOwnerCount() {

        return getSocietyUserCount(
                "Owners"
        );
    }

    // ============================================================
    // GUARD COUNT
    // ============================================================

    public int getGuardCount() {

        return getSocietyUserCount(
                "Guards"
        );
    }

    // ============================================================
    // GENERIC SOCIETY USER COUNT
    // ============================================================

    private int getSocietyUserCount(
            String collectionName) {

        int count = 0;

        try {

            if (societyName == null ||
                    societyName.trim().isEmpty()) {

                return 0;
            }

            QuerySnapshot snapshot =
                    firestore
                            .collection(collectionName)
                            .get()
                            .get();

            System.out.println(
                    collectionName
                            + " documents found = "
                            + snapshot.size()
            );

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                String society =
                        getString(
                                document,
                                "society"
                        );

                // fallback
                if (society.isEmpty()) {

                    society =
                            getString(
                                    document,
                                    "societyName"
                            );
                }

                if (society.isEmpty()) {
                    continue;
                }

                if (society
                        .trim()
                        .equalsIgnoreCase(
                                societyName.trim()
                        )) {

                    count++;
                }
            }

            System.out.println(
                    collectionName
                            + " for society ["
                            + societyName
                            + "] = "
                            + count
            );

        } catch (Exception e) {

            System.out.println(
                    "Error fetching "
                            + collectionName
                            + " count:"
            );

            e.printStackTrace();
        }

        return count;
    }

    // ============================================================
    // OPEN COMPLAINTS
    // ============================================================

    public int getOpenComplaints() {

        int count = 0;

        try {

            if (societyName == null ||
                    societyName.trim().isEmpty()) {

                return 0;
            }

            System.out.println(
                    "Fetching complaints for society = "
                            + societyName
            );

            // ====================================================
            // complaints/{residentEmail}/complaints/*
            // ====================================================

            QuerySnapshot snapshot =
                    firestore
                            .collectionGroup("complaints")
                            .get()
                            .get();

            System.out.println(
                    "Complaint documents found = "
                            + snapshot.size()
            );

            Set<String> checkedResidents =
                    new HashSet<>();

            for (QueryDocumentSnapshot complaint :
                    snapshot.getDocuments()) {

                if (complaint == null ||
                        !complaint.exists()) {

                    continue;
                }

                // =================================================
                // GET RESIDENT EMAIL FROM PATH
                //
                // complaints
                //   / residentEmail
                //      / complaints
                //          / complaintId
                // =================================================

                String residentEmail =
                        getParentResidentEmail(
                                complaint
                        );

                if (residentEmail.isEmpty()) {
                    continue;
                }

                // =================================================
                // GET RESIDENT SOCIETY
                // =================================================

                String residentSociety =
                        "";

                if (!checkedResidents.contains(
                        residentEmail)) {

                    residentSociety =
                            getResidentSociety(
                                    residentEmail
                            );

                    checkedResidents.add(
                            residentEmail
                    );

                } else {

                    residentSociety =
                            getResidentSociety(
                                    residentEmail
                            );
                }

                if (residentSociety.isEmpty()) {
                    continue;
                }

                if (!residentSociety
                        .equalsIgnoreCase(
                                societyName
                        )) {

                    continue;
                }

                // =================================================
                // STATUS
                // =================================================

                String status =
                        getString(
                                complaint,
                                "status"
                        );

                if (isOpenComplaintStatus(
                        status
                )) {

                    count++;
                }
            }

            System.out.println(
                    "Open complaints for ["
                            + societyName
                            + "] = "
                            + count
            );

        } catch (Exception e) {

            System.out.println(
                    "Error fetching open complaints:"
            );

            e.printStackTrace();
        }

        return count;
    }

    // ============================================================
    // GET RESIDENT SOCIETY
    // ============================================================

    private String getResidentSociety(
            String residentEmail) {

        try {

            if (residentEmail == null ||
                    residentEmail.trim().isEmpty()) {

                return "";
            }

            String email =
                    residentEmail
                            .trim()
                            .toLowerCase();

            DocumentSnapshot resident =
                    firestore
                            .collection("Residents")
                            .document(email)
                            .get()
                            .get();

            if (!resident.exists()) {

                return "";
            }

            String society =
                    getString(
                            resident,
                            "society"
                    );

            if (society.isEmpty()) {

                society =
                        getString(
                                resident,
                                "societyName"
                        );
            }

            return society.trim();

        } catch (Exception e) {

            System.out.println(
                    "Error fetching resident society: "
                            + residentEmail
            );

            return "";
        }
    }

    // ============================================================
    // GET RESIDENT EMAIL FROM COMPLAINT PATH
    // ============================================================

    private String getParentResidentEmail(
            DocumentSnapshot complaint) {

        try {

            DocumentReference complaintRef =
                    complaint.getReference();

            if (complaintRef == null) {
                return "";
            }

            CollectionReference complaintsCollection =
                    complaintRef.getParent();

            if (complaintsCollection == null) {
                return "";
            }

            DocumentReference residentDocument =
                    complaintsCollection.getParent();

            if (residentDocument == null) {
                return "";
            }

            return residentDocument
                    .getId()
                    .trim()
                    .toLowerCase();

        } catch (Exception e) {

            return "";
        }
    }

    // ============================================================
    // COMPLAINT STATUS
    // ============================================================

    private boolean isOpenComplaintStatus(
            String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return true;
        }

        String value =
                status
                        .trim()
                        .toLowerCase();

        // closed states
        if (value.equals("resolved") ||
                value.equals("closed") ||
                value.equals("completed") ||
                value.equals("complete") ||
                value.equals("cancelled") ||
                value.equals("canceled")) {

            return false;
        }

        return true;
    }

    // ============================================================
    // MAINTENANCE TOTAL
    // ============================================================

    public double getMaintenanceTotal() {

        double total = 0.0;

        try {

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                return 0.0;
            }

            if (societyName == null ||
                    societyName.trim().isEmpty()) {

                return 0.0;
            }

            // ====================================================
            // EXACT PROJECT STRUCTURE:
            //
            // Maintenance
            //    / secretaryEmail
            //       / records
            //          / maintenanceId
            // ====================================================

            CollectionReference records =
                    firestore
                            .collection("Maintenance")
                            .document(secretaryEmail)
                            .collection("records");

            QuerySnapshot snapshot =
                    records
                            .get()
                            .get();

            System.out.println(
                    "Maintenance records found = "
                            + snapshot.size()
            );

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                // =================================================
                // SOCIETY
                // =================================================

                String maintenanceSociety =
                        getString(
                                document,
                                "society"
                        );

                if (maintenanceSociety.isEmpty()) {

                    maintenanceSociety =
                            getString(
                                    document,
                                    "societyName"
                            );
                }

                if (maintenanceSociety.isEmpty()) {

                    continue;
                }

                if (!maintenanceSociety
                        .trim()
                        .equalsIgnoreCase(
                                societyName.trim()
                        )) {

                    continue;
                }

                // =================================================
                // AMOUNT
                // =================================================

                Object amountObject =
                        document.get("amount");

                double amount =
                        parseAmount(
                                amountObject
                        );

                total += amount;

                System.out.println(
                        "Maintenance amount = "
                                + amount
                );
            }

            System.out.println(
                    "Total Maintenance for ["
                            + societyName
                            + "] = ₹"
                            + total
            );

        } catch (Exception e) {

            System.out.println(
                    "Error fetching maintenance total:"
            );

            e.printStackTrace();
        }

        return total;
    }

    // ============================================================
    // COMPATIBILITY METHOD
    // ============================================================
    //
    // Your SecretaryDashboard previously used
    // getMaintenanceCollection().
    //
    // Keep this method so old code does not break.
    // ============================================================

    public double getMaintenanceCollection() {

        return getMaintenanceTotal();
    }

    // ============================================================
    // PARSE AMOUNT
    // ============================================================

    private double parseAmount(
            Object amountObject) {

        if (amountObject == null) {
            return 0.0;
        }

        if (amountObject instanceof Number) {

            return ((Number) amountObject)
                    .doubleValue();
        }

        String value =
                amountObject
                        .toString()
                        .replace("₹", "")
                        .replace(",", "")
                        .trim();

        if (value.isEmpty()) {
            return 0.0;
        }

        try {

            return Double.parseDouble(
                    value
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Invalid maintenance amount: "
                            + value
            );

            return 0.0;
        }
    }

    // ============================================================
    // GET RECENT SOS ALERTS
    // ============================================================

    public List<SosAlertData> getRecentSOSAlerts() {

        List<SosAlertData> result =
                new ArrayList<>();

        try {

            if (societyName == null ||
                    societyName.trim().isEmpty()) {

                return result;
            }

            // ====================================================
            // EXACT PROJECT STRUCTURE:
            //
            // emergency_alerts
            //    / residentEmail
            //       / alert
            //          / alertId
            //
            // collectionGroup("alert") searches all alert
            // subcollections.
            // ====================================================

            QuerySnapshot snapshot =
                    firestore
                            .collectionGroup("alert")
                            .get()
                            .get();

            System.out.println(
                    "SOS alerts found = "
                            + snapshot.size()
            );

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                String society =
                        getString(
                                document,
                                "society"
                        );

                if (society.isEmpty()) {

                    society =
                            getString(
                                    document,
                                    "societyName"
                            );
                }

                if (!society
                        .equalsIgnoreCase(
                                societyName
                        )) {

                    continue;
                }

                SosAlertData alert =
                        new SosAlertData();

                alert.id =
                        document.getId();

                alert.type =
                        getString(
                                document,
                                "type"
                        );

                alert.location =
                        getString(
                                document,
                                "location"
                        );

                alert.details =
                        getString(
                                document,
                                "details"
                        );

                alert.status =
                        getString(
                                document,
                                "status"
                        );

                alert.time =
                        getString(
                                document,
                                "time"
                        );

                alert.email =
                        getString(
                                document,
                                "email"
                        );

                alert.society =
                        society;

                alert.createdAt =
                        getDate(
                                document,
                                "createdAt"
                        );

                result.add(alert);
            }

            // ====================================================
            // NEWEST FIRST
            // ====================================================

            Collections.sort(
                    result,
                    new Comparator<SosAlertData>() {

                        @Override
                        public int compare(
                                SosAlertData a,
                                SosAlertData b) {

                            if (a.createdAt == null &&
                                    b.createdAt == null) {

                                return 0;
                            }

                            if (a.createdAt == null) {
                                return 1;
                            }

                            if (b.createdAt == null) {
                                return -1;
                            }

                            return b.createdAt
                                    .compareTo(
                                            a.createdAt
                                    );
                        }
                    }
            );

            // ====================================================
            // MAX 5
            // ====================================================

            if (result.size() > 5) {

                result =
                        new ArrayList<>(
                                result.subList(
                                        0,
                                        5
                                )
                        );
            }

            System.out.println(
                    "Recent SOS for ["
                            + societyName
                            + "] = "
                            + result.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error fetching SOS alerts:"
            );

            e.printStackTrace();
        }

        return result;
    }

    // ============================================================
    // GET UPCOMING EVENTS
    // ============================================================

    public List<EventData> getUpcomingEvents() {

        List<EventData> result =
                new ArrayList<>();

        try {

            if (societyName == null ||
                    societyName.trim().isEmpty()) {

                return result;
            }

            /*
             * We check both common collection names because
             * Firestore collection names are case-sensitive.
             *
             * Existing project code can therefore use:
             *
             * Events
             * events
             */

            readEventCollection(
                    "Events",
                    result
            );

            readEventCollection(
                    "events",
                    result
            );

            // ====================================================
            // ALSO SEARCH NESTED "events" COLLECTIONS
            // ====================================================

            readEventCollectionGroup(
                    "events",
                    result
            );

            // ====================================================
            // REMOVE DUPLICATES
            // ====================================================

            removeDuplicateEvents(
                    result
            );

            // ====================================================
            // SORT
            // ====================================================

            Collections.sort(
                    result,
                    new Comparator<EventData>() {

                        @Override
                        public int compare(
                                EventData a,
                                EventData b) {

                            return safe(
                                    a.date
                            ).compareTo(
                                    safe(
                                            b.date
                                    )
                            );
                        }
                    }
            );

            // ====================================================
            // ONLY FIRST 5
            // ====================================================

            if (result.size() > 5) {

                result =
                        new ArrayList<>(
                                result.subList(
                                        0,
                                        5
                                )
                        );
            }

            System.out.println(
                    "Upcoming Events for ["
                            + societyName
                            + "] = "
                            + result.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error fetching upcoming events:"
            );

            e.printStackTrace();
        }

        return result;
    }

    // ============================================================
    // READ EVENT COLLECTION
    // ============================================================

    private void readEventCollection(
            String collectionName,
            List<EventData> result) {

        try {

            QuerySnapshot snapshot =
                    firestore
                            .collection(collectionName)
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                EventData event =
                        documentToEvent(
                                document
                        );

                if (event == null) {
                    continue;
                }

                if (!event.society.isEmpty() &&
                        event.society
                                .equalsIgnoreCase(
                                        societyName
                                )) {

                    if (isUpcomingEvent(event)) {

                        result.add(event);
                    }
                }
            }

        } catch (Exception e) {

            // Non-existing collection simply returns no documents.
            System.out.println(
                    "Could not read collection "
                            + collectionName
            );
        }
    }

    // ============================================================
    // READ EVENT COLLECTION GROUP
    // ============================================================

    private void readEventCollectionGroup(
            String collectionName,
            List<EventData> result) {

        try {

            QuerySnapshot snapshot =
                    firestore
                            .collectionGroup(
                                    collectionName
                            )
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                EventData event =
                        documentToEvent(
                                document
                        );

                if (event == null) {
                    continue;
                }

                if (event.society
                        .equalsIgnoreCase(
                                societyName
                        )) {

                    if (isUpcomingEvent(event)) {

                        result.add(event);
                    }
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Could not read event collection group."
            );
        }
    }

    // ============================================================
    // DOCUMENT TO EVENT
    // ============================================================

    private EventData documentToEvent(
            DocumentSnapshot document) {

        try {

            EventData event =
                    new EventData();

            event.id =
                    document.getId();

            // ====================================================
            // TITLE
            // ====================================================

            event.title =
                    firstString(
                            document,
                            "title",
                            "eventTitle",
                            "eventName",
                            "name"
                    );

            // ====================================================
            // DATE
            // ====================================================

            event.date =
                    firstString(
                            document,
                            "date",
                            "eventDate",
                            "startDate"
                    );

            // ====================================================
            // TIME
            // ====================================================

            event.time =
                    firstString(
                            document,
                            "time",
                            "eventTime",
                            "startTime"
                    );

            // ====================================================
            // LOCATION
            // ====================================================

            event.location =
                    firstString(
                            document,
                            "location",
                            "venue",
                            "place"
                    );

            // ====================================================
            // SOCIETY
            // ====================================================

            event.society =
                    firstString(
                            document,
                            "society",
                            "societyName"
                    );

            // ====================================================
            // DESCRIPTION
            // ====================================================

            event.description =
                    firstString(
                            document,
                            "description",
                            "details",
                            "eventDescription"
                    );

            if (event.title.isEmpty() &&
                    event.date.isEmpty()) {

                return null;
            }

            return event;

        } catch (Exception e) {

            return null;
        }
    }

    // ============================================================
    // UPCOMING EVENT CHECK
    // ============================================================

    private boolean isUpcomingEvent(
            EventData event) {

        if (event == null) {
            return false;
        }

        if (event.date == null ||
                event.date.trim().isEmpty()) {

            // If no date exists, don't assume upcoming.
            return false;
        }

        String value =
                event.date.trim();

        try {

            java.time.LocalDate date =
                    java.time.LocalDate.parse(
                            value
                    );

            return !date.isBefore(
                    java.time.LocalDate.now()
            );

        } catch (Exception ignored) {
        }

        // Common format: dd-MM-yyyy

        try {

            java.time.LocalDate date =
                    java.time.LocalDate.parse(
                            value,
                            java.time.format.DateTimeFormatter
                                    .ofPattern(
                                            "dd-MM-yyyy"
                                    )
                    );

            return !date.isBefore(
                    java.time.LocalDate.now()
            );

        } catch (Exception ignored) {
        }

        // Common format: dd/MM/yyyy

        try {

            java.time.LocalDate date =
                    java.time.LocalDate.parse(
                            value,
                            java.time.format.DateTimeFormatter
                                    .ofPattern(
                                            "dd/MM/yyyy"
                                    )
                    );

            return !date.isBefore(
                    java.time.LocalDate.now()
            );

        } catch (Exception ignored) {
        }

        /*
         * If the project uses a non-standard date string,
         * still allow it to appear rather than hiding valid
         * event data.
         */

        return true;
    }

    // ============================================================
    // REMOVE DUPLICATE EVENTS
    // ============================================================

    private void removeDuplicateEvents(
            List<EventData> events) {

        Set<String> keys =
                new HashSet<>();

        List<EventData> unique =
                new ArrayList<>();

        for (EventData event : events) {

            String key =
                    safe(event.id)
                            + "|"
                            + safe(event.title)
                            + "|"
                            + safe(event.date);

            if (keys.add(key)) {

                unique.add(event);
            }
        }

        events.clear();

        events.addAll(
                unique
        );
    }

    // ============================================================
    // SAFE STRING
    // ============================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        try {

            Object value =
                    document.get(field);

            if (value == null) {
                return "";
            }

            return value
                    .toString()
                    .trim();

        } catch (Exception e) {

            return "";
        }
    }

    // ============================================================
    // FIRST STRING
    // ============================================================

    private String firstString(
            DocumentSnapshot document,
            String... fields) {

        for (String field : fields) {

            String value =
                    getString(
                            document,
                            field
                    );

            if (!value.isEmpty()) {

                return value;
            }
        }

        return "";
    }

    // ============================================================
    // GET DATE
    // ============================================================

    private Date getDate(
            DocumentSnapshot document,
            String field) {

        try {

            Timestamp timestamp =
                    document.getTimestamp(
                            field
                    );

            if (timestamp != null) {

                return timestamp.toDate();
            }

        } catch (Exception ignored) {
        }

        try {

            Date date =
                    document.getDate(
                            field
                    );

            return date;

        } catch (Exception ignored) {
        }

        return null;
    }

    // ============================================================
    // SAFE
    // ============================================================

    private String safe(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    // ============================================================
    // SOS MODEL
    // ============================================================

    public static class SosAlertData {

        public String id = "";
        public String type = "";
        public String location = "";
        public String details = "";
        public String status = "";
        public String time = "";
        public String email = "";
        public String society = "";
        public Date createdAt;
    }

    // ============================================================
    // EVENT MODEL
    // ============================================================

    public static class EventData {

        public String id = "";
        public String title = "";
        public String date = "";
        public String time = "";
        public String location = "";
        public String description = "";
        public String society = "";
    }
}