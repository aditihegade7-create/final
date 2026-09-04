package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.Guard;

public class GuardDaoImpl implements GuardDao {

    // =====================================================
    // FIRESTORE
    // =====================================================

    private final Firestore db =
            FirebaseConfig.getFirestore();

    // =====================================================
    // COLLECTION
    // =====================================================

    private static final String COLLECTION = "Guards";

    // =====================================================
    // ADD GUARD
    // =====================================================

    @Override
    public boolean addGuard(Guard guard) {

        try {

            // =================================================
            // NULL CHECK
            // =================================================

            if (guard == null) {

                System.out.println(
                        "GUARD DAO ERROR: Guard is null."
                );

                return false;
            }

            // =================================================
            // VALIDATION
            // =================================================

            if (isEmpty(guard.getName())) {

                System.out.println(
                        "GUARD DAO ERROR: Name is empty."
                );

                return false;
            }

            if (isEmpty(guard.getMobile())) {

                System.out.println(
                        "GUARD DAO ERROR: Mobile is empty."
                );

                return false;
            }

            if (isEmpty(guard.getShift())) {

                System.out.println(
                        "GUARD DAO ERROR: Shift is empty."
                );

                return false;
            }

            if (isEmpty(guard.getEmail())) {

                System.out.println(
                        "GUARD DAO ERROR: Email is empty."
                );

                return false;
            }

            if (isEmpty(guard.getStatus())) {

                System.out.println(
                        "GUARD DAO ERROR: Status is empty."
                );

                return false;
            }

            if (isEmpty(guard.getAssignedGate())) {

                System.out.println(
                        "GUARD DAO ERROR: Assigned Gate is empty."
                );

                return false;
            }

            // =================================================
            // SOCIETY IS REQUIRED
            // =================================================

            if (isEmpty(guard.getSociety())) {

                System.out.println(
                        "GUARD DAO ERROR: Society is empty."
                );

                return false;
            }

            // =================================================
            // CLEAN VALUES
            // =================================================

            String name =
                    cleanValue(guard.getName());

            String mobile =
                    cleanValue(guard.getMobile());

            String shift =
                    cleanValue(guard.getShift());

            String email =
                    cleanEmail(guard.getEmail());

            String status =
                    cleanValue(guard.getStatus());

            String assignedGate =
                    cleanValue(guard.getAssignedGate());

            String society =
                    cleanValue(guard.getSociety());

            // =================================================
            // CHECK EMAIL
            // =================================================

            Guard existingGuard =
                    getGuardByEmail(email);

            if (existingGuard != null) {

                System.out.println(
                        "GUARD DAO ERROR: Guard already exists."
                );

                System.out.println(
                        "Email = " + email
                );

                return false;
            }

            // =================================================
            // GENERATE DOCUMENT ID
            // =================================================

            String guardId =
                    UUID.randomUUID().toString();

            // =================================================
            // FIRESTORE DATA
            // =================================================

            Map<String, Object> data =
                    new HashMap<>();

            data.put(
                    "id",
                    guardId
            );

            data.put(
                    "name",
                    name
            );

            data.put(
                    "mobile",
                    mobile
            );

            data.put(
                    "shift",
                    shift
            );

            data.put(
                    "email",
                    email
            );

            data.put(
                    "status",
                    status
            );

            data.put(
                    "assignedGate",
                    assignedGate
            );

            // =================================================
            // VERY IMPORTANT
            // SOCIETY
            // =================================================

            data.put(
                    "society",
                    society
            );

            // =================================================
            // SET ID IN OBJECT
            // =================================================

            guard.setId(
                    guardId
            );

            // =================================================
            // SAVE TO FIRESTORE
            // =================================================

            db.collection(COLLECTION)
                    .document(guardId)
                    .set(data)
                    .get();

            // =================================================
            // SUCCESS LOG
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "GUARD SAVED SUCCESSFULLY"
            );

            System.out.println(
                    "Collection : "
                            + COLLECTION
            );

            System.out.println(
                    "Document ID: "
                            + guardId
            );

            System.out.println(
                    "Name       : "
                            + name
            );

            System.out.println(
                    "Mobile     : "
                            + mobile
            );

            System.out.println(
                    "Shift      : "
                            + shift
            );

            System.out.println(
                    "Email      : "
                            + email
            );

            System.out.println(
                    "Status     : "
                            + status
            );

            System.out.println(
                    "Gate       : "
                            + assignedGate
            );

            System.out.println(
                    "Society    : "
                            + society
            );

            System.out.println(
                    "=========================================="
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "GUARD DAO ERROR: addGuard()"
            );

            System.out.println(
                    "=========================================="
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // FETCH ALL GUARDS
    // =====================================================

    @Override
    public List<Guard> getAllGuards() {

        List<Guard> guards =
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Guard guard =
                        document.toObject(
                                Guard.class
                        );

                if (guard != null) {

                    // IMPORTANT:
                    // Firestore document ID
                    guard.setId(
                            document.getId()
                    );

                    guards.add(
                            guard
                    );
                }
            }

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "ALL GUARDS FETCHED"
            );

            System.out.println(
                    "Total Guards = "
                            + guards.size()
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "GUARD DAO ERROR: getAllGuards()"
            );

            e.printStackTrace();
        }

        return guards;
    }

    // =====================================================
    // FETCH GUARDS BY SOCIETY
    // =====================================================
    //
    // THIS IS VERY IMPORTANT FOR RESIDENT
    //
    // Example:
    //
    // society = "stanza"
    //
    // Firestore:
    //
    // Guards
    //    |
    //    |-- Sayali
    //    |     society = stanza       ✅
    //    |
    //    |-- Rahul
    //    |     society = Sunrise      ❌
    //    |
    //    |-- Priya
    //          society = stanza       ✅
    //
    // Result:
    //
    // Sayali
    // Priya
    //
    // =====================================================

    @Override
    public List<Guard> getGuardsBySociety(
            String society) {

        List<Guard> guards =
                new ArrayList<>();

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (isEmpty(society)) {

                System.out.println(
                        "GUARD DAO ERROR: Society is empty."
                );

                return guards;
            }

            // =================================================
            // CLEAN SOCIETY
            // =================================================

            String cleanSociety =
                    cleanValue(society);

            // =================================================
            // DEBUG
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "FETCHING GUARDS BY SOCIETY"
            );

            System.out.println(
                    "Society = "
                            + cleanSociety
            );

            System.out.println(
                    "=========================================="
            );

            // =================================================
            // FIRESTORE QUERY
            // =================================================

            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "society",
                                    cleanSociety
                            )
                            .get()
                            .get();

            // =================================================
            // CONVERT DOCUMENTS
            // =================================================

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Guard guard =
                        document.toObject(
                                Guard.class
                        );

                if (guard != null) {

                    // IMPORTANT
                    // Set Firestore document ID
                    guard.setId(
                            document.getId()
                    );

                    guards.add(
                            guard
                    );

                    System.out.println(
                            "Guard Found : "
                                    + guard.getName()
                                    + " | Society : "
                                    + guard.getSociety()
                    );
                }
            }

            // =================================================
            // RESULT
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "SOCIETY GUARDS FETCH COMPLETE"
            );

            System.out.println(
                    "Society : "
                            + cleanSociety
            );

            System.out.println(
                    "Count   : "
                            + guards.size()
            );

            System.out.println(
                    "=========================================="
            );

        } catch (Exception e) {

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "GUARD DAO ERROR: "
                            + "getGuardsBySociety()"
            );

            System.out.println(
                    "=========================================="
            );

            e.printStackTrace();
        }

        return guards;
    }

    // =====================================================
    // GET GUARD BY EMAIL
    // =====================================================

    @Override
    public Guard getGuardByEmail(
            String email) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (isEmpty(email)) {

                return null;
            }

            // =================================================
            // CLEAN EMAIL
            // =================================================

            String cleanEmail =
                    cleanEmail(email);

            // =================================================
            // FIRESTORE QUERY
            // =================================================

            QuerySnapshot snapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "email",
                                    cleanEmail
                            )
                            .limit(1)
                            .get()
                            .get();

            // =================================================
            // GET DOCUMENT
            // =================================================

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Guard guard =
                        document.toObject(
                                Guard.class
                        );

                if (guard != null) {

                    // IMPORTANT
                    // Store Firestore document ID
                    guard.setId(
                            document.getId()
                    );

                    return guard;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "GUARD DAO ERROR: "
                            + "getGuardByEmail()"
            );

            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // UPDATE GUARD
    // =====================================================

    @Override
    public boolean updateGuard(
            String id,
            String shift,
            String status,
            String assignedGate) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (isEmpty(id)) {

                System.out.println(
                        "GUARD DAO ERROR: Guard ID is empty."
                );

                return false;
            }

            if (isEmpty(shift)) {

                System.out.println(
                        "GUARD DAO ERROR: Shift is empty."
                );

                return false;
            }

            if (isEmpty(status)) {

                System.out.println(
                        "GUARD DAO ERROR: Status is empty."
                );

                return false;
            }

            if (isEmpty(assignedGate)) {

                System.out.println(
                        "GUARD DAO ERROR: Assigned Gate is empty."
                );

                return false;
            }

            // =================================================
            // CLEAN VALUES
            // =================================================

            String cleanId =
                    cleanValue(id);

            String cleanShift =
                    cleanValue(shift);

            String cleanStatus =
                    cleanValue(status);

            String cleanAssignedGate =
                    cleanValue(assignedGate);

            // =================================================
            // UPDATE DATA
            // =================================================

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "shift",
                    cleanShift
            );

            updates.put(
                    "status",
                    cleanStatus
            );

            updates.put(
                    "assignedGate",
                    cleanAssignedGate
            );

            // =================================================
            // UPDATE FIRESTORE
            // =================================================

            db.collection(COLLECTION)
                    .document(cleanId)
                    .update(updates)
                    .get();

            // =================================================
            // SUCCESS
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "GUARD UPDATED SUCCESSFULLY"
            );

            System.out.println(
                    "Guard ID : "
                            + cleanId
            );

            System.out.println(
                    "Shift    : "
                            + cleanShift
            );

            System.out.println(
                    "Status   : "
                            + cleanStatus
            );

            System.out.println(
                    "Gate     : "
                            + cleanAssignedGate
            );

            System.out.println(
                    "=========================================="
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "GUARD DAO ERROR: updateGuard()"
            );

            System.out.println(
                    "=========================================="
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // CHECK EMPTY
    // =====================================================

    private boolean isEmpty(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }

    // =====================================================
    // CLEAN VALUE
    // =====================================================

    private String cleanValue(
            String value) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }

    // =====================================================
    // CLEAN EMAIL
    // =====================================================

    private String cleanEmail(
            String email) {

        if (email == null) {

            return "";
        }

        return email
                .trim()
                .toLowerCase();
    }
}