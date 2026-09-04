package com.society.model.Secretary_model;

import java.util.Date;

/**
 * =========================================================
 * SOS ALERT MODEL
 * =========================================================
 */
public class SosAlert {

    // =========================================================
    // FIELDS
    // =========================================================

    private String alertId;
    private String email;
    private String society;
    private String location;
    private String type;
    private String time;
    private String status;
    private String details;
    private Date createdAt;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public SosAlert() {
    }

    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public SosAlert(
            String alertId,
            String email,
            String society,
            String location,
            String type,
            String time,
            String status,
            String details,
            Date createdAt) {

        this.alertId = alertId;
        this.email = email;
        this.society = society;
        this.location = location;
        this.type = type;
        this.time = time;
        this.status = status;
        this.details = details;
        this.createdAt = createdAt;
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public String getAlertId() {
        return alertId;
    }

    public String getEmail() {
        return email;
    }

    public String getSociety() {
        return society;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    // =========================================================
    // SETTERS
    // =========================================================

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSociety(String society) {
        this.society = society;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}