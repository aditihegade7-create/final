package com.society.service.resident_service;

import com.society.dao.Resident_dao.VisitorDAO;
import com.society.model.Resident_model.VisitorModel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class VisitorService {

    private final VisitorDAO visitorDAO;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VisitorService(
            VisitorDAO visitorDAO) {

        this.visitorDAO = visitorDAO;
    }

    // =====================================================
    // CREATE VISITOR
    // =====================================================

    public VisitorModel createVisitor(
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

        // =================================================
        // VALIDATION
        // =================================================

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing."
            );
        }

        if (visitorName == null ||
                visitorName.trim().isEmpty()) {

            throw new Exception(
                    "Visitor name is required."
            );
        }

        if (phoneNumber == null ||
                phoneNumber.trim().isEmpty()) {

            throw new Exception(
                    "Phone number is required."
            );
        }

        if (!phoneNumber.trim()
                .matches("\\d{10}")) {

            throw new Exception(
                    "Please enter a valid 10 digit phone number."
            );
        }

        if (purpose == null ||
                purpose.trim().isEmpty()) {

            throw new Exception(
                    "Purpose is required."
            );
        }

        if (visitDate == null ||
                visitDate.trim().isEmpty()) {

            throw new Exception(
                    "Visit date is required."
            );
        }

        if (visitTime == null ||
                visitTime.trim().isEmpty()) {

            throw new Exception(
                    "Visit time is required."
            );
        }

        if (flatNumber == null ||
                flatNumber.trim().isEmpty()) {

            throw new Exception(
                    "Flat number is required."
            );
        }

        if (gate == null ||
                gate.trim().isEmpty()) {

            throw new Exception(
                    "Gate is required."
            );
        }

        // =================================================
        // CREATE VISITOR
        // =================================================

        VisitorModel visitor =
                new VisitorModel();

        // =================================================
        // UNIQUE ID
        // =================================================

        visitor.setId(
                UUID.randomUUID().toString()
        );

        // =================================================
        // VISITOR DETAILS
        // =================================================

        visitor.setVisitorName(
                visitorName.trim()
        );

        visitor.setPhoneNumber(
                phoneNumber.trim()
        );

        visitor.setPurpose(
                purpose.trim()
        );

        visitor.setVisitDate(
                visitDate.trim()
        );

        visitor.setVisitTime(
                visitTime.trim()
        );

        visitor.setFlatNumber(
                flatNumber.trim()
        );

        visitor.setGate(
                gate.trim()
        );

        visitor.setVehicleNumber(
                vehicleNumber == null
                        ? ""
                        : vehicleNumber.trim()
        );

        // =================================================
        // DEFAULT STATUS
        // =================================================

        visitor.setStatus(
                "INVITED"
        );

        // =================================================
        // QR TOKEN
        // =================================================

        visitor.setQrToken(
                UUID.randomUUID().toString()
        );

        // =================================================
        // USED
        // =================================================

        visitor.setUsed(false);

        // =================================================
        // CREATED AT
        //
        // Local object value.
        // DAO stores server Timestamp.
        // =================================================

        visitor.setCreatedAt(
                new Date()
        );

        // =================================================
        // SAVE
        // =================================================

        visitorDAO.saveVisitor(
                residentEmail
                        .trim()
                        .toLowerCase(),
                visitor
        );

        return visitor;
    }

    // =====================================================
    // GET TODAY'S VISITORS
    // =====================================================

    public List<VisitorModel> getTodayVisitors(
            String residentEmail) throws Exception {

        if (residentEmail == null ||
                residentEmail.trim().isEmpty()) {

            throw new Exception(
                    "Resident email is missing."
            );
        }

        String email =
                residentEmail.trim().toLowerCase();

        System.out.println(
                "===================================="
        );

        System.out.println(
                "GET TODAY'S INVITED VISITORS"
        );

        System.out.println(
                "Logged-in Resident: " + email
        );

        List<VisitorModel> visitors =
                visitorDAO.getTodaysInvitedVisitors(
                        email
                );

        System.out.println(
                "Total Visitors: "
                        + visitors.size()
        );

        System.out.println(
                "===================================="
        );

        return visitors;
    }

    // =====================================================
    // VERIFY QR
    // =====================================================

    public VisitorModel verifyQR(
            String qrToken) throws Exception {

        if (qrToken == null ||
                qrToken.trim().isEmpty()) {

            throw new Exception(
                    "QR token is missing."
            );
        }

        return visitorDAO.getVisitorByQrToken(
                qrToken.trim()
        );
    }

    // =====================================================
    // ALLOW ENTRY
    // =====================================================

    public void allowEntry(
            String residentEmail,
            String visitorId) throws Exception {

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

        visitorDAO.markVisitorAsUsed(
                residentEmail.trim().toLowerCase(),
                visitorId.trim()
        );
    }
}