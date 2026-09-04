package com.society.model.Resident_model;

public class ProfileModel {

    // =========================================================
    // PERSONAL INFORMATION
    // =========================================================

    private String email;
    private String name;
    private String phone;
    private String flat;
    private String wing;
    


    // =========================================================
    // ACCOUNT INFORMATION
    // =========================================================

    private String societyName;
    private String residentType;
    private String status;


    // =========================================================
    // PROFILE IMAGE
    // =========================================================

    private String profileImageUrl;


    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public ProfileModel() {
    }


    // =========================================================
    // EMAIL
    // =========================================================

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email) {

        this.email = email;
    }


    // =========================================================
    // NAME
    // =========================================================

    public String getName() {
        return name;
    }

    public void setName(
            String name) {

        this.name = name;
    }


    // =========================================================
    // PHONE
    // =========================================================

    public String getPhone() {
        return phone;
    }

    public void setPhone(
            String phone) {

        this.phone = phone;
    }


    // =========================================================
    // FLAT
    // =========================================================

    public String getFlat() {
        return flat;
    }

    public void setFlat(
            String flat) {

        this.flat = flat;
    }


    // =========================================================
    // WING
    // =========================================================

    public String getWing() {
        return wing;
    }

    public void setWing(
            String wing) {

        this.wing = wing;
    }


    // =========================================================
    // SOCIETY NAME
    // =========================================================

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(
            String societyName) {

        this.societyName =
                societyName;
    }


    // =========================================================
    // RESIDENT TYPE
    // =========================================================

    public String getResidentType() {
        return residentType;
    }

    public void setResidentType(
            String residentType) {

        this.residentType =
                residentType;
    }


    // =========================================================
    // STATUS
    // =========================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status) {

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
}