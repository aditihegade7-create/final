package com.society.model.Guard_model;

public class VisitorModel {

    // =====================================================
    // COMMON VISITOR / GUARD LOG FIELDS
    // =====================================================

    private String visitorId;

    private String name;

    private String phone;

    private String flat;

    private String purpose;

    private String remarks;

    private String entryTime;

    private String exitTime;

    private String qrPass;

    private String status;

    private String guardId;

    // User/resident who created the visitor record
    private String email;

    // Guard who handled/admitted the visitor
    private String guardEmail;

    // Society to which the visitor record belongs
    private String society;


    // =====================================================
    // APPROVED VISITOR FIELDS
    // =====================================================

    private String gate;

    private String visitDate;

    private String visitTime;

    private String vehicleNumber;

    private Boolean used;

    /*
     * Firestore createdAt may be a Timestamp.
     * We keep it as String in the model so the existing
     * Guard UI can display it without changing the UI.
     */
    private String createdAt;


    // =====================================================
    // DEFAULT CONSTRUCTOR
    // =====================================================

    public VisitorModel() {

    }


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public VisitorModel(
            String visitorId,
            String name,
            String phone,
            String flat,
            String purpose,
            String remarks,
            String entryTime,
            String exitTime,
            String qrPass,
            String status,
            String guardId) {

        this.visitorId = visitorId;
        this.name = name;
        this.phone = phone;
        this.flat = flat;
        this.purpose = purpose;
        this.remarks = remarks;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.qrPass = qrPass;
        this.status = status;
        this.guardId = guardId;
    }


    // =====================================================
    // VISITOR ID
    // =====================================================

    public String getVisitorId() {

        return visitorId;
    }

    public void setVisitorId(String visitorId) {

        this.visitorId = visitorId;
    }


    // =====================================================
    // NAME
    // =====================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }


    // =====================================================
    // PHONE
    // =====================================================

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }


    // =====================================================
    // FLAT
    // =====================================================

    public String getFlat() {

        return flat;
    }

    public void setFlat(String flat) {

        this.flat = flat;
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
    // REMARKS
    // =====================================================

    public String getRemarks() {

        return remarks;
    }

    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }


    // =====================================================
    // ENTRY TIME
    // =====================================================

    public String getEntryTime() {

        return entryTime;
    }

    public void setEntryTime(String entryTime) {

        this.entryTime = entryTime;
    }


    // =====================================================
    // EXIT TIME
    // =====================================================

    public String getExitTime() {

        return exitTime;
    }

    public void setExitTime(String exitTime) {

        this.exitTime = exitTime;
    }


    // =====================================================
    // QR PASS
    // =====================================================

    public String getQrPass() {

        return qrPass;
    }

    public void setQrPass(String qrPass) {

        this.qrPass = qrPass;
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
    // GUARD ID
    // =====================================================

    public String getGuardId() {

        return guardId;
    }

    public void setGuardId(String guardId) {

        this.guardId = guardId;
    }


    // =====================================================
    // CREATOR EMAIL
    // =====================================================

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }


    // =====================================================
    // GUARD EMAIL
    // =====================================================

    public String getGuardEmail() {

        return guardEmail;
    }

    public void setGuardEmail(String guardEmail) {

        this.guardEmail = guardEmail;
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
    // VEHICLE NUMBER
    // =====================================================

    public String getVehicleNumber() {

        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {

        this.vehicleNumber = vehicleNumber;
    }


    // =====================================================
    // USED
    // =====================================================

    public Boolean getUsed() {

        return used;
    }

    public void setUsed(Boolean used) {

        this.used = used;
    }


    // =====================================================
    // CREATED AT
    // =====================================================

    public String getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(String createdAt) {

        this.createdAt = createdAt;
    }

    

}