package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.List;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.Owner;

public class OwnerDaoImpl implements OwnerDao {

    private final Firestore firestore;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public OwnerDaoImpl() {

        firestore = FirebaseConfig.getFirestore();

        System.out.println(
                "OwnerDaoImpl: Firestore initialized."
        );
    }

    // =========================================================
    // ADD OWNER
    // EMAIL = FIRESTORE DOCUMENT ID
    // =========================================================

    @Override
    public boolean addOwner(Owner owner) {

        try {

            // -------------------------------------------------
            // VALIDATE OWNER
            // -------------------------------------------------

            if (owner == null) {

                System.out.println(
                        "Owner object is null."
                );

                return false;
            }

            // -------------------------------------------------
            // GET EMAIL
            // -------------------------------------------------

            String email = owner.getEmail();

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Owner email is empty."
                );

                return false;
            }

            // -------------------------------------------------
            // NORMALIZE EMAIL
            // -------------------------------------------------

            email = email.trim().toLowerCase();

            // -------------------------------------------------
            // SET NORMALIZED EMAIL IN MODEL
            // -------------------------------------------------

            owner.setEmail(email);

            // -------------------------------------------------
            // CHECK DUPLICATE OWNER
            // -------------------------------------------------

            DocumentSnapshot existingDocument =
                    firestore
                            .collection("Owners")
                            .document(email)
                            .get()
                            .get();

            if (existingDocument.exists()) {

                System.out.println(
                        "Owner already exists with email: "
                                + email
                );

                return false;
            }

            // -------------------------------------------------
            // SAVE OWNER
            // EMAIL = DOCUMENT ID
            // -------------------------------------------------

            firestore
                    .collection("Owners")
                    .document(email)
                    .set(owner)
                    .get();

            System.out.println(
                    "Owner added successfully."
            );

            System.out.println(
                    "Firestore Document ID: "
                            + email
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while adding owner."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ALL OWNERS
    // =========================================================

    @Override
    public List<Owner> getAllOwners() {

        List<Owner> owners =
                new ArrayList<>();

        try {

            // -------------------------------------------------
            // FETCH ALL OWNER DOCUMENTS
            // -------------------------------------------------

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection("Owners")
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            // -------------------------------------------------
            // CONVERT DOCUMENTS TO OWNER OBJECTS
            // -------------------------------------------------

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                try {

                    Owner owner =
                            document.toObject(
                                    Owner.class
                            );

                    if (owner == null) {
                        continue;
                    }

                    // -------------------------------------------------
                    // DOCUMENT ID
                    // -------------------------------------------------

                    String documentEmail =
                            document.getId();

                    owner.setId(
                            documentEmail
                    );

                    // -------------------------------------------------
                    // IF EMAIL FIELD IS MISSING
                    // USE DOCUMENT ID
                    // -------------------------------------------------

                    if (owner.getEmail() == null ||
                            owner.getEmail()
                                    .trim()
                                    .isEmpty()) {

                        owner.setEmail(
                                documentEmail
                        );
                    }

                    owners.add(owner);

                } catch (Exception e) {

                    System.out.println(
                            "Error reading owner document: "
                                    + document.getId()
                    );

                    e.printStackTrace();
                }
            }

            System.out.println(
                    "Owners fetched: "
                            + owners.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error while fetching owners."
            );

            e.printStackTrace();
        }

        return owners;
    }

    // =========================================================
    // GET OWNERS BY SOCIETY
    //
    // This method fetches ONLY owners belonging to
    // the supplied society.
    // =========================================================

    @Override
    public List<Owner> getOwnersBySociety(
            String society) {

        List<Owner> owners =
                new ArrayList<>();

        try {

            // -------------------------------------------------
            // VALIDATE SOCIETY
            // -------------------------------------------------

            if (society == null ||
                    society.trim().isEmpty()) {

                System.out.println(
                        "Society is empty."
                );

                return owners;
            }

            society = society.trim();

            System.out.println(
                    "Fetching owners for society: "
                            + society
            );

            // -------------------------------------------------
            // FIRESTORE QUERY
            //
            // Owners
            //   |
            //   +-- society == supplied society
            // -------------------------------------------------

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection("Owners")
                            .whereEqualTo(
                                    "society",
                                    society
                            )
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            // -------------------------------------------------
            // CONVERT MATCHING DOCUMENTS
            // -------------------------------------------------

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                try {

                    Owner owner =
                            document.toObject(
                                    Owner.class
                            );

                    if (owner == null) {
                        continue;
                    }

                    // -------------------------------------------------
                    // DOCUMENT ID = EMAIL
                    // -------------------------------------------------

                    String documentEmail =
                            document.getId();

                    owner.setId(
                            documentEmail
                    );

                    // -------------------------------------------------
                    // IF EMAIL FIELD IS MISSING
                    // USE DOCUMENT ID
                    // -------------------------------------------------

                    if (owner.getEmail() == null ||
                            owner.getEmail()
                                    .trim()
                                    .isEmpty()) {

                        owner.setEmail(
                                documentEmail
                        );
                    }

                    owners.add(owner);

                } catch (Exception e) {

                    System.out.println(
                            "Error reading owner document: "
                                    + document.getId()
                    );

                    e.printStackTrace();
                }
            }

            System.out.println(
                    "Owners fetched for society '"
                            + society
                            + "': "
                            + owners.size()
            );

        } catch (Exception e) {

            System.out.println(
                    "Error while fetching owners by society."
            );

            e.printStackTrace();
        }

        return owners;
    }

    // =========================================================
    // GET OWNER BY EMAIL
    // EMAIL = FIRESTORE DOCUMENT ID
    // =========================================================

    @Override
    public Owner getOwnerByEmail(
            String email) {

        try {

            // -------------------------------------------------
            // VALIDATE EMAIL
            // -------------------------------------------------

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Email is empty."
                );

                return null;
            }

            // -------------------------------------------------
            // NORMALIZE EMAIL
            // -------------------------------------------------

            email =
                    email.trim().toLowerCase();

            // -------------------------------------------------
            // DIRECT DOCUMENT LOOKUP
            // -------------------------------------------------

            DocumentSnapshot document =
                    firestore
                            .collection("Owners")
                            .document(email)
                            .get()
                            .get();

            // -------------------------------------------------
            // CHECK DOCUMENT
            // -------------------------------------------------

            if (!document.exists()) {

                System.out.println(
                        "Owner not found: "
                                + email
                );

                return null;
            }

            // -------------------------------------------------
            // CONVERT DOCUMENT
            // -------------------------------------------------

            Owner owner =
                    document.toObject(
                            Owner.class
                    );

            if (owner == null) {
                return null;
            }

            // -------------------------------------------------
            // SET ID
            // -------------------------------------------------

            owner.setId(
                    document.getId()
            );

            // -------------------------------------------------
            // IF EMAIL FIELD IS MISSING
            // USE DOCUMENT ID
            // -------------------------------------------------

            if (owner.getEmail() == null ||
                    owner.getEmail()
                            .trim()
                            .isEmpty()) {

                owner.setEmail(
                        document.getId()
                );
            }

            return owner;

        } catch (Exception e) {

            System.out.println(
                    "Error while fetching owner by email."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // UPDATE OWNER
    // EMAIL = DOCUMENT ID
    // =========================================================

    @Override
    public boolean updateOwner(
            String email,
            String name,
            String flat,
            String mobile,
            String status,
            String society) {

        try {

            // -------------------------------------------------
            // VALIDATE EMAIL
            // -------------------------------------------------

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Owner email is empty."
                );

                return false;
            }

            // -------------------------------------------------
            // NORMALIZE EMAIL
            // -------------------------------------------------

            email =
                    email.trim().toLowerCase();

            // -------------------------------------------------
            // CHECK DOCUMENT
            // -------------------------------------------------

            DocumentSnapshot document =
                    firestore
                            .collection("Owners")
                            .document(email)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "Owner does not exist: "
                                + email
                );

                return false;
            }

            // -------------------------------------------------
            // NORMALIZE VALUES
            // -------------------------------------------------

            if (name != null) {
                name = name.trim();
            }

            if (flat != null) {
                flat = flat.trim();
            }

            if (mobile != null) {
                mobile = mobile.trim();
            }

            if (status != null) {
                status = status.trim();
            }

            if (society != null) {
                society = society.trim();
            }

            // -------------------------------------------------
            // UPDATE OWNER
            // -------------------------------------------------

            firestore
                    .collection("Owners")
                    .document(email)
                    .update(
                            "name",
                            name,

                            "flat",
                            flat,

                            "mobile",
                            mobile,

                            "status",
                            status,

                            "society",
                            society
                    )
                    .get();

            System.out.println(
                    "Owner updated successfully: "
                            + email
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while updating owner."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // DELETE OWNER
    // EMAIL = DOCUMENT ID
    // =========================================================

    @Override
    public boolean deleteOwner(
            String email) {

        try {

            // -------------------------------------------------
            // VALIDATE EMAIL
            // -------------------------------------------------

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Owner email is empty."
                );

                return false;
            }

            // -------------------------------------------------
            // NORMALIZE EMAIL
            // -------------------------------------------------

            email =
                    email.trim().toLowerCase();

            // -------------------------------------------------
            // CHECK DOCUMENT BEFORE DELETE
            // -------------------------------------------------

            DocumentSnapshot document =
                    firestore
                            .collection("Owners")
                            .document(email)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "Owner does not exist: "
                                + email
                );

                return false;
            }

            // -------------------------------------------------
            // DELETE DOCUMENT
            // -------------------------------------------------

            firestore
                    .collection("Owners")
                    .document(email)
                    .delete()
                    .get();

            System.out.println(
                    "Owner deleted successfully: "
                            + email
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error while deleting owner."
            );

            e.printStackTrace();

            return false;
        }
    }
}