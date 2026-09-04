package com.society.dao.Secretary_dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Secretary_model.Notice;

public class NoticeDao {

    // =====================================================
    // FIRESTORE
    // =====================================================

    private final Firestore firestore;

    // =====================================================
    // COLLECTIONS
    // =====================================================

    private static final String COLLECTION_NAME = "Notices";

    private static final String SECRETARY_COLLECTION =
            "Secretaries";

    private static final String SUB_COLLECTION_NAME =
            "notices";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public NoticeDao() {

        firestore = FirebaseConfig.getFirestore();

        System.out.println(
                "======================================"
        );

        System.out.println(
                "NoticeDao initialized successfully."
        );

        System.out.println(
                "Firestore connected for Notices."
        );

        System.out.println(
                "======================================"
        );
    }

    // =====================================================
    // NORMALIZE EMAIL
    // =====================================================

    private String normalizeEmail(String email) {

        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase();
    }

    // =====================================================
    // GET SECRETARY SOCIETY
    // =====================================================

    private String getSecretarySociety(String email) {

        try {

            email = normalizeEmail(email);

            if (email == null || email.isEmpty()) {

                System.out.println(
                        "ERROR: Secretary email is empty."
                );

                return null;
            }

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "Fetching Secretary information..."
            );

            System.out.println(
                    "Secretary Email: " + email
            );

            System.out.println(
                    "======================================"
            );

            DocumentSnapshot document =
                    firestore
                            .collection(SECRETARY_COLLECTION)
                            .document(email)
                            .get()
                            .get();

            // -------------------------------------------------
            // SECRETARY NOT FOUND
            // -------------------------------------------------

            if (!document.exists()) {

                System.out.println(
                        "ERROR: Secretary not found."
                );

                System.out.println(
                        "Email: " + email
                );

                return null;
            }

            // -------------------------------------------------
            // GET SOCIETY
            // -------------------------------------------------

            String society =
                    document.getString("society");

            if (society == null ||
                    society.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Secretary society is empty."
                );

                return null;
            }

            society = society.trim();

            System.out.println(
                    "Secretary Society: " + society
            );

            return society;

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching Secretary society."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // GET NOTICE COLLECTION FOR SECRETARY
    //
    // PATH:
    //
    // Notices/{secretaryEmail}/notices
    //
    // =====================================================

    private CollectionReference getNoticeCollection(
            String senderEmail) {

        String email =
                normalizeEmail(senderEmail);

        return firestore
                .collection(COLLECTION_NAME)
                .document(email)
                .collection(SUB_COLLECTION_NAME);
    }

    // =====================================================
    // ADD NOTICE
    // =====================================================

    public boolean addNotice(Notice notice) {

        try {

            // =================================================
            // NULL CHECK
            // =================================================

            if (notice == null) {

                System.out.println(
                        "ERROR: Notice object is null."
                );

                return false;
            }

            // =================================================
            // EMAIL
            // =================================================

            String email =
                    normalizeEmail(
                            notice.getSenderEmail()
                    );

            if (email == null ||
                    email.isEmpty()) {

                System.out.println(
                        "ERROR: Sender email is empty."
                );

                return false;
            }

            notice.setSenderEmail(email);

            // =================================================
            // TITLE
            // =================================================

            if (notice.getTitle() == null ||
                    notice.getTitle().trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice title is empty."
                );

                return false;
            }

            notice.setTitle(
                    notice.getTitle().trim()
            );

            // =================================================
            // DESCRIPTION
            // =================================================

            if (notice.getDescription() == null ||
                    notice.getDescription().trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice description is empty."
                );

                return false;
            }

            notice.setDescription(
                    notice.getDescription().trim()
            );

            // =================================================
            // DATE
            // =================================================

            if (notice.getDate() == null ||
                    notice.getDate().trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice date is empty."
                );

                return false;
            }

            notice.setDate(
                    notice.getDate().trim()
            );

            // =================================================
            // STATUS
            // =================================================

            if (notice.getStatus() == null ||
                    notice.getStatus().trim().isEmpty()) {

                System.out.println(
                        "ERROR: Notice status is empty."
                );

                return false;
            }

            notice.setStatus(
                    notice.getStatus().trim()
            );

            // =================================================
            // GET SECRETARY SOCIETY
            // =================================================

            String society =
                    getSecretarySociety(email);

            if (society == null ||
                    society.isEmpty()) {

                System.out.println(
                        "ERROR: Society could not be found."
                );

                return false;
            }

            // =================================================
            // SET SOCIETY
            // =================================================

            notice.setSociety(society);

            // =================================================
            // GENERATE NOTICE ID
            //
            // IMPORTANT:
            //
            // This ID is ONLY for the individual notice
            // inside the email's "notices" subcollection.
            //
            // Parent document = EMAIL
            //
            // =================================================

            String noticeId =
                    UUID.randomUUID().toString();

            notice.setNoticeId(noticeId);

            // =================================================
            // FIRESTORE PATH
            //
            // Notices
            //    └── aditi@gmail.com
            //          └── notices
            //                └── UUID
            //
            // =================================================

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "SAVING NOTICE"
            );

            System.out.println(
                    "Collection      : " +
                            COLLECTION_NAME
            );

            System.out.println(
                    "Secretary Email : " +
                            email
            );

            System.out.println(
                    "Society         : " +
                            society
            );

            System.out.println(
                    "Notice ID       : " +
                            noticeId
            );

            System.out.println(
                    "======================================"
            );

            // =================================================
            // SAVE
            // =================================================

            getNoticeCollection(email)
                    .document(noticeId)
                    .set(notice)
                    .get();

            // =================================================
            // SUCCESS
            // =================================================

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "NOTICE SAVED SUCCESSFULLY"
            );

            System.out.println(
                    "Path: Notices/" +
                            email +
                            "/notices/" +
                            noticeId
            );

            System.out.println(
                    "Secretary Email : " +
                            email
            );

            System.out.println(
                    "Society         : " +
                            society
            );

            System.out.println(
                    "======================================"
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "ERROR SAVING NOTICE"
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
    //
    // Used by RESIDENT portal.
    //
    // It searches all Secretary notice subcollections
    // and returns only notices belonging to given society.
    //
    // =====================================================

    public List<Notice> getNoticesBySociety(
            String society) {

        List<Notice> notices =
                new ArrayList<>();

        try {

            if (society == null ||
                    society.trim().isEmpty()) {

                System.out.println(
                        "ERROR: Society is empty."
                );

                return notices;
            }

            society = society.trim();

            // =================================================
            // GET ALL SECRETARY DOCUMENTS
            // =================================================

            QuerySnapshot secretarySnapshot =
                    firestore
                            .collection(COLLECTION_NAME)
                            .get()
                            .get();

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "FETCHING NOTICES BY SOCIETY"
            );

            System.out.println(
                    "Requested Society: " +
                            society
            );

            System.out.println(
                    "Secretary Documents: " +
                            secretarySnapshot.size()
            );

            System.out.println(
                    "======================================"
            );

            // =================================================
            // LOOP SECRETARY DOCUMENTS
            // =================================================

            for (
                    DocumentSnapshot secretaryDocument :
                    secretarySnapshot.getDocuments()
            ) {

                String secretaryEmail =
                        secretaryDocument.getId();

                // =================================================
                // GET NOTICE SUBCOLLECTION
                // =================================================

                QuerySnapshot noticeSnapshot =
                        getNoticeCollection(
                                secretaryEmail
                        )
                        .get()
                        .get();

                // =================================================
                // LOOP NOTICES
                // =================================================

                for (
                        DocumentSnapshot document :
                        noticeSnapshot.getDocuments()
                ) {

                    Notice notice =
                            document.toObject(
                                    Notice.class
                            );

                    if (notice == null) {
                        continue;
                    }

                    // =================================================
                    // SET NOTICE ID IF MISSING
                    // =================================================

                    if (notice.getNoticeId() == null ||
                            notice.getNoticeId()
                                    .trim()
                                    .isEmpty()) {

                        notice.setNoticeId(
                                document.getId()
                        );
                    }

                    // =================================================
                    // CHECK SOCIETY
                    // =================================================

                    String noticeSociety =
                            notice.getSociety();

                    if (noticeSociety == null) {
                        continue;
                    }

                    if (noticeSociety.trim()
                            .equalsIgnoreCase(
                                    society
                            )) {

                        notices.add(notice);
                    }
                }
            }

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "NOTICES FETCHED BY SOCIETY"
            );

            System.out.println(
                    "Society: " + society
            );

            System.out.println(
                    "Total Notices: " +
                            notices.size()
            );

            System.out.println(
                    "======================================"
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching notices by society."
            );

            e.printStackTrace();
        }

        return notices;
    }

    // =====================================================
    // GET NOTICES BY SENDER EMAIL
    //
    // Used by Secretary Manage Notices.
    //
    // =====================================================

    public List<Notice> getNoticesBySenderEmail(
            String senderEmail) {

        List<Notice> notices =
                new ArrayList<>();

        try {

            String email =
                    normalizeEmail(senderEmail);

            if (email == null ||
                    email.isEmpty()) {

                System.out.println(
                        "ERROR: Email is empty."
                );

                return notices;
            }

            // =================================================
            // FETCH
            // =================================================

            QuerySnapshot snapshot =
                    getNoticeCollection(email)
                            .get()
                            .get();

            // =================================================
            // LOOP
            // =================================================

            for (
                    DocumentSnapshot document :
                    snapshot.getDocuments()
            ) {

                Notice notice =
                        document.toObject(
                                Notice.class
                        );

                if (notice == null) {
                    continue;
                }

                // =================================================
                // SET NOTICE ID
                // =================================================

                if (notice.getNoticeId() == null ||
                        notice.getNoticeId()
                                .trim()
                                .isEmpty()) {

                    notice.setNoticeId(
                            document.getId()
                    );
                }

                notices.add(notice);
            }

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "NOTICES FETCHED BY SENDER"
            );

            System.out.println(
                    "Email: " + email
            );

            System.out.println(
                    "Total Notices: " +
                            notices.size()
            );

            System.out.println(
                    "======================================"
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching notices by sender."
            );

            e.printStackTrace();
        }

        return notices;
    }

    // =====================================================
    // GET ONE NOTICE BY ID
    //
    // Requires sender email because notice is inside
    // that Secretary's email document.
    //
    // PATH:
    //
    // Notices/{email}/notices/{noticeId}
    //
    // =====================================================

    public Notice getNoticeById(
            String senderEmail,
            String noticeId) {

        try {

            String email =
                    normalizeEmail(senderEmail);

            if (email == null ||
                    email.isEmpty()) {

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

            noticeId = noticeId.trim();

            // =================================================
            // FETCH
            // =================================================

            DocumentSnapshot document =
                    getNoticeCollection(email)
                            .document(noticeId)
                            .get()
                            .get();

            // =================================================
            // NOT FOUND
            // =================================================

            if (!document.exists()) {

                System.out.println(
                        "Notice not found."
                );

                return null;
            }

            // =================================================
            // CONVERT
            // =================================================

            Notice notice =
                    document.toObject(
                            Notice.class
                    );

            if (notice != null) {

                if (notice.getNoticeId() == null ||
                        notice.getNoticeId()
                                .trim()
                                .isEmpty()) {

                    notice.setNoticeId(
                            document.getId()
                    );
                }
            }

            return notice;

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching notice by ID."
            );

            e.printStackTrace();

            return null;
        }
    }

    // =====================================================
    // UPDATE NOTICE
    //
    // Email is REQUIRED because notice is inside:
    //
    // Notices/{email}/notices/{noticeId}
    //
    // Society is NEVER changed from UI.
    //
    // =====================================================

    public boolean updateNotice(
            String senderEmail,
            String noticeId,
            Notice notice) {

        try {

            String email =
                    normalizeEmail(senderEmail);

            if (email == null ||
                    email.isEmpty()) {

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

            if (notice == null) {

                System.out.println(
                        "ERROR: Notice object is null."
                );

                return false;
            }

            noticeId = noticeId.trim();

            // =================================================
            // GET EXISTING NOTICE
            // =================================================

            DocumentSnapshot existing =
                    getNoticeCollection(email)
                            .document(noticeId)
                            .get()
                            .get();

            if (!existing.exists()) {

                System.out.println(
                        "ERROR: Notice does not exist."
                );

                return false;
            }

            // =================================================
            // KEEP ORIGINAL VALUES
            // =================================================

            notice.setNoticeId(noticeId);

            notice.setSenderEmail(email);

            // =================================================
            // KEEP EXISTING SOCIETY
            // =================================================

            String existingSociety =
                    existing.getString("society");

            if (existingSociety != null &&
                    !existingSociety.trim().isEmpty()) {

                notice.setSociety(
                        existingSociety.trim()
                );
            }

            // =================================================
            // UPDATE
            // =================================================

            getNoticeCollection(email)
                    .document(noticeId)
                    .set(notice)
                    .get();

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "NOTICE UPDATED SUCCESSFULLY"
            );

            System.out.println(
                    "Email: " + email
            );

            System.out.println(
                    "Notice ID: " + noticeId
            );

            System.out.println(
                    "Society: " +
                            notice.getSociety()
            );

            System.out.println(
                    "======================================"
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "ERROR updating notice."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // DELETE NOTICE
    //
    // Email + Notice ID required.
    //
    // PATH:
    //
    // Notices/{email}/notices/{noticeId}
    //
    // =====================================================

    public boolean deleteNotice(
            String senderEmail,
            String noticeId) {

        try {

            String email =
                    normalizeEmail(senderEmail);

            if (email == null ||
                    email.isEmpty()) {

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

            noticeId = noticeId.trim();

            // =================================================
            // DELETE
            // =================================================

            getNoticeCollection(email)
                    .document(noticeId)
                    .delete()
                    .get();

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "NOTICE DELETED SUCCESSFULLY"
            );

            System.out.println(
                    "Email: " + email
            );

            System.out.println(
                    "Notice ID: " + noticeId
            );

            System.out.println(
                    "======================================"
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "ERROR deleting notice."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // GET ALL NOTICES
    //
    // Gets notices from all Secretary email documents.
    //
    // =====================================================

    public List<Notice> getAllNotices() {

        List<Notice> notices =
                new ArrayList<>();

        try {

            QuerySnapshot secretarySnapshot =
                    firestore
                            .collection(COLLECTION_NAME)
                            .get()
                            .get();

            // =================================================
            // LOOP EMAIL DOCUMENTS
            // =================================================

            for (
                    DocumentSnapshot secretaryDocument :
                    secretarySnapshot.getDocuments()
            ) {

                String email =
                        secretaryDocument.getId();

                // =================================================
                // GET SUBCOLLECTION
                // =================================================

                QuerySnapshot noticeSnapshot =
                        getNoticeCollection(email)
                                .get()
                                .get();

                // =================================================
                // LOOP NOTICES
                // =================================================

                for (
                        DocumentSnapshot document :
                        noticeSnapshot.getDocuments()
                ) {

                    Notice notice =
                            document.toObject(
                                    Notice.class
                            );

                    if (notice == null) {
                        continue;
                    }

                    if (notice.getNoticeId() == null ||
                            notice.getNoticeId()
                                    .trim()
                                    .isEmpty()) {

                        notice.setNoticeId(
                                document.getId()
                        );
                    }

                    notices.add(notice);
                }
            }

            System.out.println(
                    "======================================"
            );

            System.out.println(
                    "ALL NOTICES FETCHED"
            );

            System.out.println(
                    "Total Notices: " +
                            notices.size()
            );

            System.out.println(
                    "======================================"
            );

        } catch (Exception e) {

            System.out.println(
                    "ERROR fetching all notices."
            );

            e.printStackTrace();
        }

        return notices;
    }
}