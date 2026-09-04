package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.Maintenance;

public class manegedao {

    // =========================================================
    // FIRESTORE COLLECTIONS
    // =========================================================

    private static final String MAIN_COLLECTION = "Maintenance";
    private static final String RECORDS_COLLECTION = "records";
    private static final String RESIDENTS_COLLECTION = "Residents";

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore firestore;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public manegedao() {

        this.firestore = FirebaseConfig.getFirestore();

        if (this.firestore == null) {

            throw new IllegalStateException(
                    "Firestore initialization failed. "
                    + "FirebaseConfig.getFirestore() returned null."
            );
        }

        System.out.println(
                "Maintenance DAO initialized successfully."
        );
    }

    // =========================================================
    // ADD MAINTENANCE
    // =========================================================
    //
    // Firestore:
    //
    // Maintenance
    //    └── secretaryEmail
    //         └── records
    //              └── maintenanceId
    //
    // =========================================================

    public boolean addMaintenance(Maintenance maintenance) {

        try {

            if (maintenance == null) {

                System.err.println(
                        "Maintenance object is null."
                );

                return false;
            }

            // -------------------------------------------------
            // SECRETARY EMAIL
            // -------------------------------------------------

            String secretaryEmail =
                    safeValue(
                            maintenance.getAddedBySecretaryEmail()
                    );

            if (secretaryEmail.isEmpty()) {

                System.err.println(
                        "Secretary email is required."
                );

                return false;
            }

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            // -------------------------------------------------
            // SOCIETY
            // -------------------------------------------------

            String society =
                    safeValue(
                            maintenance.getSociety()
                    );

            if (society.isEmpty()) {

                System.err.println(
                        "Society is required."
                );

                return false;
            }

            society = society.trim();

            // -------------------------------------------------
            // SECRETARY DOCUMENT
            // -------------------------------------------------

            DocumentReference secretaryDocument =
                    firestore
                            .collection(MAIN_COLLECTION)
                            .document(secretaryEmail);

            // -------------------------------------------------
            // CREATE / UPDATE PARENT DOCUMENT
            // -------------------------------------------------

            Map<String, Object> secretaryData =
                    new HashMap<>();

            secretaryData.put(
                    "email",
                    secretaryEmail
            );

            secretaryData.put(
                    "society",
                    society
            );

            secretaryDocument
                    .set(secretaryData)
                    .get();

            // -------------------------------------------------
            // RECORDS COLLECTION
            // -------------------------------------------------

            CollectionReference records =
                    secretaryDocument
                            .collection(RECORDS_COLLECTION);

            // -------------------------------------------------
            // NEW DOCUMENT
            // -------------------------------------------------

            DocumentReference document =
                    records.document();

            String maintenanceId =
                    document.getId();

            // -------------------------------------------------
            // DATA
            // -------------------------------------------------

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "maintenanceId",
                    maintenanceId
            );

            data.put(
                    "amount",
                    safeValue(
                            maintenance.getAmount()
                    )
            );

            data.put(
                    "month",
                    safeValue(
                            maintenance.getMonth()
                    )
            );

            data.put(
                    "date",
                    safeValue(
                            maintenance.getDate()
                    )
            );

            data.put(
                    "status",
                    normalizeStatus(
                            maintenance.getStatus()
                    )
            );

            data.put(
                    "addedBySecretaryEmail",
                    secretaryEmail
            );

            data.put(
                    "society",
                    society
            );

            // -------------------------------------------------
            // DEBUG
            // -------------------------------------------------

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "SAVING MAINTENANCE"
            );

            System.out.println(
                    "Secretary Email = "
                            + secretaryEmail
            );

            System.out.println(
                    "Society = "
                            + society
            );

            System.out.println(
                    "Amount = "
                            + maintenance.getAmount()
            );

            System.out.println(
                    "Month = "
                            + maintenance.getMonth()
            );

            System.out.println(
                    "Date = "
                            + maintenance.getDate()
            );

            System.out.println(
                    "Status = "
                            + normalizeStatus(
                                    maintenance.getStatus()
                            )
            );

            System.out.println(
                    "Maintenance ID = "
                            + maintenanceId
            );

            System.out.println(
                    "Firestore Path = Maintenance/"
                            + secretaryEmail
                            + "/records/"
                            + maintenanceId
            );

            System.out.println(
                    "=========================================="
            );

            // -------------------------------------------------
            // SAVE
            // -------------------------------------------------

            document
                    .set(data)
                    .get();

            System.out.println(
                    "Maintenance added successfully."
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Error while adding maintenance:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // SECRETARY:
    // GET MAINTENANCE BY SECRETARY EMAIL + SOCIETY
    // =========================================================

    public List<Maintenance>
    getMaintenanceBySecretaryEmailAndSociety(
            String secretaryEmail,
            String secretarySociety) {

        List<Maintenance> list =
                new ArrayList<>();

        try {

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                System.err.println(
                        "Secretary email is empty."
                );

                return list;
            }

            if (secretarySociety == null ||
                    secretarySociety.trim().isEmpty()) {

                System.err.println(
                        "Secretary society is empty."
                );

                return list;
            }

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            secretarySociety =
                    secretarySociety.trim();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "FETCHING SECRETARY MAINTENANCE"
            );

            System.out.println(
                    "Secretary Email = "
                            + secretaryEmail
            );

            System.out.println(
                    "Secretary Society = "
                            + secretarySociety
            );

            CollectionReference records =
                    firestore
                            .collection(MAIN_COLLECTION)
                            .document(secretaryEmail)
                            .collection(RECORDS_COLLECTION);

            QuerySnapshot snapshot =
                    records
                            .get()
                            .get();

            System.out.println(
                    "Records found = "
                            + snapshot.size()
            );

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                String recordEmail =
                        getString(
                                document,
                                "addedBySecretaryEmail"
                        );

                String recordSociety =
                        getString(
                                document,
                                "society"
                        );

                boolean emailMatch =
                        recordEmail
                                .equalsIgnoreCase(
                                        secretaryEmail
                                );

                boolean societyMatch =
                        recordSociety
                                .equalsIgnoreCase(
                                        secretarySociety
                                );

                if (!emailMatch ||
                        !societyMatch) {

                    continue;
                }

                Maintenance maintenance =
                        documentToMaintenance(
                                document,
                                secretaryEmail
                        );

                if (maintenance != null) {

                    list.add(
                            maintenance
                    );
                }
            }

            System.out.println(
                    "Matching maintenance = "
                            + list.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Error while fetching secretary maintenance:"
            );

            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // GET MAINTENANCE BY SECRETARY EMAIL
    // =========================================================

    public List<Maintenance>
    getMaintenanceBySecretaryEmail(
            String secretaryEmail) {

        List<Maintenance> list =
                new ArrayList<>();

        try {

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                return list;
            }

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            CollectionReference records =
                    firestore
                            .collection(MAIN_COLLECTION)
                            .document(secretaryEmail)
                            .collection(RECORDS_COLLECTION);

            QuerySnapshot snapshot =
                    records
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                Maintenance maintenance =
                        documentToMaintenance(
                                document,
                                secretaryEmail
                        );

                if (maintenance != null) {

                    list.add(
                            maintenance
                    );
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error while fetching maintenance by secretary:"
            );

            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // RESIDENT:
    // GET MAINTENANCE BY RESIDENT EMAIL
    // =========================================================
    //
    // IMPORTANT:
    //
    // Resident email is NOT used as Maintenance document ID.
    //
    // First:
    //
    // Residents/{residentEmail}
    //
    // get society
    //
    // Then:
    //
    // Maintenance/{secretaryEmail}/records/*
    //
    // filter:
    //
    // maintenance.society == resident.society
    //
    // collectionGroup is used so records are found even if
    // some parent Maintenance documents were previously missing.
    //
    // =========================================================

    public List<Maintenance>
    getMaintenanceByEmail(
            String residentEmail) {

        List<Maintenance> list =
                new ArrayList<>();

        try {

            // -------------------------------------------------
            // VALIDATE RESIDENT EMAIL
            // -------------------------------------------------

            if (residentEmail == null ||
                    residentEmail.trim().isEmpty()) {

                System.err.println(
                        "Resident email is empty."
                );

                return list;
            }

            residentEmail =
                    residentEmail
                            .trim()
                            .toLowerCase();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "FETCHING RESIDENT MAINTENANCE"
            );

            System.out.println(
                    "Resident Email = "
                            + residentEmail
            );

            // -------------------------------------------------
            // GET RESIDENT DOCUMENT
            // -------------------------------------------------

            DocumentReference residentReference =
                    firestore
                            .collection(
                                    RESIDENTS_COLLECTION
                            )
                            .document(
                                    residentEmail
                            );

            DocumentSnapshot residentDocument =
                    residentReference
                            .get()
                            .get();

            // -------------------------------------------------
            // CHECK RESIDENT
            // -------------------------------------------------

            if (!residentDocument.exists()) {

                System.err.println(
                        "Resident document not found."
                );

                System.err.println(
                        "Path = Residents/"
                                + residentEmail
                );

                return list;
            }

            // -------------------------------------------------
            // GET SOCIETY
            // -------------------------------------------------

            String residentSociety =
                    getString(
                            residentDocument,
                            "society"
                    );

            if (residentSociety.isEmpty()) {

                System.err.println(
                        "Society not found for resident."
                );

                System.err.println(
                        "Resident Email = "
                                + residentEmail
                );

                return list;
            }

            residentSociety =
                    residentSociety.trim();

            System.out.println(
                    "Resident Society = "
                            + residentSociety
            );

            // -------------------------------------------------
            // FETCH ALL RECORDS
            // -------------------------------------------------
            //
            // records is the subcollection name:
            //
            // Maintenance/{secretaryEmail}/records
            //
            // -------------------------------------------------

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collectionGroup(
                                    RECORDS_COLLECTION
                            )
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            System.out.println(
                    "Total maintenance records = "
                            + snapshot.size()
            );

            // -------------------------------------------------
            // FILTER BY SOCIETY
            // -------------------------------------------------

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                // -------------------------------------------------
                // RECORD SOCIETY
                // -------------------------------------------------

                String maintenanceSociety =
                        getString(
                                document,
                                "society"
                        );

                if (maintenanceSociety.isEmpty()) {

                    continue;
                }

                // -------------------------------------------------
                // SOCIETY MATCH
                // -------------------------------------------------

                boolean societyMatch =
                        maintenanceSociety
                                .trim()
                                .equalsIgnoreCase(
                                        residentSociety
                                );

                if (!societyMatch) {

                    continue;
                }

                // -------------------------------------------------
                // SECRETARY EMAIL
                // -------------------------------------------------

                String secretaryEmail =
                        getString(
                                document,
                                "addedBySecretaryEmail"
                        );

                // -------------------------------------------------
                // FALLBACK SECRETARY EMAIL
                // -------------------------------------------------

                if (secretaryEmail.isEmpty()) {

                    try {

                        secretaryEmail =
                                document
                                        .getReference()
                                        .getParent()
                                        .getParent()
                                        .getId();

                    } catch (Exception e) {

                        secretaryEmail = "";
                    }
                }

                // -------------------------------------------------
                // CONVERT
                // -------------------------------------------------

                Maintenance maintenance =
                        documentToMaintenance(
                                document,
                                secretaryEmail
                        );

                if (maintenance != null) {

                    list.add(
                            maintenance
                    );

                    System.out.println(
                            "Resident maintenance matched:"
                    );

                    System.out.println(
                            "Maintenance ID = "
                                    + maintenance
                                            .getMaintenanceId()
                    );

                    System.out.println(
                            "Society = "
                                    + maintenance
                                            .getSociety()
                    );

                    System.out.println(
                            "Secretary = "
                                    + maintenance
                                            .getAddedBySecretaryEmail()
                    );
                }
            }

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "TOTAL MAINTENANCE FOR RESIDENT SOCIETY = "
                            + list.size()
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.err.println(
                    "Error while fetching maintenance by resident email:"
            );

            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // GET ALL MAINTENANCE
    // =========================================================

    public List<Maintenance> getAllMaintenance() {

        List<Maintenance> list =
                new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collectionGroup(
                                    RECORDS_COLLECTION
                            )
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                if (document == null ||
                        !document.exists()) {

                    continue;
                }

                String secretaryEmail =
                        getString(
                                document,
                                "addedBySecretaryEmail"
                        );

                if (secretaryEmail.isEmpty()) {

                    try {

                        secretaryEmail =
                                document
                                        .getReference()
                                        .getParent()
                                        .getParent()
                                        .getId();

                    } catch (Exception e) {

                        secretaryEmail = "";
                    }
                }

                Maintenance maintenance =
                        documentToMaintenance(
                                document,
                                secretaryEmail
                        );

                if (maintenance != null) {

                    list.add(
                            maintenance
                    );
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Error while fetching all maintenance:"
            );

            e.printStackTrace();
        }

        return list;
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    public boolean updateMaintenanceStatus(
            String secretaryEmail,
            String maintenanceId,
            String newStatus) {

        try {

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                return false;
            }

            if (maintenanceId == null ||
                    maintenanceId.trim().isEmpty()) {

                return false;
            }

            if (newStatus == null ||
                    newStatus.trim().isEmpty()) {

                return false;
            }

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            maintenanceId =
                    maintenanceId.trim();

            newStatus =
                    normalizeStatus(
                            newStatus
                    );

            DocumentReference ref =
                    firestore
                            .collection(MAIN_COLLECTION)
                            .document(secretaryEmail)
                            .collection(RECORDS_COLLECTION)
                            .document(maintenanceId);

            DocumentSnapshot snapshot =
                    ref.get()
                            .get();

            if (!snapshot.exists()) {

                System.err.println(
                        "Maintenance record not found."
                );

                return false;
            }

            ref.update(
                    "status",
                    newStatus
            ).get();

            System.out.println(
                    "Maintenance status updated."
            );

            return true;

        } catch (Exception e) {

            System.err.println(
                    "Error while updating maintenance status:"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ONE MAINTENANCE
    // =========================================================

    public Maintenance getMaintenance(
            String secretaryEmail,
            String maintenanceId) {

        try {

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                return null;
            }

            if (maintenanceId == null ||
                    maintenanceId.trim().isEmpty()) {

                return null;
            }

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            maintenanceId =
                    maintenanceId.trim();

            DocumentSnapshot document =
                    firestore
                            .collection(MAIN_COLLECTION)
                            .document(secretaryEmail)
                            .collection(RECORDS_COLLECTION)
                            .document(maintenanceId)
                            .get()
                            .get();

            if (!document.exists()) {

                return null;
            }

            return documentToMaintenance(
                    document,
                    secretaryEmail
            );

        } catch (Exception e) {

            System.err.println(
                    "Error while getting maintenance:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // DOCUMENT -> MAINTENANCE
    // =========================================================

    private Maintenance documentToMaintenance(
            DocumentSnapshot document,
            String secretaryEmail) {

        try {

            if (document == null ||
                    !document.exists()) {

                return null;
            }

            // -------------------------------------------------
            // ID
            // -------------------------------------------------

            String maintenanceId =
                    getString(
                            document,
                            "maintenanceId"
                    );

            if (maintenanceId.isEmpty()) {

                maintenanceId =
                        document.getId();
            }

            // -------------------------------------------------
            // AMOUNT
            // -------------------------------------------------

            String amount =
                    getString(
                            document,
                            "amount"
                    );

            // -------------------------------------------------
            // MONTH
            // -------------------------------------------------

            String month =
                    getString(
                            document,
                            "month"
                    );

            // -------------------------------------------------
            // DATE
            // -------------------------------------------------

            String date =
                    getString(
                            document,
                            "date"
                    );

            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            String status =
                    normalizeStatus(
                            getString(
                                    document,
                                    "status"
                            )
                    );

            // -------------------------------------------------
            // SECRETARY EMAIL
            // -------------------------------------------------

            String addedBySecretaryEmail =
                    getString(
                            document,
                            "addedBySecretaryEmail"
                    );

            if (addedBySecretaryEmail.isEmpty()) {

                addedBySecretaryEmail =
                        secretaryEmail;
            }

            addedBySecretaryEmail =
                    addedBySecretaryEmail
                            .trim()
                            .toLowerCase();

            // -------------------------------------------------
            // SOCIETY
            // -------------------------------------------------

            String society =
                    getString(
                            document,
                            "society"
                    );

            // -------------------------------------------------
            // MODEL
            // -------------------------------------------------

            return new Maintenance(
                    maintenanceId,
                    amount,
                    month,
                    date,
                    status,
                    addedBySecretaryEmail,
                    society
            );

        } catch (Exception e) {

            System.err.println(
                    "Error converting document to Maintenance:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET STRING
    // =========================================================

    private String getString(
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

            return String.valueOf(
                    value
            ).trim();

        } catch (Exception e) {

            return "";
        }
    }

    // =========================================================
    // SAFE VALUE
    // =========================================================

    private String safeValue(
            String value) {

        if (value == null) {

            return "";
        }

        return value.trim();
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

        if (value.equalsIgnoreCase("pending")) {

            return "Pending";
        }

        if (value.equalsIgnoreCase("paid")) {

            return "Paid";
        }

        if (value.equalsIgnoreCase("overdue")) {

            return "Overdue";
        }

        if (value.equalsIgnoreCase("unpaid")) {

            return "Pending";
        }

        if (value.equalsIgnoreCase("open")) {

            return "Pending";
        }

        if (value.equalsIgnoreCase("in progress")) {

            return "In Progress";
        }

        return value;
    }
}