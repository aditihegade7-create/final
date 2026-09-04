package com.society.dao.Guard_dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.model.Guard_model.NoticeModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NoticeDao {

    private final Firestore firestore;

    private static final String COLLECTION = "Notices";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public NoticeDao() {

        firestore = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // GET LATEST ACTIVE NOTICE
    // =====================================================

    public NoticeModel getLatestActiveNotice() {

        List<NoticeModel> notices =
                new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    firestore
                            .collection(COLLECTION)
                            .whereEqualTo(
                                    "status",
                                    "Active"
                            )
                            .get();

            QuerySnapshot snapshot =
                    future.get();

            // =================================================
            // CONVERT FIRESTORE DOCUMENTS
            // =================================================

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                NoticeModel notice =
                        document.toObject(
                                NoticeModel.class
                        );

                if (notice != null) {

                    notice.setId(
                            document.getId()
                    );

                    notices.add(
                            notice
                    );
                }
            }

            // =================================================
            // SORT NEWEST → OLDEST
            // =================================================

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "M/d/yyyy"
                    );

            notices.sort(
                    (a, b) -> {

                        try {

                            LocalDate dateA =
                                    LocalDate.parse(
                                            a.getDate(),
                                            formatter
                                    );

                            LocalDate dateB =
                                    LocalDate.parse(
                                            b.getDate(),
                                            formatter
                                    );

                            return dateB.compareTo(
                                    dateA
                            );

                        } catch (Exception e) {

                            return 0;
                        }
                    }
            );

            // =================================================
            // RETURN LATEST NOTICE
            // =================================================

            if (!notices.isEmpty()) {

                return notices.get(0);
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch latest notice."
            );

            e.printStackTrace();
        }

        return null;
    }
}