package com.society.controller.Secretary_Controller;

import java.util.Collections;
import java.util.List;

import com.society.dao.Secretary_dao.GuardDao;
import com.society.dao.Secretary_dao.GuardDaoImpl;
import com.society.model.Secretary_model.Guard;

public class GuardController {

    // =====================================================
    // DAO
    // =====================================================

    private final GuardDao guardDao;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public GuardController() {

        guardDao = new GuardDaoImpl();

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "GuardController initialized."
        );

        System.out.println(
                "=========================================="
        );
    }

    // =====================================================
    // ADD GUARD
    // =====================================================
    // Society is REQUIRED.
    //
    // Guard Firestore मध्ये save होताना society field
    // नक्की save होईल.
    // =====================================================

    public boolean addGuard(
            String name,
            String mobile,
            String shift,
            String email,
            String status,
            String assignedGate,
            String society) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (isEmpty(name)) {

                System.out.println(
                        "GUARD ERROR: Name is required."
                );

                return false;
            }

            if (isEmpty(mobile)) {

                System.out.println(
                        "GUARD ERROR: Mobile is required."
                );

                return false;
            }

            if (isEmpty(shift)) {

                System.out.println(
                        "GUARD ERROR: Shift is required."
                );

                return false;
            }

            if (isEmpty(email)) {

                System.out.println(
                        "GUARD ERROR: Email is required."
                );

                return false;
            }

            if (isEmpty(status)) {

                System.out.println(
                        "GUARD ERROR: Status is required."
                );

                return false;
            }

            if (isEmpty(assignedGate)) {

                System.out.println(
                        "GUARD ERROR: Assigned Gate is required."
                );

                return false;
            }

            if (isEmpty(society)) {

                System.out.println(
                        "GUARD ERROR: Society is required."
                );

                return false;
            }

            // =================================================
            // CLEAN DATA
            // =================================================

            name =
                    cleanValue(name);

            mobile =
                    cleanValue(mobile);

            shift =
                    cleanValue(shift);

            email =
                    cleanEmail(email);

            status =
                    cleanValue(status);

            assignedGate =
                    cleanValue(assignedGate);

            society =
                    cleanValue(society);

            // =================================================
            // EMAIL VALIDATION
            // =================================================

            if (!isValidEmail(email)) {

                System.out.println(
                        "GUARD ERROR: Invalid email."
                );

                return false;
            }

            // =================================================
            // CREATE GUARD OBJECT
            // =================================================

            Guard guard =
                    new Guard();

            guard.setName(
                    name
            );

            guard.setMobile(
                    mobile
            );

            guard.setShift(
                    shift
            );

            guard.setEmail(
                    email
            );

            guard.setStatus(
                    status
            );

            guard.setAssignedGate(
                    assignedGate
            );

            // =================================================
            // VERY IMPORTANT
            // SOCIETY SET HERE
            // =================================================

            guard.setSociety(
                    society
            );

            // =================================================
            // DEBUG
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "ADDING GUARD TO FIRESTORE"
            );

            System.out.println(
                    "Name          : " + guard.getName()
            );

            System.out.println(
                    "Mobile        : " + guard.getMobile()
            );

            System.out.println(
                    "Shift         : " + guard.getShift()
            );

            System.out.println(
                    "Email         : " + guard.getEmail()
            );

            System.out.println(
                    "Status        : " + guard.getStatus()
            );

            System.out.println(
                    "Assigned Gate : "
                            + guard.getAssignedGate()
            );

            System.out.println(
                    "Society       : " + guard.getSociety()
            );

            System.out.println(
                    "=========================================="
            );

            // =================================================
            // SAVE TO DAO
            // =================================================

            boolean result =
                    guardDao.addGuard(
                            guard
                    );

            // =================================================
            // RESULT
            // =================================================

            if (result) {

                System.out.println(
                        "=========================================="
                );

                System.out.println(
                        "GUARD SAVED SUCCESSFULLY"
                );

                System.out.println(
                        "Society : "
                                + guard.getSociety()
                );

                System.out.println(
                        "=========================================="
                );

            } else {

                System.out.println(
                        "GUARD SAVE FAILED."
                );
            }

            return result;

        } catch (Exception e) {

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "GUARD CONTROLLER ERROR: addGuard()"
            );

            System.out.println(
                    "=========================================="
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // UPDATE GUARD
    // =====================================================

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
                        "GUARD ERROR: Guard ID is required."
                );

                return false;
            }

            if (isEmpty(shift)) {

                System.out.println(
                        "GUARD ERROR: Shift is required."
                );

                return false;
            }

            if (isEmpty(status)) {

                System.out.println(
                        "GUARD ERROR: Status is required."
                );

                return false;
            }

            if (isEmpty(assignedGate)) {

                System.out.println(
                        "GUARD ERROR: Assigned Gate is required."
                );

                return false;
            }

            // =================================================
            // CLEAN DATA
            // =================================================

            id =
                    cleanValue(id);

            shift =
                    cleanValue(shift);

            status =
                    cleanValue(status);

            assignedGate =
                    cleanValue(assignedGate);

            // =================================================
            // UPDATE
            // =================================================

            boolean result =
                    guardDao.updateGuard(
                            id,
                            shift,
                            status,
                            assignedGate
                    );

            if (result) {

                System.out.println(
                        "Guard updated successfully."
                );

            } else {

                System.out.println(
                        "Guard update failed."
                );
            }

            return result;

        } catch (Exception e) {

            System.out.println(
                    "GUARD CONTROLLER ERROR: "
                            + "updateGuard()"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // FETCH ALL GUARDS
    // =====================================================
    // NOTE:
    // This returns guards from ALL societies.
    //
    // Secretary screen साठी हा method वापरू नका,
    // जर फक्त current society चे guards पाहिजेत.
    // त्यासाठी getGuardsBySociety() वापरा.
    // =====================================================

    public List<Guard> getAllGuards() {

        try {

            List<Guard> guards =
                    guardDao.getAllGuards();

            if (guards == null) {

                return Collections.emptyList();
            }

            return guards;

        } catch (Exception e) {

            System.out.println(
                    "GUARD CONTROLLER ERROR: "
                            + "getAllGuards()"
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    // =====================================================
    // FETCH GUARDS BY SOCIETY
    // =====================================================
    // Firestore query:
    //
    // Guards
    //    -> whereEqualTo("society", society)
    //
    // त्यामुळे दुसऱ्या society चे guards येणार नाहीत.
    // =====================================================

    public List<Guard> getGuardsBySociety(
            String society) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (isEmpty(society)) {

                System.out.println(
                        "GUARD ERROR: Society is required."
                );

                return Collections.emptyList();
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
                    "Requested Society : "
                            + cleanSociety
            );

            System.out.println(
                    "=========================================="
            );

            // =================================================
            // DAO QUERY
            // =================================================

            List<Guard> guards =
                    guardDao.getGuardsBySociety(
                            cleanSociety
                    );

            // =================================================
            // NULL CHECK
            // =================================================

            if (guards == null) {

                System.out.println(
                        "No guards returned from DAO."
                );

                return Collections.emptyList();
            }

            // =================================================
            // DEBUG RESULT
            // =================================================

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "GUARDS FETCHED"
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

            return guards;

        } catch (Exception e) {

            System.out.println(
                    "GUARD CONTROLLER ERROR: "
                            + "getGuardsBySociety()"
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    // =====================================================
    // GET GUARD BY EMAIL
    // =====================================================

    public Guard getGuardByEmail(
            String email) {

        try {

            if (isEmpty(email)) {

                System.out.println(
                        "GUARD ERROR: Email is required."
                );

                return null;
            }

            String cleanEmail =
                    cleanEmail(email);

            if (!isValidEmail(cleanEmail)) {

                System.out.println(
                        "GUARD ERROR: Invalid email."
                );

                return null;
            }

            Guard guard =
                    guardDao.getGuardByEmail(
                            cleanEmail
                    );

            if (guard != null) {

                System.out.println(
                        "Guard found: "
                                + guard.getName()
                );

                System.out.println(
                        "Society: "
                                + guard.getSociety()
                );
            }

            return guard;

        } catch (Exception e) {

            System.out.println(
                    "GUARD CONTROLLER ERROR: "
                            + "getGuardByEmail()"
            );

            e.printStackTrace();

            return null;
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

    // =====================================================
    // VALIDATE EMAIL
    // =====================================================

    private boolean isValidEmail(
            String email) {

        if (email == null
                || email.trim().isEmpty()) {

            return false;
        }

        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }
}