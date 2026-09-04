package com.society.controller.Secretary_Controller;

import java.util.List;

import com.society.dao.Secretary_dao.DashboardDao;
import com.society.model.Secretary_model.DashboardData;

public class DashboardController {

    // ============================================================
    // DAO
    // ============================================================

    private final DashboardDao dashboardDao;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public DashboardController(
            String secretaryEmail) {

        dashboardDao =
                new DashboardDao(
                        secretaryEmail
                );

        System.out.println(
                "========================================"
        );

        System.out.println(
                "DashboardController created"
        );

        System.out.println(
                "Secretary Email : "
                        + secretaryEmail
        );

        System.out.println(
                "========================================"
        );
    }

    // ============================================================
    // COMPLETE DASHBOARD
    // ============================================================

    public DashboardData getDashboardData() {

        try {

            return dashboardDao
                    .getDashboardData();

        } catch (Exception e) {

            System.out.println(
                    "DashboardController Error:"
            );

            e.printStackTrace();

            return null;
        }
    }

    // ============================================================
    // SOCIETY
    // ============================================================

    public String getSocietyName() {

        return dashboardDao
                .getSocietyName();
    }

    // ============================================================
    // SECRETARY EMAIL
    // ============================================================

    public String getSecretaryEmail() {

        return dashboardDao
                .getSecretaryEmail();
    }

    // ============================================================
    // RESIDENTS
    // ============================================================

    public int getResidentCount() {

        return dashboardDao
                .getResidentCount();
    }

    // ============================================================
    // OWNERS
    // ============================================================

    public int getOwnerCount() {

        return dashboardDao
                .getOwnerCount();
    }

    // ============================================================
    // GUARDS
    // ============================================================

    public int getGuardCount() {

        return dashboardDao
                .getGuardCount();
    }

    // ============================================================
    // COMPLAINTS
    // ============================================================

    public int getOpenComplaints() {

        return dashboardDao
                .getOpenComplaints();
    }

    // ============================================================
    // MAINTENANCE
    // ============================================================

    public double getMaintenanceTotal() {

        return dashboardDao
                .getMaintenanceTotal();
    }

    // ============================================================
    // MAINTENANCE COMPATIBILITY
    // ============================================================

    public double getMaintenanceCollection() {

        return dashboardDao
                .getMaintenanceCollection();
    }

    // ============================================================
    // SOS
    // ============================================================

    public List<DashboardDao.SosAlertData>
    getRecentSOSAlerts() {

        return dashboardDao
                .getRecentSOSAlerts();
    }

    // ============================================================
    // EVENTS
    // ============================================================

    public List<DashboardDao.EventData>
    getUpcomingEvents() {

        return dashboardDao
                .getUpcomingEvents();
    }
}