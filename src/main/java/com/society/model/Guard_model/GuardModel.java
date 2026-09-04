package com.society.model.Guard_model;

public class GuardModel {

    // =========================================================
    // GUARD ID
    // =========================================================

    private String guardId;

    // =========================================================
    // SOCIETY
    // =========================================================

    private String society;

    // =========================================================
    // BASIC INFORMATION
    // =========================================================

    private String name;
    private String phone;
    private String email;

    // =========================================================
    // DUTY INFORMATION
    // =========================================================

    private String gate;
    private String shift;
    private String joiningDate;
    private String status;

    // =========================================================
    // PROFILE IMAGE
    // =========================================================

    private String profileImageUrl;


    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public String getGuardId() {
        return guardId;
    }

    public void setGuardId(String guardId) {
        this.guardId = guardId;
    }


    public String getSociety() {
        return society;
    }

    public void setSociety(String society) {
        this.society = society;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }


    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }


    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}