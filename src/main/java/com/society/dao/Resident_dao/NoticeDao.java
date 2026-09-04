package com.society.dao.Resident_dao;

import java.util.ArrayList;
import java.util.List;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import com.society.config.FirebaseConfig;
import com.society.model.Resident_model.NoticeModel;

/**
 * NoticeDao
 *
 * Firestore structure:
 *
 * Residents/{residentEmail}
 *      -> society
 *
 * Secretaries/{secretaryEmail}
 *      -> society
 *
 * Notices/{secretaryEmail}/notices/{noticeId}
 *      -> title
 *      -> date
 *      -> description
 *      -> status
 *      -> society
 *
 * Notices are fetched only for secretaries belonging
 * to the same society as the logged-in resident.
 */
public class NoticeDao {

    private final Firestore db;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public NoticeDao() {
        db = FirebaseConfig.getFirestore();
    }

    // =========================================================
    // GET RESIDENT SOCIETY
    // =========================================================

    public String getResidentSociety(
            String residentEmail) {

        try {

            if (residentEmail == null
                    || residentEmail.trim().isEmpty()) {

                System.out.println(
                        "NoticeDao: Resident email is empty."
                );

                return null;
            }

            String email =
                    residentEmail.trim();

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "NoticeDao: Fetching resident society"
            );

            System.out.println(
                    "Resident Email: "
                            + email
            );

            System.out.println(
                    "========================================"
            );

            // -------------------------------------------------
            // FIRST TRY:
            // Residents/{email}
            // -------------------------------------------------

            DocumentSnapshot residentDocument =
                    db.collection("Residents")
                            .document(email)
                            .get()
                            .get();

            // -------------------------------------------------
            // IF DOCUMENT EXISTS
            // -------------------------------------------------

            if (residentDocument.exists()) {

                String society =
                        residentDocument.getString(
                                "society"
                        );

                System.out.println(
                        "Resident document found."
                );

                System.out.println(
                        "Resident Society: "
                                + society
                );

                if (society != null
                        && !society.trim().isEmpty()) {

                    return society.trim();
                }

                System.out.println(
                        "Resident society field is empty."
                );

                return null;
            }

            // -------------------------------------------------
            // FALLBACK:
            // Search Residents collection by email field
            // -------------------------------------------------

            System.out.println(
                    "Resident document not found using document ID."
            );

            System.out.println(
                    "Searching Residents collection by email field..."
            );

            QuerySnapshot residentsSnapshot =
                    db.collection("Residents")
                            .get()
                            .get();

            for (QueryDocumentSnapshot resident
                    : residentsSnapshot.getDocuments()) {

                String storedEmail =
                        resident.getString("email");

                if (storedEmail != null
                        && storedEmail.trim()
                                .equalsIgnoreCase(email)) {

                    String society =
                            resident.getString(
                                    "society"
                            );

                    System.out.println(
                            "Resident found by email field."
                    );

                    System.out.println(
                            "Resident Society: "
                                    + society
                    );

                    if (society != null
                            && !society.trim().isEmpty()) {

                        return society.trim();
                    }

                    return null;
                }
            }

            System.out.println(
                    "Resident NOT FOUND: "
                            + email
            );

            return null;

        } catch (Exception e) {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "NoticeDao: ERROR FETCHING RESIDENT SOCIETY"
            );

            System.out.println(
                    "========================================"
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // GET NOTICES FOR RESIDENT
    // =========================================================

    public List<NoticeModel> getNoticesForResident(
            String residentEmail) {

        List<NoticeModel> notices =
                new ArrayList<>();

        try {

            if (residentEmail == null
                    || residentEmail.trim().isEmpty()) {

                System.out.println(
                        "NoticeDao: Resident email is empty."
                );

                return notices;
            }

            String email =
                    residentEmail.trim();

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "RESIDENT NOTICE FETCH STARTED"
            );

            System.out.println(
                    "Resident Email : "
                            + email
            );

            System.out.println(
                    "========================================"
            );

            // -------------------------------------------------
            // STEP 1:
            // GET RESIDENT SOCIETY
            // -------------------------------------------------

            String residentSociety =
                    getResidentSociety(email);

            if (residentSociety == null
                    || residentSociety.trim().isEmpty()) {

                System.out.println(
                        "Cannot fetch notices."
                );

                System.out.println(
                        "Resident society is missing."
                );

                return notices;
            }

            residentSociety =
                    residentSociety.trim();

            System.out.println(
                    "Resident Society: "
                            + residentSociety
            );

            // -------------------------------------------------
            // STEP 2:
            // GET ALL SECRETARIES
            // -------------------------------------------------

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "Fetching Secretaries..."
            );

            System.out.println(
                    "========================================"
            );

            QuerySnapshot secretarySnapshot =
                    db.collection("Secretaries")
                            .get()
                            .get();

            System.out.println(
                    "Secretary documents found: "
                            + secretarySnapshot.size()
            );

            // -------------------------------------------------
            // STEP 3:
            // CHECK EACH SECRETARY
            // -------------------------------------------------

            for (QueryDocumentSnapshot secretary
                    : secretarySnapshot.getDocuments()) {

                String secretaryEmail =
                        secretary.getId();

                String secretarySociety =
                        secretary.getString(
                                "society"
                        );

                System.out.println(
                        "----------------------------------------"
                );

                System.out.println(
                        "Secretary Email : "
                                + secretaryEmail
                );

                System.out.println(
                        "Secretary Society : "
                                + secretarySociety
                );

                // -------------------------------------------------
                // IGNORE SECRETARIES WITHOUT SOCIETY
                // -------------------------------------------------

                if (secretarySociety == null
                        || secretarySociety.trim().isEmpty()) {

                    System.out.println(
                            "Secretary society is empty. Skipping."
                    );

                    continue;
                }

                // -------------------------------------------------
                // ONLY SAME SOCIETY
                // -------------------------------------------------

                if (!secretarySociety.trim()
                        .equalsIgnoreCase(
                                residentSociety
                        )) {

                    System.out.println(
                            "Different society. Skipping secretary."
                    );

                    continue;
                }

                System.out.println(
                        "MATCHED SOCIETY."
                );

                System.out.println(
                        "Fetching notices for: "
                                + secretaryEmail
                );

                // -------------------------------------------------
                // STEP 4:
                // FETCH:
                //
                // Notices/{secretaryEmail}/notices
                // -------------------------------------------------

                QuerySnapshot noticeSnapshot =
                        db.collection("Notices")
                                .document(secretaryEmail)
                                .collection("notices")
                                .get()
                                .get();

                System.out.println(
                        "Notice documents found for "
                                + secretaryEmail
                                + " : "
                                + noticeSnapshot.size()
                );

                // -------------------------------------------------
                // STEP 5:
                // READ EACH NOTICE
                // -------------------------------------------------

                for (QueryDocumentSnapshot notice
                        : noticeSnapshot.getDocuments()) {

                    try {

                        String noticeId =
                                notice.getId();

                        String title =
                                notice.getString(
                                        "title"
                                );

                        String date =
                                notice.getString(
                                        "date"
                                );

                        String description =
                                notice.getString(
                                        "description"
                                );

                        String status =
                                notice.getString(
                                        "status"
                                );

                        String noticeSociety =
                                notice.getString(
                                        "society"
                                );

                        // -------------------------------------------------
                        // ADDITIONAL SOCIETY SAFETY CHECK
                        // -------------------------------------------------

                        if (noticeSociety != null
                                && !noticeSociety.trim().isEmpty()) {

                            if (!noticeSociety.trim()
                                    .equalsIgnoreCase(
                                            residentSociety
                                    )) {

                                System.out.println(
                                        "Notice skipped because "
                                                + "society does not match."
                                );

                                continue;
                            }
                        }

                        // -------------------------------------------------
                        // DEFAULT VALUES
                        // -------------------------------------------------

                        if (title == null) {
                            title = "";
                        }

                        if (date == null) {
                            date = "";
                        }

                        if (description == null) {
                            description = "";
                        }

                        if (status == null) {
                            status = "";
                        }

                        // -------------------------------------------------
                        // DEBUG
                        // -------------------------------------------------

                        System.out.println(
                                "----------------------------------------"
                        );

                        System.out.println(
                                "Notice ID : "
                                        + noticeId
                        );

                        System.out.println(
                                "Title : "
                                        + title
                        );

                        System.out.println(
                                "Date : "
                                        + date
                        );

                        System.out.println(
                                "Status : "
                                        + status
                        );

                        System.out.println(
                                "Society : "
                                        + residentSociety
                        );

                        // -------------------------------------------------
                        // CREATE NOTICE MODEL
                        // -------------------------------------------------

                        NoticeModel noticeModel =
                                new NoticeModel(
                                        title,
                                        date,
                                        description,
                                        status
                                );

                        notices.add(
                                noticeModel
                        );

                    } catch (Exception noticeException) {

                        System.out.println(
                                "Error reading individual notice."
                        );

                        noticeException.printStackTrace();
                    }
                }
            }

            // =================================================
            // FINAL RESULT
            // =================================================

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "NoticeDao: Final notices = "
                            + notices.size()
            );

            System.out.println(
                    "Resident Society = "
                            + residentSociety
            );

            System.out.println(
                    "========================================"
            );

            return notices;

        } catch (Exception e) {

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "NoticeDao: ERROR FETCHING NOTICES"
            );

            System.out.println(
                    "========================================"
            );

            e.printStackTrace();

            return notices;
        }
    }
}