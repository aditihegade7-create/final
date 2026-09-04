package com.society.model.Secretary_model;

public class Guard {

    // =====================================================
    // FIELDS
    // =====================================================

    private String id;
    private String name;
    private String mobile;
    private String shift;
    private String email;
    private String status;
    private String assignedGate;
    private String society;

    // =====================================================
    // DEFAULT CONSTRUCTOR
    // Required by Firestore
    // =====================================================

    public Guard() {
    }

    // =====================================================
    // CONSTRUCTOR WITHOUT SOCIETY
    // Backward compatibility
    // =====================================================

    public Guard(
            String name,
            String mobile,
            String shift,
            String email,
            String status,
            String assignedGate) {

        this.name = name;
        this.mobile = mobile;
        this.shift = shift;
        this.email = email;
        this.status = status;
        this.assignedGate = assignedGate;
        this.society = "";
    }

    // =====================================================
    // CONSTRUCTOR WITH SOCIETY
    // =====================================================

    public Guard(
            String name,
            String mobile,
            String shift,
            String email,
            String status,
            String assignedGate,
            String society) {

        this.name = name;
        this.mobile = mobile;
        this.shift = shift;
        this.email = email;
        this.status = status;
        this.assignedGate = assignedGate;
        this.society = society;
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
    // NAME
    // =====================================================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // =====================================================
    // MOBILE
    // =====================================================

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    // =====================================================
    // SHIFT
    // =====================================================

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
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
    // STATUS
    // =====================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // =====================================================
    // ASSIGNED GATE
    // =====================================================

    public String getAssignedGate() {
        return assignedGate;
    }

    public void setAssignedGate(String assignedGate) {
        this.assignedGate = assignedGate;
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
    // TO STRING
    // =====================================================

    @Override
    public String toString() {

        return "Guard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", shift='" + shift + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", assignedGate='" + assignedGate + '\'' +
                ", society='" + society + '\'' +
                '}';
    }
}