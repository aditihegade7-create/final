package com.society.model.Secretary_model;

public class Payment {

    // =========================================================
    // AMENITY FIELDS
    // =========================================================

    private String amenityId;
    private String amenityName;
    private String price;
    private String description;
    private String availability;
    private String society;


    // =========================================================
    // RESIDENT / BOOKING FIELDS
    // =========================================================

    private String email;
    private String bookingId;
    private String residentName;
    private String flatNo;
    private String bookingDate;
    private String startTime;
    private String endTime;
    private String paymentAmount;
    private String paymentStatus;
    private String status;


    // =========================================================
    // EMPTY CONSTRUCTOR
    // =========================================================

    public Payment() {
    }


    // =========================================================
    // AMENITY CONSTRUCTOR
    // =========================================================

    public Payment(
            String amenityName,
            String price,
            String description,
            String availability) {

        this.amenityName = amenityName;
        this.price = price;
        this.description = description;
        this.availability = availability;
    }


    // =========================================================
    // BOOKING CONSTRUCTOR
    // =========================================================

    public Payment(
            String residentName,
            String flatNo,
            String amenityName,
            String bookingDate,
            String startTime,
            String endTime,
            String status) {

        this.residentName = residentName;
        this.flatNo = flatNo;
        this.amenityName = amenityName;
        this.bookingDate = bookingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }


    // =========================================================
    // AMENITY ID
    // =========================================================

    public String getAmenityId() {
        return amenityId;
    }

    public void setAmenityId(String amenityId) {
        this.amenityId = amenityId;
    }


    // =========================================================
    // AMENITY NAME
    // =========================================================

    public String getAmenityName() {
        return amenityName;
    }

    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }


    // =========================================================
    // PRICE
    // =========================================================

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    // =========================================================
    // DESCRIPTION
    // =========================================================

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // =========================================================
    // AVAILABILITY
    // =========================================================

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }


    // =========================================================
    // SOCIETY
    // =========================================================

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }


    // =========================================================
    // EMAIL
    // =========================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // =========================================================
    // BOOKING ID
    // =========================================================

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }


    // =========================================================
    // RESIDENT NAME
    // =========================================================

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }


    // =========================================================
    // FLAT NUMBER
    // =========================================================

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }


    // =========================================================
    // BOOKING DATE
    // =========================================================

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }


    // =========================================================
    // START TIME
    // =========================================================

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    // =========================================================
    // END TIME
    // =========================================================

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    // =========================================================
    // PAYMENT AMOUNT
    // =========================================================

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }


    // =========================================================
    // PAYMENT STATUS
    // =========================================================

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }


    // =========================================================
    // BOOKING STATUS
    // =========================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}