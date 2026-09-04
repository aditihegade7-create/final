package com.society.controller.Resident_Controller;

import com.society.dao.Resident_dao.AmenitiesDAO;
import com.society.model.Resident_model.Amenities;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AmenitiesController {

    private final AmenitiesDAO amenitiesDAO;
    private String secretaryEmail;
    private final String residentEmail;

    public AmenitiesController() {
        this.amenitiesDAO = new AmenitiesDAO();
        this.residentEmail = "";
        this.secretaryEmail = "";
    }

    public AmenitiesController(String residentEmail) {
        this.amenitiesDAO = new AmenitiesDAO();
        this.residentEmail = normalize(residentEmail);
        this.secretaryEmail = amenitiesDAO.getSecretaryEmailForResident(this.residentEmail);
    }

    public void setSecretaryEmail(String secretaryEmail) {
        this.secretaryEmail = normalize(secretaryEmail);
    }

    public String getSecretaryEmail() {
        ensureSecretaryEmail();
        return secretaryEmail;
    }

    private void ensureSecretaryEmail() {
        if (isEmpty(secretaryEmail) && !isEmpty(residentEmail)) {
            secretaryEmail = amenitiesDAO.getSecretaryEmailForResident(residentEmail);
        }
    }

    public List<Amenities> getAmenities() {
        ensureSecretaryEmail();
        if (isEmpty(secretaryEmail)) {
            return Collections.emptyList();
        }
        return amenitiesDAO.getAmenities(secretaryEmail);
    }

    public Set<String> getUnavailableAcceptedSlots(Amenities amenity, String bookingDate) {
        if (amenity == null || isEmpty(bookingDate)) {
            return Collections.emptySet();
        }

        String email = normalize(amenity.getSecretaryEmail());
        if (isEmpty(email)) {
            ensureSecretaryEmail();
            email = secretaryEmail;
        }

        if (isEmpty(email) || isEmpty(amenity.getAmenityId())) {
            return Collections.emptySet();
        }

        return amenitiesDAO.getUnavailableAcceptedSlots(
                email,
                amenity.getAmenityId(),
                bookingDate
        );
    }

    public boolean isSlotAvailable(Amenities amenity, String bookingDate,
                                   String startTime, String endTime) {
        if (amenity == null || isEmpty(amenity.getAmenityId())) {
            return false;
        }

        String email = normalize(amenity.getSecretaryEmail());
        if (isEmpty(email)) {
            ensureSecretaryEmail();
            email = secretaryEmail;
        }

        if (isEmpty(email)) {
            return false;
        }

        return amenitiesDAO.isSlotAvailable(
                email,
                amenity.getAmenityId(),
                bookingDate,
                startTime,
                endTime
        );
    }

    public String saveBooking(Amenities amenity, String bookingDate,
                              String startTime, String endTime,
                              String flatNo, String residentName,
                              String residentEmail, String bookingStatus,
                              String paymentStatus, String paymentAmount) {
        if (amenity == null || isEmpty(amenity.getAmenityId())) {
            return null;
        }

        String email = normalize(amenity.getSecretaryEmail());
        if (isEmpty(email)) {
            ensureSecretaryEmail();
            email = secretaryEmail;
        }

        if (isEmpty(email)) {
            return null;
        }

        return amenitiesDAO.saveBooking(
                email,
                amenity.getAmenityId(),
                amenity.getAmenityName(),
                bookingDate,
                startTime,
                endTime,
                flatNo,
                residentName,
                residentEmail,
                bookingStatus,
                paymentStatus,
                paymentAmount
        );
    }

    public List<Map<String, Object>> getResidentBookings(String residentEmail) {
        return amenitiesDAO.getResidentBookings(residentEmail);
    }

    public boolean updateBookingStatus(String amenityId, String residentEmail,
                                       String bookingDocumentId, String status) {
        ensureSecretaryEmail();
        if (isEmpty(secretaryEmail)) {
            return false;
        }
        return amenitiesDAO.updateBookingStatus(
                secretaryEmail,
                amenityId,
                residentEmail,
                bookingDocumentId,
                status
        );
    }

    public boolean acceptBooking(String amenityId, String residentEmail,
                                 String bookingDocumentId) {
        return updateBookingStatus(
                amenityId,
                residentEmail,
                bookingDocumentId,
                "ACCEPTED"
        );
    }

    public boolean rejectBooking(String amenityId, String residentEmail,
                                 String bookingDocumentId) {
        return updateBookingStatus(
                amenityId,
                residentEmail,
                bookingDocumentId,
                "REJECTED"
        );
    }

    public boolean markPaymentPaid(String amenityId, String residentEmail,
                                   String bookingDocumentId) {
        ensureSecretaryEmail();
        if (isEmpty(secretaryEmail)) {
            return false;
        }
        return amenitiesDAO.updatePaymentStatus(
                secretaryEmail,
                amenityId,
                residentEmail,
                bookingDocumentId,
                "PAID"
        );
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase();
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
