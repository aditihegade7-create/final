package com.society.controller.Secretary_Controller;

import com.society.dao.Secretary_dao.ReportDao;
import com.society.dao.Welcome.UserDao;
import com.society.model.Secretary_model.Report;
import com.society.model.Welcome.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ReportController
 *
 * Flow:
 *
 * Logged-in Secretary
 *        ↓
 * UserDao
 *        ↓
 * Secretary society
 *        ↓
 * ReportDao
 *        ↓
 * Reports + Visitor records
 */
public class ReportController {

    // =========================================================
    // DAO
    // =========================================================

    private final ReportDao reportDao;

    private final UserDao userDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ReportController() {

        reportDao =
                new ReportDao();

        userDao =
                new UserDao();
    }

    // =========================================================
    // LOGGED-IN EMAIL
    // =========================================================

    public String getLoggedInEmail() {

        String email =
                UserDao.getLoggedInEmail();

        if (email == null) {
            return "";
        }

        return email.trim()
                .toLowerCase();
    }

    // =========================================================
    // LOGGED-IN SECRETARY
    // =========================================================

    public User getLoggedInSecretary() {

        String email =
                getLoggedInEmail();

        if (email.isEmpty()) {
            return null;
        }

        try {

            return userDao.getSecretaryByEmail(
                    email
            );

        } catch (Exception e) {

            System.err.println(
                    "Unable to get secretary: "
                            + e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }

    // =========================================================
    // SOCIETY NAME
    // =========================================================

    public String getSocietyName() {

        User secretary =
                getLoggedInSecretary();

        if (secretary == null) {
            return "";
        }

        String society =
                secretary.getSociety();

        if (society == null) {
            return "";
        }

        return society.trim();
    }

    // =========================================================
    // SOCIETY ID
    // =========================================================
    //
    // Current User model uses society name as society identity.
    //
    // =========================================================

    public String getSocietyId() {

        return getSocietyName();
    }

    // =========================================================
    // GET ALL REPORTS
    // =========================================================
    //
    // This is the main method used by GenerateReports.
    //
    // It returns:
    //
    // Reports
    // +
    // Visitor records
    //
    // belonging to the secretary's society.
    //
    // =========================================================

    public List<Report> getAllReports() {

        String society =
                getSocietyName();

        if (society.isEmpty()) {

            System.err.println(
                    "ReportController: Society not found."
            );

            return new ArrayList<>();
        }

        System.out.println(
                "ReportController: Fetching reports."
        );

        System.out.println(
                "Logged-in Email: "
                        + getLoggedInEmail()
        );

        System.out.println(
                "Society: "
                        + society
        );

        return reportDao.getReportsBySociety(
                society
        );
    }

    // =========================================================
    // GET MY REPORTS
    // =========================================================

    public List<Report> getMyReports() {

        String society =
                getSocietyName();

        String email =
                getLoggedInEmail();

        if (society.isEmpty()
                || email.isEmpty()) {

            return new ArrayList<>();
        }

        return reportDao
                .getReportsBySocietyAndEmail(
                        society,
                        email
                );
    }

    // =========================================================
    // ADD REPORT
    // =========================================================

    public boolean addReport(
            String type,
            String title,
            String details,
            String source,
            String submittedBy) {

        String email =
                getLoggedInEmail();

        String society =
                getSocietyName();

        if (email.isEmpty()
                || society.isEmpty()) {

            return false;
        }

        Report report =
                new Report();

        // =====================================================
        // BASIC DATA
        // =====================================================

        report.setEmail(
                email
        );

        report.setSocietyId(
                society
        );

        report.setSocietyName(
                society
        );

        report.setSource(
                safe(source)
        );

        report.setType(
                safe(type)
        );

        report.setTitle(
                safe(title)
        );

        report.setDetails(
                safe(details)
        );

        // =====================================================
        // SUBMITTED BY
        // =====================================================

        String submitter =
                safe(submittedBy);

        if (submitter.isEmpty()) {

            submitter =
                    email;
        }

        report.setSubmittedBy(
                submitter
        );

        // =====================================================
        // DATE
        // =====================================================

        report.setDate(
                new SimpleDateFormat(
                        "dd MMM yyyy HH:mm"
                ).format(
                        new Date()
                )
        );

        // =====================================================
        // STATUS
        // =====================================================

        report.setStatus(
                "Pending"
        );

        // =====================================================
        // TIMESTAMP
        // =====================================================

        report.setTimestamp(
                System.currentTimeMillis()
        );

        // =====================================================
        // SAVE
        // =====================================================

        return reportDao.addReport(
                report
        );
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    public boolean updateStatus(
            String reportId,
            String status) {

        return reportDao.updateStatus(
                reportId,
                status
        );
    }

    // =========================================================
    // DELETE
    // =========================================================

    public boolean deleteReport(
            String reportId) {

        return reportDao.deleteReport(
                reportId
        );
    }

    // =========================================================
    // SAFE
    // =========================================================

    private String safe(
            String value) {

        if (value == null) {
            return "";
        }

        return value.trim();
    }
}