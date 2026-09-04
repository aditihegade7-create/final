package com.society.service.resident_service;

import com.society.dao.Resident_dao.ComplaintDAO;
import com.society.model.Resident_model.ComplaintModel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ComplaintService {

    // =========================================================
    // DAO
    // =========================================================

    private final ComplaintDAO complaintDAO;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ComplaintService(ComplaintDAO complaintDAO) {

        if (complaintDAO == null) {
            throw new IllegalArgumentException(
                    "ComplaintDAO cannot be null."
            );
        }

        this.complaintDAO = complaintDAO;
    }

    // =========================================================
    // CREATE COMPLAINT
    // =========================================================

    public ComplaintModel createComplaint(
            String email,
            String flatNumber,
            String category,
            String title,
            String description,
            String imageFileName,
            String preferredDate)
            throws Exception {

        // =====================================================
        // VALIDATION
        // =====================================================

        if (email == null
                || email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Logged-in email is missing."
            );
        }

        if (category == null
                || category.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint category is required."
            );
        }

        if (title == null
                || title.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint title is required."
            );
        }

        if (description == null
                || description.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint description is required."
            );
        }

        if (preferredDate == null
                || preferredDate.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Preferred date is required."
            );
        }

        // =====================================================
        // CREATE MODEL
        // =====================================================

        ComplaintModel complaint =
                new ComplaintModel();

        // =====================================================
        // ID
        // =====================================================

        complaint.setId(
                UUID.randomUUID().toString()
        );

        // =====================================================
        // EMAIL
        // =====================================================

        complaint.setEmail(
                email.trim()
        );

        // =====================================================
        // FLAT NUMBER
        // =====================================================

        complaint.setFlatNumber(
                flatNumber == null
                        ? ""
                        : flatNumber.trim()
        );

        // =====================================================
        // CATEGORY
        // =====================================================

        complaint.setCategory(
                category.trim()
        );

        // =====================================================
        // TITLE
        // =====================================================

        complaint.setTitle(
                title.trim()
        );

        // =====================================================
        // DESCRIPTION
        // =====================================================

        complaint.setDescription(
                description.trim()
        );

        // =====================================================
        // IMAGE
        // =====================================================

        complaint.setImageFileName(
                imageFileName == null
                        ? ""
                        : imageFileName
        );

        // =====================================================
        // PREFERRED DATE
        // =====================================================

        complaint.setPreferredDate(
                preferredDate.trim()
        );

        // =====================================================
        // DEFAULT STATUS
        // =====================================================

        complaint.setStatus(
                "IN PROGRESS"
        );

        // =====================================================
        // CREATED DATE
        // =====================================================

        complaint.setCreatedAt(
                new Date()
        );

        // =====================================================
        // SAVE
        // =====================================================

        complaintDAO.saveComplaint(
                complaint
        );

        return complaint;
    }

    // =========================================================
    // GET MY COMPLAINTS
    // =========================================================

    public List<ComplaintModel> getMyComplaints(
            String email)
            throws Exception {

        if (email == null
                || email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email is required."
            );
        }

        return complaintDAO.getComplaintsByEmail(
                email.trim()
        );
    }

    // =========================================================
    // GET ALL COMPLAINTS
    // =========================================================

    public List<ComplaintModel> getAllComplaints()
            throws Exception {

        return complaintDAO.getAllComplaints();
    }

    // =========================================================
    // GET COMPLAINTS BY FLAT
    // =========================================================

    public List<ComplaintModel> getComplaintsByFlat(
            String flatNumber)
            throws Exception {

        if (flatNumber == null
                || flatNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Flat number is required."
            );
        }

        return complaintDAO.getComplaintsByFlat(
                flatNumber.trim()
        );
    }

    // =========================================================
    // UPDATE STATUS
    // =========================================================

    public void updateStatus(
            String email,
            String complaintId,
            String status)
            throws Exception {

        if (email == null
                || email.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Email is required."
            );
        }

        if (complaintId == null
                || complaintId.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Complaint ID is required."
            );
        }

        if (status == null
                || status.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Status is required."
            );
        }

        complaintDAO.updateStatus(
                email.trim(),
                complaintId.trim(),
                status.trim()
        );
    }
}
