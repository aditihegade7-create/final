package com.society.controller.Owner_Controller;

import com.society.dao.Owner_dao.ResidentDao;
import com.society.model.Owner_model.ResidentModel;

import java.util.List;


public class ResidentController {

    // =========================================================
    // DAO
    // =========================================================

    private final ResidentDao residentDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ResidentController() {

        residentDao =
                new ResidentDao();
    }


    // =========================================================
    // ADD TENANT
    //
    // Used by AddTenant.java
    // =========================================================

    public boolean addTenant(

            String ownerEmail,
            String name,
            String phone,
            String email,
            String flat,
            String moveInDate,
            String monthlyRent,
            String securityDeposit,
            String idProof,
            String emergencyContact,
            String remarks) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return false;
            }


            if (name == null ||
                    name.trim().isEmpty()) {

                return false;
            }


            if (phone == null ||
                    phone.trim().isEmpty()) {

                return false;
            }


            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            if (flat == null ||
                    flat.trim().isEmpty()) {

                return false;
            }


            if (moveInDate == null ||
                    moveInDate.trim().isEmpty()) {

                return false;
            }


            if (monthlyRent == null ||
                    monthlyRent.trim().isEmpty()) {

                return false;
            }


            if (securityDeposit == null ||
                    securityDeposit.trim().isEmpty()) {

                return false;
            }


            if (idProof == null ||
                    idProof.trim().isEmpty()) {

                return false;
            }


            if (emergencyContact == null ||
                    emergencyContact.trim().isEmpty()) {

                return false;
            }


            // =================================================
            // CREATE RESIDENT MODEL
            // =================================================

            ResidentModel tenant =
                    new ResidentModel();


            // =================================================
            // OWNER INFORMATION
            // =================================================

            tenant.setOwnerEmail(
                    ownerEmail
                            .trim()
                            .toLowerCase()
            );


            // =================================================
            // TENANT INFORMATION
            // =================================================

            tenant.setName(
                    name.trim()
            );


            tenant.setPhone(
                    phone.trim()
            );


            tenant.setEmail(
                    email
                            .trim()
                            .toLowerCase()
            );


            tenant.setFlat(
                    flat.trim()
            );


            // =================================================
            // TENANT TYPE
            // =================================================

            tenant.setResidentType(
                    "Tenant"
            );


            // =================================================
            // RENTAL INFORMATION
            // =================================================

            tenant.setMoveInDate(
                    moveInDate.trim()
            );


            tenant.setMoveOutDate(
                    ""
            );


            tenant.setMonthlyRent(
                    monthlyRent.trim()
            );


            tenant.setSecurityDeposit(
                    securityDeposit.trim()
            );


            // =================================================
            // OTHER INFORMATION
            // =================================================

            tenant.setIdProof(
                    idProof.trim()
            );


            tenant.setEmergencyContact(
                    emergencyContact.trim()
            );


            if (remarks == null) {

                tenant.setRemarks("");

            } else {

                tenant.setRemarks(
                        remarks.trim()
                );
            }


            // =================================================
            // RELATIONSHIP
            // =================================================

            tenant.setPrimaryResidentEmail(
                    ""
            );


            // =================================================
            // SOCIETY FROM OWNER
            // =================================================

            ResidentModel owner =
                    residentDao.getOwnerByEmail(
                            ownerEmail
                                    .trim()
                                    .toLowerCase()
                    );

            if (owner == null ||
                    owner.getSociety() == null ||
                    owner.getSociety().trim().isEmpty()) {

                System.out.println(
                        "Owner society not found for: "
                                + ownerEmail
                );

                return false;
            }

            tenant.setSociety(
                    owner.getSociety().trim()
            );


            tenant.setDateOfBirth(
                    ""
            );


            tenant.setGender(
                    ""
            );


            tenant.setRemovalReason(
                    ""
            );


            // =================================================
            // STATUS
            // =================================================

            tenant.setStatus(
                    "Active"
            );


            // =================================================
            // SAVE TO FIRESTORE
            // =================================================

            return residentDao.addTenant(
                    tenant
            );


        } catch (Exception e) {

            System.out.println(
                    "ResidentController Add Tenant Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // ADD RESIDENT
    //
    // Used by general Resident/Owner signup
    // =========================================================

    public boolean addResident(
            ResidentModel resident) {

        try {

            if (resident == null) {

                return false;
            }


            if (resident.getEmail() == null ||
                    resident.getEmail()
                            .trim()
                            .isEmpty()) {

                return false;
            }


            // =================================================
            // SAVE THROUGH DAO
            // =================================================

            return residentDao.addResident(
                    resident
            );


        } catch (Exception e) {

            System.out.println(
                    "ResidentController Add Resident Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // REMOVE TENANT BY OWNER + EMAIL
    //
    // Used by the new Owner Portal UI.
    // The owner email is checked so an owner can only remove
    // a tenant belonging to that owner.
    // =========================================================

    public boolean removeTenant(
            String ownerEmail,
            String tenantEmail,
            String moveOutDate,
            String reason) {

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {
                return false;
            }

            if (tenantEmail == null ||
                    tenantEmail.trim().isEmpty()) {
                return false;
            }

            if (moveOutDate == null ||
                    moveOutDate.trim().isEmpty()) {
                return false;
            }

            if (reason == null ||
                    reason.trim().isEmpty()) {
                return false;
            }

            return residentDao.removeTenant(
                    ownerEmail.trim().toLowerCase(),
                    tenantEmail.trim().toLowerCase(),
                    moveOutDate.trim(),
                    reason.trim()
            );

        } catch (Exception e) {

            System.out.println(
                    "ResidentController Remove Tenant Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // REMOVE TENANT BY EMAIL
    //
    // Firestore:
    //
    // Residents/{tenantEmail}
    //
    // Example:
    //
    // Residents/kashish@gmail.com
    // =========================================================

    public boolean removeTenant(

            String tenantEmail,
            String moveOutDate,
            String reason) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (tenantEmail == null ||
                    tenantEmail.trim().isEmpty()) {

                return false;
            }


            if (moveOutDate == null ||
                    moveOutDate.trim().isEmpty()) {

                return false;
            }


            if (reason == null) {

                reason = "";
            }


            // =================================================
            // SEND TO DAO
            // =================================================

            return residentDao.removeTenant(

                    tenantEmail
                            .trim()
                            .toLowerCase(),

                    moveOutDate.trim(),

                    reason.trim()
            );


        } catch (Exception e) {

            System.out.println(
                    "ResidentController Remove Tenant Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // REMOVE TENANT
    //
    // Compatibility method for older RemoveTenant UI
    //
    // ownerEmail + name + flat
    // =========================================================

    public boolean removeTenant(

            String ownerEmail,
            String name,
            String flat,
            String moveOutDate,
            String reason) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

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


            if (moveOutDate == null ||
                    moveOutDate.trim().isEmpty()) {

                return false;
            }


            if (reason == null ||
                    reason.trim().isEmpty()) {

                return false;
            }


            // =================================================
            // SEND TO DAO
            // =================================================

            return residentDao.removeTenant(

                    ownerEmail
                            .trim()
                            .toLowerCase(),

                    name.trim(),

                    flat.trim(),

                    moveOutDate.trim(),

                    reason.trim()
            );


        } catch (Exception e) {

            System.out.println(
                    "ResidentController Remove Tenant Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // GET RESIDENTS BY OWNER EMAIL
    // =========================================================

    public List<ResidentModel> getResidentsByOwnerEmail(

            String ownerEmail) {

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return List.of();
            }


            return residentDao
                    .getResidentsByOwnerEmail(

                            ownerEmail
                                    .trim()
                                    .toLowerCase()
                    );


        } catch (Exception e) {

            System.out.println(
                    "ResidentController Get Residents Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return List.of();
        }
    }


    // =========================================================
    // GET ACTIVE TENANTS BY OWNER EMAIL
    //
    // Used by Owner Portal Remove Tenant page.
    // Only active Tenant records belonging to the logged-in owner
    // are returned.
    // =========================================================

    public List<ResidentModel> getActiveTenants(String ownerEmail) {

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return List.of();
            }

            return residentDao.getActiveTenants(
                    ownerEmail.trim().toLowerCase()
            );

        } catch (Exception e) {

            System.out.println(
                    "ResidentController Get Active Tenants Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return List.of();
        }
    }


    // =========================================================
    // GET RENTAL HISTORY
    // =========================================================

    public List<ResidentModel> getRentalHistory(

            String ownerEmail) {

        try {

            if (ownerEmail == null ||
                    ownerEmail.trim().isEmpty()) {

                return List.of();
            }


            return residentDao.getRentalHistory(

                    ownerEmail
                            .trim()
                            .toLowerCase()
            );


        } catch (Exception e) {

            System.out.println(
                    "ResidentController Rental History Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return List.of();
        }
    }


    // =========================================================
    // GET RESIDENT BY EMAIL
    //
    // Example:
    //
    // getResidentByEmail("kashish@gmail.com")
    // =========================================================

    public ResidentModel getResidentByEmail(

            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }


            return residentDao.getResidentByEmail(

                    email
                            .trim()
                            .toLowerCase()
            );


        } catch (Exception e) {

            System.out.println(
                    "ResidentController Get Resident Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // UPDATE OWNER PROFILE IMAGE
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

            return residentDao.updateOwnerProfileImage(
                    email.trim().toLowerCase(),
                    profileImageUrl.trim()
            );

        } catch (Exception e) {

            System.out.println(
                    "ResidentController Profile Image Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }



    // =========================================================
    // GET OWNER BY EMAIL
    // =========================================================

    public ResidentModel getOwnerByEmail(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }

            return residentDao.getOwnerByEmail(
                    email.trim().toLowerCase()
            );

        } catch (Exception e) {

            System.out.println(
                    "ResidentController Get Owner Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
// UPDATE OWNER PROFILE
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

        if (name == null ||
                name.trim().isEmpty()) {

            return false;
        }

        if (phone == null ||
                phone.trim().isEmpty()) {

            return false;
        }

        if (flat == null ||
                flat.trim().isEmpty()) {

            return false;
        }

        if (society == null ||
                society.trim().isEmpty()) {

            return false;
        }

        return residentDao.updateResidentProfile(
                email.trim().toLowerCase(),
                name.trim(),
                phone.trim(),
                flat.trim(),
                society.trim()
        );

    } catch (Exception e) {

        System.out.println(
                "ResidentController Error: "
                        + e.getMessage()
        );

        e.printStackTrace();

        return false;
    }
}


// =========================================================
// UPDATE OWNER PROFILE
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


        if (name == null ||
                name.trim().isEmpty()) {

            System.out.println(
                    "Owner name is empty."
            );

            return false;
        }


        return residentDao.updateOwnerProfile(
                email.trim().toLowerCase(),
                name.trim(),
                phone == null ? "" : phone.trim(),
                flat == null ? "" : flat.trim(),
                society == null
                        ? ""
                        : society.trim()
        );

    } catch (Exception e) {

        System.out.println(
                "ResidentController Update Owner Profile Error: "
                        + e.getMessage()
        );

        e.printStackTrace();

        return false;
    }
}
}