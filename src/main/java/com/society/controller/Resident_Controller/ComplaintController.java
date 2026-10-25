/*package com.society.controller.Resident_Controller;

import com.society.dao.Resident_dao.ComplaintDAO;
import com.society.model.Resident_model.ComplaintModel;
import com.society.service.resident_service.ComplaintService;

import javafx.scene.control.Label;

import java.io.File;
import java.time.LocalDate;
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
            Label description,
            File selectedFile,
            LocalDate preferredDateValue)
            throws Exception {

        return complaintService.createComplaint(

                email,

                flatNumber,

                category,

                title,

                description,

                selectedFile,

                preferredDateValue
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

    public List<ComplaintModel> getComplaintsByEmail(String currentEmail) {
        
        throw new UnsupportedOperationException("Unimplemented method 'getComplaintsByEmail'");
    }

   /*  public void submitComplaint(String currentEmail, String currentFlat, String category, String complaintTitle,
            Label description, File file, LocalDate preferredDateValue) {
       
        throw new UnsupportedOperationException("Unimplemented method 'submitComplaint'");
    }

    public List<ComplaintModel> getComplaintsByEmail(String currentEmail) {
        
        throw new UnsupportedOperationException("Unimplemented method 'getComplaintsByEmail'");
    }*/

        

package com.society.controller.Resident_Controller;

import com.society.dao.Resident_dao.ComplaintDAO;
import com.society.model.Resident_model.ComplaintModel;
import com.society.service.resident_service.ComplaintService;

import javafx.scene.control.Label;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ComplaintController {

    // =========================================================
    // SERVICE
    // =========================================================

    private final ComplaintService complaintService;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public ComplaintController(ComplaintDAO complaintDAO) {

        if (complaintDAO == null) {
            throw new IllegalArgumentException(
                    "ComplaintDAO cannot be null"
            );
        }

        this.complaintService = new ComplaintService(
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
    // GET COMPLAINTS BY EMAIL
    // =========================================================

    public List<ComplaintModel> getComplaintsByEmail(
            String currentEmail)
            throws Exception {

        return complaintService.getMyComplaints(
                currentEmail
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

    public void submitComplaint(String currentEmail, String currentFlat, String category, String complaintTitle,
            Label description, File file, LocalDate preferredDateValue) {
        
        throw new UnsupportedOperationException("Unimplemented method 'submitComplaint'");
    }
}


