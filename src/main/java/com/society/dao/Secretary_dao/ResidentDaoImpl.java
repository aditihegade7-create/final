package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.Resident;

public class ResidentDaoImpl implements ResidentDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore firestore;

    // =========================================================
    // COLLECTION
    // =========================================================

    private static final String COLLECTION =
            "Residents";

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ResidentDaoImpl() {

        firestore = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // ADD / UPDATE RESIDENT
    // =========================================================

    @Override
    public boolean addResident(Resident resident) {

        try {

            if (resident == null) {

                System.out.println(
                        "Resident is null."
                );

                return false;
            }

            // -------------------------------------------------
            // EMAIL
            // -------------------------------------------------

            String email =
                    cleanEmail(resident.getEmail());

            if (email.isEmpty()) {

                System.out.println(
                        "Resident email is required."
                );

                return false;
            }

            // -------------------------------------------------
            // SOCIETY
            // -------------------------------------------------

            String society =
                    clean(resident.getSociety());

            if (society.isEmpty()) {

                System.out.println(
                        "Resident society is required."
                );

                return false;
            }

            // -------------------------------------------------
            // DOCUMENT
            //
            // Residents/{email}
            // -------------------------------------------------

            DocumentReference document =
                    firestore
                            .collection(COLLECTION)
                            .document(email);

            // -------------------------------------------------
            // DATA
            // -------------------------------------------------

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "name",
                    clean(resident.getName())
            );

            data.put(
                    "email",
                    email
            );

            data.put(
                    "phone",
                    clean(resident.getPhone())
            );

            data.put(
                    "flatNo",
                    clean(resident.getFlatNo())
            );

            data.put(
                    "status",
                    clean(resident.getStatus())
            );

            data.put(
                    "aadhar",
                    clean(resident.getAadhar())
            );

            data.put(
                    "address",
                    clean(resident.getAddress())
            );

            data.put(
                    "dob",
                    clean(resident.getDob())
            );

            data.put(
                    "gender",
                    clean(resident.getGender())
            );

            data.put(
                    "joiningDate",
                    clean(resident.getJoiningDate())
            );

            data.put(
                    "memberSince",
                    clean(resident.getMemberSince())
            );

            data.put(
                    "ownerName",
                    clean(resident.getOwnerName())
            );

            // IMPORTANT
            // Society stored in Firestore
            data.put(
                    "society",
                    society
            );

            data.put(
                    "role",
                    clean(resident.getRole())
            );

            // -------------------------------------------------
            // SAVE
            // -------------------------------------------------

            document
                    .set(data)
                    .get();

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "RESIDENT SAVED"
            );

            System.out.println(
                    "Email   : " + email
            );

            System.out.println(
                    "Society : " + society
            );

            System.out.println(
                    "=========================================="
            );

            return true;

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
    // GET ALL RESIDENTS
    // =========================================================

    @Override
    public List<Resident> getAllResidents() {

        List<Resident> residents =
                new ArrayList<>();

        try {

            List<QueryDocumentSnapshot> documents =
                    firestore
                            .collection(COLLECTION)
                            .get()
                            .get()
                            .getDocuments();

            for (QueryDocumentSnapshot document
                    : documents) {

                Resident resident =
                        documentToResident(document);

                if (resident != null) {

                    residents.add(resident);
                }
            }

            System.out.println(
                    "ALL RESIDENTS = "
                            + residents.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "getAllResidents ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return residents;
    }

    // =========================================================
    // GET RESIDENT BY EMAIL
    // =========================================================

    @Override
    public Resident getResidentByEmail(
            String email
    ) {

        try {

            String cleanEmail =
                    cleanEmail(email);

            if (cleanEmail.isEmpty()) {

                return null;
            }

            DocumentSnapshot document =
                    firestore
                            .collection(COLLECTION)
                            .document(cleanEmail)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "Resident not found: "
                                + cleanEmail
                );

                return null;
            }

            return documentToResident(document);

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
    // GET RESIDENTS BY SOCIETY
    // =========================================================

    @Override
    public List<Resident> getResidentsBySociety(
            String society
    ) {

        List<Resident> residents =
                new ArrayList<>();

        try {

            String cleanSociety =
                    clean(society);

            if (cleanSociety.isEmpty()) {

                System.out.println(
                        "Society is empty."
                );

                return residents;
            }

            // =================================================
            // FIRESTORE SOCIETY FILTER
            // =================================================

            List<QueryDocumentSnapshot> documents =
                    firestore
                            .collection(COLLECTION)
                            .whereEqualTo(
                                    "society",
                                    cleanSociety
                            )
                            .get()
                            .get()
                            .getDocuments();

            // =================================================
            // CONVERT
            // =================================================

            for (QueryDocumentSnapshot document
                    : documents) {

                Resident resident =
                        documentToResident(document);

                if (resident != null) {

                    residents.add(resident);
                }
            }

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "DAO SOCIETY FILTER"
            );

            System.out.println(
                    "Society = ["
                            + cleanSociety
                            + "]"
            );

            System.out.println(
                    "Residents = "
                            + residents.size()
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "getResidentsBySociety ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return residents;
    }

    // =========================================================
    // DOCUMENT -> RESIDENT
    // =========================================================

    private Resident documentToResident(
            DocumentSnapshot document
    ) {

        if (document == null ||
                !document.exists()) {

            return null;
        }

        Resident resident =
                new Resident();

        // -----------------------------------------------------
        // NAME
        // -----------------------------------------------------

        resident.setName(
                getString(
                        document,
                        "name"
                )
        );

        // -----------------------------------------------------
        // EMAIL
        // -----------------------------------------------------

        String email =
                getString(
                        document,
                        "email"
                );

        // If email field is missing,
        // use document ID.

        if (email.isEmpty()) {

            email =
                    document.getId();
        }

        resident.setEmail(
                cleanEmail(email)
        );

        // -----------------------------------------------------
        // PHONE
        // -----------------------------------------------------

        resident.setPhone(
                getString(
                        document,
                        "phone"
                )
        );

        // -----------------------------------------------------
        // FLAT
        // -----------------------------------------------------

        resident.setFlatNo(
                getString(
                        document,
                        "flatNo"
                )
        );

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        resident.setStatus(
                getString(
                        document,
                        "status"
                )
        );

        // -----------------------------------------------------
        // AADHAR
        // -----------------------------------------------------

        resident.setAadhar(
                getString(
                        document,
                        "aadhar"
                )
        );

        // -----------------------------------------------------
        // ADDRESS
        // -----------------------------------------------------

        resident.setAddress(
                getString(
                        document,
                        "address"
                )
        );

        // -----------------------------------------------------
        // DOB
        // -----------------------------------------------------

        resident.setDob(
                getString(
                        document,
                        "dob"
                )
        );

        // -----------------------------------------------------
        // GENDER
        // -----------------------------------------------------

        resident.setGender(
                getString(
                        document,
                        "gender"
                )
        );

        // -----------------------------------------------------
        // JOINING DATE
        // -----------------------------------------------------

        resident.setJoiningDate(
                getString(
                        document,
                        "joiningDate"
                )
        );

        // -----------------------------------------------------
        // MEMBER SINCE
        // -----------------------------------------------------

        resident.setMemberSince(
                getString(
                        document,
                        "memberSince"
                )
        );

        // -----------------------------------------------------
        // OWNER NAME
        // -----------------------------------------------------

        resident.setOwnerName(
                getString(
                        document,
                        "ownerName"
                )
        );

        // -----------------------------------------------------
        // SOCIETY
        // -----------------------------------------------------

        resident.setSociety(
                getString(
                        document,
                        "society"
                )
        );

        // -----------------------------------------------------
        // ROLE
        // -----------------------------------------------------

        resident.setRole(
                getString(
                        document,
                        "role"
                )
        );

        return resident;
    }

    // =========================================================
    // GET STRING
    // =========================================================

    private String getString(
            DocumentSnapshot document,
            String field
    ) {

        try {

            String value =
                    document.getString(field);

            if (value == null) {

                return "";
            }

            return value.trim();

        } catch (Exception e) {

            return "";
        }
    }

    // =========================================================
    // CLEAN
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