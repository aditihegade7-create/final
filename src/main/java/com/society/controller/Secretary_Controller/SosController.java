package com.society.controller.Secretary_Controller;

import java.util.ArrayList;
import java.util.List;

import com.society.dao.Secretary_dao.SosDao;
import com.society.model.Secretary_model.SosAlert;

/**
 * =========================================================
 * SOS CONTROLLER
 * =========================================================
 *
 * Controller cha kaam:
 * 1. Society validate karne
 * 2. DAO madhun SOS alerts fetch karne
 * 3. Active / Resolved filter karne
 *
 * IMPORTANT:
 * Vaishnavi, Diya kiwa kontahi specific email
 * ya controller madhe hardcode nahi.
 *
 * DAO Firestore madhun available sagle email documents
 * check karel ani specific society che alerts return karel.
 */
public class SosController {

    // =========================================================
    // DAO
    // =========================================================

    private final SosDao sosDao;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public SosController() {
        sosDao = new SosDao();
    }

    // =========================================================
    // GET ALL ALERTS BY SOCIETY
    // =========================================================

    public List<SosAlert> getAlertsBySociety(String society) {

        if (society == null || society.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return sosDao.getAlertsBySociety(
                society.trim()
        );
    }

    // =========================================================
    // GET ACTIVE ALERTS BY SOCIETY
    // =========================================================

    public List<SosAlert> getActiveAlerts(String society) {

        List<SosAlert> allAlerts =
                getAlertsBySociety(society);

        List<SosAlert> activeAlerts =
                new ArrayList<>();

        for (SosAlert alert : allAlerts) {

            if (alert == null) {
                continue;
            }

            String status = alert.getStatus();

            if (status != null &&
                    status.trim().equalsIgnoreCase("ACTIVE")) {

                activeAlerts.add(alert);
            }
        }

        return activeAlerts;
    }

    // =========================================================
    // GET RESOLVED ALERTS BY SOCIETY
    // =========================================================

    public List<SosAlert> getResolvedAlerts(String society) {

        List<SosAlert> allAlerts =
                getAlertsBySociety(society);

        List<SosAlert> resolvedAlerts =
                new ArrayList<>();

        for (SosAlert alert : allAlerts) {

            if (alert == null) {
                continue;
            }

            String status = alert.getStatus();

            if (status != null &&
                    status.trim().equalsIgnoreCase("RESOLVED")) {

                resolvedAlerts.add(alert);
            }
        }

        return resolvedAlerts;
    }

    // =========================================================
    // GET ALERT COUNT
    // =========================================================

    public int getAlertCount(String society) {

        return getAlertsBySociety(society).size();
    }

    // =========================================================
    // GET ACTIVE COUNT
    // =========================================================

    public int getActiveAlertCount(String society) {

        return getActiveAlerts(society).size();
    }

    // =========================================================
    // GET RESOLVED COUNT
    // =========================================================

    public int getResolvedAlertCount(String society) {

        return getResolvedAlerts(society).size();
    }
}