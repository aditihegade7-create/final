package com.society.controller.Secretary_Controller;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;

import com.society.config.FirebaseConfig;
import com.society.dao.Secretary_dao.OwnerDao;
import com.society.dao.Secretary_dao.OwnerDaoImpl;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Owner;

public class OwnerController {

    // =========================================================
    // DAO
    // =========================================================

    private final OwnerDao ownerDao;

    // =========================================================
    // FIRESTORE
    // =========================================================

    // Used to find logged-in Secretary's society
    private final com.google.cloud.firestore.Firestore firestore;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public OwnerController() {

        ownerDao =
                new OwnerDaoImpl();

        firestore =
                FirebaseConfig.getFirestore();

        System.out.println(
                "OwnerController initialized."
        );
    }

    // =========================================================
    // ADD OWNER
    // =========================================================

    public boolean addOwner(
            String name,
            String flat,
            String mobile,
            String email,
            String status,
            String society) {

        try {

            // -------------------------------------------------
            // VALIDATION
            // -------------------------------------------------

            if (name == null ||
                    name.trim().isEmpty()) {

                return false;
            }

            if (flat == null ||
                    flat.trim().isEmpty()) {

                return false;
            }

            if (mobile == null ||
                    mobile.trim().isEmpty()) {

                return false;
            }

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            if (status == null ||
                    status.trim().isEmpty()) {

                return false;
            }

            if (society == null ||
                    society.trim().isEmpty()) {

                return false;
            }

            // -------------------------------------------------
            // NORMALIZE EMAIL
            // -------------------------------------------------

            email =
                    email.trim().toLowerCase();

            // -------------------------------------------------
            // NORMALIZE VALUES
            // -------------------------------------------------

            name =
                    name.trim();

            flat =
                    flat.trim();

            mobile =
                    mobile.trim();

            status =
                    status.trim();

            society =
                    society.trim();

            // -------------------------------------------------
            // CREATE OWNER
            // -------------------------------------------------

            Owner owner =
                    new Owner(
                            name,
                            flat,
                            mobile,
                            email,
                            status,
                            society
                    );

            // -------------------------------------------------
            // SAVE
            // -------------------------------------------------

            return ownerDao.addOwner(
                    owner
            );

        } catch (Exception e) {

            System.out.println(
                    "Error in OwnerController.addOwner()"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ALL OWNERS
    //
    // NOTE:
    // This returns owners from ALL societies.
    //
    // Do NOT use this on Secretary society-specific page.
    // =========================================================

    public List<Owner> getAllOwners() {

        try {

            List<Owner> owners =
                    ownerDao.getAllOwners();

            if (owners == null) {

                return new ArrayList<>();
            }

            return owners;

        } catch (Exception e) {

            System.out.println(
                    "Error in getAllOwners()"
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // =========================================================
    // GET OWNERS BY SOCIETY
    //
    // MANUAL SOCIETY VERSION
    //
    // Example:
    //
    // getOwnersBySociety("Green Valley")
    //
    // =========================================================

    public List<Owner> getOwnersBySociety(
            String society) {

        try {

            // -------------------------------------------------
            // VALIDATE SOCIETY
            // -------------------------------------------------

            if (society == null ||
                    society.trim().isEmpty()) {

                return new ArrayList<>();
            }

            society =
                    society.trim();

            // -------------------------------------------------
            // FETCH FROM DAO
            // -------------------------------------------------

            List<Owner> owners =
                    ownerDao.getOwnersBySociety(
                            society
                    );

            // -------------------------------------------------
            // NEVER RETURN NULL
            // -------------------------------------------------

            if (owners == null) {

                return new ArrayList<>();
            }

            return owners;

        } catch (Exception e) {

            System.out.println(
                    "Error in getOwnersBySociety(String)"
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // =========================================================
    // GET OWNERS BY LOGGED-IN SECRETARY'S SOCIETY
    //
    // THIS IS THE METHOD YOU NEED IN ManageOwner
    //
    // No argument required.
    //
    // Flow:
    //
    // Logged-in Secretary Email
    //          ↓
    // Secretaries/{email}
    //          ↓
    // society
    //          ↓
    // Owners where society == secretary society
    //
    // =========================================================

    public List<Owner> getOwnersBySociety() {

        List<Owner> owners =
                new ArrayList<>();

        try {

            // =================================================
            // GET LOGGED-IN SECRETARY EMAIL
            // =================================================

            String secretaryEmail =
                    UserDao.getLoggedInEmail();

            if (secretaryEmail == null ||
                    secretaryEmail.trim().isEmpty()) {

                System.out.println(
                        "Logged-in Secretary email not found."
                );

                return owners;
            }

            // -------------------------------------------------
            // NORMALIZE EMAIL
            // -------------------------------------------------

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            System.out.println(
                    "Logged-in Secretary email: "
                            + secretaryEmail
            );

            // =================================================
            // GET SECRETARY DOCUMENT
            // =================================================

            DocumentSnapshot secretaryDocument =
                    firestore
                            .collection("Secretaries")
                            .document(secretaryEmail)
                            .get()
                            .get();

            // =================================================
            // CHECK SECRETARY DOCUMENT
            // =================================================

            if (!secretaryDocument.exists()) {

                System.out.println(
                        "Secretary document not found: "
                                + secretaryEmail
                );

                return owners;
            }

            // =================================================
            // GET SOCIETY
            // =================================================

            String society =
                    secretaryDocument.getString(
                            "society"
                    );

            if (society == null ||
                    society.trim().isEmpty()) {

                System.out.println(
                        "Society not found for Secretary: "
                                + secretaryEmail
                );

                return owners;
            }

            society =
                    society.trim();

            System.out.println(
                    "Secretary society: "
                            + society
            );

            // =================================================
            // FETCH OWNERS OF THAT SOCIETY
            // =================================================

            owners =
                    ownerDao.getOwnersBySociety(
                            society
                    );

            // =================================================
            // NULL SAFETY
            // =================================================

            if (owners == null) {

                owners =
                        new ArrayList<>();
            }

            // =================================================
            // LOG
            // =================================================

            System.out.println(
                    "Owners fetched for Secretary society '"
                            + society
                            + "': "
                            + owners.size()
            );

            return owners;

        } catch (Exception e) {

            System.out.println(
                    "Error in getOwnersBySociety()"
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }

    // =========================================================
    // GET OWNER BY EMAIL
    // =========================================================

    public Owner getOwnerByEmail(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }

            return ownerDao.getOwnerByEmail(
                    email
                            .trim()
                            .toLowerCase()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error in getOwnerByEmail()"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // UPDATE OWNER
    // =========================================================

    public boolean updateOwner(
            String email,
            String name,
            String flat,
            String mobile,
            String status,
            String society) {

        try {

            // -------------------------------------------------
            // EMAIL VALIDATION
            // -------------------------------------------------

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            // -------------------------------------------------
            // OTHER VALIDATIONS
            // -------------------------------------------------

            if (name == null ||
                    name.trim().isEmpty()) {

                return false;
            }

            if (flat == null ||
                    flat.trim().isEmpty()) {

                return false;
            }

            if (mobile == null ||
                    mobile.trim().isEmpty()) {

                return false;
            }

            if (status == null ||
                    status.trim().isEmpty()) {

                return false;
            }

            if (society == null ||
                    society.trim().isEmpty()) {

                return false;
            }

            // -------------------------------------------------
            // UPDATE
            // -------------------------------------------------

            return ownerDao.updateOwner(
                    email
                            .trim()
                            .toLowerCase(),

                    name.trim(),

                    flat.trim(),

                    mobile.trim(),

                    status.trim(),

                    society.trim()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error in updateOwner()"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // DELETE OWNER
    // =========================================================

    public boolean deleteOwner(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }

            return ownerDao.deleteOwner(
                    email
                            .trim()
                            .toLowerCase()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error in deleteOwner()"
            );

            e.printStackTrace();

            return false;
        }
    }
}