package com.society.controller.Resident_Controller;

import com.society.dao.Resident_dao.NoticeDao;
import com.society.model.Resident_model.NoticeModel;

import java.util.ArrayList;
import java.util.List;

public class NoticeController {

    private final NoticeDao noticeDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public NoticeController() {
        noticeDao = new NoticeDao();
    }

    // =========================================================
    // GET NOTICES FOR RESIDENT
    // =========================================================

    public List<NoticeModel> getNoticesForResident(
            String residentEmail) {

        try {

            // -------------------------------------------------
            // VALIDATE RESIDENT EMAIL
            // -------------------------------------------------

            if (residentEmail == null
                    || residentEmail.trim().isEmpty()) {

                System.out.println(
                        "NoticeController: Resident email is empty."
                );

                return new ArrayList<>();
            }

            String email =
                    residentEmail.trim();

            // -------------------------------------------------
            // LOG
            // -------------------------------------------------

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "NoticeController: Fetching notices"
            );

            System.out.println(
                    "Resident Email: "
                            + email
            );

            System.out.println(
                    "========================================"
            );

            // -------------------------------------------------
            // FETCH NOTICES FROM DAO
            // -------------------------------------------------

            List<NoticeModel> notices =
                    noticeDao.getNoticesForResident(
                            email
                    );

            // -------------------------------------------------
            // SAFETY CHECK
            // -------------------------------------------------

            if (notices == null) {
                notices =
                        new ArrayList<>();
            }

            // -------------------------------------------------
            // LOG RESULT
            // -------------------------------------------------

            System.out.println(
                    "NoticeController: Notices fetched = "
                            + notices.size()
            );

            return notices;

        } catch (Exception e) {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "NoticeController: ERROR"
            );

            System.out.println(
                    "========================================"
            );

            e.printStackTrace();

            return new ArrayList<>();
        }
    }
}