package com.society.controller.Secretary_Controller;

import com.society.dao.Secretary_dao.SecretaryDao;
import com.society.model.Secretary_model.SecretaryModel;

public class SecretaryController {

    private final SecretaryDao secretaryDao;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public SecretaryController() {

        secretaryDao = new SecretaryDao();
    }


    // =====================================================
    // GET SECRETARY BY EMAIL
    // =====================================================

    public SecretaryModel getSecretary(String email) {

        if (email == null || email.trim().isEmpty()) {

            System.out.println(
                    "SecretaryController: Email is empty."
            );

            return null;
        }

        return secretaryDao.getSecretary(
                email.trim()
        );
    }


    // =====================================================
    // SAVE SECRETARY
    // =====================================================

    public boolean saveSecretary(
            SecretaryModel secretary) {

        if (secretary == null) {

            System.out.println(
                    "SecretaryController: Secretary is null."
            );

            return false;
        }

        return secretaryDao.saveSecretary(
                secretary
        );
    }


    // =====================================================
    // UPDATE SECRETARY
    // =====================================================

    public boolean updateSecretary(
            String email,
            String name,
            String phone,
            String dateOfBirth,
            String gender,
            String aadhaarNumber,
            String societyName) {

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "SecretaryController: Email is empty."
            );

            return false;
        }

        return secretaryDao.updateSecretary(
                email.trim(),
                name,
                phone,
                dateOfBirth,
                gender,
                aadhaarNumber,
                societyName
        );
    }


    // =====================================================
    // UPDATE PROFILE IMAGE
    // =====================================================

    public boolean updateProfileImage(
            String email,
            String profileImageUrl) {

        if (email == null ||
                email.trim().isEmpty()) {

            System.out.println(
                    "SecretaryController: Email is empty."
            );

            return false;
        }

        if (profileImageUrl == null ||
                profileImageUrl.trim().isEmpty()) {

            System.out.println(
                    "SecretaryController: Image URL is empty."
            );

            return false;
        }

        return secretaryDao.updateProfileImage(
                email.trim(),
                profileImageUrl.trim()
        );
    }
}