package com.society.controller.Guard_Controller;

import com.society.dao.Guard_dao.GuardDao;
import com.society.model.Guard_model.GuardModel;

public class GuardController {

    private final GuardDao guardDao;


    public GuardController() {

        guardDao =
                new GuardDao();
    }


    // =========================================================
    // GET GUARD BY EMAIL
    // =========================================================

    public GuardModel getGuard(
            String email) {

        return guardDao.getGuard(
                email
        );
    }


    // =========================================================
    // UPDATE GUARD
    // =========================================================

    public boolean updateGuard(
            String email,
            String name,
            String phone,
            String guardId,
            String gate,
            String shift,
            String joiningDate,
            String status) {

        return guardDao.updateGuard(
                email,
                name,
                phone,
                guardId,
                gate,
                shift,
                joiningDate,
                status
        );
    }


    // =========================================================
    // UPDATE PROFILE IMAGE
    // =========================================================

    public boolean updateProfileImage(
            String email,
            String profileImageUrl) {

        return guardDao.updateProfileImage(
                email,
                profileImageUrl
        );
    }
}