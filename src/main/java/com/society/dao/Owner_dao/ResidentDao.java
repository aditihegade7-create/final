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
    // Document ID = Resident Email
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


            // -------------------------------------------------
            // EMAIL
            // -------------------------------------------------

            String email =
                    resident.getEmail()
                            .trim()
                            .toLowerCase();


            // -------------------------------------------------
            // CREATE DATA
            // -------------------------------------------------

            Map<String, Object> data =
                    new HashMap<>();


            // =================================================
            // BASIC INFORMATION
            // =================================================

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


            // =================================================
            // RESIDENT TYPE
            // =================================================

            data.put(
                    "residentType",
                    safeString(
                            resident.getResidentType()
                    )
            );


            // =================================================
            // RELATIONSHIPS
            // =================================================

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


            // =================================================
            // RENTAL INFORMATION
            // =================================================

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


            // =================================================
            // OTHER INFORMATION
            // =================================================

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


            // =================================================
            // STATUS
            // =================================================

            data.put(
                    "status",
                    safeString(
                            resident.getStatus()
                    )
            );


            // =================================================
            // SAVE TO FIRESTORE
            //
            // Residents/{email}
            // =================================================

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
                    "Collection: " + COLLECTION
            );

            System.out.println(
                    "Document ID: " + email
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
    // Tenant is also stored inside Residents collection.
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
    // Used by Owner Profile
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


            email =
                    email.trim()
                            .toLowerCase();


            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(email)
                            .get()
                            .get();


            if (!document.exists()) {

                System.out.println(
                        "Resident not found: "
                                + email
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
    // Owners use Firestore auto-generated document IDs.
    // Therefore we query the email FIELD, not document(email).
    //
    // Owners/{autoId}
    // email, name, mobile, flat, status
    // =========================================================

    public ResidentModel getOwnerByEmail(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }

            email =
                    email.trim()
                            .toLowerCase();

            QuerySnapshot querySnapshot =
                    db.collection("Owners")
                            .whereEqualTo(
                                    "email",
                                    email
                            )
                            .limit(1)
                            .get()
                            .get();

            if (querySnapshot.isEmpty()) {

                System.out.println(
                        "Owner not found for email: " + email
                );

                return null;
            }

            DocumentSnapshot document =
                    querySnapshot.getDocuments().get(0);

            ResidentModel owner =
                    new ResidentModel();

            // Owners collection uses these exact field names.
            owner.setEmail(
                    document.getString("email")
            );

            owner.setName(
                    document.getString("name")
            );

            // IMPORTANT: Owners uses 'mobile', while ResidentModel
            // uses 'phone'.
            owner.setPhone(
                    document.getString("mobile")
            );

            owner.setFlat(
                    document.getString("flat")
            );

            owner.setStatus(
                    document.getString("status")
            );

            owner.setResidentType(
                    "Owner"
            );

            // =====================================================
            // SOCIETY
            // =====================================================

            String society =
                    document.getString("society");

            if (society == null ||
                    society.trim().isEmpty()) {

                society =
                        document.getString("societyName");
            }

            owner.setSociety(
                    safeString(society)
            );

            owner.setProfileImageUrl(
                    document.getString("profileImageUrl")
            );

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
                            + document.getString("email")
            );

            System.out.println(
                    "Owner Name: "
                            + document.getString("name")
            );

            System.out.println(
                    "Owner Mobile: "
                            + document.getString("mobile")
            );

            System.out.println(
                    "Owner Flat: "
                            + document.getString("flat")
            );

            System.out.println(
                    "Owner Status: "
                            + document.getString("status")
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
    // Owners/{autoId}
    // Finds the owner by email and stores the Cloudinary URL.
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
                    email.trim().toLowerCase();

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
                    querySnapshot.getDocuments().get(0);

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
    // Used in Owner Portal
    //
    // Finds tenants/family belonging to owner.
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


            ownerEmail =
                    ownerEmail.trim()
                            .toLowerCase();


            QuerySnapshot querySnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    ownerEmail
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
    // Only Tenant records with status = Active are returned.
    // The query uses ownerEmail only and filters the remaining
    // fields in Java, avoiding a Firestore composite-index
    // requirement.
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
                    ownerEmail.trim().toLowerCase();

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
                        document.getString("residentType");

                String status =
                        document.getString("status");

                if (residentType != null &&
                        residentType.equalsIgnoreCase("Tenant") &&
                        status != null &&
                        status.equalsIgnoreCase("Active")) {

                    tenants.add(
                            convertDocumentToModel(document)
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
    // Gets only Tenant records belonging
    // to the owner.
    //
    // Active + Inactive
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


            ownerEmail =
                    ownerEmail.trim()
                            .toLowerCase();


            QuerySnapshot querySnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    ownerEmail
                            )
                            .whereEqualTo(
                                    "residentType",
                                    "Tenant"
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
    // REMOVE TENANT BY OWNER + EMAIL
    //
    // Secure version used by the new Owner Portal UI.
    // The tenant must belong to the supplied owner, be a Tenant,
    // and currently be Active.
    // =========================================================

    public boolean removeTenant(
            String ownerEmail,
            String tenantEmail,
            String moveOutDate,
            String reason) {

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty() ||
                    tenantEmail == null ||
                    tenantEmail.trim().isEmpty()) {

                return false;
            }

            String normalizedOwnerEmail =
                    ownerEmail.trim().toLowerCase();

            String normalizedTenantEmail =
                    tenantEmail.trim().toLowerCase();

            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(normalizedTenantEmail)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "Tenant not found for this owner."
                );

                return false;
            }

            String documentOwnerEmail =
                    document.getString("ownerEmail");

            if (documentOwnerEmail == null ||
                    !documentOwnerEmail.trim().equalsIgnoreCase(
                            normalizedOwnerEmail)) {

                System.out.println(
                        "Tenant does not belong to this owner."
                );

                return false;
            }

            String residentType =
                    document.getString("residentType");

            String status =
                    document.getString("status");

            if (residentType == null ||
                    !residentType.equalsIgnoreCase("Tenant")) {

                System.out.println(
                        "Selected record is not a Tenant."
                );

                return false;
            }

            if (status == null ||
                    !status.equalsIgnoreCase("Active")) {

                System.out.println(
                        "Tenant is not currently active."
                );

                return false;
            }

            Map<String, Object> updates =
                    new HashMap<>();

            updates.put(
                    "status",
                    "Inactive"
            );

            updates.put(
                    "moveOutDate",
                    safeString(moveOutDate)
            );

            updates.put(
                    "removalReason",
                    safeString(reason)
            );

            document.getReference()
                    .update(updates)
                    .get();

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
    // IMPORTANT:
    // We DO NOT delete the tenant document.
    //
    // We update:
    //
    // status        -> Inactive
    // moveOutDate   -> selected date
    // removalReason -> reason
    //
    // This preserves rental history.
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


            tenantEmail =
                    tenantEmail.trim()
                            .toLowerCase();


            // -------------------------------------------------
            // GET TENANT
            // -------------------------------------------------

            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(tenantEmail)
                            .get()
                            .get();


            if (!document.exists()) {

                System.out.println(
                        "Tenant not found: "
                                + tenantEmail
                );

                return false;
            }


            // -------------------------------------------------
            // CHECK RESIDENT TYPE
            // -------------------------------------------------

            String residentType =
                    document.getString(
                            "residentType"
                    );


            if (residentType == null ||
                    !residentType.equalsIgnoreCase(
                            "Tenant"
                    )) {

                System.out.println(
                        "This resident is not a Tenant."
                );

                return false;
            }


            // -------------------------------------------------
            // CHECK STATUS
            // -------------------------------------------------

            String status =
                    document.getString(
                            "status"
                    );


            if (status != null &&
                    status.equalsIgnoreCase(
                            "Inactive"
                    )) {

                System.out.println(
                        "Tenant is already inactive."
                );

                return false;
            }


            // -------------------------------------------------
            // UPDATE
            // -------------------------------------------------

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "Inactive"
            );

            updates.put(
                    "moveOutDate",
                    safeString(moveOutDate)
            );

            updates.put(
                    "removalReason",
                    safeString(reason)
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
                            + tenantEmail
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
    // Existing RemoveTenant UI can continue
    // sending:
    //
    // ownerEmail
    // name
    // flat
    // moveOutDate
    // reason
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


            ownerEmail =
                    ownerEmail.trim()
                            .toLowerCase();

            name =
                    name.trim();

            flat =
                    flat.trim();


            // -------------------------------------------------
            // FIND ACTIVE TENANT
            // -------------------------------------------------

            QuerySnapshot querySnapshot =
                    db.collection(COLLECTION)
                            .whereEqualTo(
                                    "ownerEmail",
                                    ownerEmail
                            )
                            .whereEqualTo(
                                    "residentType",
                                    "Tenant"
                            )
                            .whereEqualTo(
                                    "name",
                                    name
                            )
                            .whereEqualTo(
                                    "flat",
                                    flat
                            )
                            .whereEqualTo(
                                    "status",
                                    "Active"
                            )
                            .get()
                            .get();


            if (querySnapshot.isEmpty()) {

                System.out.println(
                        "Active tenant not found."
                );

                return false;
            }


            DocumentSnapshot document =
                    querySnapshot
                            .getDocuments()
                            .get(0);


            // -------------------------------------------------
            // UPDATE
            // -------------------------------------------------

            Map<String, Object> updates =
                    new HashMap<>();


            updates.put(
                    "status",
                    "Inactive"
            );

            updates.put(
                    "moveOutDate",
                    safeString(moveOutDate)
            );

            updates.put(
                    "removalReason",
                    safeString(reason)
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
    // Document ID = Email
    //
    // Email itself is NOT changed.
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

                System.out.println(
                        "Resident email is empty."
                );

                return false;
            }


            email =
                    email.trim()
                            .toLowerCase();


            // -------------------------------------------------
            // GET DOCUMENT
            // -------------------------------------------------

            DocumentSnapshot document =
                    db.collection(COLLECTION)
                            .document(email)
                            .get()
                            .get();


            if (!document.exists()) {

                System.out.println(
                        "Resident not found: "
                                + email
                );

                return false;
            }


            // -------------------------------------------------
            // UPDATE DATA
            // -------------------------------------------------

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


            // -------------------------------------------------
            // UPDATE FIRESTORE
            // -------------------------------------------------

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
                    "Email: " + email
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
    // CONVERT FIRESTORE DOCUMENT TO MODEL
    // =========================================================

    private ResidentModel convertDocumentToModel(
            DocumentSnapshot document) {

        ResidentModel resident =
                new ResidentModel();


        // =====================================================
        // BASIC INFORMATION
        // =====================================================

        resident.setEmail(
                document.getString(
                        "email"
                )
        );

        resident.setName(
                document.getString(
                        "name"
                )
        );

        resident.setPhone(
                document.getString(
                        "phone"
                )
        );

        resident.setDateOfBirth(
                document.getString(
                        "dateOfBirth"
                )
        );

        resident.setGender(
                document.getString(
                        "gender"
                )
        );

        resident.setFlat(
                document.getString(
                        "flat"
                )
        );

        resident.setSociety(
                document.getString(
                        "society"
                )
        );


        // =====================================================
        // RESIDENT TYPE
        // =====================================================

        resident.setResidentType(
                document.getString(
                        "residentType"
                )
        );


        // =====================================================
        // RELATIONSHIPS
        // =====================================================

        resident.setOwnerEmail(
                document.getString(
                        "ownerEmail"
                )
        );

        resident.setPrimaryResidentEmail(
                document.getString(
                        "primaryResidentEmail"
                )
        );


        // =====================================================
        // RENTAL INFORMATION
        // =====================================================

        resident.setMoveInDate(
                document.getString(
                        "moveInDate"
                )
        );

        resident.setMoveOutDate(
                document.getString(
                        "moveOutDate"
                )
        );

        resident.setMonthlyRent(
                document.getString(
                        "monthlyRent"
                )
        );

        resident.setSecurityDeposit(
                document.getString(
                        "securityDeposit"
                )
        );


        // =====================================================
        // OTHER INFORMATION
        // =====================================================

        resident.setIdProof(
                document.getString(
                        "idProof"
                )
        );

        resident.setEmergencyContact(
                document.getString(
                        "emergencyContact"
                )
        );

        resident.setRemarks(
                document.getString(
                        "remarks"
                )
        );

        resident.setRemovalReason(
                document.getString(
                        "removalReason"
                )
        );


        // =====================================================
        // STATUS
        // =====================================================

        resident.setStatus(
                document.getString(
                        "status"
                )
        );


        return resident;
    }


    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safeString(
            String value) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
// UPDATE OWNER PROFILE
//
// Owners collection:
// Owners/{autoGeneratedId}
//
// Owner is located using the email FIELD.
// =========================================================

public boolean updateOwnerProfile(
        String email,
        String name,
        String phone,
        String flat,
        String society) {

    try {

        // =====================================================
        // VALIDATE EMAIL
        // =====================================================

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "Owner email is empty."
            );

            return false;
        }


        email =
                email.trim()
                        .toLowerCase();


        // =====================================================
        // FIND OWNER
        // =====================================================

        QuerySnapshot querySnapshot =
                db.collection("Owners")
                        .whereEqualTo(
                                "email",
                                email
                        )
                        .limit(1)
                        .get()
                        .get();


        // =====================================================
        // OWNER NOT FOUND
        // =====================================================

        if (querySnapshot.isEmpty()) {

            System.out.println(
                    "Owner not found for email: "
                            + email
            );

            return false;
        }


        // =====================================================
        // GET OWNER DOCUMENT
        // =====================================================

        DocumentSnapshot ownerDocument =
                querySnapshot
                        .getDocuments()
                        .get(0);


        // =====================================================
        // PREPARE UPDATE
        // =====================================================

        Map<String, Object> updates =
                new HashMap<>();


        updates.put(
                "name",
                safeString(name)
        );


        // IMPORTANT:
        // Owners collection uses "mobile",
        // NOT "phone".

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


        // =====================================================
        // UPDATE FIRESTORE
        // =====================================================

        ownerDocument
                .getReference()
                .update(updates)
                .get();


        // =====================================================
        // SUCCESS LOG
        // =====================================================

        System.out.println(
                "===================================="
        );

        System.out.println(
                "Owner profile updated successfully."
        );

        System.out.println(
                "Owner Email: "
                        + email
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
}