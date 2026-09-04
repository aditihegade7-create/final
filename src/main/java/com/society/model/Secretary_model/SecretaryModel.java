package com.society.model.Secretary_model;

public class SecretaryModel {

    private String secretaryId;
    private String name;
    private String phone;
    private String dateOfBirth;
    private String email;
    private String gender;
    private String aadhaarNumber;
    private String societyName;
    private String role;
    private String status;


    // =====================================================
    // DEFAULT CONSTRUCTOR
    // =====================================================

    public SecretaryModel() {
        // Required by Firestore
    }


    // =====================================================
    // SECRETARY ID
    // =====================================================

    public String getSecretaryId() {
        return secretaryId;
    }

    public void setSecretaryId(String secretaryId) {
        this.secretaryId = secretaryId;
    }

    private String profileImageUrl;

public String getProfileImageUrl() {
    return profileImageUrl;
}

public void setProfileImageUrl(String profileImageUrl) {
    this.profileImageUrl = profileImageUrl;
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
    // DATE OF BIRTH
    // =====================================================

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    // =====================================================
    // EMAIL
    // =====================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // =====================================================
    // GENDER
    // =====================================================

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    // =====================================================
    // AADHAAR NUMBER
    // =====================================================

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }


    // =====================================================
    // SOCIETY NAME
    // =====================================================

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }


    // =====================================================
    // ROLE
    // =====================================================

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}