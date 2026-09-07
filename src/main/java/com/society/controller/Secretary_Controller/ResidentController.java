package com.society.controller.Secretary_Controller;

import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.society.config.FirebaseConfig;
import com.society.dao.Secretary_dao.ResidentDao;
import com.society.dao.Secretary_dao.ResidentDaoImpl;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Resident;

public class ResidentController {

    private final ResidentDao residentDao;
    private final Firestore firestore;

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
    // ADD RESIDENT
    //
    // Existing 5-field method.
    // =========================================================

    public boolean addResident(
            String name,
            String flat,
            String mobile,
            String email,
            String status
    ) {

        String residentEmail =
                cleanEmail(email);

        if (residentEmail.isEmpty()) {

            System.out.println(
                    "ResidentController: Email is empty."
            );

            return false;
        }

        // -----------------------------------------------------
        // FIRST FIND EXISTING RESIDENT
        // -----------------------------------------------------

        Resident existingResident =
                residentDao.getResidentByEmail(
                        residentEmail
                );

        // -----------------------------------------------------
        // GET SECRETARY SOCIETY
        // -----------------------------------------------------

        String secretarySociety =
                getLoggedInSecretarySociety();

        if (secretarySociety == null
                || secretarySociety.trim().isEmpty()) {

            System.out.println(
                    "ResidentController: Secretary society not found."
            );

            return false;
        }

        // -----------------------------------------------------
        // IF EXISTING RESIDENT EXISTS
        // -----------------------------------------------------

        if (existingResident != null) {

            // Preserve existing complete registration data.

            existingResident.setName(
                    clean(name)
            );

            existingResident.setEmail(
                    residentEmail
            );

            existingResident.setPhone(
                    clean(mobile)
            );

            existingResident.setFlatNo(
                    clean(flat)
            );

            existingResident.setStatus(
                    clean(status)
            );

            // IMPORTANT:
            // Society is always taken from logged-in Secretary.

            existingResident.setSociety(
                    secretarySociety.trim()
            );

            if (existingResident.getRole() == null
                    || existingResident.getRole().trim().isEmpty()) {

                existingResident.setRole(
                        "Resident"
                );
            }

            System.out.println(
                    "Updating existing resident: "
                            + residentEmail
            );

            return residentDao.addResident(
                    existingResident
            );
        }

        // -----------------------------------------------------
        // IF NO EXISTING RESIDENT
        // -----------------------------------------------------
        //
        // The Secretary UI only has 5 fields.
        // Therefore the other fields cannot be invented.
        //
        // We create a record with the available details.
        // -----------------------------------------------------

        Resident resident =
                new Resident();

        resident.setName(
                clean(name)
        );

        resident.setEmail(
                residentEmail
        );

        resident.setPhone(
                clean(mobile)
        );

        resident.setFlatNo(
                clean(flat)
        );

        resident.setStatus(
                clean(status)
        );

        resident.setSociety(
                secretarySociety.trim()
        );

        resident.setRole(
                "Resident"
        );

        System.out.println(
                "No existing resident found."
        );

        System.out.println(
                "Creating new resident with available details: "
                        + residentEmail
        );

        return residentDao.addResident(
                resident
        );
    }

    // =========================================================
    // FULL ADD / UPDATE RESIDENT
    //
    // Used by ManageResidents when complete existing
    // registration details are available.
    // =========================================================

    public boolean addResident(
            String name,
            String email,
            String phone,
            String flatNo,
            String status,
            String aadhar,
            String address,
            String dob,
            String gender,
            String joiningDate,
            String memberSince,
            String ownerName,
            String society,
            String role
    ) {

        String residentEmail =
                cleanEmail(email);

        if (residentEmail.isEmpty()) {

            System.out.println(
                    "ResidentController: Email is empty."
            );

            return false;
        }

        // -----------------------------------------------------
        // GET SECRETARY SOCIETY
        // -----------------------------------------------------

        String secretarySociety =
                getLoggedInSecretarySociety();

        if (secretarySociety == null
                || secretarySociety.trim().isEmpty()) {

            System.out.println(
                    "ResidentController: Secretary society missing."
            );

            return false;
        }

        // -----------------------------------------------------
        // GET EXISTING RESIDENT
        // -----------------------------------------------------

        Resident existingResident =
                residentDao.getResidentByEmail(
                        residentEmail
                );

        Resident resident;

        if (existingResident != null) {

            resident =
                    existingResident;

        } else {

            resident =
                    new Resident();
        }

        // -----------------------------------------------------
        // UPDATE VALUES
        // -----------------------------------------------------

        resident.setName(
                clean(name)
        );

        resident.setEmail(
                residentEmail
        );

        resident.setPhone(
                clean(phone)
        );

        resident.setFlatNo(
                clean(flatNo)
        );

        resident.setStatus(
                clean(status)
        );

        // -----------------------------------------------------
        // PRESERVE COMPLETE DETAILS
        //
        // Only replace them if a non-empty value is supplied.
        // -----------------------------------------------------

        if (!clean(aadhar).isEmpty()) {

            resident.setAadhar(
                    clean(aadhar)
            );
        }

        if (!clean(address).isEmpty()) {

            resident.setAddress(
                    clean(address)
            );
        }

        if (!clean(dob).isEmpty()) {

            resident.setDob(
                    clean(dob)
            );
        }

        if (!clean(gender).isEmpty()) {

            resident.setGender(
                    clean(gender)
            );
        }

        if (!clean(joiningDate).isEmpty()) {

            resident.setJoiningDate(
                    clean(joiningDate)
            );
        }

        if (!clean(memberSince).isEmpty()) {

            resident.setMemberSince(
                    clean(memberSince)
            );
        }

        if (!clean(ownerName).isEmpty()) {

            resident.setOwnerName(
                    clean(ownerName)
            );
        }

        // -----------------------------------------------------
        // SOCIETY
        //
        // NEVER trust society coming from UI.
        // Always use logged-in Secretary society.
        // -----------------------------------------------------

        resident.setSociety(
                secretarySociety.trim()
        );

        // -----------------------------------------------------
        // ROLE
        // -----------------------------------------------------

        if (!clean(role).isEmpty()) {

            resident.setRole(
                    clean(role)
            );

        } else {

            resident.setRole(
                    "Resident"
            );
        }

        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        return residentDao.addResident(
                resident
        );
    }

    // =========================================================
    // GET ALL RESIDENTS
    // =========================================================

    public List<Resident> getAllResidents() {

        return residentDao.getAllResidents();
    }

    // =========================================================
    // GET RESIDENT BY EMAIL
    // =========================================================

    public Resident getResidentByEmail(
            String email
    ) {

        String cleanedEmail =
                cleanEmail(email);

        if (cleanedEmail.isEmpty()) {

            return null;
        }

        return residentDao.getResidentByEmail(
                cleanedEmail
        );
    }

    // =========================================================
    // GET RESIDENTS BY CURRENT SECRETARY SOCIETY
    // =========================================================

    public List<Resident> getResidentsBySociety() {

        String society =
                getLoggedInSecretarySociety();

        if (society == null
                || society.trim().isEmpty()) {

            System.out.println(
                    "ResidentController: Cannot fetch residents."
                            + " Secretary society is empty."
            );

            return List.of();
        }

        return residentDao.getResidentsBySociety(
                society.trim()
        );
    }

    // =========================================================
    // GET RESIDENTS BY SOCIETY
    // =========================================================

    public List<Resident> getResidentsBySociety(
            String society
    ) {

        if (society == null
                || society.trim().isEmpty()) {

            return List.of();
        }

        return residentDao.getResidentsBySociety(
                society.trim()
        );
    }

    // =========================================================
    // GET LOGGED-IN SECRETARY SOCIETY
    // =========================================================

    public String getLoggedInSecretarySociety() {

        try {

            String secretaryEmail =
                    UserDao.getLoggedInEmail();

            if (secretaryEmail == null
                    || secretaryEmail.trim().isEmpty()) {

                System.out.println(
                        "Secretary email not found."
                );

                return "";
            }

            secretaryEmail =
                    secretaryEmail
                            .trim()
                            .toLowerCase();

            System.out.println(
                    "Logged-in Secretary Email = "
                            + secretaryEmail
            );

            DocumentSnapshot document =
                    firestore
                            .collection(SECRETARIES)
                            .document(secretaryEmail)
                            .get()
                            .get();

            if (!document.exists()) {

                System.out.println(
                        "Secretary document not found: "
                                + secretaryEmail
                );

                return "";
            }

            Object societyValue =
                    document.get("society");

            if (societyValue == null) {

                System.out.println(
                        "Secretary society field is null."
                );

                return "";
            }

            String society =
                    String.valueOf(
                            societyValue
                    ).trim();

            System.out.println(
                    "Secretary Society = ["
                            + society
                            + "]"
            );

            return society;

        } catch (Exception e) {

            System.out.println(
                    "Error getting Secretary society."
            );

            e.printStackTrace();

            return "";
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
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value
                .trim()
                .toLowerCase();
    }
}