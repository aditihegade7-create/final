package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.Resident;

public class ResidentDaoImpl implements ResidentDao {

    private final Firestore firestore;

    private static final String COLLECTION = "Residents";

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

        if (resident == null) {
            System.out.println("ResidentDaoImpl: Resident is null.");
            return false;
        }

        String email = clean(resident.getEmail());

        if (email.isEmpty()) {
            System.out.println("ResidentDaoImpl: Email is empty.");
            return false;
        }

        try {

            // -------------------------------------------------
            // EMAIL IS USED AS DOCUMENT ID
            // -------------------------------------------------

            DocumentSnapshot existingDoc = firestore
                    .collection(COLLECTION)
                    .document(email)
                    .get()
                    .get();

            Map<String, Object> data = new HashMap<>();

            // -------------------------------------------------
            // EXISTING DATA
            //
            // If document already exists, first load its data.
            // This prevents blank values from deleting old data.
            // -------------------------------------------------

            if (existingDoc.exists() && existingDoc.getData() != null) {

                data.putAll(existingDoc.getData());

                System.out.println(
                        "Existing resident found: " + email
                );
            }

            // -------------------------------------------------
            // BASIC DETAILS
            // -------------------------------------------------

            putIfNotEmpty(
                    data,
                    "name",
                    resident.getName()
            );

            putIfNotEmpty(
                    data,
                    "email",
                    email
            );

            putIfNotEmpty(
                    data,
                    "phone",
                    resident.getPhone()
            );

            putIfNotEmpty(
                    data,
                    "flatNo",
                    resident.getFlatNo()
            );

            putIfNotEmpty(
                    data,
                    "status",
                    resident.getStatus()
            );

            // -------------------------------------------------
            // COMPLETE RESIDENT DETAILS
            // -------------------------------------------------

            putIfNotEmpty(
                    data,
                    "aadhar",
                    resident.getAadhar()
            );

            putIfNotEmpty(
                    data,
                    "address",
                    resident.getAddress()
            );

            putIfNotEmpty(
                    data,
                    "dob",
                    resident.getDob()
            );

            putIfNotEmpty(
                    data,
                    "gender",
                    resident.getGender()
            );

            putIfNotEmpty(
                    data,
                    "joiningDate",
                    resident.getJoiningDate()
            );

            putIfNotEmpty(
                    data,
                    "memberSince",
                    resident.getMemberSince()
            );

            putIfNotEmpty(
                    data,
                    "ownerName",
                    resident.getOwnerName()
            );

            putIfNotEmpty(
                    data,
                    "society",
                    resident.getSociety()
            );

            putIfNotEmpty(
                    data,
                    "role",
                    resident.getRole()
            );

            // -------------------------------------------------
            // DEFAULT ROLE
            // -------------------------------------------------

            if (!data.containsKey("role")
                    || clean(
                            String.valueOf(
                                    data.get("role")
                            )
                    ).isEmpty()) {

                data.put(
                        "role",
                        "Resident"
                );
            }

            // -------------------------------------------------
            // DEFAULT EMAIL
            // -------------------------------------------------

            data.put(
                    "email",
                    email
            );

            // -------------------------------------------------
            // SAVE
            // -------------------------------------------------

            firestore
                    .collection(COLLECTION)
                    .document(email)
                    .set(data)
                    .get();

            System.out.println(
                    "Resident saved successfully."
            );

            System.out.println(
                    "Resident Email : " + email
            );

            System.out.println(
                    "Resident Society : "
                            + data.get("society")
            );

            System.out.println(
                    "Resident Phone : "
                            + data.get("phone")
            );

            System.out.println(
                    "Resident Flat : "
                            + data.get("flatNo")
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "ResidentDaoImpl: Error saving resident."
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

            CollectionReference collection =
                    firestore.collection(COLLECTION);

            QuerySnapshot snapshot =
                    collection.get().get();

            for (QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                Resident resident =
                        documentToResident(document);

                if (resident != null) {

                    residents.add(resident);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ResidentDaoImpl: Error fetching all residents."
            );

            e.printStackTrace();
        }

        return residents;
    }

    // =========================================================
    // GET RESIDENT BY EMAIL
    //
    // IMPORTANT:
    // Email is the Firestore DOCUMENT ID.
    // So we directly use:
    //
    // Residents/{email}
    // =========================================================

    @Override
    public Resident getResidentByEmail(String email) {

        email = cleanEmail(email);

        if (email.isEmpty()) {

            System.out.println(
                    "getResidentByEmail: Email is empty."
            );

            return null;
        }

        try {

            System.out.println(
                    "Searching resident document: "
                            + COLLECTION
                            + "/"
                            + email
            );

            DocumentSnapshot document =
                    firestore
                            .collection(COLLECTION)
                            .document(email)
                            .get()
                            .get();

            // -------------------------------------------------
            // DOCUMENT NOT FOUND
            // -------------------------------------------------

            if (!document.exists()) {

                System.out.println(
                        "Resident document NOT FOUND: "
                                + email
                );

                return null;
            }

            System.out.println(
                    "Resident document FOUND: "
                            + email
            );

            Resident resident =
                    documentToResident(document);

            // -------------------------------------------------
            // DEBUG
            // -------------------------------------------------

            if (resident != null) {

                System.out.println(
                        "Name       : "
                                + resident.getName()
                );

                System.out.println(
                        "Email      : "
                                + resident.getEmail()
                );

                System.out.println(
                        "Phone      : "
                                + resident.getPhone()
                );

                System.out.println(
                        "Flat       : "
                                + resident.getFlatNo()
                );

                System.out.println(
                        "Society    : "
                                + resident.getSociety()
                );

                System.out.println(
                        "Role       : "
                                + resident.getRole()
                );
            }

            return resident;

        } catch (Exception e) {

            System.out.println(
                    "ResidentDaoImpl: Error fetching resident by email."
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

        society = clean(society);

        if (society.isEmpty()) {

            System.out.println(
                    "getResidentsBySociety: Society is empty."
            );

            return residents;
        }

        try {

            QuerySnapshot snapshot =
                    firestore
                            .collection(COLLECTION)
                            .whereEqualTo(
                                    "society",
                                    society
                            )
                            .get()
                            .get();

            for (QueryDocumentSnapshot document
                    : snapshot.getDocuments()) {

                Resident resident =
                        documentToResident(document);

                if (resident != null) {

                    residents.add(resident);
                }
            }

            System.out.println(
                    "Residents found for society ["
                            + society
                            + "] = "
                            + residents.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "ResidentDaoImpl: Error fetching residents by society."
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

        if (document == null
                || !document.exists()) {

            return null;
        }

        try {

            Resident resident =
                    new Resident();

            // -------------------------------------------------
            // NAME
            // -------------------------------------------------

            resident.setName(
                    getString(
                            document,
                            "name"
                    )
            );

            // -------------------------------------------------
            // EMAIL
            // -------------------------------------------------

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
                    email
            );

            // -------------------------------------------------
            // PHONE
            // -------------------------------------------------

            resident.setPhone(
                    getString(
                            document,
                            "phone"
                    )
            );

            // -------------------------------------------------
            // FLAT
            // -------------------------------------------------

            resident.setFlatNo(
                    getString(
                            document,
                            "flatNo"
                    )
            );

            // -------------------------------------------------
            // STATUS
            // -------------------------------------------------

            resident.setStatus(
                    getString(
                            document,
                            "status"
                    )
            );

            // -------------------------------------------------
            // AADHAR
            // -------------------------------------------------

            resident.setAadhar(
                    getString(
                            document,
                            "aadhar"
                    )
            );

            // -------------------------------------------------
            // ADDRESS
            // -------------------------------------------------

            resident.setAddress(
                    getString(
                            document,
                            "address"
                    )
            );

            // -------------------------------------------------
            // DOB
            // -------------------------------------------------

            resident.setDob(
                    getString(
                            document,
                            "dob"
                    )
            );

            // -------------------------------------------------
            // GENDER
            // -------------------------------------------------

            resident.setGender(
                    getString(
                            document,
                            "gender"
                    )
            );

            // -------------------------------------------------
            // JOINING DATE
            // -------------------------------------------------

            resident.setJoiningDate(
                    getString(
                            document,
                            "joiningDate"
                    )
            );

            // -------------------------------------------------
            // MEMBER SINCE
            // -------------------------------------------------

            resident.setMemberSince(
                    getString(
                            document,
                            "memberSince"
                    )
            );

            // -------------------------------------------------
            // OWNER NAME
            // -------------------------------------------------

            resident.setOwnerName(
                    getString(
                            document,
                            "ownerName"
                    )
            );

            // -------------------------------------------------
            // SOCIETY
            // -------------------------------------------------

            resident.setSociety(
                    getString(
                            document,
                            "society"
                    )
            );

            // -------------------------------------------------
            // ROLE
            // -------------------------------------------------

            resident.setRole(
                    getString(
                            document,
                            "role"
                    )
            );

            return resident;

        } catch (Exception e) {

            System.out.println(
                    "ResidentDaoImpl: Error converting document."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET STRING FROM FIRESTORE
    // =========================================================

    private String getString(
            DocumentSnapshot document,
            String field
    ) {

        Object value =
                document.get(field);

        if (value == null) {

            return "";
        }

        return String.valueOf(value).trim();
    }

    // =========================================================
    // PUT ONLY NON-EMPTY VALUE
    //
    // This is important.
    //
    // Empty UI fields will NOT overwrite existing
    // registration data.
    // =========================================================

    private void putIfNotEmpty(
            Map<String, Object> data,
            String key,
            String value
    ) {

        String cleaned =
                clean(value);

        if (!cleaned.isEmpty()) {

            data.put(
                    key,
                    cleaned
            );
        }
    }

    // =========================================================
    // CLEAN
    // =========================================================

    private String clean(
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }

    // =========================================================
    // CLEAN EMAIL
    // =========================================================

    private String cleanEmail(
            String email
    ) {

        if (email == null) {

            return "";
        }

        return email
                .trim()
                .toLowerCase();
    }
}