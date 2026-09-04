package com.society.controller.Resident_Controller;

import com.society.config.FirebaseConfig;
import com.society.dao.Resident_dao.ComplaintDAO;
import com.society.dao.Resident_dao.MaintenanceDAO;
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
 * maintenance/{residentEmail}/bills/{month}
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
    private final MaintenanceDAO maintenanceDAO;
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

        /*
         * IMPORTANT:
         *
         * This is the ACTUAL maintenance DAO in your project.
         */
        this.maintenanceDAO =
                new MaintenanceDAO();

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
        // MAINTENANCE
        // =====================================================

        List<Map<String, Object>>
                maintenanceBills =
                fetchMaintenance(email);

        double totalBills =
                calculateTotalMaintenance(
                        maintenanceBills
                );

        double outstandingMaintenance =
                calculateOutstandingMaintenance(
                        maintenanceBills
                );

        Map<String, Object>
                nextDueBill =
                findNextOutstandingBill(
                        maintenanceBills
                );

        String maintenanceDueDate =
                getString(
                        nextDueBill,
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
    // MAINTENANCE FETCH
    // =========================================================

    private List<Map<String, Object>>
    fetchMaintenance(
            String email
    ) {

        try {

            /*
             * ACTUAL METHOD FROM YOUR MaintenanceDAO:
             *
             * getMaintenanceByEmail(String email)
             *
             * Return type:
             *
             * List<Map<String,Object>>
             */
            List<Map<String, Object>> bills =
                    maintenanceDAO
                            .getMaintenanceByEmail(
                                    email
                            );

            if (bills == null) {
                return Collections.emptyList();
            }

            return bills;

        } catch (Exception e) {

            System.err.println(
                    "Dashboard maintenance fetch failed."
            );

            e.printStackTrace();

            return Collections.emptyList();
        }
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

        try {

            return LocalDate.parse(
                    date
            );

        } catch (DateTimeParseException e) {

            return LocalDate.MAX;
        }
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