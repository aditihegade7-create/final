package com.society.controller.Resident_Controller;

import com.society.dao.Resident_dao.ComplaintDAO;
import com.society.model.Resident_model.ComplaintModel;
import com.society.service.resident_service.ComplaintService;

import java.util.List;

public class ComplaintController {

    // =========================================================
    // SERVICE
    // =========================================================

    private final ComplaintService complaintService;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ComplaintController(
            ComplaintDAO complaintDAO) {

        if (complaintDAO == null) {
            throw new IllegalArgumentException(
                    "ComplaintDAO cannot be null"
            );
        }

        this.complaintService =
                new ComplaintService(
                        complaintDAO
                );
    }

    // =========================================================
    // SUBMIT COMPLAINT
    // =========================================================

    public ComplaintModel submitComplaint(
            String email,
            String flatNumber,
            String category,
            String title,
            String description,
            String imageFileName,
            String preferredDate)
            throws Exception {

        return complaintService.createComplaint(

                email,

                flatNumber,

                category,

                title,

                description,

                imageFileName,

                preferredDate
        );
    }

    // =========================================================
    // GET MY COMPLAINTS
    // =========================================================

    public List<ComplaintModel> getMyComplaints(
            String email)
            throws Exception {

        return complaintService.getMyComplaints(
                email
        );
    }

    // =========================================================
    // GET ALL COMPLAINTS
    // =========================================================

    public List<ComplaintModel> getAllComplaints()
            throws Exception {

        return complaintService.getAllComplaints();
    }

    // =========================================================
    // GET COMPLAINTS BY FLAT
    // =========================================================

    public List<ComplaintModel> getComplaintsByFlat(
            String flatNumber)
            throws Exception {

        return complaintService.getComplaintsByFlat(
                flatNumber
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

        complaintService.updateStatus(
                email,
                complaintId,
                status
        );
    }
}
