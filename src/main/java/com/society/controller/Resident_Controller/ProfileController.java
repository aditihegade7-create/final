package com.society.controller.Resident_Controller;

import com.society.dao.Resident_dao.ProfileDao;
import com.society.model.Resident_model.ProfileModel;

public class ProfileController {
    // =========================================================
    // DAO
    // =========================================================

    private final ProfileDao profileDao;


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ProfileController() {

        profileDao =
                new ProfileDao();
    }


    // =========================================================
    // GET PROFILE
    // =========================================================

    public ProfileModel getProfile(
            String email) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return null;
            }


            return profileDao.getProfile(
                    email.trim().toLowerCase()
            );


        } catch (Exception e) {

            System.out.println(
                    "ProfileController Get Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // UPDATE PROFILE
    // =========================================================

    public boolean updateProfile(
            String email,
            String name,
            String phone) {

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


            return profileDao.updateProfile(
                    email.trim().toLowerCase(),
                    name.trim(),
                    phone.trim()
            );


        } catch (Exception e) {

            System.out.println(
                    "ProfileController Update Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // UPDATE PROFILE IMAGE
    // =========================================================

    public boolean updateProfileImage(
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


            return profileDao.updateProfileImage(
                    email.trim().toLowerCase(),
                    profileImageUrl.trim()
            );


        } catch (Exception e) {

            System.out.println(
                    "ProfileController Image Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================================================
    // CHANGE PASSWORD
    // =========================================================

    public boolean changePassword(
            String email,
            String currentPassword,
            String newPassword) {

        try {

            if (email == null ||
                    email.trim().isEmpty()) {

                return false;
            }


            if (currentPassword == null ||
                    currentPassword.isEmpty()) {

                return false;
            }


            if (newPassword == null ||
                    newPassword.isEmpty()) {

                return false;
            }


            return profileDao.changePassword(
                    email.trim().toLowerCase(),
                    currentPassword,
                    newPassword
            );


        } catch (Exception e) {

            System.out.println(
                    "ProfileController Password Error: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }

}