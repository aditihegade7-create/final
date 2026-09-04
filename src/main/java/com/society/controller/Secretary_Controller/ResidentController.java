package com.society.controller.Secretary_Controller;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

import com.society.config.FirebaseConfig;
import com.society.dao.Secretary_dao.ResidentDao;
import com.society.dao.Secretary_dao.ResidentDaoImpl;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Resident;

public class ResidentController {

    // =========================================================
    // DAO
    // =========================================================

    private final ResidentDao residentDao;

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore firestore;

    // =========================================================
    // COLLECTIONS
    // =========================================================

    private static final String SECRETARIES =
            "Secretaries";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ResidentController() {

        residentDao =
                new ResidentDaoImpl();

        firestore =
                FirebaseConfig.getFirestore();
    }

    // =========================================================
    // ADD / UPDATE RESIDENT
    //
    // IMPORTANT:
    // Society is NOT taken from UI.
    //
    // Society comes from logged-in Secretary.
    // =========================================================

    public boolean addResident(
            String name,
            String flat,
            String mobile,
            String email,
            String status
    ) {

        try {

            // =================================================
            // 1. GET LOGGED-IN SECRETARY EMAIL
            // =================================================

            String secretaryEmail =
                    UserDao.getLoggedInEmail();

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Logged-in Secretary email not found."
                );

                return false;
            }

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            System.out.println(
                    "Logged-in Secretary = "
                            + secretaryEmail
            );

            // =================================================
            // 2. GET SECRETARY DOCUMENT
            //
            // Secretaries/{secretaryEmail}
            // =================================================

            DocumentSnapshot secretaryDocument =
                    firestore
                            .collection(SECRETARIES)
                            .document(secretaryEmail)
                            .get()
                            .get();

            if (!secretaryDocument.exists()) {

                System.out.println(
                        "ERROR: Secretary document not found."
                );

                System.out.println(
                        "Document = "
                                + secretaryEmail
                );

                return false;
            }

            // =================================================
            // 3. GET SECRETARY SOCIETY
            // =================================================

            String secretarySociety =
                    secretaryDocument
                            .getString("society");

            if (secretarySociety == null ||
                    secretarySociety.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Secretary society not found."
                );

                return false;
            }

            secretarySociety =
                    secretarySociety.trim();

            System.out.println(
                    "Secretary Society = ["
                            + secretarySociety
                            + "]"
            );

            // =================================================
            // 4. CLEAN RESIDENT EMAIL
            // =================================================

            String residentEmail =
                    cleanEmail(email);

            if (residentEmail.isEmpty()) {

                System.out.println(
                        "ERROR: Resident email is required."
                );

                return false;
            }

            // =================================================
            // 5. CREATE RESIDENT
            // =================================================

            Resident resident =
                    new Resident();

            resident.setName(
                    clean(name)
            );

            resident.setFlatNo(
                    clean(flat)
            );

            resident.setPhone(
                    clean(mobile)
            );

            resident.setEmail(
                    residentEmail
            );

            resident.setStatus(
                    clean(status)
            );

            // =================================================
            // VERY IMPORTANT
            //
            // Society automatically comes from Secretary.
            // =================================================

            resident.setSociety(
                    secretarySociety
            );

            // =================================================
            // 6. SAVE
            // =================================================

            boolean saved =
                    residentDao.addResident(
                            resident
                    );

            if (saved) {

                System.out.println(
                        "=========================================="
                );

                System.out.println(
                        "RESIDENT SAVED SUCCESSFULLY"
                );

                System.out.println(
                        "Email   = "
                                + residentEmail
                );

                System.out.println(
                        "Society = "
                                + secretarySociety
                );

                System.out.println(
                        "=========================================="
                );
            }

            return saved;

        } catch (Exception e) {

            System.out.println(
                    "addResident ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET LOGGED-IN SECRETARY SOCIETY
    // =========================================================

    public String getLoggedInSecretarySociety() {

        try {

            // -------------------------------------------------
            // GET EMAIL
            // -------------------------------------------------

            String secretaryEmail =
                    UserDao.getLoggedInEmail();

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Secretary email not found."
                );

                return "";
            }

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            // -------------------------------------------------
            // GET SECRETARY DOCUMENT
            // -------------------------------------------------

            DocumentSnapshot document =
                    firestore
                            .collection(SECRETARIES)
                            .document(secretaryEmail)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "ERROR: Secretary document not found."
                );

                return "";
            }

            // -------------------------------------------------
            // SOCIETY
            // -------------------------------------------------

            String society =
                    document.getString("society");

            if (society == null ||
                    society.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Society field missing."
                );

                return "";
            }

            return society.trim();

        } catch (Exception e) {

            System.out.println(
                    "getLoggedInSecretarySociety ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return "";
        }
    }

    // =========================================================
    // GET RESIDENTS FOR LOGGED-IN SECRETARY
    //
    // THIS METHOD MUST BE USED BY SECRETARY UI.
    // =========================================================

    public List<Resident> getResidentsBySociety() {

        List<Resident> finalResidents =
                new ArrayList<>();

        try {

            // =================================================
            // 1. GET SECRETARY SOCIETY
            // =================================================

            String secretarySociety =
                    getLoggedInSecretarySociety();

            if (secretarySociety == null ||
                    secretarySociety.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Secretary society is empty."
                );

                return finalResidents;
            }

            secretarySociety =
                    secretarySociety.trim();

            // =================================================
            // 2. FIRESTORE FILTER
            // =================================================

            List<Resident> residents =
                    residentDao
                            .getResidentsBySociety(
                                    secretarySociety
                            );

            if (residents == null ||
                    residents.isEmpty()) {

                System.out.println(
                        "No residents found for society: "
                                + secretarySociety
                );

                return finalResidents;
            }

            // =================================================
            // 3. SECOND SAFETY FILTER
            //
            // Exact comparison in Java.
            // =================================================

            for (Resident resident : residents) {

                if (resident == null) {

                    continue;
                }

                String residentSociety =
                        resident.getSociety();

                if (residentSociety == null ||
                        residentSociety.trim().isEmpty()) {

                    System.out.println(
                            "BLOCKED - Society missing"
                    );

                    continue;
                }

                residentSociety =
                        residentSociety.trim();

                // -------------------------------------------------
                // CASE-INSENSITIVE EXACT MATCH
                // -------------------------------------------------

                if (residentSociety.equalsIgnoreCase(
                        secretarySociety
                )) {

                    finalResidents.add(
                            resident
                    );

                    System.out.println(
                            "ALLOWED: "
                                    + resident.getEmail()
                                    + " -> "
                                    + residentSociety
                    );

                } else {

                    System.out.println(
                            "BLOCKED: "
                                    + resident.getEmail()
                                    + " -> "
                                    + residentSociety
                    );
                }
            }

            // =================================================
            // 4. FINAL RESULT
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "FINAL RESIDENT FILTER"
            );

            System.out.println(
                    "Secretary Society = ["
                            + secretarySociety
                            + "]"
            );

            System.out.println(
                    "DAO Results       = "
                            + residents.size()
            );

            System.out.println(
                    "Final Results     = "
                            + finalResidents.size()
            );

            System.out.println(
                    "=========================================="
            );

            return finalResidents;

        } catch (Exception e) {

            System.out.println(
                    "getResidentsBySociety ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // =========================================================
    // GET RESIDENTS BY MANUALLY PROVIDED SOCIETY
    // =========================================================

    public List<Resident> getResidentsBySociety(
            String society
    ) {

        try {

            String cleanSociety =
                    clean(society);

            if (cleanSociety.isEmpty()) {

                return new ArrayList<>();
            }

            return residentDao
                    .getResidentsBySociety(
                            cleanSociety
                    );

        } catch (Exception e) {

            System.out.println(
                    "getResidentsBySociety(society) ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // =========================================================
    // GET ALL RESIDENTS
    //
    // WARNING:
    // Do NOT use this for Secretary society table.
    // =========================================================

    public List<Resident> getAllResidents() {

        try {

            List<Resident> residents =
                    residentDao.getAllResidents();

            if (residents == null) {

                return new ArrayList<>();
            }

            return residents;

        } catch (Exception e) {

            System.out.println(
                    "getAllResidents ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // =========================================================
    // GET RESIDENT BY EMAIL
    // =========================================================

    public Resident getResidentByEmail(
            String email
    ) {

        try {

            String cleanEmail =
                    cleanEmail(email);

            if (cleanEmail.isEmpty()) {

                return null;
            }

            return residentDao
                    .getResidentByEmail(
                            cleanEmail
                    );

        } catch (Exception e) {

            System.out.println(
                    "getResidentByEmail ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // CLEAN STRING
    // =========================================================

    private String clean(String value) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }

    // =========================================================
    // CLEAN EMAIL
    // =========================================================

    private String cleanEmail(String email) {

        if (email == null) {

            return "";
        }

        return email
                .trim()
                .toLowerCase();
    }
}