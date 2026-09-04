package com.society.controller.Secretary_Controller;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.dao.Secretary_dao.manegedao;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Maintenance;

public class MaintenanceController {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore firestore;

    // =========================================================
    // DAO
    // =========================================================

    private final manegedao maintenanceDao;

    // =========================================================
    // COLLECTIONS
    // =========================================================

    private static final String SECRETARY_COLLECTION =
            "Secretaries";

    private static final String RESIDENT_COLLECTION =
            "Residents";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MaintenanceController() {

        this.firestore =
                FirebaseConfig.getFirestore();

        if (this.firestore == null) {

            throw new IllegalStateException(
                    "Firestore initialization failed. "
                    + "FirebaseConfig.getFirestore() returned null."
            );
        }

        this.maintenanceDao =
                new manegedao();

        System.out.println(
                "MaintenanceController initialized successfully."
        );
    }

    // =========================================================
    // GET CURRENT LOGGED-IN SECRETARY EMAIL
    // =========================================================

    private String getCurrentSecretaryEmail() {

        String secretaryEmail =
                UserDao.getLoggedInEmail();

        if (secretaryEmail == null ||
                secretaryEmail.trim().isEmpty()) {

            throw new IllegalStateException(
                    "No Secretary is currently logged in."
            );
        }

        return secretaryEmail
                .trim()
                .toLowerCase();
    }

    // =========================================================
    // GET CURRENT SECRETARY SOCIETY
    // =========================================================

    private String getCurrentSecretarySociety()
            throws Exception {

        String secretaryEmail =
                getCurrentSecretaryEmail();

        // -----------------------------------------------------
        // GET SECRETARY DOCUMENT
        // -----------------------------------------------------

        DocumentSnapshot secretaryDocument =
                firestore
                        .collection(
                                SECRETARY_COLLECTION
                        )
                        .document(
                                secretaryEmail
                        )
                        .get()
                        .get();

        // -----------------------------------------------------
        // CHECK DOCUMENT
        // -----------------------------------------------------

        if (!secretaryDocument.exists()) {

            throw new IllegalStateException(
                    "Secretary profile not found for email: "
                            + secretaryEmail
            );
        }

        // -----------------------------------------------------
        // GET SOCIETY
        // -----------------------------------------------------

        String society =
                getFirestoreString(
                        secretaryDocument,
                        "society"
                );

        if (society.isEmpty()) {

            throw new IllegalStateException(
                    "Society not found for Secretary: "
                            + secretaryEmail
            );
        }

        return society.trim();
    }

    // =========================================================
    // ADD MAINTENANCE
    // =========================================================
    //
    // IMPORTANT:
    //
    // Maintenance आता resident email खाली save होणार नाही.
    //
    // Firestore structure:
    //
    // Maintenance
    //    └── aditi@gmail.com
    //         └── records
    //              └── maintenanceId
    //
    // Secretary email आणि society automatically घेतली जातात.
    //
    // =========================================================

    public boolean addMaintenance(
            String amount,
            String month,
            String date,
            String status) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (amount == null ||
                    amount.trim().isEmpty()) {

                System.err.println(
                        "Maintenance amount is required."
                );

                return false;
            }

            if (month == null ||
                    month.trim().isEmpty()) {

                System.err.println(
                        "Maintenance month is required."
                );

                return false;
            }

            if (date == null ||
                    date.trim().isEmpty()) {

                System.err.println(
                        "Maintenance date is required."
                );

                return false;
            }

            if (status == null ||
                    status.trim().isEmpty()) {

                System.err.println(
                        "Maintenance status is required."
                );

                return false;
            }

            // =================================================
            // GET LOGGED-IN SECRETARY
            // =================================================

            String secretaryEmail =
                    getCurrentSecretaryEmail();

            // =================================================
            // GET SECRETARY SOCIETY
            // =================================================

            String society =
                    getCurrentSecretarySociety();

            // =================================================
            // DEBUG
            // =================================================

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "ADDING MAINTENANCE"
            );

            System.out.println(
                    "Secretary Email : "
                            + secretaryEmail
            );

            System.out.println(
                    "Secretary Society : "
                            + society
            );

            System.out.println(
                    "Amount : "
                            + amount.trim()
            );

            System.out.println(
                    "Month : "
                            + month.trim()
            );

            System.out.println(
                    "Date : "
                            + date.trim()
            );

            System.out.println(
                    "Status : "
                            + normalizeStatus(status)
            );

            System.out.println(
                    "=============================================="
            );

            // =================================================
            // CREATE MAINTENANCE
            // =================================================
            //
            // Resident email/name/flat intentionally empty.
            //
            // कारण:
            // Maintenance हा आता society-level record आहे.
            //
            // =================================================

            Maintenance maintenance =
                    new Maintenance(
                            "",
                            "",
                            "",
                            amount.trim(),
                            month.trim(),
                            date.trim(),
                            normalizeStatus(status),
                            secretaryEmail,
                            society
                    );

            // =================================================
            // SAVE
            // =================================================

            boolean success =
                    maintenanceDao.addMaintenance(
                            maintenance
                    );

            // =================================================
            // FINAL DEBUG
            // =================================================

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "MAINTENANCE ADD RESULT"
            );

            System.out.println(
                    "Secretary Email : "
                            + secretaryEmail
            );

            System.out.println(
                    "Society         : "
                            + society
            );

            System.out.println(
                    "Maintenance Added : "
                            + success
            );

            System.out.println(
                    "=============================================="
            );

            return success;

        } catch (Exception e) {

            System.err.println(
                    "Error while adding maintenance:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // OLD METHOD - COMPATIBILITY
    // =========================================================
    //
    // जर ManageMaintenance मध्ये अजून:
    //
    // addMaintenanceToAllResidents(...)
    //
    // call होत असेल तर application compile होण्यासाठी
    // हा method ठेवला आहे.
    //
    // पण आता तो residents वर loop करत नाही.
    //
    // तो फक्त एक society-level maintenance तयार करतो.
    //
    // =========================================================

    public boolean addMaintenanceToAllResidents(
            String amount,
            String month,
            String date,
            String status) {

        return addMaintenance(
                amount,
                month,
                date,
                status
        );
    }

    // =========================================================
    // OLD SINGLE RESIDENT METHOD - COMPATIBILITY
    // =========================================================
    //
    // हा method residentEmail वापरणार नाही.
    //
    // Maintenance नेहमी logged-in Secretary च्या
    // email आणि society खाली save होईल.
    //
    // =========================================================

    public boolean addMaintenance(
            String residentEmail,
            String residentName,
            String flatNo,
            String amount,
            String month,
            String date,
            String status) {

        return addMaintenance(
                amount,
                month,
                date,
                status
        );
    }

    // =========================================================
    // GET ALL MAINTENANCE
    // =========================================================
    //
    // फक्त logged-in Secretary च्या society चे
    // maintenance records return होतील.
    //
    // =========================================================

    public List<Maintenance> getAllMaintenance() {

        List<Maintenance> result =
                new ArrayList<>();

        try {

            // -------------------------------------------------
            // CURRENT SECRETARY
            // -------------------------------------------------

            String secretaryEmail =
                    getCurrentSecretaryEmail();

            String secretarySociety =
                    getCurrentSecretarySociety();

            // -------------------------------------------------
            // FETCH ALL MAINTENANCE
            // -------------------------------------------------

            List<Maintenance> allMaintenance =
                    maintenanceDao.getAllMaintenance();

            if (allMaintenance == null ||
                    allMaintenance.isEmpty()) {

                System.out.println(
                        "No maintenance records found."
                );

                return result;
            }

            // -------------------------------------------------
            // FILTER
            // -------------------------------------------------

            for (Maintenance maintenance :
                    allMaintenance) {

                if (maintenance == null) {
                    continue;
                }

                String maintenanceSociety =
                        maintenance.getSociety();

                if (maintenanceSociety == null ||
                        maintenanceSociety.trim().isEmpty()) {

                    continue;
                }

                // -------------------------------------------------
                // SOCIETY MATCH
                // -------------------------------------------------

                if (!maintenanceSociety
                        .trim()
                        .equalsIgnoreCase(
                                secretarySociety.trim()
                        )) {

                    continue;
                }

                result.add(
                        maintenance
                );
            }

            // -------------------------------------------------
            // DEBUG
            // -------------------------------------------------

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "GET SOCIETY MAINTENANCE"
            );

            System.out.println(
                    "Secretary Email : "
                            + secretaryEmail
            );

            System.out.println(
                    "Secretary Society : "
                            + secretarySociety
            );

            System.out.println(
                    "Maintenance Found : "
                            + result.size()
            );

            System.out.println(
                    "=============================================="
            );

        } catch (Exception e) {

            System.err.println(
                    "Error while fetching society maintenance:"
            );

            e.printStackTrace();
        }

        return result;
    }

    // =========================================================
    // GET MAINTENANCE BY STATUS
    // =========================================================

    public List<Maintenance> getMaintenanceByStatus(
            String status) {

        List<Maintenance> result =
                new ArrayList<>();

        try {

            if (status == null ||
                    status.trim().isEmpty()) {

                return result;
            }

            String requiredStatus =
                    normalizeStatus(status);

            List<Maintenance> allMaintenance =
                    getAllMaintenance();

            for (Maintenance maintenance :
                    allMaintenance) {

                if (maintenance == null) {
                    continue;
                }

                String maintenanceStatus =
                        maintenance.getStatus();

                if (maintenanceStatus == null ||
                        maintenanceStatus.trim().isEmpty()) {

                    continue;
                }

                if (maintenanceStatus
                        .trim()
                        .equalsIgnoreCase(
                                requiredStatus
                        )) {

                    result.add(
                            maintenance
                    );
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error while fetching maintenance by status:"
            );

            e.printStackTrace();
        }

        return result;
    }

    // =========================================================
    // GET MAINTENANCE BY RESIDENT EMAIL
    // =========================================================
    //
    // IMPORTANT:
    //
    // नवीन structure मध्ये maintenance resident email
    // खाली store होत नाही.
    //
    // त्यामुळे resident email वापरून direct Firestore path
    // शोधणे चुकीचे आहे.
    //
    // Resident ची society शोधून त्या society चे maintenance
    // records return केले जातील.
    //
    // हा method Resident-side compatibility साठी useful आहे.
    //
    // =========================================================

    public List<Maintenance> getMaintenanceByEmail(
            String residentEmail) {

        List<Maintenance> result =
                new ArrayList<>();

        try {

            if (residentEmail == null ||
                    residentEmail.trim().isEmpty()) {

                return result;
            }

            residentEmail =
                    residentEmail
                            .trim()
                            .toLowerCase();

            // -------------------------------------------------
            // GET RESIDENT
            // -------------------------------------------------

            DocumentSnapshot residentDocument =
                    firestore
                            .collection(
                                    RESIDENT_COLLECTION
                            )
                            .document(
                                    residentEmail
                            )
                            .get()
                            .get();

            if (!residentDocument.exists()) {

                System.err.println(
                        "Resident not found: "
                                + residentEmail
                );

                return result;
            }

            // -------------------------------------------------
            // GET RESIDENT SOCIETY
            // -------------------------------------------------

            String residentSociety =
                    getFirestoreString(
                            residentDocument,
                            "society"
                    );

            if (residentSociety.isEmpty()) {

                System.err.println(
                        "Resident society not found for: "
                                + residentEmail
                );

                return result;
            }

            // -------------------------------------------------
            // FETCH ALL MAINTENANCE
            // -------------------------------------------------

            List<Maintenance> allMaintenance =
                    maintenanceDao.getAllMaintenance();

            // -------------------------------------------------
            // FILTER BY RESIDENT SOCIETY
            // -------------------------------------------------

            for (Maintenance maintenance :
                    allMaintenance) {

                if (maintenance == null) {
                    continue;
                }

                String society =
                        maintenance.getSociety();

                if (society == null ||
                        society.trim().isEmpty()) {

                    continue;
                }

                if (society
                        .trim()
                        .equalsIgnoreCase(
                                residentSociety.trim()
                        )) {

                    result.add(
                            maintenance
                    );
                }
            }

            System.out.println(
                    "=============================================="
            );

            System.out.println(
                    "RESIDENT MAINTENANCE FETCH"
            );

            System.out.println(
                    "Resident Email : "
                            + residentEmail
            );

            System.out.println(
                    "Resident Society : "
                            + residentSociety
            );

            System.out.println(
                    "Maintenance Found : "
                            + result.size()
            );

            System.out.println(
                    "=============================================="
            );

        } catch (Exception e) {

            System.err.println(
                    "Error while fetching maintenance for resident:"
            );

            e.printStackTrace();
        }

        return result;
    }

    // =========================================================
    // UPDATE MAINTENANCE STATUS
    // =========================================================
    //
    // NEW structure:
    //
    // Maintenance/{secretaryEmail}/records/{maintenanceId}
    //
    // म्हणून residentEmail वरून update करू नये.
    //
    // Existing UI जर residentEmail पाठवत असेल तर
    // maintenanceId शोधून Secretary email काढला जाईल.
    //
    // =========================================================

    public boolean updateMaintenanceStatus(
            String residentEmail,
            String maintenanceId,
            String newStatus) {

        try {

            if (maintenanceId == null ||
                    maintenanceId.trim().isEmpty()) {

                System.err.println(
                        "Maintenance ID is required."
                );

                return false;
            }

            if (newStatus == null ||
                    newStatus.trim().isEmpty()) {

                System.err.println(
                        "New status is required."
                );

                return false;
            }

            // -------------------------------------------------
            // CURRENT SECRETARY
            // -------------------------------------------------

            String secretaryEmail =
                    getCurrentSecretaryEmail();

            String secretarySociety =
                    getCurrentSecretarySociety();

            // -------------------------------------------------
            // FIND MAINTENANCE
            // -------------------------------------------------

            Maintenance maintenance =
                    findMaintenanceById(
                            maintenanceId.trim()
                    );

            if (maintenance == null) {

                System.err.println(
                        "Maintenance record not found: "
                                + maintenanceId
                );

                return false;
            }

            // -------------------------------------------------
            // SOCIETY CHECK
            // -------------------------------------------------

            String maintenanceSociety =
                    maintenance.getSociety();

            if (maintenanceSociety == null ||
                    maintenanceSociety.trim().isEmpty()) {

                System.err.println(
                        "Maintenance society is missing."
                );

                return false;
            }

            if (!maintenanceSociety
                    .trim()
                    .equalsIgnoreCase(
                            secretarySociety.trim()
                    )) {

                System.err.println(
                        "Unauthorized maintenance update."
                );

                return false;
            }

            // -------------------------------------------------
            // SECRETARY CHECK
            // -------------------------------------------------

            String addedBySecretary =
                    maintenance
                            .getAddedBySecretaryEmail();

            if (addedBySecretary == null ||
                    addedBySecretary.trim().isEmpty()) {

                System.err.println(
                        "Maintenance secretary email is missing."
                );

                return false;
            }

            if (!addedBySecretary
                    .trim()
                    .equalsIgnoreCase(
                            secretaryEmail
                    )) {

                System.err.println(
                        "Maintenance was not created "
                                + "by current Secretary."
                );

                return false;
            }

            // -------------------------------------------------
            // UPDATE
            // -------------------------------------------------

            return maintenanceDao
                    .updateMaintenanceStatus(
                            secretaryEmail,
                            maintenanceId.trim(),
                            normalizeStatus(
                                    newStatus
                            )
                    );

        } catch (Exception e) {

            System.err.println(
                    "Error while updating maintenance status:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // FIND MAINTENANCE BY ID
    // =========================================================

    private Maintenance findMaintenanceById(
            String maintenanceId) {

        try {

            List<Maintenance> allMaintenance =
                    maintenanceDao.getAllMaintenance();

            if (allMaintenance == null ||
                    allMaintenance.isEmpty()) {

                return null;
            }

            for (Maintenance maintenance :
                    allMaintenance) {

                if (maintenance == null) {
                    continue;
                }

                // -------------------------------------------------
                // DAO model मध्ये ID असल्यास check करा.
                // Current model मध्ये ID field नसेल तर
                // खालील society/secretary matching पुरेसे नाही.
                //
                // त्यामुळे direct Firestore search वापरतो.
                // -------------------------------------------------

            }

            // -------------------------------------------------
            // SEARCH DIRECTLY IN FIRESTORE
            // -------------------------------------------------

            QuerySnapshot secretarySnapshot =
                    firestore
                            .collection(
                                    "Maintenance"
                            )
                            .get()
                            .get();

            for (DocumentSnapshot secretaryDocument :
                    secretarySnapshot.getDocuments()) {

                if (secretaryDocument == null ||
                        !secretaryDocument.exists()) {

                    continue;
                }

                String secretaryEmail =
                        secretaryDocument.getId();

                QuerySnapshot recordsSnapshot =
                        secretaryDocument
                                .getReference()
                                .collection(
                                        "records"
                                )
                                .get()
                                .get();

                for (DocumentSnapshot record :
                        recordsSnapshot.getDocuments()) {

                    if (record == null ||
                            !record.exists()) {

                        continue;
                    }

                    String id =
                            getFirestoreString(
                                    record,
                                    "maintenanceId"
                            );

                    if (id.isEmpty()) {
                        id = record.getId();
                    }

                    if (id.equals(
                            maintenanceId
                    )) {

                        String amount =
                                getFirestoreString(
                                        record,
                                        "amount"
                                );

                        String month =
                                getFirestoreString(
                                        record,
                                        "month"
                                );

                        String date =
                                getFirestoreString(
                                        record,
                                        "date"
                                );

                        String status =
                                normalizeStatus(
                                        getFirestoreString(
                                                record,
                                                "status"
                                        )
                                );

                        String addedBySecretary =
                                getFirestoreString(
                                        record,
                                        "addedBySecretaryEmail"
                                );

                        if (addedBySecretary.isEmpty()) {

                            addedBySecretary =
                                    secretaryEmail;
                        }

                        String society =
                                getFirestoreString(
                                        record,
                                        "society"
                                );

                        return new Maintenance(
                                "",
                                "",
                                "",
                                amount,
                                month,
                                date,
                                status,
                                addedBySecretary,
                                society
                        );
                    }
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error while finding maintenance by ID:"
            );

            e.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // GET SINGLE MAINTENANCE
    // =========================================================
    //
    // Existing code compatibility.
    //
    // residentEmail आता path म्हणून वापरला जात नाही.
    //
    // =========================================================

    public Maintenance getMaintenance(
            String residentEmail,
            String maintenanceId) {

        try {

            if (maintenanceId == null ||
                    maintenanceId.trim().isEmpty()) {

                return null;
            }

            Maintenance maintenance =
                    findMaintenanceById(
                            maintenanceId.trim()
                    );

            if (maintenance == null) {

                return null;
            }

            // -------------------------------------------------
            // CURRENT SECRETARY SOCIETY
            // -------------------------------------------------

            String secretarySociety =
                    getCurrentSecretarySociety();

            String maintenanceSociety =
                    maintenance.getSociety();

            if (maintenanceSociety == null ||
                    maintenanceSociety.trim().isEmpty()) {

                return null;
            }

            if (!maintenanceSociety
                    .trim()
                    .equalsIgnoreCase(
                            secretarySociety.trim()
                    )) {

                System.err.println(
                        "Unauthorized maintenance access."
                );

                return null;
            }

            return maintenance;

        } catch (Exception e) {

            System.err.println(
                    "Error while getting maintenance:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET RESIDENT EMAILS OF SECRETARY SOCIETY
    // =========================================================
    //
    // हा method आता maintenance save करण्यासाठी वापरला जात नाही.
    //
    // फक्त future use/debug साठी.
    //
    // =========================================================

    public List<String>
    getResidentEmailsOfSecretarySociety() {

        List<String> emails =
                new ArrayList<>();

        try {

            String society =
                    getCurrentSecretarySociety();

            QuerySnapshot snapshot =
                    firestore
                            .collection(
                                    RESIDENT_COLLECTION
                            )
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                String residentSociety =
                        getFirestoreString(
                                document,
                                "society"
                        );

                if (residentSociety.isEmpty()) {
                    continue;
                }

                if (!residentSociety
                        .trim()
                        .equalsIgnoreCase(
                                society.trim()
                        )) {

                    continue;
                }

                String email =
                        getFirestoreString(
                                document,
                                "email"
                        );

                if (email.isEmpty()) {

                    email =
                            document.getId();
                }

                if (email == null ||
                        email.trim().isEmpty()) {

                    continue;
                }

                email =
                        email
                                .trim()
                                .toLowerCase();

                if (!emails.contains(email)) {

                    emails.add(email);
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error while getting resident emails:"
            );

            e.printStackTrace();
        }

        return emails;
    }

    // =========================================================
    // GET FIRESTORE STRING
    // =========================================================

    private String getFirestoreString(
            DocumentSnapshot document,
            String field) {

        try {

            if (document == null ||
                    field == null ||
                    field.trim().isEmpty()) {

                return "";
            }

            Object value =
                    document.get(field);

            if (value == null) {

                return "";
            }

            return String
                    .valueOf(value)
                    .trim();

        } catch (Exception e) {

            return "";
        }
    }

    // =========================================================
    // NORMALIZE STATUS
    // =========================================================

    private String normalizeStatus(
            String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return "Pending";
        }

        String value =
                status.trim();

        if (value.equalsIgnoreCase(
                "pending")) {

            return "Pending";
        }

        if (value.equalsIgnoreCase(
                "paid")) {

            return "Paid";
        }

        if (value.equalsIgnoreCase(
                "overdue")) {

            return "Overdue";
        }

        if (value.equalsIgnoreCase(
                "open")) {

            return "Pending";
        }

        if (value.equalsIgnoreCase(
                "in progress")) {

            return "In Progress";
        }

        return value;
    }
}