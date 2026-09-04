package com.society.dao.Owner_dao;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Owner_model.ResidentModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResidentDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db;


    // =========================================================
    // COLLECTION
    // =========================================================

    private static final String COLLECTION =
            "Residents";


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ResidentDao() {

        db = FirebaseConfig.getFirestore();
    }


    // =========================================================
    // ADD RESIDENT
    //
    // Residents/{email}
    // =========================================================

    public boolean addResident(
            ResidentModel resident) {

        try {

            if (resident == null) {

                System.out.println(
                        "Resident object is null."
                );

                return false;
            }


            if (resident.getEmail() == null ||
                    resident.getEmail().trim().isEmpty()) {

                System.out.println(
                        "Resident email is empty."
                );

                return false;
            }


            String email =
                    resident.getEmail()
                            .trim()
                            .toLowerCase();


            Map<String, Object> data =
                    new HashMap<>();


            // =====================================================
            // BASIC INFORMATION
            // =====================================================

            data.put(
                    "email",
                    email
            );

            data.put(
                    "name",
                    safeString(
                            resident.getName()
                    )
            );

            data.put(
                    "phone",
                    safeString(
                            resident.getPhone()
                    )
            );

            data.put(
                    "dateOfBirth",
                    safeString(
                            resident.getDateOfBirth()
                    )
            );

            data.put(
                    "gender",
                    safeString(
                            resident.getGender()
                    )
            );

            data.put(
                    "flat",
                    safeString(
                            resident.getFlat()
                    )
            );

            data.put(
                    "society",
                    safeString(
                            resident.getSociety()
                    )
            );


            // =====================================================
            // RESIDENT TYPE
            // =====================================================

            data.put(
                    "residentType",
                    safeString(
                            resident.getResidentType()
                    )
            );


            // =====================================================
            // RELATIONSHIP INFORMATION
            // =====================================================

            data.put(
                    "ownerEmail",
                    safeString(
                            resident.getOwnerEmail()
                    )
            );

            data.put(
                    "primaryResidentEmail",
                    safeString(
                            resident.getPrimaryResidentEmail()
                    )
            );


            // =====================================================
            // RENTAL INFORMATION
            // =====================================================

            data.put(
                    "moveInDate",
                    safeString(
                            resident.getMoveInDate()
                    )
            );

            data.put(
                    "moveOutDate",
                    safeString(
                            resident.getMoveOutDate()
                    )
            );

            data.put(
                    "monthlyRent",
                    safeString(
                            resident.getMonthlyRent()
                    )
            );

            data.put(
                    "securityDeposit",
                    safeString(
                            resident.getSecurityDeposit()
                    )
            );


            // =====================================================
            // OTHER INFORMATION
            // =====================================================

            data.put(
                    "idProof",
                    safeString(
                            resident.getIdProof()
                    )
            );

            data.put(
                    "emergencyContact",
                    safeString(
                            resident.getEmergencyContact()
                    )
            );

            data.put(
                    "remarks",
                    safeString(
                            resident.getRemarks()
                    )
            );

            data.put(
                    "removalReason",
                    safeString(
                            resident.getRemovalReason()
                    )
            );


            // =====================================================
            // STATUS
            // =====================================================

            data.put(
                    "status",
                    safeString(
                            resident.getStatus()
                    )
            );


            // =====================================================
            // PROFILE IMAGE
            // =====================================================

            data.put(
                    "profileImageUrl",
                    safeString(
                            resident.getProfileImageUrl()
                    )
            );


            // =====================================================
            // SAVE
            // =====================================================

            db.collection(COLLECTION)
                    .document(email)
                    .set(data)
                    .get();


            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Resident added successfully."
            );

            System.out.println(
                    "Collection: "
                            + COLLECTION
            );

            System.out.println(
                    "Document ID: "
                            + email
            );

            System.out.println(
                    "Resident Type: "
                            + resident.getResidentType()
            );

            System.out.println(
                    "===================================="
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error adding resident: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // ADD TENANT
    //
    // Tenant is stored inside Residents collection.
    //
    // Document ID = Tenant Email
    // =========================================================

    public boolean addTenant(
            ResidentModel tenant) {

        return addResident(
                tenant
        );
    }


    // =========================================================
    // GET RESIDENT BY EMAIL
    //
    // Residents/{email}
    // =========================================================

    public ResidentModel getResidentByEmail(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }


            String normalizedEmail =
                    email.trim()
                            .toLowerCase();


            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(normalizedEmail)
                            .get()
                            .get();


            if (!document.exists()) {

                System.out.println(
                        "Resident not found: "
                                + normalizedEmail
                );

                return null;
            }


            return convertDocumentToModel(
                    document
            );


        } catch (Exception e) {

            System.out.println(
                    "Error getting resident: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // GET OWNER BY EMAIL
    //
    // Owners/{autoGeneratedId}
    //
    // IMPORTANT:
    // Owners use auto-generated Firestore document IDs.
    // Therefore the owner is searched using the email FIELD.
    //
    // This method also stores:
    //
    // ownerId = Firestore document ID
    // =========================================================

    public ResidentModel getOwnerByEmail(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }


            String normalizedEmail =
                    email.trim()
                            .toLowerCase();


            QuerySnapshot querySnapshot =
                    db.collection("Owners")
                            .whereEqualTo(
                                    "email",
                                    normalizedEmail
                            )
                            .limit(1)
                            .get()
                            .get();


            if (querySnapshot.isEmpty()) {

                System.out.println(
                        "Owner not found for email: "
                                + normalizedEmail
                );

                return null;
            }


            DocumentSnapshot document =
                    querySnapshot
                            .getDocuments()
                            .get(0);


            ResidentModel owner =
                    new ResidentModel();


            // =====================================================
            // OWNER ID
            //
            // Owners use an auto-generated Firestore document ID.
            // =====================================================

            owner.setOwnerId(
                    document.getId()
            );


            // =====================================================
            // BASIC INFORMATION
            // =====================================================

            owner.setEmail(
                    getString(
                            document,
                            "email"
                    )
            );


            owner.setName(
                    getString(
                            document,
                            "name"
                    )
            );


            /*
             * Owners currently use "mobile".
             *
             * We also support "phone" as a fallback so that
             * an older Owner document does not lose its value.
             */

            String phone =
                    getString(
                            document,
                            "mobile"
                    );

            if (phone.isEmpty()) {

                phone =
                        getString(
                                document,
                                "phone"
                        );
            }

            owner.setPhone(
                    phone
            );


            // =====================================================
            // FLAT
            // =====================================================

            String flat =
                    getString(
                            document,
                            "flat"
                    );

            if (flat.isEmpty()) {

                flat =
                        getString(
                                document,
                                "flatNo"
                        );
            }

            if (flat.isEmpty()) {

                flat =
                        getString(
                                document,
                                "flatNumber"
                        );
            }

            owner.setFlat(
                    flat
            );


            // =====================================================
            // SOCIETY
            // =====================================================

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

            owner.setSociety(
                    society
            );


            // =====================================================
            // DATE OF BIRTH
            // =====================================================

            owner.setDateOfBirth(
                    getString(
                            document,
                            "dateOfBirth"
                    )
            );


            // =====================================================
            // GENDER
            // =====================================================

            owner.setGender(
                    getString(
                            document,
                            "gender"
                    )
            );


            // =====================================================
            // RESIDENT TYPE
            // =====================================================

            owner.setResidentType(
                    "Owner"
            );


            // =====================================================
            // STATUS
            // =====================================================

            owner.setStatus(
                    getString(
                            document,
                            "status"
                    )
            );


            // =====================================================
            // PROFILE IMAGE
            // =====================================================

            owner.setProfileImageUrl(
                    getString(
                            document,
                            "profileImageUrl"
                    )
            );


            // =====================================================
            // PRINT OWNER INFORMATION
            // =====================================================

            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Owner found successfully."
            );

            System.out.println(
                    "Owner Document ID: "
                            + document.getId()
            );

            System.out.println(
                    "Owner Email: "
                            + owner.getEmail()
            );

            System.out.println(
                    "Owner Name: "
                            + owner.getName()
            );

            System.out.println(
                    "Owner Mobile: "
                            + owner.getPhone()
            );

            System.out.println(
                    "Owner Flat: "
                            + owner.getFlat()
            );

            System.out.println(
                    "Owner Society: "
                            + owner.getSociety()
            );

            System.out.println(
                    "Owner DOB: "
                            + owner.getDateOfBirth()
            );

            System.out.println(
                    "Owner Gender: "
                            + owner.getGender()
            );

            System.out.println(
                    "Owner Status: "
                            + owner.getStatus()
            );

            System.out.println(
                    "===================================="
            );


            return owner;


        } catch (Exception e) {

            System.out.println(
                    "Error getting owner: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // UPDATE OWNER PROFILE IMAGE
    //
    // Owners/{autoGeneratedId}
    // =========================================================

    public boolean updateOwnerProfileImage(
            String email,
            String profileImageUrl) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            if (profileImageUrl == null ||
                    profileImageUrl.trim().isEmpty()) {

                return false;
            }


            String normalizedEmail =
                    email.trim()
                            .toLowerCase();


            QuerySnapshot querySnapshot =
                    db.collection("Owners")
                            .whereEqualTo(
                                    "email",
                                    normalizedEmail
                            )
                            .limit(1)
                            .get()
                            .get();


            if (querySnapshot.isEmpty()) {

                System.out.println(
                        "Owner not found while saving profile image: "
                                + normalizedEmail
                );

                return false;
            }


            DocumentSnapshot document =
                    querySnapshot
                            .getDocuments()
                            .get(0);


            document.getReference()
                    .update(
                            "profileImageUrl",
                            profileImageUrl.trim()
                    )
                    .get();


            System.out.println(
                    "Owner profile image URL saved successfully."
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error updating owner profile image: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET RESIDENTS BY OWNER EMAIL
    //
    // Gets all Residents belonging to this owner.
    // =========================================================

    public List<ResidentModel> getResidentsByOwnerEmail(
            String ownerEmail) {

        List<ResidentModel> residents =
                new ArrayList<>();


        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return residents;
            }


            String normalizedOwnerEmail =
                    ownerEmail.trim()
                            .toLowerCase();


            QuerySnapshot querySnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    normalizedOwnerEmail
                            )
                            .get()
                            .get();


            for (DocumentSnapshot document :
                    querySnapshot.getDocuments()) {

                ResidentModel resident =
                        convertDocumentToModel(
                                document
                        );


                residents.add(
                        resident
                );
            }


            System.out.println(
                    "Residents found: "
                            + residents.size()
            );


        } catch (Exception e) {

            System.out.println(
                    "Error loading residents: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return residents;
    }


    // =========================================================
    // GET ACTIVE TENANTS BY OWNER EMAIL
    //
    // This is used by the new Remove Tenant UI.
    //
    // Only records satisfying BOTH conditions are returned:
    //
    // residentType = Tenant
    // status       = Active
    //
    // Owner filtering is done using ownerEmail.
    //
    // We intentionally filter Tenant + Active in Java after
    // querying ownerEmail so Firestore does not require a
    // composite index for this screen.
    // =========================================================

    public List<ResidentModel> getActiveTenants(
            String ownerEmail) {

        List<ResidentModel> tenants =
                new ArrayList<>();


        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return tenants;
            }


            String normalizedOwnerEmail =
                    ownerEmail.trim()
                            .toLowerCase();


            QuerySnapshot querySnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    normalizedOwnerEmail
                            )
                            .get()
                            .get();


            for (DocumentSnapshot document :
                    querySnapshot.getDocuments()) {

                String residentType =
                        getString(
                                document,
                                "residentType"
                        );


                String status =
                        getString(
                                document,
                                "status"
                        );


                if (residentType.equalsIgnoreCase(
                        "Tenant"
                ) &&
                        status.equalsIgnoreCase(
                                "Active"
                        )) {


                    ResidentModel tenant =
                            convertDocumentToModel(
                                    document
                            );


                    tenants.add(
                            tenant
                    );
                }
            }


            System.out.println(
                    "Active tenants found for owner: "
                            + tenants.size()
            );


        } catch (Exception e) {

            System.out.println(
                    "Error loading active tenants: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return tenants;
    }


    // =========================================================
    // GET RENTAL HISTORY
    //
    // Returns Tenant records belonging to owner.
    //
    // Includes:
    // Active tenants
    // Inactive tenants
    //
    // This is intentionally filtered in Java after querying
    // ownerEmail to avoid a Firestore composite-index dependency.
    // =========================================================

    public List<ResidentModel> getRentalHistory(
            String ownerEmail) {

        List<ResidentModel> residents =
                new ArrayList<>();


        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return residents;
            }


            String normalizedOwnerEmail =
                    ownerEmail.trim()
                            .toLowerCase();


            QuerySnapshot querySnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    normalizedOwnerEmail
                            )
                            .get()
                            .get();


            for (DocumentSnapshot document :
                    querySnapshot.getDocuments()) {

                String residentType =
                        getString(
                                document,
                                "residentType"
                        );


                if (residentType.equalsIgnoreCase(
                        "Tenant"
                )) {

                    residents.add(
                            convertDocumentToModel(
                                    document
                            )
                    );
                }
            }


            System.out.println(
                    "Rental history records found: "
                            + residents.size()
            );


        } catch (Exception e) {

            System.out.println(
                    "Error loading rental history: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }


        return residents;
    }


    // =========================================================
    // REMOVE TENANT BY OWNER EMAIL + TENANT EMAIL
    //
    // NEW METHOD FOR THE NEW REMOVE TENANT UI
    //
    // IMPORTANT:
    // The tenant document is NOT deleted.
    //
    // Instead:
    //
    // status        -> Inactive
    // moveOutDate   -> selected date
    // removalReason -> reason
    //
    // This preserves Rental History.
    // =========================================================

    public boolean removeTenant(
            String ownerEmail,
            String tenantEmail,
            String moveOutDate,
            String reason) {

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                System.out.println(
                        "Owner email is empty."
                );

                return false;
            }


            if (tenantEmail == null ||
                    tenantEmail.trim().isEmpty()) {

                System.out.println(
                        "Tenant email is empty."
                );

                return false;
            }


            String normalizedOwnerEmail =
                    ownerEmail.trim()
                            .toLowerCase();


            String normalizedTenantEmail =
                    tenantEmail.trim()
                            .toLowerCase();


            // =====================================================
            // GET TENANT
            // =====================================================

            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(
                                    normalizedTenantEmail
                            )
                            .get()
                            .get();


            if (!document.exists()) {

                System.out.println(
                        "Tenant not found: "
                                + normalizedTenantEmail
                );

                return false;
            }


            // =====================================================
            // VERIFY OWNER
            // =====================================================

            String documentOwnerEmail =
                    getString(
                            document,
                            "ownerEmail"
                    );


            if (!documentOwnerEmail.equalsIgnoreCase(
                    normalizedOwnerEmail
            )) {

                System.out.println(
                        "Tenant does not belong to this owner."
                );

                return false;
            }


            // =====================================================
            // VERIFY RESIDENT TYPE
            // =====================================================

            String residentType =
                    getString(
                            document,
                            "residentType"
                    );


            if (!residentType.equalsIgnoreCase(
                    "Tenant"
            )) {

                System.out.println(
                        "Selected record is not a Tenant."
                );

                return false;
            }


            // =====================================================
            // VERIFY STATUS
            // =====================================================

            String status =
                    getString(
                            document,
                            "status"
                    );


            if (!status.equalsIgnoreCase(
                    "Active"
            )) {

                System.out.println(
                        "Tenant is not currently active."
                );

                return false;
            }


            // =====================================================
            // PREPARE UPDATE
            // =====================================================

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "Inactive"
            );


            updates.put(
                    "moveOutDate",
                    safeString(
                            moveOutDate
                    )
            );


            updates.put(
                    "removalReason",
                    safeString(
                            reason
                    )
            );


            // =====================================================
            // UPDATE FIRESTORE
            // =====================================================

            document.getReference()
                    .update(updates)
                    .get();


            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Tenant removed successfully."
            );

            System.out.println(
                    "Tenant Email: "
                            + normalizedTenantEmail
            );

            System.out.println(
                    "Owner Email: "
                            + normalizedOwnerEmail
            );

            System.out.println(
                    "Status: Inactive"
            );

            System.out.println(
                    "Move-out Date: "
                            + moveOutDate
            );

            System.out.println(
                    "Reason: "
                            + reason
            );

            System.out.println(
                    "===================================="
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error removing tenant by owner and email: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // REMOVE TENANT BY EMAIL
    //
    // Compatibility method.
    //
    // Used if another part of the project calls:
    //
    // removeTenant(
    //     tenantEmail,
    //     moveOutDate,
    //     reason
    // )
    // =========================================================

    public boolean removeTenant(
            String tenantEmail,
            String moveOutDate,
            String reason) {

        try {

            if (tenantEmail == null ||
                    tenantEmail.trim().isEmpty()) {

                System.out.println(
                        "Tenant email is empty."
                );

                return false;
            }


            String normalizedTenantEmail =
                    tenantEmail.trim()
                            .toLowerCase();


            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(
                                    normalizedTenantEmail
                            )
                            .get()
                            .get();


            if (!document.exists()) {

                System.out.println(
                        "Tenant not found: "
                                + normalizedTenantEmail
                );

                return false;
            }


            // =====================================================
            // CHECK RESIDENT TYPE
            // =====================================================

            String residentType =
                    getString(
                            document,
                            "residentType"
                    );


            if (!residentType.equalsIgnoreCase(
                    "Tenant"
            )) {

                System.out.println(
                        "This resident is not a Tenant."
                );

                return false;
            }


            // =====================================================
            // CHECK STATUS
            // =====================================================

            String status =
                    getString(
                            document,
                            "status"
                    );


            if (status.equalsIgnoreCase(
                    "Inactive"
            )) {

                System.out.println(
                        "Tenant is already inactive."
                );

                return false;
            }


            // =====================================================
            // UPDATE
            // =====================================================

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "Inactive"
            );


            updates.put(
                    "moveOutDate",
                    safeString(
                            moveOutDate
                    )
            );


            updates.put(
                    "removalReason",
                    safeString(
                            reason
                    )
            );


            document.getReference()
                    .update(updates)
                    .get();


            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Tenant removed successfully."
            );

            System.out.println(
                    "Tenant Email: "
                            + normalizedTenantEmail
            );

            System.out.println(
                    "Status: Inactive"
            );

            System.out.println(
                    "===================================="
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error removing tenant: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // REMOVE TENANT
    //
    // COMPATIBILITY METHOD
    //
    // Existing UI may send:
    //
    // ownerEmail
    // name
    // flat
    // moveOutDate
    // reason
    //
    // We keep this method so existing code does not break.
    // =========================================================

    public boolean removeTenant(
            String ownerEmail,
            String name,
            String flat,
            String moveOutDate,
            String reason) {

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return false;
            }


            if (name == null ||
                    name.trim().isEmpty()) {

                return false;
            }


            if (flat == null ||
                    flat.trim().isEmpty()) {

                return false;
            }


            String normalizedOwnerEmail =
                    ownerEmail.trim()
                            .toLowerCase();


            String normalizedName =
                    name.trim();


            String normalizedFlat =
                    flat.trim();


            // =====================================================
            // GET ALL OWNER RESIDENTS
            //
            // We avoid a multi-field Firestore query here.
            // =====================================================

            QuerySnapshot querySnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    normalizedOwnerEmail
                            )
                            .get()
                            .get();


            for (DocumentSnapshot document :
                    querySnapshot.getDocuments()) {

                String residentType =
                        getString(
                                document,
                                "residentType"
                        );


                String status =
                        getString(
                                document,
                                "status"
                        );


                String documentName =
                        getString(
                                document,
                                "name"
                        );


                String documentFlat =
                        getString(
                                document,
                                "flat"
                        );


                if (residentType.equalsIgnoreCase(
                        "Tenant"
                ) &&
                        status.equalsIgnoreCase(
                                "Active"
                        ) &&
                        documentName.equalsIgnoreCase(
                                normalizedName
                        ) &&
                        documentFlat.equalsIgnoreCase(
                                normalizedFlat
                        )) {


                    Map<String, Object> updates =
                            new HashMap<>();


                    updates.put(
                            "status",
                            "Inactive"
                    );


                    updates.put(
                            "moveOutDate",
                            safeString(
                                    moveOutDate
                            )
                    );


                    updates.put(
                            "removalReason",
                            safeString(
                                    reason
                            )
                    );


                    document.getReference()
                            .update(updates)
                            .get();


                    System.out.println(
                            "Tenant removed successfully."
                    );


                    System.out.println(
                            "Tenant Email: "
                                    + document.getId()
                    );


                    return true;
                }
            }


            System.out.println(
                    "Active tenant not found."
            );


            return false;


        } catch (Exception e) {

            System.out.println(
                    "Error removing tenant: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // UPDATE RESIDENT PROFILE
    //
    // Residents/{email}
    // =========================================================

    public boolean updateResidentProfile(
            String email,
            String name,
            String phone,
            String flat,
            String society) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            String normalizedEmail =
                    email.trim()
                            .toLowerCase();


            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(
                                    normalizedEmail
                            )
                            .get()
                            .get();


            if (!document.exists()) {

                System.out.println(
                        "Resident not found: "
                                + normalizedEmail
                );

                return false;
            }


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "name",
                    safeString(name)
            );


            updates.put(
                    "phone",
                    safeString(phone)
            );


            updates.put(
                    "flat",
                    safeString(flat)
            );


            updates.put(
                    "society",
                    safeString(society)
            );


            document.getReference()
                    .update(updates)
                    .get();


            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Resident profile updated."
            );

            System.out.println(
                    "Email: "
                            + normalizedEmail
            );

            System.out.println(
                    "===================================="
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error updating resident profile: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // UPDATE OWNER PROFILE
    //
    // Owners/{autoGeneratedId}
    //
    // Owner is located using email.
    //
    // IMPORTANT:
    // Owners use:
    //
    // mobile
    // flat
    // society
    //
    // NOT phone.
    // =========================================================

    public boolean updateOwnerProfile(
            String email,
            String name,
            String phone,
            String flat,
            String society) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                System.out.println(
                        "Owner email is empty."
                );

                return false;
            }


            String normalizedEmail =
                    email.trim()
                            .toLowerCase();


            QuerySnapshot querySnapshot =
                    db.collection("Owners")
                            .whereEqualTo(
                                    "email",
                                    normalizedEmail
                            )
                            .limit(1)
                            .get()
                            .get();


            if (querySnapshot.isEmpty()) {

                System.out.println(
                        "Owner not found for email: "
                                + normalizedEmail
                );

                return false;
            }


            DocumentSnapshot ownerDocument =
                    querySnapshot
                            .getDocuments()
                            .get(0);


            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "name",
                    safeString(name)
            );


            // =====================================================
            // OWNER PHONE FIELD
            // =====================================================

            updates.put(
                    "mobile",
                    safeString(phone)
            );


            updates.put(
                    "flat",
                    safeString(flat)
            );


            updates.put(
                    "society",
                    safeString(society)
            );


            ownerDocument
                    .getReference()
                    .update(updates)
                    .get();


            System.out.println(
                    "===================================="
            );

            System.out.println(
                    "Owner profile updated successfully."
            );

            System.out.println(
                    "Owner Email: "
                            + normalizedEmail
            );

            System.out.println(
                    "Owner Document ID: "
                            + ownerDocument.getId()
            );

            System.out.println(
                    "===================================="
            );


            return true;


        } catch (Exception e) {

            System.out.println(
                    "Error updating owner profile: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // CONVERT RESIDENT DOCUMENT TO MODEL
    //
    // Used for Residents collection.
    // =========================================================

    private ResidentModel convertDocumentToModel(
            DocumentSnapshot document) {

        ResidentModel resident =
                new ResidentModel();


        // =====================================================
        // RESIDENT ID
        //
        // Residents/{email}
        // =====================================================

        /*
         * The ResidentModel getResidentId() already returns
         * the email, because the Residents collection uses
         * email as its document ID.
         */


        // =====================================================
        // BASIC INFORMATION
        // =====================================================

        resident.setEmail(
                getString(
                        document,
                        "email"
                )
        );


        resident.setName(
                getString(
                        document,
                        "name"
                )
        );


        resident.setPhone(
                getString(
                        document,
                        "phone"
                )
        );


        resident.setDateOfBirth(
                getString(
                        document,
                        "dateOfBirth"
                )
        );


        resident.setGender(
                getString(
                        document,
                        "gender"
                )
        );


        resident.setFlat(
                getString(
                        document,
                        "flat"
                )
        );


        resident.setSociety(
                getString(
                        document,
                        "society"
                )
        );


        // =====================================================
        // RESIDENT TYPE
        // =====================================================

        resident.setResidentType(
                getString(
                        document,
                        "residentType"
                )
        );


        // =====================================================
        // RELATIONSHIPS
        // =====================================================

        resident.setOwnerEmail(
                getString(
                        document,
                        "ownerEmail"
                )
        );


        resident.setPrimaryResidentEmail(
                getString(
                        document,
                        "primaryResidentEmail"
                )
        );


        // =====================================================
        // RENTAL INFORMATION
        // =====================================================

        resident.setMoveInDate(
                getString(
                        document,
                        "moveInDate"
                )
        );


        resident.setMoveOutDate(
                getString(
                        document,
                        "moveOutDate"
                )
        );


        resident.setMonthlyRent(
                getString(
                        document,
                        "monthlyRent"
                )
        );


        resident.setSecurityDeposit(
                getString(
                        document,
                        "securityDeposit"
                )
        );


        // =====================================================
        // OTHER INFORMATION
        // =====================================================

        resident.setIdProof(
                getString(
                        document,
                        "idProof"
                )
        );


        resident.setEmergencyContact(
                getString(
                        document,
                        "emergencyContact"
                )
        );


        resident.setRemarks(
                getString(
                        document,
                        "remarks"
                )
        );


        resident.setRemovalReason(
                getString(
                        document,
                        "removalReason"
                )
        );


        // =====================================================
        // STATUS
        // =====================================================

        resident.setStatus(
                getString(
                        document,
                        "status"
                )
        );


        // =====================================================
        // PROFILE IMAGE
        // =====================================================

        resident.setProfileImageUrl(
                getString(
                        document,
                        "profileImageUrl"
                )
        );


        return resident;
    }


    // =========================================================
    // GET STRING SAFELY
    //
    // Prevents null values from reaching the UI.
    // =========================================================

    private String getString(
            DocumentSnapshot document,
            String fieldName) {

        if (document == null ||
                fieldName == null) {

            return "";
        }


        String value =
                document.getString(
                        fieldName
                );


        if (value == null) {

            return "";
        }


        return value.trim();
    }


    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safeString(
            String value) {

        if (value == null) {

            return "";
        }


        return value.trim();
    }

}