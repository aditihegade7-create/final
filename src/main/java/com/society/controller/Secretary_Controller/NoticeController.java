package com.society.controller.Secretary_Controller;

import java.util.ArrayList;
import java.util.List;

import com.society.dao.Secretary_dao.NoticeDao;
import com.society.model.Secretary_model.Notice;

public class NoticeController {

    // =====================================================
    // DAO
    // =====================================================

    private final NoticeDao noticeDao;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public NoticeController() {

        noticeDao = new NoticeDao();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "NoticeController initialized successfully."
        );

        System.out.println(
                "======================================"
        );
    }

    // =====================================================
    // ADD NOTICE
    // =====================================================
    /*
     * Society is NOT passed from UI.
     *
     * NoticeDao automatically gets society from:
     *
     * Secretaries/{senderEmail}
     *
     * Then notice is stored as:
     *
     * Notices
     *   └── senderEmail
     *        └── notices
     *             └── noticeId
     *
     * Example:
     *
     * Notices
     *   └── aditi@gmail.com
     *        └── notices
     *             └── abc123
     *                  ├── noticeId
     *                  ├── title
     *                  ├── description
     *                  ├── date
     *                  ├── status
     *                  ├── senderEmail
     *                  └── society
     */

    public boolean addNotice(
            String title,
            String description,
            String date,
            String status,
            String senderEmail) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (title == null ||
                    title.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice title is empty."
                );

                return false;
            }

            if (description == null ||
                    description.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice description is empty."
                );

                return false;
            }

            if (date == null ||
                    date.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice date is empty."
                );

                return false;
            }

            if (status == null ||
                    status.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice status is empty."
                );

                return false;
            }

            if (senderEmail == null ||
                    senderEmail.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Sender email is empty."
                );

                return false;
            }

            // =================================================
            // NORMALIZE
            // =================================================

            title = title.trim();

            description = description.trim();

            date = date.trim();

            status = status.trim();

            senderEmail =
                    senderEmail
                            .trim()
                            .toLowerCase();

            // =================================================
            // CREATE NOTICE
            // =================================================

            Notice notice = new Notice();

            notice.setTitle(title);

            notice.setDescription(description);

            notice.setDate(date);

            notice.setStatus(status);

            notice.setSenderEmail(senderEmail);

            /*
             * IMPORTANT:
             *
             * Society is NOT set here.
             *
             * NoticeDao will automatically fetch society
             * from:
             *
             * Secretaries/{senderEmail}
             */

            // =================================================
            // DEBUG
            // =================================================

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "NoticeController.addNotice()"
            );

            System.out.println(
                    "Title       : " + title
            );

            System.out.println(
                    "Description : " + description
            );

            System.out.println(
                    "Date        : " + date
            );

            System.out.println(
                    "Status      : " + status
            );

            System.out.println(
                    "Sender Email: " + senderEmail
            );

            System.out.println(
                    "Society     : Will be fetched from Secretary"
            );

            System.out.println(
                    "======================================"
            );

            // =================================================
            // DAO
            // =================================================

            boolean result =
                    noticeDao.addNotice(notice);

            if (result) {

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "NOTICE ADDED SUCCESSFULLY"
                );

                System.out.println(
                        "======================================"
                );

            } else {

                System.out.println(
                        "======================================"
                );

                System.out.println(
                        "FAILED TO ADD NOTICE"
                );

                System.out.println(
                        "======================================"
                );
            }

            return result;

        } catch (Exception e) {

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "ERROR in NoticeController.addNotice()"
            );

            System.out.println(
                    e.getMessage()
            );

            System.out.println(
                    "======================================"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET NOTICES BY SOCIETY
    // =====================================================
    /*
     * Used mainly by Resident.
     *
     * Example:
     *
     * Resident society = "stanza"
     *
     * Only notices having:
     *
     * society = "stanza"
     *
     * will be returned.
     */

    public List<Notice> getNoticesBySociety(
            String society) {

        if (society == null ||
                society.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Society is empty."
            );

            return new ArrayList<>();
        }

        society = society.trim();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "NoticeController.getNoticesBySociety()"
        );

        System.out.println(
                "Society: " + society
        );

        System.out.println(
                "======================================"
        );

        return noticeDao.getNoticesBySociety(
                society
        );
    }

    // =====================================================
    // GET NOTICES BY SECRETARY EMAIL
    // =====================================================
    /*
     * Used by Secretary Manage Notices page.
     *
     * Firestore:
     *
     * Notices/{senderEmail}/notices
     */

    public List<Notice> getNoticesBySenderEmail(
            String senderEmail) {

        if (senderEmail == null ||
                senderEmail.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Sender email is empty."
            );

            return new ArrayList<>();
        }

        senderEmail =
                senderEmail
                        .trim()
                        .toLowerCase();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "NoticeController.getNoticesBySenderEmail()"
        );

        System.out.println(
                "Sender Email: " + senderEmail
        );

        System.out.println(
                "======================================"
        );

        return noticeDao.getNoticesBySenderEmail(
                senderEmail
        );
    }

    // =====================================================
    // GET ONE NOTICE BY ID
    // =====================================================
    /*
     * IMPORTANT:
     *
     * New Firestore structure:
     *
     * Notices/{senderEmail}/notices/{noticeId}
     *
     * Therefore BOTH senderEmail and noticeId
     * are required.
     */

    public Notice getNoticeById(
            String senderEmail,
            String noticeId) {

        if (senderEmail == null ||
                senderEmail.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Sender email is empty."
            );

            return null;
        }

        if (noticeId == null ||
                noticeId.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Notice ID is empty."
            );

            return null;
        }

        senderEmail =
                senderEmail
                        .trim()
                        .toLowerCase();

        noticeId = noticeId.trim();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "NoticeController.getNoticeById()"
        );

        System.out.println(
                "Sender Email: " + senderEmail
        );

        System.out.println(
                "Notice ID   : " + noticeId
        );

        System.out.println(
                "======================================"
        );

        return noticeDao.getNoticeById(
                senderEmail,
                noticeId
        );
    }

    // =====================================================
    // UPDATE NOTICE
    // =====================================================
    /*
     * Firestore:
     *
     * Notices/{senderEmail}/notices/{noticeId}
     *
     * Society is NOT changed.
     *
     * Existing society remains in the document.
     */

    public boolean updateNotice(
            String senderEmail,
            String noticeId,
            String title,
            String description,
            String date,
            String status) {

        try {

            // =================================================
            // VALIDATION
            // =================================================

            if (senderEmail == null ||
                    senderEmail.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Sender email is empty."
                );

                return false;
            }

            if (noticeId == null ||
                    noticeId.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice ID is empty."
                );

                return false;
            }

            if (title == null ||
                    title.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Title is empty."
                );

                return false;
            }

            if (description == null ||
                    description.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Description is empty."
                );

                return false;
            }

            if (date == null ||
                    date.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Date is empty."
                );

                return false;
            }

            if (status == null ||
                    status.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Status is empty."
                );

                return false;
            }

            // =================================================
            // NORMALIZE
            // =================================================

            senderEmail =
                    senderEmail
                            .trim()
                            .toLowerCase();

            noticeId = noticeId.trim();

            title = title.trim();

            description = description.trim();

            date = date.trim();

            status = status.trim();

            // =================================================
            // CREATE NOTICE MODEL
            // =================================================

            Notice notice = new Notice();

            notice.setNoticeId(noticeId);

            notice.setTitle(title);

            notice.setDescription(description);

            notice.setDate(date);

            notice.setStatus(status);

            notice.setSenderEmail(senderEmail);

            /*
             * IMPORTANT:
             *
             * Society is intentionally NOT set.
             *
             * DAO will preserve existing society.
             */

            // =================================================
            // DEBUG
            // =================================================

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "NoticeController.updateNotice()"
            );

            System.out.println(
                    "Sender Email: " + senderEmail
            );

            System.out.println(
                    "Notice ID   : " + noticeId
            );

            System.out.println(
                    "Title       : " + title
            );

            System.out.println(
                    "Description : " + description
            );

            System.out.println(
                    "Date        : " + date
            );

            System.out.println(
                    "Status      : " + status
            );

            System.out.println(
                    "======================================"
            );

            // =================================================
            // DAO UPDATE
            // =================================================

            return noticeDao.updateNotice(
                    senderEmail,
                    noticeId,
                    notice
            );

        } catch (Exception e) {

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "ERROR in NoticeController.updateNotice()"
            );

            System.out.println(
                    e.getMessage()
            );

            System.out.println(
                    "======================================"
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE NOTICE
    // =====================================================
    /*
     * Firestore:
     *
     * Notices/{senderEmail}/notices/{noticeId}
     *
     * Therefore senderEmail is required.
     */

    public boolean deleteNotice(
            String senderEmail,
            String noticeId) {

        if (senderEmail == null ||
                senderEmail.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Sender email is empty."
            );

            return false;
        }

        if (noticeId == null ||
                noticeId.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Notice ID is empty."
            );

            return false;
        }

        senderEmail =
                senderEmail
                        .trim()
                        .toLowerCase();

        noticeId = noticeId.trim();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "NoticeController.deleteNotice()"
        );

        System.out.println(
                "Sender Email: " + senderEmail
        );

        System.out.println(
                "Notice ID   : " + noticeId
        );

        System.out.println(
                "======================================"
        );

        return noticeDao.deleteNotice(
                senderEmail,
                noticeId
        );
    }

    // =====================================================
    // GET ALL NOTICES
    // =====================================================
    /*
     * Gets notices from all Secretary documents.
     *
     * Mainly useful for admin/debugging.
     */

    public List<Notice> getAllNotices() {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "NoticeController.getAllNotices()"
        );

        System.out.println(
                "======================================"
        );

        return noticeDao.getAllNotices();
    }
}