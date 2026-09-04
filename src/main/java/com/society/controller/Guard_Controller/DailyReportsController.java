package com.society.controller.Guard_Controller;

import java.util.List;

import com.society.dao.Guard_dao.DailyReportsDao;
import com.society.model.Guard_model.VisitorModel;

public class DailyReportsController {

    private final DailyReportsDao dailyReportsDao;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public DailyReportsController() {

        dailyReportsDao =
                new DailyReportsDao();
    }

    // =====================================================
    // GET ALL VISITORS - LEGACY
    // =====================================================

    public List<VisitorModel> getAllVisitors() {

        return dailyReportsDao.getAllVisitors();
    }

    // =====================================================
    // GET VISITORS BY GUARD EMAIL
    // =====================================================

    public List<VisitorModel> getAllVisitors(
            String guardEmail) {

        return dailyReportsDao.getAllVisitors(
                guardEmail
        );
    }

    // =====================================================
    // SEND REPORT TO SECRETARY
    // =====================================================

    public boolean sendReportToSecretary(

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

        return dailyReportsDao.saveDailyReport(

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

                shift,
                gate
        );
    }

    // =====================================================
    // BACKWARD COMPATIBILITY
    // =====================================================

    public boolean sendReportToSecretary(

            String guardEmail,

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

        return dailyReportsDao.saveDailyReport(

                guardEmail,
                "",
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