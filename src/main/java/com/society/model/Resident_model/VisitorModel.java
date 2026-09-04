package com.society.model.Resident_model;

import java.util.Date;

public class VisitorModel {

    private String id;
    private String visitorName;
    private String phoneNumber;
    private String purpose;
    private String visitDate;
    private String visitTime;
    private String flatNumber;
    private String gate;
    private String vehicleNumber;
    private String status;
    private String qrToken;
    private boolean used;

    // Firestore Timestamp -> Java Date
    private Date createdAt;

    // Resident society
    private String society;

    // =====================================================
    // REQUIRED BY FIRESTORE
    // =====================================================

    public VisitorModel() {
    }

    // =====================================================
    // ID
    // =====================================================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // =====================================================
    // VISITOR NAME
    // =====================================================

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    // =====================================================
    // PHONE NUMBER
    // =====================================================

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // =====================================================
    // PURPOSE
    // =====================================================

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    // =====================================================
    // VISIT DATE
    // =====================================================

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    // =====================================================
    // VISIT TIME
    // =====================================================

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    // =====================================================
    // FLAT NUMBER
    // =====================================================

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    // =====================================================
    // GATE
    // =====================================================

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

    // =====================================================
    // VEHICLE NUMBER
    // =====================================================

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    // =====================================================
    // STATUS
    // =====================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =====================================================
    // QR TOKEN
    // =====================================================

    public String getQrToken() {
        return qrToken;
    }

    public void setQrToken(String qrToken) {
        this.qrToken = qrToken;
    }

    // =====================================================
    // USED
    // =====================================================

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    // =====================================================
    // CREATED AT
    // =====================================================

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    // =====================================================
    // SOCIETY
    // =====================================================

    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }
}