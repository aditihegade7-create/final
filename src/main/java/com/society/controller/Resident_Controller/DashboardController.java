package com.society.controller.Resident_Controller;

import com.society.config.FirebaseConfig;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.society.dao.Resident_dao.ComplaintDAO;
import com.society.dao.Resident_dao.ElectricityDAO;
import com.society.dao.Resident_dao.PollDao;
import com.society.dao.Resident_dao.VisitorDAO;
import com.society.model.Resident_model.ComplaintModel;
import com.society.model.Resident_model.NoticeModel;
import com.society.model.Resident_model.PollModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ============================================================
 * RESIDENT DASHBOARD CONTROLLER
 * ============================================================
 *
 * Fetches dashboard information using the existing project
 * DAOs/controllers.
 *
 * No new Firestore collection is created.
 * No dashboard data is hardcoded.
 *
 * Existing sources:
 *
 * Complaints:
 * complaints/{residentEmail}/complaints/{complaintId}
 *
 * Visitors:
 * visitors/{residentEmail}/visitor_records/{visitorId}
 *
 * Maintenance:
 * Maintenance/{residentEmail}/records/{recordId}
 *
 * Electricity:
 * electricity/{residentEmail}/bills/{billId}
 *
 * Polls:
 * Polls/{secretaryEmail}/polls/{pollId}
 *
 * Notices:
 * Existing NoticeController
 */
public class DashboardController {

    // =========================================================
    // DAOS / CONTROLLERS
    // =========================================================

    private final ComplaintDAO complaintDAO;
    private final VisitorDAO visitorDAO;
    private final ElectricityDAO electricityDAO;
    private final PollDao pollDao;
    private final NoticeController noticeController;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DashboardController() {

        this.complaintDAO =
                new ComplaintDAO(
                        FirebaseConfig.getFirestore()
                );

        this.visitorDAO =
                new VisitorDAO(
                        FirebaseConfig.getFirestore()
                );

        this.electricityDAO =
                new ElectricityDAO();

        this.pollDao =
                new PollDao(
                        FirebaseConfig.getFirestore()
                );

        this.noticeController =
                new NoticeController();
    }

    // =========================================================
    // LOAD COMPLETE DASHBOARD
    // =========================================================

    public DashboardData loadDashboardData(
            String residentEmail
    ) throws Exception {

        String email =
                normalizeEmail(residentEmail);

        if (email.isEmpty()) {

            throw new IllegalArgumentException(
                    "Resident email is missing."
            );
        }

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "RESIDENT DASHBOARD DATA FETCH"
        );

        System.out.println(
                "Resident Email : " + email
        );

        System.out.println(
                "=========================================="
        );

        // =====================================================
        // COMPLAINTS
        // =====================================================

        List<ComplaintModel> complaints =
                fetchComplaints(email);

        int activeComplaints =
                countActiveComplaints(
                        complaints
                );

        // =====================================================
        // VISITORS TODAY
        // =====================================================

        List<com.society.model.Resident_model.VisitorModel>
                visitors =
                fetchTodayVisitors(email);

        int visitorsToday =
                visitors.size();

        // =====================================================
        // MAINTENANCE + ELECTRICITY BILLS
        // =====================================================

        List<Map<String, Object>>
                maintenanceBills =
                fetchMaintenance(email);

        List<Map<String, Object>>
                electricityBills =
                fetchElectricity(email);

        double outstandingMaintenance =
                calculateOutstandingAmount(
                        maintenanceBills
                );

        double outstandingElectricity =
                calculateOutstandingAmount(
                        electricityBills
                );

        // Total Bills shown on Resident Dashboard
        // means total currently outstanding amount.
        double totalBills =
                outstandingMaintenance
                        + outstandingElectricity;

        Map<String, Object> nextMaintenanceBill =
                findNextOutstandingBill(
                        maintenanceBills
                );

        String maintenanceDueDate =
                getString(
                        nextMaintenanceBill,
                        "date"
                );

        // =====================================================
        // NOTICES
        // =====================================================

        List<NoticeModel> notices =
                fetchNotices(email);

        // =====================================================
        // ACTIVE POLLS
        // =====================================================

        List<PollModel> polls =
                fetchActivePolls();

        // =====================================================
        // DEBUG
        // =====================================================

        System.out.println(
                "------------------------------------------"
        );

        System.out.println(
                "Dashboard complaints : "
                        + complaints.size()
        );

        System.out.println(
                "Active complaints    : "
                        + activeComplaints
        );

        System.out.println(
                "Visitors today       : "
                        + visitorsToday
        );

        System.out.println(
                "Maintenance bills    : "
                        + maintenanceBills.size()
        );

        System.out.println(
                "Electricity bills    : "
                        + electricityBills.size()
        );

        System.out.println(
                "Outstanding maintenance : "
                        + outstandingMaintenance
        );

        System.out.println(
                "Outstanding electricity : "
                        + outstandingElectricity
        );

        System.out.println(
                "Total bills          : "
                        + totalBills
        );

        System.out.println(
                "Outstanding          : "
                        + outstandingMaintenance
        );

        System.out.println(
                "Maintenance due      : "
                        + maintenanceDueDate
        );

        System.out.println(
                "Notices              : "
                        + notices.size()
        );

        System.out.println(
                "Active polls         : "
                        + polls.size()
        );

        System.out.println(
                "=========================================="
        );

        return new DashboardData(
                email,
                activeComplaints,
                visitorsToday,
                totalBills,
                outstandingMaintenance,
                maintenanceDueDate,
                complaints,
                visitors,
                maintenanceBills,
                electricityBills,
                notices,
                polls
        );
    }

    // =========================================================
    // COMPLAINT FETCH
    // =========================================================

    private List<ComplaintModel> fetchComplaints(
            String email
    ) {

        try {

            List<ComplaintModel> complaints =
                    complaintDAO.getComplaintsByEmail(
                            email
                    );

            if (complaints == null) {
                return Collections.emptyList();
            }

            return complaints;

        } catch (Exception e) {

            System.err.println(
                    "Dashboard complaint fetch failed."
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    // =========================================================
    // COUNT ACTIVE COMPLAINTS
    // =========================================================

    private int countActiveComplaints(
            List<ComplaintModel> complaints
    ) {

        if (complaints == null
                || complaints.isEmpty()) {

            return 0;
        }

        int count = 0;

        for (ComplaintModel complaint :
                complaints) {

            if (complaint == null) {
                continue;
            }

            String status =
                    safe(
                            complaint.getStatus()
                    ).toLowerCase();

            /*
             * These statuses mean the complaint is no longer active.
             */
            if (status.equals("resolved")
                    || status.equals("closed")
                    || status.equals("completed")
                    || status.equals("complete")
                    || status.equals("cancelled")
                    || status.equals("canceled")) {

                continue;
            }

            count++;
        }

        return count;
    }

    // =========================================================
    // VISITOR FETCH
    // =========================================================

    private List<com.society.model.Resident_model.VisitorModel>
    fetchTodayVisitors(
            String email
    ) {

        try {

            List<com.society.model.Resident_model.VisitorModel>
                    visitors =
                    visitorDAO
                            .getTodaysInvitedVisitors(
                                    email
                            );

            if (visitors == null) {
                return Collections.emptyList();
            }

            /*
             * Prevent accidental duplicate counting.
             */
            return removeDuplicateVisitors(
                    visitors
            );

        } catch (Exception e) {

            System.err.println(
                    "Dashboard visitor fetch failed."
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    // =========================================================
    // REMOVE DUPLICATE VISITORS
    // =========================================================

    private List<com.society.model.Resident_model.VisitorModel>
    removeDuplicateVisitors(
            List<com.society.model.Resident_model.VisitorModel>
                    visitors
    ) {

        List<com.society.model.Resident_model.VisitorModel>
                result =
                new ArrayList<>();

        Set<String> ids =
                new HashSet<>();

        for (com.society.model.Resident_model.VisitorModel
                visitor : visitors) {

            if (visitor == null) {
                continue;
            }

            String id =
                    safe(visitor.getId());

            if (!id.isEmpty()) {

                if (ids.add(id)) {
                    result.add(visitor);
                }

                continue;
            }

            /*
             * Fallback for old records where ID is absent.
             */
            String fallback =
                    safe(visitor.getVisitorName())
                            + "|"
                            + safe(visitor.getVisitDate())
                            + "|"
                            + safe(visitor.getVisitTime());

            if (ids.add(fallback)) {
                result.add(visitor);
            }
        }

        return result;
    }

    // =========================================================
    // MAINTENANCE FETCH - EXISTING FIRESTORE STRUCTURE
    // =========================================================
    /*
     * Existing Firestore structure:
     *
     * Maintenance
     *   ├── residentEmail1
     *   │     ├── email
     *   │     ├── society
     *   │     └── records
     *   │           └── recordId
     *   │                ├── amount
     *   │                ├── date
     *   │                ├── maintenanceId
     *   │                ├── month
     *   │                ├── society
     *   │                └── status
     *   └── residentEmail2
     *        └── ...
     *
     * We do not modify the Firestore structure.
     *
     * We first get the logged-in resident's society from Residents,
     * then scan the existing Maintenance parent documents and take
     * only records belonging to the same society.
     */
    private List<Map<String, Object>> fetchMaintenance(
            String email
    ) {

        List<Map<String, Object>> bills =
                new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            return bills;
        }

        try {

            Firestore db =
                    FirebaseConfig.getFirestore();

            String currentEmail =
                    normalizeEmail(email);

            // -------------------------------------------------
            // 1. LOGGED-IN RESIDENT
            // -------------------------------------------------
            DocumentSnapshot residentDocument =
                    db.collection("Residents")
                            .document(currentEmail)
                            .get()
                            .get();

            // Fallback if collection is lowercase.
            if (!residentDocument.exists()) {
                residentDocument =
                        db.collection("residents")
                                .document(currentEmail)
                                .get()
                                .get();
            }

            if (!residentDocument.exists()) {

                System.err.println(
                        "Resident document not found for: "
                                + currentEmail
                );

                return bills;
            }

            // -------------------------------------------------
            // 2. SOCIETY
            // -------------------------------------------------
            String residentSociety =
                    residentDocument.getString("society");

            if (residentSociety == null ||
                    residentSociety.trim().isEmpty()) {

                residentSociety =
                        residentDocument.getString(
                                "societyName"
                        );
            }

            if (residentSociety == null ||
                    residentSociety.trim().isEmpty()) {

                System.err.println(
                        "Society not found for resident: "
                                + currentEmail
                );

                return bills;
            }

            residentSociety =
                    residentSociety.trim();

            System.out.println(
                    "Dashboard resident = "
                            + currentEmail
                            + " | society = "
                            + residentSociety
            );

            // -------------------------------------------------
            // 3. ALL MAINTENANCE PARENTS
            // -------------------------------------------------
            QuerySnapshot parentSnapshot =
                    db.collection("Maintenance")
                            .get()
                            .get();

            // Fallback for lowercase collection name.
            if (parentSnapshot.isEmpty()) {
                QuerySnapshot lowercaseSnapshot =
                        db.collection("maintenance")
                                .get()
                                .get();

                if (!lowercaseSnapshot.isEmpty()) {
                    parentSnapshot =
                            lowercaseSnapshot;
                }
            }

            // -------------------------------------------------
            // 4. SAME-SOCIETY PARENTS ONLY
            // -------------------------------------------------
            for (DocumentSnapshot parent :
                    parentSnapshot.getDocuments()) {

                String parentSociety =
                        parent.getString("society");

                if (parentSociety == null ||
                        parentSociety.trim().isEmpty()) {
                    continue;
                }

                if (!residentSociety.equalsIgnoreCase(
                        parentSociety.trim()
                )) {
                    continue;
                }

                // -------------------------------------------------
                // 5. records SUBCOLLECTION
                // -------------------------------------------------
                QuerySnapshot recordSnapshot =
                        parent.getReference()
                                .collection("records")
                                .get()
                                .get();

                for (DocumentSnapshot record :
                        recordSnapshot.getDocuments()) {

                    Map<String, Object> data =
                            record.getData();

                    if (data == null) {
                        continue;
                    }

                    data =
                            new java.util.HashMap<>(data);

                    String recordSociety =
                            String.valueOf(
                                    data.getOrDefault(
                                            "society",
                                            parentSociety
                                    )
                            ).trim();

                    if (!residentSociety.equalsIgnoreCase(
                            recordSociety
                    )) {
                        continue;
                    }

                    String billId =
                            String.valueOf(
                                    data.getOrDefault(
                                            "maintenanceId",
                                            record.getId()
                                    )
                            ).trim();

                    data.put("billId", billId);

                    if (!data.containsKey("status") ||
                            data.get("status") == null ||
                            String.valueOf(
                                    data.get("status")
                            ).trim().isEmpty()) {

                        data.put("status", "Pending");
                    }

                    bills.add(data);
                }
            }

            System.out.println(
                    "Dashboard maintenance records fetched = "
                            + bills.size()
            );

        } catch (Exception e) {

            System.err.println(
                    "Dashboard maintenance fetch failed."
            );

            e.printStackTrace();
        }

        return bills;
    }

    // =========================================================
    // ELECTRICITY FETCH
    // =========================================================
    private List<Map<String, Object>> fetchElectricity(
            String email
    ) {

        try {

            List<Map<String, Object>> bills =
                    electricityDAO
                            .getElectricityBillsByEmail(
                                    email
                            );

            if (bills == null) {
                return Collections.emptyList();
            }

            return bills;

        } catch (Exception e) {

            System.err.println(
                    "Dashboard electricity fetch failed."
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    // =========================================================
    // CALCULATE OUTSTANDING AMOUNT
    // =========================================================
    private double calculateOutstandingAmount(
            List<Map<String, Object>> bills
    ) {

        if (bills == null || bills.isEmpty()) {
            return 0.0;
        }

        double total = 0.0;

        for (Map<String, Object> bill : bills) {

            if (bill == null) {
                continue;
            }

            String status =
                    getString(bill, "status");

            if (!isOutstanding(status)) {
                continue;
            }

            total += parseAmount(
                    getString(bill, "amount")
            );
        }

        return total;
    }

    // =========================================================
    // TOTAL MAINTENANCE
    // =========================================================

    private double calculateTotalMaintenance(
            List<Map<String, Object>> bills
    ) {

        if (bills == null
                || bills.isEmpty()) {

            return 0.0;
        }

        double total = 0.0;

        for (Map<String, Object> bill :
                bills) {

            if (bill == null) {
                continue;
            }

            String amount =
                    getString(
                            bill,
                            "amount"
                    );

            total += parseAmount(
                    amount
            );
        }

        return total;
    }

    // =========================================================
    // OUTSTANDING MAINTENANCE
    // =========================================================

    private double calculateOutstandingMaintenance(
            List<Map<String, Object>> bills
    ) {

        if (bills == null
                || bills.isEmpty()) {

            return 0.0;
        }

        double total = 0.0;

        for (Map<String, Object> bill :
                bills) {

            if (bill == null) {
                continue;
            }

            String status =
                    getString(
                            bill,
                            "status"
                    );

            if (!isOutstanding(status)) {
                continue;
            }

            total += parseAmount(
                    getString(
                            bill,
                            "amount"
                    )
            );
        }

        return total;
    }

    // =========================================================
    // FIND NEXT DUE BILL
    // =========================================================

    private Map<String, Object>
    findNextOutstandingBill(
            List<Map<String, Object>> bills
    ) {

        if (bills == null
                || bills.isEmpty()) {

            return null;
        }

        List<Map<String, Object>>
                outstanding =
                new ArrayList<>();

        for (Map<String, Object> bill :
                bills) {

            if (bill == null) {
                continue;
            }

            String status =
                    getString(
                            bill,
                            "status"
                    );

            if (!isOutstanding(status)) {
                continue;
            }

            outstanding.add(bill);
        }

        outstanding.sort(
                Comparator.comparing(
                        this::getBillDate
                )
        );

        if (outstanding.isEmpty()) {
            return null;
        }

        return outstanding.get(0);
    }

    // =========================================================
    // CHECK OUTSTANDING
    // =========================================================

    private boolean isOutstanding(
            String status
    ) {

        String value =
                safe(status)
                        .toLowerCase();

        /*
         * Your MaintenanceDAO defaults missing status
         * to Pending while adding a bill.
         */
        return value.isEmpty()
                || value.equals("pending")
                || value.equals("unpaid")
                || value.equals("overdue")
                || value.equals("due")
                || value.equals("open");
    }

    // =========================================================
    // BILL DATE
    // =========================================================

    private LocalDate getBillDate(
            Map<String, Object> bill
    ) {

        String date =
                getString(
                        bill,
                        "date"
                );

        if (date.isEmpty()) {
            return LocalDate.MAX;
        }

        String[] patterns = {
                "dd-MM-yyyy",
                "dd-MM-yy",
                "yyyy-MM-dd"
        };

        for (String pattern : patterns) {

            try {

                return LocalDate.parse(
                        date,
                        DateTimeFormatter.ofPattern(pattern)
                );

            } catch (DateTimeParseException ignored) {
                // Try the next format.
            }
        }

        return LocalDate.MAX;
    }

    // =========================================================
    // ACTIVE POLLS
    // =========================================================

    private List<PollModel>
    fetchActivePolls() {

        try {

            List<PollModel> polls =
                    pollDao.getActivePolls();

            if (polls == null) {
                return Collections.emptyList();
            }

            return polls;

        } catch (Exception e) {

            System.err.println(
                    "Dashboard poll fetch failed."
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    // =========================================================
    // NOTICES
    // =========================================================

    private List<NoticeModel>
    fetchNotices(
            String email
    ) {

        try {

            List<NoticeModel> notices =
                    noticeController
                            .getNoticesForResident(
                                    email
                            );

            if (notices == null) {
                return Collections.emptyList();
            }

            return notices;

        } catch (Exception e) {

            System.err.println(
                    "Dashboard notice fetch failed."
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    // =========================================================
    // PARSE AMOUNT
    // =========================================================

    private double parseAmount(
            String value
    ) {

        String cleaned =
                safe(value);

        if (cleaned.isEmpty()) {
            return 0.0;
        }

        /*
         * Supports:
         *
         * 1250
         * 1,250
         * ₹1250
         * ₹ 1,250
         * Rs. 1250
         */
        cleaned =
                cleaned
                        .replace(",", "")
                        .replace("₹", "")
                        .replace("Rs.", "")
                        .replace("Rs", "")
                        .replace("rs.", "")
                        .replace("rs", "")
                        .trim();

        /*
         * Remove anything that is not part of
         * a normal numeric amount.
         */
        cleaned =
                cleaned.replaceAll(
                        "[^0-9.\\-]",
                        ""
                );

        if (cleaned.isEmpty()) {
            return 0.0;
        }

        try {

            return Double.parseDouble(
                    cleaned
            );

        } catch (NumberFormatException e) {

            return 0.0;
        }
    }

    // =========================================================
    // GET STRING FROM MAP
    // =========================================================

    private String getString(
            Map<String, Object> data,
            String key
    ) {

        if (data == null
                || key == null) {

            return "";
        }

        Object value =
                data.get(key);

        if (value == null) {
            return "";
        }

        return value.toString().trim();
    }

    // =========================================================
    // NORMALIZE EMAIL
    // =========================================================

    private String normalizeEmail(
            String email
    ) {

        return safe(email)
                .toLowerCase();
    }

    // =========================================================
    // SAFE STRING
    // =========================================================

    private String safe(
            String value
    ) {

        return value == null
                ? ""
                : value.trim();
    }

    // =========================================================
    // DASHBOARD DATA
    // =========================================================

    public static class DashboardData {

        private final String residentEmail;

        private final int activeComplaints;

        private final int visitorsToday;

        private final double totalBills;

        private final double outstandingMaintenance;

        private final String maintenanceDueDate;

        private final List<ComplaintModel>
                complaints;

        private final List<com.society.model.Resident_model.VisitorModel>
                visitors;

        private final List<Map<String, Object>>
                maintenanceBills;

        private final List<Map<String, Object>>
                electricityBills;

        private final List<NoticeModel>
                notices;

        private final List<PollModel>
                polls;

        // =====================================================
        // CONSTRUCTOR
        // =====================================================

        public DashboardData(
                String residentEmail,
                int activeComplaints,
                int visitorsToday,
                double totalBills,
                double outstandingMaintenance,
                String maintenanceDueDate,
                List<ComplaintModel> complaints,
                List<com.society.model.Resident_model.VisitorModel>
                        visitors,
                List<Map<String, Object>>
                        maintenanceBills,
                List<Map<String, Object>>
                        electricityBills,
                List<NoticeModel> notices,
                List<PollModel> polls
        ) {

            this.residentEmail =
                    residentEmail;

            this.activeComplaints =
                    activeComplaints;

            this.visitorsToday =
                    visitorsToday;

            this.totalBills =
                    totalBills;

            this.outstandingMaintenance =
                    outstandingMaintenance;

            this.maintenanceDueDate =
                    maintenanceDueDate;

            this.complaints =
                    complaints == null
                            ? Collections.emptyList()
                            : Collections.unmodifiableList(
                                    new ArrayList<>(
                                            complaints
                                    )
                            );

            this.visitors =
                    visitors == null
                            ? Collections.emptyList()
                            : Collections.unmodifiableList(
                                    new ArrayList<>(
                                            visitors
                                    )
                            );

            this.maintenanceBills =
                    maintenanceBills == null
                            ? Collections.emptyList()
                            : Collections.unmodifiableList(
                                    new ArrayList<>(
                                            maintenanceBills
                                    )
                            );

            this.electricityBills =
                    electricityBills == null
                            ? Collections.emptyList()
                            : Collections.unmodifiableList(
                                    new ArrayList<>(
                                            electricityBills
                                    )
                            );

            this.notices =
                    notices == null
                            ? Collections.emptyList()
                            : Collections.unmodifiableList(
                                    new ArrayList<>(
                                            notices
                                    )
                            );

            this.polls =
                    polls == null
                            ? Collections.emptyList()
                            : Collections.unmodifiableList(
                                    new ArrayList<>(
                                            polls
                                    )
                            );
        }

        // =====================================================
        // GETTERS
        // =====================================================

        public String getResidentEmail() {
            return residentEmail;
        }

        public int getActiveComplaints() {
            return activeComplaints;
        }

        public int getVisitorsToday() {
            return visitorsToday;
        }

        public double getTotalBills() {
            return totalBills;
        }

        public double getOutstandingMaintenance() {
            return outstandingMaintenance;
        }

        public String getMaintenanceDueDate() {
            return maintenanceDueDate;
        }

        public List<ComplaintModel>
        getComplaints() {
            return complaints;
        }

        public List<com.society.model.Resident_model.VisitorModel>
        getVisitors() {
            return visitors;
        }

        public List<Map<String, Object>>
        getMaintenanceBills() {
            return maintenanceBills;
        }

        public List<Map<String, Object>>
        getElectricityBills() {
            return electricityBills;
        }

        public List<NoticeModel>
        getNotices() {
            return notices;
        }

        public List<PollModel>
        getPolls() {
            return polls;
        }
    }
}