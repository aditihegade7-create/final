package com.society.dao.Guard_dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.model.Guard_model.VisitorModel;

public class DailyReportsDao {

    private final Firestore firestore;

    private static final String VISITOR_COLLECTION = "visitors";
    private static final String REPORT_COLLECTION = "daily_reports";
    private static final String REPORTS_SUBCOLLECTION = "reports";

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DailyReportsDao() {
        firestore = FirebaseConfig.getFirestore();
    }

    // =====================================================
    // GET ALL VISITORS - LEGACY
    // =====================================================

    public List<VisitorModel> getAllVisitors() {

        List<VisitorModel> visitors = new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    firestore
                            .collection(VISITOR_COLLECTION)
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                VisitorModel visitor =
                        document.toObject(
                                VisitorModel.class
                        );

                if (visitor != null) {

                    visitor.setVisitorId(
                            document.getId()
                    );

                    visitors.add(visitor);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch visitors."
            );

            e.printStackTrace();
        }

        return visitors;
    }

    // =====================================================
    // GET VISITORS BY GUARD EMAIL
    // =====================================================

    public List<VisitorModel> getAllVisitors(
            String guardEmail) {

        List<VisitorModel> visitors =
                new ArrayList<>();

        if (guardEmail == null ||
                guardEmail.trim().isEmpty()) {

            return visitors;
        }

        String normalizedEmail =
                guardEmail.trim().toLowerCase();

        try {

            QuerySnapshot snapshot =
                    firestore
                            .collection(VISITOR_COLLECTION)
                            .whereEqualTo(
                                    "guardEmail",
                                    normalizedEmail
                            )
                            .get()
                            .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                VisitorModel visitor =
                        document.toObject(
                                VisitorModel.class
                        );

                if (visitor != null) {

                    visitor.setVisitorId(
                            document.getId()
                    );

                    visitors.add(visitor);
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to fetch guard visitors."
            );

            e.printStackTrace();
        }

        return visitors;
    }

    // =====================================================
    // SAVE DAILY REPORT
    // =====================================================

    public boolean saveDailyReport(

            String guardEmail,
            String society,
            String reportDate,

            int totalVisitors,
            int insideVisitors,
            int checkedOutVisitors,
            int deliveries,

            int parkingOccupied,
            int parkingAvailable,

            boolean emergencyActive,

            String shiftNotes,

            String shift,
            String gate
    ) {

        if (firestore == null) {

            System.out.println(
                    "ERROR: Firestore is not initialized."
            );

            return false;
        }

        if (guardEmail == null ||
                guardEmail.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Guard email is missing."
            );

            return false;
        }

        if (society == null ||
                society.trim().isEmpty()) {

            System.out.println(
                    "ERROR: Society is missing."
            );

            return false;
        }

        try {

            String normalizedEmail =
                    guardEmail.trim().toLowerCase();

            String normalizedSociety =
                    society.trim();

            String normalizedShift =
                    shift == null
                            ? ""
                            : shift.trim();

            String normalizedGate =
                    gate == null
                            ? ""
                            : gate.trim();

            // =================================================
            // PARENT DOCUMENT
            // daily_reports/{guardEmail}
            // =================================================

            Map<String, Object> parentData =
                    new HashMap<>();

            parentData.put(
                    "email",
                    normalizedEmail
            );

            parentData.put(
                    "society",
                    normalizedSociety
            );

            parentData.put(
                    "shift",
                    normalizedShift
            );

            parentData.put(
                    "gate",
                    normalizedGate
            );

            firestore
                    .collection(REPORT_COLLECTION)
                    .document(normalizedEmail)
                    .set(parentData)
                    .get();

            // =================================================
            // REPORT DOCUMENT
            // daily_reports/{guardEmail}/reports/{reportId}
            // =================================================

            String reportId =
                    firestore
                            .collection(REPORT_COLLECTION)
                            .document(normalizedEmail)
                            .collection(REPORTS_SUBCOLLECTION)
                            .document()
                            .getId();

            Map<String, Object> report =
                    new HashMap<>();

            report.put(
                    "reportId",
                    reportId
            );

            report.put(
                    "guardEmail",
                    normalizedEmail
            );

            report.put(
                    "society",
                    normalizedSociety
            );

            report.put(
                    "reportDate",
                    reportDate == null
                            ? ""
                            : reportDate
            );

            report.put(
                    "shift",
                    normalizedShift
            );

            report.put(
                    "gate",
                    normalizedGate
            );

            report.put(
                    "totalVisitors",
                    totalVisitors
            );

            report.put(
                    "insideVisitors",
                    insideVisitors
            );

            report.put(
                    "checkedOutVisitors",
                    checkedOutVisitors
            );

            report.put(
                    "deliveries",
                    deliveries
            );

            report.put(
                    "parkingOccupied",
                    parkingOccupied
            );

            report.put(
                    "parkingAvailable",
                    parkingAvailable
            );

            report.put(
                    "emergencyActive",
                    emergencyActive
            );

            report.put(
                    "shiftNotes",
                    shiftNotes == null
                            ? ""
                            : shiftNotes.trim()
            );

            report.put(
                    "createdAt",
                    new java.util.Date()
            );

            firestore
                    .collection(REPORT_COLLECTION)
                    .document(normalizedEmail)
                    .collection(REPORTS_SUBCOLLECTION)
                    .document(reportId)
                    .set(report)
                    .get();

            System.out.println(
                    "Daily report saved successfully."
            );

            return true;

        } catch (Exception e) {

            System.out.println(
                    "ERROR: Unable to save daily report."
            );

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // BACKWARD COMPATIBILITY
    // =====================================================

    public boolean saveDailyReport(

            String guardEmail,
            String society,
            String reportDate,

            int totalVisitors,
            int insideVisitors,
            int checkedOutVisitors,
            int deliveries,

            int parkingOccupied,
            int parkingAvailable,

            boolean emergencyActive,

            String shiftNotes
    ) {

        return saveDailyReport(

                guardEmail,
                society,
                reportDate,

                totalVisitors,
                insideVisitors,
                checkedOutVisitors,
                deliveries,

                parkingOccupied,
                parkingAvailable,

                emergencyActive,

                shiftNotes,

                "",
                ""
        );
    }
}