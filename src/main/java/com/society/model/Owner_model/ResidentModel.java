package com.society.model.Owner_model;

public class ResidentModel {

    // =========================================================
    // OWNER ID
    // =========================================================

    private String ownerId;

    // =========================================================
    // BASIC INFORMATION
    // =========================================================

    private String email;

    private String name;

    private String phone;

    private String dateOfBirth;

    private String gender;

    private String flat;

    private String society;

    private String residentType;

    // =========================================================
    // RELATIONSHIP FIELDS
    // =========================================================

    private String ownerEmail;

    private String primaryResidentEmail;

    // =========================================================
    // TENANT-SPECIFIC FIELDS
    // =========================================================

    private String moveInDate;

    private String moveOutDate;

    private String monthlyRent;

    private String securityDeposit;

    // =========================================================
    // OTHER INFORMATION
    // =========================================================

    private String idProof;

    private String emergencyContact;

    private String remarks;

    private String removalReason;

    private String status;

    // =========================================================
    // CLOUDINARY PROFILE IMAGE URL
    // =========================================================

    private String profileImageUrl;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public ResidentModel() {

    }


    // =========================================================
    // OWNER ID
    // =========================================================

    public String getOwnerId() {

        return ownerId;
    }

    public void setOwnerId(String ownerId) {

        this.ownerId = ownerId;
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
    // NAME
    // =========================================================

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }


    // =========================================================
    // PHONE
    // =========================================================

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }


    // =========================================================
    // DATE OF BIRTH
    // =========================================================

    public String getDateOfBirth() {

        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {

        this.dateOfBirth = dateOfBirth;
    }


    // =========================================================
    // GENDER
    // =========================================================

    public String getGender() {

        return gender;
    }

    public void setGender(String gender) {

        this.gender = gender;
    }


    // =========================================================
    // FLAT
    // =========================================================

    public String getFlat() {

        return flat;
    }

    public void setFlat(String flat) {

        this.flat = flat;
    }


    // =========================================================
    // SOCIETY NAME
    // =========================================================

    public String getSociety() {

        return society;
    }

    public void setSociety(String society) {

        this.society = society;
    }


    // =========================================================
    // RESIDENT TYPE
    // =========================================================

    public String getResidentType() {

        return residentType;
    }

    public void setResidentType(String residentType) {

        this.residentType = residentType;
    }


    // =========================================================
    // OWNER EMAIL
    // =========================================================

    public String getOwnerEmail() {

        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {

        this.ownerEmail = ownerEmail;
    }


    // =========================================================
    // PRIMARY RESIDENT EMAIL
    // =========================================================

    public String getPrimaryResidentEmail() {

        return primaryResidentEmail;
    }

    public void setPrimaryResidentEmail(
            String primaryResidentEmail) {

        this.primaryResidentEmail =
                primaryResidentEmail;
    }


    // =========================================================
    // MOVE-IN DATE
    // =========================================================

    public String getMoveInDate() {

        return moveInDate;
    }

    public void setMoveInDate(String moveInDate) {

        this.moveInDate = moveInDate;
    }


    // =========================================================
    // MOVE-OUT DATE
    // =========================================================

    public String getMoveOutDate() {

        return moveOutDate;
    }

    public void setMoveOutDate(String moveOutDate) {

        this.moveOutDate = moveOutDate;
    }


    // =========================================================
    // MONTHLY RENT
    // =========================================================

    public String getMonthlyRent() {

        return monthlyRent;
    }

    public void setMonthlyRent(String monthlyRent) {

        this.monthlyRent = monthlyRent;
    }


    // =========================================================
    // SECURITY DEPOSIT
    // =========================================================

    public String getSecurityDeposit() {

        return securityDeposit;
    }

    public void setSecurityDeposit(
            String securityDeposit) {

        this.securityDeposit =
                securityDeposit;
    }


    // =========================================================
    // ID PROOF
    // =========================================================

    public String getIdProof() {

        return idProof;
    }

    public void setIdProof(String idProof) {

        this.idProof = idProof;
    }


    // =========================================================
    // EMERGENCY CONTACT
    // =========================================================

    public String getEmergencyContact() {

        return emergencyContact;
    }

    public void setEmergencyContact(
            String emergencyContact) {

        this.emergencyContact =
                emergencyContact;
    }


    // =========================================================
    // REMARKS
    // =========================================================

    public String getRemarks() {

        return remarks;
    }

    public void setRemarks(String remarks) {

        this.remarks = remarks;
    }


    // =========================================================
    // REMOVAL REASON
    // =========================================================

    public String getRemovalReason() {

        return removalReason;
    }

    public void setRemovalReason(
            String removalReason) {

        this.removalReason =
                removalReason;
    }


    // =========================================================
    // STATUS
    // =========================================================

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {

        this.status = status;
    }


    // =========================================================
    // PROFILE IMAGE URL
    // =========================================================

    public String getProfileImageUrl() {

        return profileImageUrl;
    }

    public void setProfileImageUrl(
            String profileImageUrl) {

        this.profileImageUrl =
                profileImageUrl;
    }


    // =========================================================
    // RESIDENT ID
    // =========================================================
    
    /*
     * Residents collection uses the resident's email
     * as the Firestore document ID.
     *
     * Therefore this method returns the email.
     */

    public String getResidentId() {

        return email;
    }

}