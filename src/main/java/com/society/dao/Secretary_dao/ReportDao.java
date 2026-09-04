package com.society.dao.Secretary_dao;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.config.FirebaseConfig;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Report;
import com.society.model.Welcome.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * ReportDao
 *
 * Handles:
 *
 * 1. Normal Reports collection
 * 2. Visitor records
 *
 * Firestore visitor structure:
 *
 * visitors
 *    └── residentEmail
 *         └── visitor_records
 *              └── visitorId
 *
 * IMPORTANT:
 * Every visitor field is read individually.
 *
 * Visitor records are converted into the existing Report model.
 * No VisitorReport model is required.
 */
public class ReportDao {

    // =========================================================
    // FIRESTORE
    // =========================================================

    private final Firestore db;

    // =========================================================
    // COLLECTIONS
    // =========================================================

    private static final String REPORTS_COLLECTION = "Reports";

    private static final String VISITORS_COLLECTION = "visitors";

    private static final String VISITOR_RECORDS_COLLECTION =
            "visitor_records";

    // =========================================================
    // DATE FORMAT
    // =========================================================

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd MMM yyyy HH:mm");

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ReportDao() {

        db = FirebaseConfig.getFirestore();

        if (db == null) {
            throw new IllegalStateException(
                    "Firestore initialization failed."
            );
        }
    }

    // =========================================================
    // ADD NORMAL REPORT
    // =========================================================

    public boolean addReport(Report report) {

        if (report == null) {
            return false;
        }

        if (isEmpty(report.getEmail())) {
            return false;
        }

        try {

            String id = clean(report.getId());

            if (id.isEmpty()) {

                id = UUID.randomUUID().toString();

                report.setId(id);
            }

            report.setEmail(
                    clean(report.getEmail())
                            .toLowerCase()
            );

            if (report.getTimestamp() <= 0) {

                report.setTimestamp(
                        System.currentTimeMillis()
                );
            }

            if (isEmpty(report.getStatus())) {

                report.setStatus("Pending");
            }

            db.collection(REPORTS_COLLECTION)
                    .document(id)
                    .set(report)
                    .get();

            return true;

        } catch (Exception e) {

            System.err.println(
                    "ReportDao.addReport ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET REPORTS FOR SOCIETY
    // =========================================================
    //
    // This method returns:
    //
    // 1. Reports collection records
    // 2. Visitor records
    //
    // for the logged-in secretary's society.
    //
    // =========================================================

    public List<Report> getReportsBySociety(
            String societyName) {

        List<Report> result =
                new ArrayList<>();

        if (isEmpty(societyName)) {
            return result;
        }

        String targetSociety =
                societyName.trim();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "REPORT DAO - FETCH SOCIETY DATA"
        );

        System.out.println(
                "Society : " + targetSociety
        );

        try {

            // =================================================
            // 1. NORMAL REPORTS
            // =================================================

            result.addAll(
                    getNormalReports(
                            targetSociety
                    )
            );

            // =================================================
            // 2. VISITOR RECORDS
            // =================================================

            result.addAll(
                    getVisitorReports(
                            targetSociety
                    )
            );

            // =================================================
            // SORT
            // =================================================

            result.sort(
                    Comparator.comparingLong(
                            Report::getTimestamp
                    ).reversed()
            );

            System.out.println(
                    "Normal Reports + Visitor Reports : "
                            + result.size()
            );

            System.out.println(
                    "========================================"
            );

            return result;

        } catch (Exception e) {

            System.err.println(
                    "ReportDao.getReportsBySociety ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return result;
        }
    }

    // =========================================================
    // GET NORMAL REPORTS
    // =========================================================

    private List<Report> getNormalReports(
            String targetSociety) {

        List<Report> reports =
                new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    db.collection(
                            REPORTS_COLLECTION
                    ).get();

            QuerySnapshot snapshot =
                    future.get();

            UserDao userDao =
                    new UserDao();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                try {

                    Report report =
                            document.toObject(
                                    Report.class
                            );

                    if (report == null) {
                        continue;
                    }

                    // =========================================
                    // REPORT ID
                    // =========================================

                    String reportId =
                            clean(report.getId());

                    if (reportId.isEmpty()) {

                        reportId =
                                document.getId();

                        report.setId(reportId);
                    }

                    // =========================================
                    // EMAIL
                    // =========================================

                    String email =
                            clean(report.getEmail());

                    if (email.isEmpty()) {

                        Object submittedByEmail =
                                document.get(
                                        "submittedByEmail"
                                );

                        if (submittedByEmail != null) {

                            email =
                                    submittedByEmail
                                            .toString()
                                            .trim()
                                            .toLowerCase();
                        }
                    }

                    if (email.isEmpty()
                            && document.getId()
                            .contains("@")) {

                        email =
                                document.getId()
                                        .trim()
                                        .toLowerCase();
                    }

                    if (!email.isEmpty()) {

                        report.setEmail(email);
                    }

                    // =========================================
                    // SOCIETY
                    // =========================================

                    String actualSociety =
                            clean(
                                    report.getSocietyName()
                            );

                    if (actualSociety.isEmpty()) {

                        actualSociety =
                                clean(
                                        report.getSocietyId()
                                );
                    }

                    // =========================================
                    // USER FALLBACK
                    // =========================================

                    User submitter = null;

                    if (!email.isEmpty()) {

                        try {

                            submitter =
                                    userDao.getUserByEmail(
                                            email
                                    );

                        } catch (Exception ignored) {
                            // Continue using report data.
                        }
                    }

                    if (actualSociety.isEmpty()
                            && submitter != null) {

                        actualSociety =
                                clean(
                                        submitter.getSociety()
                                );
                    }

                    // =========================================
                    // SOCIETY FILTER
                    // =========================================

                    if (!sameText(
                            actualSociety,
                            targetSociety
                    )) {

                        continue;
                    }

                    // =========================================
                    // SET SOCIETY
                    // =========================================

                    if (isEmpty(
                            report.getSocietyName()
                    )) {

                        report.setSocietyName(
                                targetSociety
                        );
                    }

                    if (isEmpty(
                            report.getSocietyId()
                    )) {

                        report.setSocietyId(
                                targetSociety
                        );
                    }

                    // =========================================
                    // SUBMITTED BY
                    // =========================================

                    if (isEmpty(
                            report.getSubmittedBy()
                    )) {

                        if (submitter != null) {

                            String name =
                                    clean(
                                            submitter.getName()
                                    );

                            if (!name.isEmpty()) {

                                report.setSubmittedBy(
                                        name
                                );

                            } else {

                                report.setSubmittedBy(
                                        email
                                );
                            }

                        } else {

                            report.setSubmittedBy(
                                    email
                            );
                        }
                    }

                    // =========================================
                    // SOURCE
                    // =========================================

                    if (isEmpty(
                            report.getSource()
                    )) {

                        report.setSource(
                                "Report"
                        );
                    }

                    // =========================================
                    // STATUS
                    // =========================================

                    if (isEmpty(
                            report.getStatus()
                    )) {

                        report.setStatus(
                                "Pending"
                        );
                    }

                    // =========================================
                    // TIMESTAMP FALLBACK
                    // =========================================

                    if (report.getTimestamp() <= 0) {

                        report.setTimestamp(
                                parseDateToMillis(
                                        report.getDate()
                                )
                        );
                    }

                    reports.add(report);

                } catch (Exception itemError) {

                    System.err.println(
                            "Skipping report document "
                                    + document.getId()
                                    + " : "
                                    + itemError.getMessage()
                    );
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "getNormalReports ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return reports;
    }

    // =========================================================
    // GET VISITOR REPORTS
    // =========================================================
    //
    // IMPORTANT:
    //
    // We intentionally read each visitor document individually.
    //
    // Every field is mapped separately:
    //
    // id
    // visitorName
    // phoneNumber
    // purpose
    // visitDate
    // visitTime
    // flatNumber
    // gate
    // vehicleNumber
    // status
    // qrToken
    // used
    // createdAt
    // society
    //
    // =========================================================

    private List<Report> getVisitorReports(
            String targetSociety) {

        List<Report> reports =
                new ArrayList<>();

        try {

            ApiFuture<QuerySnapshot> future =
                    db.collectionGroup(
                            VISITOR_RECORDS_COLLECTION
                    ).get();

            QuerySnapshot snapshot =
                    future.get();

            System.out.println(
                    "Visitor documents found : "
                            + snapshot.size()
            );

            Set<String> processedIds =
                    new HashSet<>();

            for (DocumentSnapshot visitorDocument :
                    snapshot.getDocuments()) {

                try {

                    // =========================================
                    // PARENT RESIDENT EMAIL
                    // =========================================

                    String residentEmail =
                            getParentResidentEmail(
                                    visitorDocument
                            );

                    // =========================================
                    // SOCIETY
                    // =========================================

                    String society =
                            getString(
                                    visitorDocument,
                                    "society"
                            );

                    // =========================================
                    // FALLBACK SOCIETY FROM PARENT
                    // =========================================

                    if (society.isEmpty()) {

                        society =
                                getParentSociety(
                                        residentEmail
                                );
                    }

                    // =========================================
                    // SOCIETY FILTER
                    // =========================================

                    if (!sameText(
                            society,
                            targetSociety
                    )) {

                        continue;
                    }

                    // =========================================
                    // VISITOR ID
                    // =========================================

                    String visitorId =
                            getString(
                                    visitorDocument,
                                    "id"
                            );

                    if (visitorId.isEmpty()) {

                        visitorId =
                                getString(
                                        visitorDocument,
                                        "visitorId"
                                );
                    }

                    if (visitorId.isEmpty()) {

                        visitorId =
                                visitorDocument.getId();
                    }

                    // =========================================
                    // DUPLICATE PROTECTION
                    // =========================================

                    String uniqueKey =
                            residentEmail
                                    + "|"
                                    + visitorId;

                    if (processedIds.contains(
                            uniqueKey
                    )) {

                        continue;
                    }

                    processedIds.add(
                            uniqueKey
                    );

                    // =========================================
                    // VISITOR NAME
                    // =========================================

                    String visitorName =
                            getString(
                                    visitorDocument,
                                    "visitorName"
                            );

                    if (visitorName.isEmpty()) {

                        visitorName =
                                getString(
                                        visitorDocument,
                                        "name"
                                );
                    }

                    // =========================================
                    // PHONE
                    // =========================================

                    String phoneNumber =
                            getString(
                                    visitorDocument,
                                    "phoneNumber"
                            );

                    if (phoneNumber.isEmpty()) {

                        phoneNumber =
                                getString(
                                        visitorDocument,
                                        "phone"
                                );
                    }

                    // =========================================
                    // PURPOSE
                    // =========================================

                    String purpose =
                            getString(
                                    visitorDocument,
                                    "purpose"
                            );

                    // =========================================
                    // VISIT DATE
                    // =========================================

                    String visitDate =
                            getString(
                                    visitorDocument,
                                    "visitDate"
                            );

                    // =========================================
                    // VISIT TIME
                    // =========================================

                    String visitTime =
                            getString(
                                    visitorDocument,
                                    "visitTime"
                            );

                    // =========================================
                    // FLAT NUMBER
                    // =========================================

                    String flatNumber =
                            getString(
                                    visitorDocument,
                                    "flatNumber"
                            );

                    if (flatNumber.isEmpty()) {

                        flatNumber =
                                getString(
                                        visitorDocument,
                                        "flat"
                                );
                    }

                    // =========================================
                    // GATE
                    // =========================================

                    String gate =
                            getString(
                                    visitorDocument,
                                    "gate"
                            );

                    // =========================================
                    // VEHICLE NUMBER
                    // =========================================

                    String vehicleNumber =
                            getString(
                                    visitorDocument,
                                    "vehicleNumber"
                            );

                    // =========================================
                    // STATUS
                    // =========================================

                    String status =
                            getString(
                                    visitorDocument,
                                    "status"
                            );

                    // =========================================
                    // QR TOKEN
                    // =========================================

                    String qrToken =
                            getString(
                                    visitorDocument,
                                    "qrToken"
                            );

                    if (qrToken.isEmpty()) {

                        qrToken =
                                getString(
                                        visitorDocument,
                                        "qrPass"
                                );
                    }

                    // =========================================
                    // USED
                    // =========================================

                    String used =
                            getBooleanAsString(
                                    visitorDocument,
                                    "used"
                            );

                    // =========================================
                    // CREATED AT
                    // =========================================

                    String createdAt =
                            getDateValue(
                                    visitorDocument,
                                    "createdAt"
                            );

                    // =========================================
                    // ENTRY TIME
                    // =========================================

                    String entryTime =
                            getDateValue(
                                    visitorDocument,
                                    "entryTime"
                            );

                    // =========================================
                    // EXIT TIME
                    // =========================================

                    String exitTime =
                            getDateValue(
                                    visitorDocument,
                                    "exitTime"
                            );

                    // =========================================
                    // REMARKS
                    // =========================================

                    String remarks =
                            getString(
                                    visitorDocument,
                                    "remarks"
                            );

                    // =========================================
                    // GUARD ID
                    // =========================================

                    String guardId =
                            getString(
                                    visitorDocument,
                                    "guardId"
                            );

                    // =========================================
                    // GUARD EMAIL
                    // =========================================

                    String guardEmail =
                            getString(
                                    visitorDocument,
                                    "guardEmail"
                            );

                    if (guardEmail.isEmpty()) {

                        guardEmail =
                                getString(
                                        visitorDocument,
                                        "email"
                                );
                    }

                    // =========================================
                    // BUILD REPORT
                    // =========================================

                    Report report =
                            new Report();

                    // -----------------------------------------
                    // COMMON REPORT FIELDS
                    // -----------------------------------------

                    report.setId(
                            "VISITOR_" +
                                    visitorId
                    );

                    report.setEmail(
                            residentEmail
                    );

                    report.setSocietyId(
                            society
                    );

                    report.setSocietyName(
                            society
                    );

                    report.setSource(
                            "Visitor Log"
                    );

                    report.setType(
                            "Visitor"
                    );

                    report.setTitle(
                            visitorName.isEmpty()
                                    ? "Visitor Record"
                                    : visitorName
                    );

                    report.setSubmittedBy(
                            residentEmail.isEmpty()
                                    ? guardEmail
                                    : residentEmail
                    );

                    // -----------------------------------------
                    // DATE
                    // -----------------------------------------

                    String displayDate =
                            buildVisitorDate(
                                    visitDate,
                                    visitTime,
                                    createdAt
                            );

                    report.setDate(
                            displayDate
                    );

                    // -----------------------------------------
                    // STATUS
                    // -----------------------------------------

                    report.setStatus(
                            status.isEmpty()
                                    ? "Recorded"
                                    : status
                    );

                    // -----------------------------------------
                    // TIMESTAMP
                    // -----------------------------------------

                    long timestamp =
                            getVisitorTimestamp(
                                    visitorDocument,
                                    visitDate,
                                    visitTime,
                                    createdAt
                            );

                    report.setTimestamp(
                            timestamp
                    );

                    // -----------------------------------------
                    // ALL VISITOR DATA
                    // -----------------------------------------
                    //
                    // Every field is preserved separately
                    // inside details.
                    //
                    // -----------------------------------------

                    StringBuilder details =
                            new StringBuilder();

                    appendDetail(
                            details,
                            "Visitor ID",
                            visitorId
                    );

                    appendDetail(
                            details,
                            "Visitor Name",
                            visitorName
                    );

                    appendDetail(
                            details,
                            "Phone Number",
                            phoneNumber
                    );

                    appendDetail(
                            details,
                            "Flat Number",
                            flatNumber
                    );

                    appendDetail(
                            details,
                            "Purpose",
                            purpose
                    );

                    appendDetail(
                            details,
                            "Visit Date",
                            visitDate
                    );

                    appendDetail(
                            details,
                            "Visit Time",
                            visitTime
                    );

                    appendDetail(
                            details,
                            "Gate",
                            gate
                    );

                    appendDetail(
                            details,
                            "Vehicle Number",
                            vehicleNumber
                    );

                    appendDetail(
                            details,
                            "Status",
                            status
                    );

                    appendDetail(
                            details,
                            "QR Token",
                            qrToken
                    );

                    appendDetail(
                            details,
                            "Used",
                            used
                    );

                    appendDetail(
                            details,
                            "Created At",
                            createdAt
                    );

                    appendDetail(
                            details,
                            "Entry Time",
                            entryTime
                    );

                    appendDetail(
                            details,
                            "Exit Time",
                            exitTime
                    );

                    appendDetail(
                            details,
                            "Remarks",
                            remarks
                    );

                    appendDetail(
                            details,
                            "Guard ID",
                            guardId
                    );

                    appendDetail(
                            details,
                            "Guard Email",
                            guardEmail
                    );

                    appendDetail(
                            details,
                            "Resident Email",
                            residentEmail
                    );

                    appendDetail(
                            details,
                            "Society",
                            society
                    );

                    report.setDetails(
                            details.toString()
                    );

                    reports.add(report);

                    System.out.println(
                            "Visitor mapped: "
                                    + visitorName
                                    + " | Flat: "
                                    + flatNumber
                                    + " | Society: "
                                    + society
                    );

                } catch (Exception visitorError) {

                    System.err.println(
                            "Skipping visitor document "
                                    + visitorDocument.getId()
                                    + " : "
                                    + visitorError.getMessage()
                    );

                    visitorError.printStackTrace();
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "getVisitorReports ERROR: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }

        return reports;
    }

    // =========================================================
    // GET PARENT RESIDENT EMAIL
    // =========================================================

    private String getParentResidentEmail(
            DocumentSnapshot visitorDocument) {

        try {

            DocumentReference parentDocument =
                    visitorDocument
                            .getReference()
                            .getParent()
                            .getParent();

            if (parentDocument != null) {

                return clean(
                        parentDocument.getId()
                ).toLowerCase();
            }

        } catch (Exception ignored) {
        }

        return "";
    }

    // =========================================================
    // GET PARENT SOCIETY
    // =========================================================

    private String getParentSociety(
            String residentEmail) {

        if (residentEmail == null
                || residentEmail.trim().isEmpty()) {

            return "";
        }

        try {

            DocumentSnapshot parent =
                    db.collection(
                            VISITORS_COLLECTION
                    )
                    .document(
                            residentEmail
                                    .trim()
                                    .toLowerCase()
                    )
                    .get()
                    .get();

            if (parent.exists()) {

                String society =
                        getString(
                                parent,
                                "society"
                        );

                if (!society.isEmpty()) {
                    return society;
                }

                society =
                        getString(
                                parent,
                                "societyName"
                        );

                if (!society.isEmpty()) {
                    return society;
                }
            }

        } catch (Exception e) {

            System.err.println(
                    "Unable to read parent society: "
                            + e.getMessage()
            );
        }

        return "";
    }

    // =========================================================
    // GET STRING FIELD
    // =========================================================

    private String getString(
            DocumentSnapshot document,
            String field) {

        if (document == null
                || field == null
                || field.trim().isEmpty()) {

            return "";
        }

        try {

            Object value =
                    document.get(field);

            if (value == null) {
                return "";
            }

            return value.toString().trim();

        } catch (Exception e) {

            return "";
        }
    }

    // =========================================================
    // GET BOOLEAN FIELD
    // =========================================================

    private String getBooleanAsString(
            DocumentSnapshot document,
            String field) {

        if (document == null) {
            return "";
        }

        try {

            Object value =
                    document.get(field);

            if (value == null) {
                return "";
            }

            if (value instanceof Boolean) {

                return String.valueOf(
                        (Boolean) value
                );
            }

            return value.toString();

        } catch (Exception e) {

            return "";
        }
    }

    // =========================================================
    // GET DATE VALUE
    // =========================================================

    private String getDateValue(
            DocumentSnapshot document,
            String field) {

        if (document == null) {
            return "";
        }

        try {

            Object value =
                    document.get(field);

            if (value == null) {
                return "";
            }

            if (value instanceof com.google.cloud.Timestamp) {

                return DATE_FORMAT.format(
                        ((com.google.cloud.Timestamp) value).toDate()
                );
            }

            if (value instanceof Date) {

                return DATE_FORMAT.format(
                        (Date) value
                );
            }

            return value.toString();

        } catch (Exception e) {

            return "";
        }
    }

    // =========================================================
    // BUILD VISITOR DISPLAY DATE
    // =========================================================

    private String buildVisitorDate(
            String visitDate,
            String visitTime,
            String createdAt) {

        String date =
                clean(visitDate);

        String time =
                clean(visitTime);

        if (!date.isEmpty()
                && !time.isEmpty()) {

            return date + " " + time;
        }

        if (!date.isEmpty()) {

            return date;
        }

        return clean(createdAt);
    }

    // =========================================================
    // GET VISITOR TIMESTAMP
    // =========================================================

    private long getVisitorTimestamp(
            DocumentSnapshot document,
            String visitDate,
            String visitTime,
            String createdAt) {

        try {

            Object created =
                    document.get("createdAt");

            if (created instanceof com.google.cloud.Timestamp) {

                return ((com.google.cloud.Timestamp) created)
                        .toDate()
                        .getTime();
            }

            if (created instanceof Date) {

                return ((Date) created)
                        .getTime();
            }

        } catch (Exception ignored) {
        }

        long parsed =
                parseDateToMillis(
                        visitDate
                                + " "
                                + visitTime
                );

        if (parsed > 0) {
            return parsed;
        }

        parsed =
                parseDateToMillis(
                        createdAt
                );

        if (parsed > 0) {
            return parsed;
        }

        return System.currentTimeMillis();
    }

    // =========================================================
    // APPEND DETAIL
    // =========================================================

    private void appendDetail(
            StringBuilder builder,
            String field,
            String value) {

        String safeValue =
                clean(value);

        if (safeValue.isEmpty()) {

            safeValue = "Not Available";
        }

        builder.append(field)
                .append(": ")
                .append(safeValue)
                .append("\n");
    }

    // =========================================================
    // GET REPORTS BY SOCIETY + EMAIL
    // =========================================================

    public List<Report> getReportsBySocietyAndEmail(
            String societyName,
            String email) {

        List<Report> result =
                new ArrayList<>();

        if (isEmpty(societyName)
                || isEmpty(email)) {

            return result;
        }

        String targetEmail =
                email.trim().toLowerCase();

        List<Report> societyReports =
                getReportsBySociety(
                        societyName
                );

        for (Report report :
                societyReports) {

            if (report == null) {
                continue;
            }

            if (targetEmail.equalsIgnoreCase(
                    clean(report.getEmail())
            )) {

                result.add(report);
            }
        }

        result.sort(
                Comparator.comparingLong(
                        Report::getTimestamp
                ).reversed()
        );

        return result;
    }

    // =========================================================
    // GET ALL REPORTS
    // =========================================================

    public List<Report> getAllReports() {

        List<Report> reports =
                new ArrayList<>();

        try {

            QuerySnapshot snapshot =
                    db.collection(
                            REPORTS_COLLECTION
                    )
                    .get()
                    .get();

            for (DocumentSnapshot document :
                    snapshot.getDocuments()) {

                Report report =
                        document.toObject(
                                Report.class
                        );

                if (report == null) {
                    continue;
                }

                if (isEmpty(report.getId())) {

                    report.setId(
                            document.getId()
                    );
                }

                reports.add(report);
            }

            reports.sort(
                    Comparator.comparingLong(
                            Report::getTimestamp
                    ).reversed()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }

        return reports;
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    public boolean updateStatus(
            String reportId,
            String status) {

        if (isEmpty(reportId)
                || isEmpty(status)) {

            return false;
        }

        try {

            db.collection(
                    REPORTS_COLLECTION
            )
            .document(
                    reportId.trim()
            )
            .update(
                    "status",
                    status.trim()
            )
            .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // DELETE REPORT
    // =========================================================

    public boolean deleteReport(
            String reportId) {

        if (isEmpty(reportId)) {
            return false;
        }

        try {

            db.collection(
                    REPORTS_COLLECTION
            )
            .document(
                    reportId.trim()
            )
            .delete()
            .get();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // PARSE DATE
    // =========================================================

    private long parseDateToMillis(
            String date) {

        if (isEmpty(date)) {
            return 0L;
        }

        String value =
                date.trim();

        String[] patterns = {

                "dd MMM yyyy HH:mm",

                "dd MMM yyyy",

                "yyyy-MM-dd HH:mm",

                "yyyy-MM-dd",

                "dd/MM/yyyy HH:mm",

                "dd/MM/yyyy",

                "MM/dd/yyyy HH:mm",

                "MM/dd/yyyy"
        };

        for (String pattern :
                patterns) {

            try {

                SimpleDateFormat formatter =
                        new SimpleDateFormat(
                                pattern
                        );

                Date parsed =
                        formatter.parse(
                                value
                        );

                if (parsed != null) {

                    return parsed.getTime();
                }

            } catch (Exception ignored) {
            }
        }

        return 0L;
    }

    // =========================================================
    // SAME TEXT
    // =========================================================

    private boolean sameText(
            String first,
            String second) {

        return clean(first)
                .equalsIgnoreCase(
                        clean(second)
                );
    }

    // =========================================================
    // CLEAN
    // =========================================================

    private String clean(
            String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }

    // =========================================================
    // EMPTY
    // =========================================================

    private boolean isEmpty(
            String value) {

        return value == null
                || value.trim().isEmpty();
    }
}