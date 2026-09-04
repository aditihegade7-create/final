package com.society.controller.Resident_Controller;

import com.society.dao.Resident_dao.VisitorDAO;
import com.society.model.Resident_model.VisitorModel;
import com.society.service.resident_service.VisitorService;

import java.util.List;

public class VisitorController {

    private final VisitorService visitorService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VisitorController(VisitorDAO visitorDAO) {

        if (visitorDAO == null) {
            throw new IllegalArgumentException(
                    "VisitorDAO cannot be null."
            );
        }

        this.visitorService =
                new VisitorService(visitorDAO);
    }

    // =====================================================
    // SEND INVITE
    // =====================================================

    public VisitorModel sendInvite(
            String residentEmail,
            String visitorName,
            String phoneNumber,
            String purpose,
            String visitDate,
            String visitTime,
            String flatNumber,
            String gate,
            String vehicleNumber
    ) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing."
            );
        }

        return visitorService.createVisitor(
                residentEmail.trim().toLowerCase(),
                visitorName,
                phoneNumber,
                purpose,
                visitDate,
                visitTime,
                flatNumber,
                gate,
                vehicleNumber
        );
    }

    // =====================================================
    // GET TODAY'S INVITED VISITORS
    // =====================================================

    public List<VisitorModel> getTodayVisitors(
            String residentEmail
    ) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing."
            );
        }

        String email =
                residentEmail.trim().toLowerCase();

        return visitorService.getTodayVisitors(
                email
        );
    }

    // =====================================================
    // VERIFY QR
    // =====================================================

    public VisitorModel verifyQR(
            String qrToken
    ) throws Exception {

        if (qrToken == null ||
                qrToken.trim().isEmpty()) {

            throw new Exception(
                    "QR token is missing."
            );
        }

        return visitorService.verifyQR(
                qrToken.trim()
        );
    }

    // =====================================================
    // ALLOW ENTRY
    // =====================================================

    public void allowEntry(
            String residentEmail,
            String visitorId
    ) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing."
            );
        }

        if (visitorId == null ||
                visitorId.trim().isEmpty()) {

            throw new Exception(
                    "Visitor ID is missing."
            );
        }

        visitorService.allowEntry(
                residentEmail.trim().toLowerCase(),
                visitorId.trim()
        );
    }
}